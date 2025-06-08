from flask import Flask, request, jsonify
import tensorflow as tf
import numpy as np
from PIL import Image
import os
import tempfile
import traceback
import logging
import sys
import threading
import time
import gc

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger = logging.getLogger('dog_breed_api_simple')

# Create a file handler for logging
file_handler = logging.FileHandler('dog_breed_api_simple.log')
file_handler.setLevel(logging.INFO)
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)
logger.addHandler(file_handler)

# Also log to console
console_handler = logging.StreamHandler(sys.stdout)
console_handler.setLevel(logging.INFO)
console_handler.setFormatter(formatter)
logger.addHandler(console_handler)

app = Flask(__name__)

# Configure TensorFlow for stability
tf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)
os.environ['CUDA_VISIBLE_DEVICES'] = '-1'  # Use CPU only
tf.config.threading.set_intra_op_parallelism_threads(1)
tf.config.threading.set_inter_op_parallelism_threads(1)

# Get absolute paths for model and labels
current_dir = os.path.dirname(os.path.abspath(__file__))
MODEL_PATH = os.path.join(current_dir, "src/main/resources/model/best_model.h5")
LABELS_PATH = os.path.join(current_dir, "src/main/resources/model/labels.txt")

logger.info(f"Model path: {MODEL_PATH}")
logger.info(f"Labels path: {LABELS_PATH}")
logger.info(f"Model exists: {os.path.exists(MODEL_PATH)}")
logger.info(f"Labels exist: {os.path.exists(LABELS_PATH)}")

# Load labels
labels = []
try:
    with open(LABELS_PATH, 'r') as f:
        labels = [line.strip() for line in f]
    logger.info(f"Loaded {len(labels)} dog breed labels")
except Exception as e:
    logger.error(f"Warning: Could not load labels: {e}")
    traceback.print_exc()

# Global model variable
model = None
model_lock = threading.Lock()

def load_model_safely():
    """Load model with proper error handling and thread safety"""
    global model
    with model_lock:
        if model is None:
            try:
                logger.info(f"Loading model from {MODEL_PATH}")
                model = tf.keras.models.load_model(MODEL_PATH, compile=False)
                logger.info("Model loaded successfully")
                return True
            except Exception as e:
                logger.error(f"Error loading model: {e}")
                traceback.print_exc()
                return False
        return True

# Load model at startup
try:
    load_model_safely()
except Exception as e:
    logger.error(f"Initial model loading failed: {e}")

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    logger.info("Health check endpoint called")
    return jsonify({
        'status': 'UP',
        'model_loaded': model is not None,
        'labels_loaded': len(labels) > 0,
        'model_path': MODEL_PATH,
        'model_exists': os.path.exists(MODEL_PATH)
    })

def make_prediction_with_timeout(img_array, timeout_seconds=45):
    """Make prediction with threading-based timeout"""
    global model
    
    if model is None:
        if not load_model_safely():
            raise Exception("Failed to load model")
    
    result = {'predictions': None, 'error': None, 'completed': False}
    
    def predict():
        try:
            logger.info("Starting prediction in thread...")
            predictions = model.predict(img_array, verbose=0)
            result['predictions'] = predictions
            result['completed'] = True
            logger.info("Prediction completed successfully")
        except Exception as e:
            result['error'] = str(e)
            logger.error(f"Prediction error: {e}")
    
    # Start prediction in a separate thread
    thread = threading.Thread(target=predict)
    thread.daemon = True
    thread.start()
    
    # Wait for completion or timeout
    thread.join(timeout_seconds)
    
    if thread.is_alive():
        logger.error(f"Prediction timed out after {timeout_seconds} seconds")
        raise TimeoutError(f"Prediction timed out after {timeout_seconds} seconds")
    
    if result['error']:
        raise Exception(result['error'])
    
    if not result['completed']:
        raise Exception("Prediction failed to complete")
    
    return result['predictions']

@app.route('/analyze', methods=['POST'])
def analyze_image():
    """Analyze dog breed from image"""
    global model, labels
    
    logger.info("Analyze endpoint called")
    
    # Check if we have received any file
    if 'image' not in request.files:
        logger.error("No image file provided in request")
        return jsonify({'error': 'No image file provided'}), 400

    image_file = request.files['image']
    
    # Log details about the received file
    logger.info(f"Received image file: {image_file.filename}")
    logger.info(f"Content type: {image_file.content_type}")
    
    # Check if file is empty
    if image_file.filename == '':
        logger.error("Empty file provided")
        return jsonify({'error': 'Empty file provided'}), 400
        
    temp_file_path = None
    try:
        # Save to a temporary file
        temp_file = tempfile.NamedTemporaryFile(delete=False, suffix='.jpg')
        temp_file_path = temp_file.name
        temp_file.close()
        
        # Save the uploaded file
        image_file.save(temp_file_path)
        logger.info(f"Saved image to temporary file: {temp_file_path}")
        
        # Check file size
        if os.path.exists(temp_file_path):
            file_size = os.path.getsize(temp_file_path)
            logger.info(f"Temporary file size: {file_size} bytes")
            if file_size == 0:
                logger.error("Temporary file is empty")
                return jsonify({'error': 'Uploaded file is empty'}), 400
        else:
            logger.error("Failed to create temporary file")
            return jsonify({'error': 'Failed to process image'}), 500
        
        # Read and preprocess the image
        try:
            img = Image.open(temp_file_path)
            logger.info(f"Image opened successfully: format={img.format}, size={img.size}, mode={img.mode}")
            
            img = img.convert('RGB')
            img = img.resize((224, 224))
            img_array = np.array(img) / 255.0
            img_array = np.expand_dims(img_array, axis=0)
            
            logger.info(f"Image preprocessed successfully: shape={img_array.shape}")
            
            # Make prediction with timeout
            try:
                predictions = make_prediction_with_timeout(img_array, timeout_seconds=45)
                logger.info(f"Prediction completed: shape={predictions.shape}")
                
                # Force garbage collection to free memory
                gc.collect()
                
            except TimeoutError as e:
                logger.error(f"Prediction timed out: {e}")
                return jsonify({'error': 'Prediction timed out. Please try again.'}), 500
            except Exception as e:
                logger.error(f"Prediction failed: {e}")
                traceback.print_exc()
                return jsonify({'error': f'Prediction failed: {str(e)}'}), 500
            
            # Get top 3 predictions
            if len(labels) > 0:
                # Get top 3 predictions with labels
                top_indices = np.argsort(predictions[0])[-3:][::-1]
                top_predictions = {
                    labels[i]: float(predictions[0][i]) for i in top_indices
                }
                
                # Get primary breed (highest confidence)
                primary_breed = labels[top_indices[0]]
                confidence = float(predictions[0][top_indices[0]])
                
                logger.info(f"Analysis complete. Primary breed: {primary_breed}, confidence: {confidence}")
                
                return jsonify({
                    'topPredictions': top_predictions,
                    'primaryBreed': primary_breed,
                    'confidence': confidence
                })
            else:
                # No labels available, return raw predictions
                top_indices = np.argsort(predictions[0])[-3:][::-1]
                top_predictions = {
                    str(i): float(predictions[0][i]) for i in top_indices
                }
                logger.info(f"Analysis complete with raw indices (no labels). Primary index: {top_indices[0]}")
                return jsonify({
                    'topPredictions': top_predictions,
                    'primaryBreed': str(top_indices[0]),
                    'confidence': float(predictions[0][top_indices[0]])
                })
        except Exception as e:
            logger.error(f"Error processing image: {e}")
            traceback.print_exc()
            return jsonify({'error': f'Error processing image: {str(e)}'}), 500
    
    except Exception as e:
        logger.error(f"Error during analysis: {e}")
        traceback.print_exc()
        return jsonify({'error': str(e)}), 500
    finally:
        # Clean up the temporary file
        if temp_file_path and os.path.exists(temp_file_path):
            try:
                os.unlink(temp_file_path)
                logger.info(f"Deleted temporary file: {temp_file_path}")
            except Exception as e:
                logger.error(f"Error deleting temporary file: {e}")

if __name__ == '__main__':
    logger.info("Starting simple Flask server on port 5015")
    app.run(host='0.0.0.0', port=5015, debug=False, threaded=True)
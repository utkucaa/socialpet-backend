from flask import Flask, request, jsonify
import tensorflow as tf
import numpy as np
from PIL import Image
import io
import os
import tempfile
import traceback
import logging
import sys

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger = logging.getLogger('dog_breed_api')

# Create a file handler for logging
file_handler = logging.FileHandler('dog_breed_api.log')
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

# Configure TensorFlow
tf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)

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

# Load model
model = None
try:
    logger.info(f"Loading model from {MODEL_PATH}")
    # Use a more robust approach to load the model
    model = tf.keras.models.load_model(
        MODEL_PATH,
        compile=False  # Don't compile the model, just load the weights
    )
    logger.info("Model loaded successfully")
except Exception as e:
    logger.error(f"Error loading model: {e}")
    traceback.print_exc()
    logger.info("Will attempt to load model on first request")

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

@app.route('/analyze', methods=['POST'])
def analyze_image():
    """Analyze dog breed from image"""
    global model, labels
    
    logger.info("Analyze endpoint called")
    
    # Check if we have received any file
    if 'image' not in request.files:
        logger.error("No image file provided in request")
        logger.info(f"Request files: {request.files}")
        logger.info(f"Request form: {request.form}")
        return jsonify({'error': 'No image file provided'}), 400

    image_file = request.files['image']
    
    # Log details about the received file
    logger.info(f"Received image file: {image_file.filename}")
    logger.info(f"Content type: {image_file.content_type}")
    logger.info(f"Content length: {request.content_length}")
    
    # Check if file is empty
    if image_file.filename == '':
        logger.error("Empty file provided")
        return jsonify({'error': 'Empty file provided'}), 400
        
    try:
        # Try to load model if not already loaded
        if model is None:
            try:
                logger.info(f"Attempting to load model on first request from {MODEL_PATH}")
                model = tf.keras.models.load_model(MODEL_PATH, compile=False)
                logger.info("Model loaded successfully on first request")
            except Exception as e:
                logger.error(f"Failed to load model on request: {e}")
                traceback.print_exc()
                return jsonify({'error': f'Failed to load model: {str(e)}'}), 500
        
        # Save to a temporary file to ensure proper handling
        temp_file = tempfile.NamedTemporaryFile(delete=False, suffix='.jpg')
        temp_file_path = temp_file.name
        temp_file.close()
        
        try:
            # Save the uploaded file to the temporary file
            image_file.save(temp_file_path)
            logger.info(f"Saved image to temporary file: {temp_file_path}")
            
            # Check if the file was saved correctly
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
                
                # Make prediction
                logger.info("Making prediction...")
                predictions = model.predict(img_array)
                logger.info(f"Prediction complete: shape={predictions.shape}")
                
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
        finally:
            # Clean up the temporary file
            try:
                if os.path.exists(temp_file_path):
                    os.unlink(temp_file_path)
                    logger.info(f"Deleted temporary file: {temp_file_path}")
            except Exception as e:
                logger.error(f"Error deleting temporary file: {e}")
    
    except Exception as e:
        logger.error(f"Error during analysis: {e}")
        traceback.print_exc()
        return jsonify({'error': str(e)}), 500

# Create a WSGI application for Gunicorn
application = app

if __name__ == '__main__':
    # Run the Flask app with Gunicorn in production
    port = int(os.environ.get('PORT', 5012))
    workers = int(os.environ.get('GUNICORN_WORKERS', 4))
    logger.info(f"Starting Gunicorn server with {workers} workers on port {port}")
    
    # Use Gunicorn's command line interface
    from gunicorn.app.base import BaseApplication
    
    class StandaloneApplication(BaseApplication):
        def __init__(self, app, options=None):
            self.options = options or {}
            self.application = app
            super().__init__()

        def load_config(self):
            for key, value in self.options.items():
                self.cfg.set(key, value)

        def load(self):
            return self.application

    options = {
        'bind': f'0.0.0.0:{port}',
        'workers': workers,
        'worker_class': 'sync',
        'timeout': 120,
        'accesslog': '-',
        'errorlog': '-',
        'loglevel': 'info',
    }
    
    StandaloneApplication(application, options).run()
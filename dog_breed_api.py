from flask import Flask, request, jsonify
import tensorflow as tf
import numpy as np
from PIL import Image
import io
import os
import tempfile

app = Flask(__name__)

# Configure TensorFlow
tf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)

# Load model and labels once at startup
MODEL_PATH = "src/main/resources/model/best_model.h5"
LABELS_PATH = "src/main/resources/model/labels.txt"

# Load labels
labels = []
try:
    with open(LABELS_PATH, 'r') as f:
        labels = [line.strip() for line in f]
    print(f"Loaded {len(labels)} dog breed labels")
except Exception as e:
    print(f"Warning: Could not load labels: {e}")

# Load model
model = None
try:
    print(f"Loading model from {MODEL_PATH}")
    model = tf.keras.models.load_model(MODEL_PATH)
    print("Model loaded successfully")
except Exception as e:
    print(f"Error loading model: {e}")
    print("Will attempt to load model on first request")

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({
        'status': 'UP',
        'model_loaded': model is not None,
        'labels_loaded': len(labels) > 0
    })

@app.route('/analyze', methods=['POST'])
def analyze_image():
    """Analyze dog breed from image"""
    global model, labels
    
    # Check if we have received any file
    if 'image' not in request.files:
        return jsonify({'error': 'No image file provided'}), 400

    image_file = request.files['image']
    
    # Check if file is empty
    if image_file.filename == '':
        return jsonify({'error': 'Empty file provided'}), 400
        
    try:
        # Try to load model if not already loaded
        if model is None:
            model = tf.keras.models.load_model(MODEL_PATH)
            print("Model loaded successfully on first request")
        
        # Read and preprocess the image
        img = Image.open(image_file).convert('RGB').resize((224, 224))
        img_array = np.array(img) / 255.0
        img_array = np.expand_dims(img_array, axis=0)
        
        # Make prediction
        predictions = model.predict(img_array)
        
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
            return jsonify({
                'topPredictions': top_predictions,
                'primaryBreed': str(top_indices[0]),
                'confidence': float(predictions[0][top_indices[0]])
            })
    
    except Exception as e:
        print(f"Error during analysis: {e}")
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    # Run the Flask app
    port = int(os.environ.get('PORT', 8080))
    app.run(host='0.0.0.0', port=port, debug=False) 
import os
import flask
from flask import Flask,request
import werkzeug
import cv2

ROOT = 'C:/Users/anike/Desktop/code/faces'

app = Flask(__name__)

def get_file_number(directory):
    return len([image for image in os.listdir(directory)])+1


@app.route('/',methods = ['GET','POST'])
def handle_request():
    #read image and get filepath
    IMAGE_FILE = flask.request.files['image']
    filename = werkzeug.utils.secure_filename(IMAGE_FILE.filename)
    print(f"received image: {IMAGE_FILE.filename}")
    filepath = os.path.join(ROOT,filename[:-4])

    #create directory if missing
    if not os.path.isdir(filepath):
        os.mkdir(filepath)
    
    #make filename according to number of images in folder
    count = get_file_number(filepath)
    filename = filename[:-4] + str(count) + filename[-4:]
    filepath = os.path.join(filepath,filename)
    
    #flip and save image (android flips front camera image)
    IMAGE_FILE.save(filepath)
    img = cv2.imread(filepath)
    cv2.flip(img,1)
    cv2.imwrite(filepath,img)
    return "successfully uploaded image to server"

#0.0.0.0 sets to current ip address
app.run(host="0.0.0.0",port =5000, debug = True) 

import os
import face_recognition
import cv2
import numpy as np
from tkinter import *
from tkinter import messagebox
import email_test
from email_test import send_email

ROOT = 'C:/Users/anike/Desktop/code/faces'
root = Tk()
root.withdraw()
face_cap = cv2.VideoCapture(0)

#empty dictionary for encodings in folders
dictionary = {name:[] for name in os.listdir(ROOT)}
valid_count = 0
invalid_count = 0

#appending encodings to above initialized dictionary
for folder in os.listdir(ROOT):
    for image in os.listdir(os.path.join(ROOT,folder)):
        img = face_recognition.load_image_file(f"{ROOT}/{folder}/{image}")
        dictionary[folder].append(face_recognition.face_encodings(img,face_recognition.face_locations(img)))


user = input("enter your username")

early_break = False

while(True):
    ret,frame = face_cap.read()
    #frame = cv2.resize(frame, (0, 0), fx=0.25, fy=0.25)
    frame = cv2.flip(frame,1)
    temp_frame = cv2.cvtColor(frame,cv2.COLOR_BGR2RGB)
    locs = face_recognition.face_locations(temp_frame)
    encodings = face_recognition.face_encodings(temp_frame,locs)

    if encodings:
        temp = np.array(dictionary[user]).reshape(-1,128)
        matches = face_recognition.compare_faces(np.array(encodings[0]),temp)
        if True in matches:
            invalid_count = 0
            valid_count +=1
            print(f"found {user} {valid_count}")
            if valid_count == 10:
                
                send_email("vedank.pande@gmail.com",f"{user} has unlocked the door",temp_frame)
                messagebox.showinfo("login","successfully logged in")
                root.destroy()
                face_cap.release()
                cv2.destroyAllWindows()
                early_break = True
                break
        else:
            valid_count = 0
            invalid_count += 1
            print(f"unknown {invalid_count}")
            if invalid_count == 10:
                send_email("vedank.pande@gmail.com","An unknown individual is trying to unlock the door",temp_frame)
                messagebox.showinfo("login","unable to login")
                root.destroy()
                face_cap.release()
                cv2.destroyAllWindows()
                early_break = True
                break
            
    else:
        print("no face")
        # if matches and True in matches[0]:
        #     face_cap.release()
        #     cv2.destroyAllWindows()
        #     print("done")
        #     early_break = True
        #     break
    



    face_locs = face_recognition.face_locations(temp_frame)
    for(top,right,bottom,left) in face_locs:
        cv2.rectangle(frame,(left,top),(right,bottom),(0,255,0),2)



    cv2.imshow("webcam",frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

if not early_break:
    face_cap.release()
    cv2.destroyAllWindows()
    print("done")

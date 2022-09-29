#sem5miniprojectalpha@gmail.com
import smtplib,ssl
from email.mime.text import MIMEText
from email.mime.image import MIMEImage
from email.mime.multipart import MIMEMultipart
from PIL import Image
import PIL


def send_email(receiver,text_string,image_data):

    smtp_server = "smtp.gmail.com"
    port = 587
    sender = "#sender email
    password = #password

    message = MIMEMultipart("alternative")
    message["Subject"] = "MP5Subject"
    message["From"] = sender
    message["To"] = receiver

    #img_data  = open('C:/Users/anike/Desktop/code/vedank.jpg','rb').read()
    image_data = Image.fromarray(image_data)
    im = image_data.save("C:/Users/anike/Desktop/code/imagedump.jpg")
    image_data = open('C:/Users/anike/Desktop/code/imagedump.jpg','rb').read()
    image = MIMEImage(image_data,name = "test")

    part = MIMEText(text_string,"plain")

    message.attach(part)
    message.attach(image)

    context = ssl.create_default_context()

    try:
        server = smtplib.SMTP(smtp_server,port)
        server.starttls(context=context)
        server.login(sender,password)
        server.sendmail(sender,receiver, message.as_string())

    except Exception as e:
        print(e)
    finally:
        server.quit()

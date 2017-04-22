import pyrebase
from subprocess import Popen, PIPE
import time
import os
import webbrowser

config = {
  "apiKey": "AIzaSyBw7fexfbjVhN9XTYxVG7bIxfQPH2zpvrI",
  "authDomain": "cameraapp-6a5ab.firebaseapp.com",
  "databaseURL": "https://cameraapp-6a5ab.firebaseio.com/",
  "storageBucket": "cameraapp-6a5ab.appspot.com"
}

firebase = pyrebase.initialize_app(config)
storage  = firebase.storage()
db = firebase.database()


# set the child to the respective user!
users = db.child('darklordofasgard').get()
d=dict(users.val())
print("The URL of the Image\n"+d['url'])
url = d['url']
webbrowser.open(url)

time.sleep(3)

s= d['url']
os.system("main.py "+s)

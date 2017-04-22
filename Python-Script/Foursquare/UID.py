import pyrebase
from subprocess import Popen, PIPE
import os

config = {
  "apiKey": "AIzaSyBw7fexfbjVhN9XTYxVG7bIxfQPH2zpvrI",
  "authDomain": "cameraapp-6a5ab.firebaseapp.com",
  "databaseURL": "https://cameraapp-6a5ab.firebaseio.com/",
  "storageBucket": "cameraapp-6a5ab.appspot.com"
}

firebase = pyrebase.initialize_app(config)
storage  = firebase.storage()
db = firebase.database()

users = db.child('darklordofasgard').get()

# print(users.val())

d=dict(users.val())
# print(d['url'])
#
# s= d['url']
# os.system("main.py "+s)


def get_lastUID():
    # print (d['timeStamp'])
    return d['timeStamp']

def get_location():
    return d['Location']

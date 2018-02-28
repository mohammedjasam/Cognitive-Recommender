import pyrebase
from subprocess import Popen, PIPE
import os
#need to hide the api keys
config = {
  "apiKey": "AIzaSyCuygRGzgjeLAZcu5NJasnL3DK8GMweuh4",
  "authDomain": "cognitive-recommender.firebaseapp.com",
  "databaseURL": "https://cognitive-recommender.firebaseio.com/",
  "storageBucket": "cognitive-recommender.appspot.com"
}

firebase = pyrebase.initialize_app(config)
storage  = firebase.storage()
db = firebase.database()

users = db.child("User").get()

# print(users.val())
s= storage.child("smoking-cigarette.jpg").get_url(None)
os.system("main.py "+s)

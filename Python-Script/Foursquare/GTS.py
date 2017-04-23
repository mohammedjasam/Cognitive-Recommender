import pyrebase
from subprocess import Popen, PIPE
import time
import os
import webbrowser

config ={
  "apiKey": "AIzaSyBw7fexfbjVhN9XTYxVG7bIxfQPH2zpvrI",
  "authDomain": "cameraapp-6a5ab.firebaseapp.com",
  "databaseURL": "https://cameraapp-6a5ab.firebaseio.com/",
  "storageBucket": "cameraapp-6a5ab.appspot.com"
}

firebase = pyrebase.initialize_app(config)
storage  = firebase.storage()
db = firebase.database()




# print(str(db.child('GlobalTimeStamp').get().val()))
def getGlobalTimeStamp():
    return db.child('GlobalTimeStamp').get().val()
#
import json
import os

## Path Navigation!
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path+'\\DataSets\\')
l=[]
names=[]

def getCity():
    with open('Restaurants.json') as data_file:
        data = json.load(data_file)

        data = dict(data)
        return data['venues'][0]['location']['city']
# set the child to the respective user!
# users = db.child('darklordofasgard').get()
# d=dict(users.val())
## Opening the image of the user!
# print("The URL of the Image\n"+d['url'])
# url = d['url']
# webbrowser.open(url)

# time.sleep(3)

# s= d['url']
# os.system("main.py "+s)

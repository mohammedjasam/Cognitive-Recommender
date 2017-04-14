import json
from pprint import pprint
from ast import literal_eval
with open('op.json') as data_file:
    data = json.load(data_file)

import pyrebase
import sys
import os

config = {
  "apiKey": "AIzaSyCuygRGzgjeLAZcu5NJasnL3DK8GMweuh4",
  "authDomain": "cognitive-recommender.firebaseapp.com",
  "databaseURL": "https://cognitive-recommender.firebaseio.com/",
  "storageBucket": "cognitive-recommender.appspot.com"
}

firebase = pyrebase.initialize_app(config)
storage  = firebase.storage()
db = firebase.database()

data = dict(data)
ll = data['outputs'][0]['data']['concepts']


for x in ll:
    print(x['name'],str(x['value']*100)+"%")
    db.child('User').child('1').push('{'+x['name']+':'+str(x['value']*100)+"%"+'}')

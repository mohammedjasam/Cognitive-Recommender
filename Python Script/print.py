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

# print(ll)

d={}
for x in ll:
    d[x['name']]=x['value']*100
    print(x['name'],str(x['value']*100)+"%")
    # db.child('User').child('1').push('{'+x['name']+':'+str(x['value']*100)+"%"+'}')
# print(d)
finalList = {}


###### Smoker
if d['smoker']<5:
    finalList['smoker']=0
else:
    finalList['smoker']=1

###### Drinker
if d['drinker']<5:
    finalList['drink_level']=0
else:
    finalList['drink_level']=1

###### Informal Dress
if d['informal']<d['formal']:
    finalList['dress_preference']=1
else:
    finalList['dress_preference']=0

###### Family
if d['family']>d['friends'] and d['family']>d['single']:
    finalList['ambience']=0
elif d['friends']>d['family'] and d['friends']>d['single']:
    finalList['ambience']=1
else:
    finalList['ambience']=2

###### Student
if d['student']>d['professional']:
    finalList['activity']=0
else:
    finalList['activity']=1
# print(finalList)

if finalList['dress_preference']==1:
    finalList['budget']=1
else:
    finalList['budget']=0

    
# resolving family vs friends when students are encountered
if finalList['ambience']==0 and finalList['activity']==0:
    finalList['ambience']=1
#### is a student and single and formal ==> med
if finalList['activity']==0 and finalList['ambience']==2 and finalList['dress_preference']==1:
    finalList['budget']=1
#### is a student and single and informal ==> low
elif finalList['activity']==0 and finalList['ambience']==2 and finalList['dress_preference']==0:
    finalList['budget']=0
#### is a student and friends and formal ==> high
elif finalList['activity']==0 and finalList['ambience']==1 and finalList['dress_preference']==1:
    finalList['budget']=2
#### is a student and friends and informal ==> med
elif finalList['activity']==0 and finalList['ambience']==1 and finalList['dress_preference']==0:
    finalList['budget']=1
#### is a professional and single and informal ==> low
elif finalList['activity']==1 and finalList['ambience']==2 and finalList['dress_preference']==0:
    finalList['budget']=0
#### is a professional and friends and informal ==> med
elif finalList['activity']==1 and finalList['ambience']==1 and finalList['dress_preference']==0:
    finalList['budget']=1
#### is a professional and friends and formal ==> high
elif finalList['activity']==1 and finalList['ambience']==2 and finalList['dress_preference']==0:
    finalList['budget']=2

print(finalList)
# print(finalList)

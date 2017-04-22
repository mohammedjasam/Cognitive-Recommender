import json
from pprint import pprint
from ast import literal_eval
import pyrebase
import sys
import os
import subprocess
import UID

with open('op.json') as data_file:
    data = json.load(data_file)

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

d={}
for x in ll:
    d[x['name']]=x['value']*100
    print(x['name'],str(x['value']*100)+"%")
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
db.child('User').push(finalList)

import csv

def get_lastUID(csv_filename):
    with open(csv_filename, 'r') as f:
        lastrow = None
        for lastrow in csv.reader(f): pass
        s=lastrow[0]
        n=int(s[1:])+1
        s=s[0]+str(n)
        return s
testdata=r'C:/Users/Stark/Desktop/Programming/Android-Development/CognitiveRecommender/Python-Script/Recommender/Testing_Binary.csv'
s='U'+ UID.get_lastUID()



for x in finalList.values():
    s+=','+str(x)
fd = open(testdata,'w')
fd.write("userID,smoker,drink_level,dress_preference,ambience,activity,budget\n")
fd.write(s+'\n')
fd.close()

# add = os.path.dirname(os.path.realpath(__file__))
# os.chdir(add)
# os.chdir(add + "\Recommender\ ")
# subprocess.call("python Recommend_Project_KNN.py",shell=True)
# subprocess.call("python Recommend_Project_decision_randomForest.py",shell=True)


dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path+ "\Foursquare\ ")
subprocess.call("python localRecommender.py",shell=True)
os.chdir(dir_path+ "\Foursquare\ ")
subprocess.call('python RestaurantExtractor.py',shell=True)


add = os.path.dirname(os.path.realpath(__file__))
os.chdir(add)
os.chdir(add + "\Recommender\ ")
subprocess.call("python Recommend_Project_KNN.py",shell=True)
subprocess.call("python Recommend_Project_decision_randomForest.py",shell=True)

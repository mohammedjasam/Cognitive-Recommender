import json
import os
import pyrebase
## Path Navigation!
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path+'\\DataSets\\')
l=[]
names=[]

a=""
print()
print("=======================================================================================================================")

with open('Restaurants.json') as data_file:
    data = json.load(data_file)

    data = dict(data)
    city=data['venues'][0]['location']['city']

    print("These are the top Restaurants in %s:"%city)
    print("=======================================================================================================================\n")
    for i in range(100):
        try:
            city=data['venues'][0]['location']['city']
            a=city
            x= data['venues'][i]['name']
            # print((data['venues'][i]['stats']['checkinsCount']) , (data['venues'][i]['stats']['usersCount']))
            if ((data['venues'][i]['stats']['checkinsCount'] > 1500) and (data['venues'][i]['stats']['usersCount']>400)):
                if x not in names:
                    names.append(data['venues'][i]['name'])
                    # print(str(i+1) + ". " + x + "                                        Address ====> " + data['venues'][i]['location']['formattedAddress'][0]+" "+data['venues'][i]['location']['formattedAddress'][1]+"\n")
                    print(x + "                                        Address ====> " + data['venues'][i]['location']['formattedAddress'][0]+" "+data['venues'][i]['location']['formattedAddress'][1]+"\n")

                else:
                    pass

        except:
            pass

config = {
  "apiKey": "AIzaSyCuygRGzgjeLAZcu5NJasnL3DK8GMweuh4",
  "authDomain": "cognitive-recommender.firebaseapp.com",
  "databaseURL": "https://cognitive-recommender.firebaseio.com/",
  "storageBucket": "cognitive-recommender.appspot.com"
}

firebase = pyrebase.initialize_app(config)
storage  = firebase.storage()
db = firebase.database()

import GTS
db.child('City').child(GTS.getCity()).child('Restaurants').set(names)

# db.child('City').


def getRestaurants():
    return names

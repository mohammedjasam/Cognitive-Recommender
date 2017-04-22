import json
import os

## Path Navigation!
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path+'\\DataSets\\')
l=[]
names=[]

with open('Restaurants.json') as data_file:
    data = json.load(data_file)

    data = dict(data)
    print("These are the Restaurants in your location:")
    for i in range(100):
        try:
            x= data['venues'][i]['name']
            if x not in names:
                names.append(data['venues'][i]['name'])
                print(str(i+1) + ". " + x)
            else:
                pass

        except:
            pass

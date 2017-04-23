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
#
#     print("These are the top Restaurants in %s:"%city)
#     print("=======================================================================================================================\n")
#     for i in range(100):
#         try:
#             city=data['venues'][0]['location']['city']
#             a=city
#             x= data['venues'][i]['name']
#             # print((data['venues'][i]['stats']['checkinsCount']) , (data['venues'][i]['stats']['usersCount']))
#             if ((data['venues'][i]['stats']['checkinsCount'] > 1500) and (data['venues'][i]['stats']['usersCount']>400)):
#                 if x not in names:
#                     names.append(data['venues'][i]['name'])
#                     # print(str(i+1) + ". " + x + "                                        Address ====> " + data['venues'][i]['location']['formattedAddress'][0]+" "+data['venues'][i]['location']['formattedAddress'][1]+"\n")
#                     print(x + "                                        Address ====> " + data['venues'][i]['location']['formattedAddress'][0]+" "+data['venues'][i]['location']['formattedAddress'][1]+"\n")
#
#                 else:
#                     pass
#
#         except:
#             pass
# def cityExtract():
#     return a

import json
import os
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path+'\\DataSets\\')
key=['chicken','steak','pizza','Restaurant']
# key=['categories']
l=[]
names=[]

for x in key:
    with open(x+'.json') as data_file:
        data = json.load(data_file)
        
    data = dict(data)

    for i in range(100):
        try:
            x= data['venues'][i]['name']
            if x not in names:
                names.append(data['venues'][i]['name'])
                print(x)
            else:
                pass

        except:
            pass

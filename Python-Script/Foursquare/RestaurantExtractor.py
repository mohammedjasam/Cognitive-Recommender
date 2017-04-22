import json
import os
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path+'\\DataSets\\')
key=['Restaurant','chicken','steak','pizza','veg']
# key=['categories']
l=[]
names=[]

for x in key:
    with open(x+'.json') as data_file:
        data = json.load(data_file)

    # ll=[]
    data = dict(data)

    for i in range(100):
        try:
            x= data['venues'][i]['name']
            names.append(data['venues'][i]['name'])
            print(x)
        except:
            pass
# for i in range(30):
#     try:
#         x=data['venues'][i]['hasMenu']
#         l.append(i)
#         ll.append(x)
#     except:
#         pass
#     try:
#         y=data['venues'][i]['allowMenuUrlEdit']
#         l.append(i)
#         ll.append(x)
#     except:
#         pass
#
#     print(x)
# print(list(set(l)))


#
# data = dict(data)
# ll = data['outputs'][0]['data']['concepts']
#
# # print(ll)
#
# d={}
# for x in ll:
#     d[x['name']]=x['value']*100
#     print(x['name'],str(x['value']*100)+"%")

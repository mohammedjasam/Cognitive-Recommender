import foursquare
from foursquare import Foursquare
import json

client = foursquare.Foursquare(client_id='Q3JHJJ1TS1XOR0TK1LTF5OFJHQC4OTLHMBIK1GYOIYXORSMB', client_secret='C54VXQIUTU2PKCPZI3RJQICLYNLYKZQ40ST1T50PYN5YVHUO')
key=['Restaurant','chicken','steak','pizza','veg']

client.users('9650322')
import os
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path+'\\DataSets\\')
# for x in key:
with open('categories.json', 'w') as f:
    # print(json.dumps(client.venues.search(params={'ll':'37.9485,-91.7715','query':x,'radius':'1000'}),sort_keys=True, indent=4, separators=(',', ': ')),file=f)
    print(json.dumps(client.venues.categories(),sort_keys=True, indent=4, separators=(',', ': ')),file=f)




import subprocess
dir_path = os.path.dirname(os.path.realpath(__file__))

os.chdir(dir_path)
subprocess.call('python RestaurantExtractor.py',shell=True)

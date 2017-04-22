import os
import json
import subprocess
import foursquare
from foursquare import Foursquare


ll='37.9485,-91.7715'


### Creating a Client to Access the API
client = foursquare.Foursquare(client_id='Q3JHJJ1TS1XOR0TK1LTF5OFJHQC4OTLHMBIK1GYOIYXORSMB', client_secret='C54VXQIUTU2PKCPZI3RJQICLYNLYKZQ40ST1T50PYN5YVHUO')

## Accessing the app from the user!
client.users('9650322')
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path+'\\DataSets\\')

## Extracting the data by querying!
with open('Restaurants.json', 'w') as f:
    print(json.dumps(client.venues.trending(params={'ll':ll,'radius':'10000','categoryId':'4d4b7105d754a06374d81259'}),sort_keys=True, indent=4, separators=(',', ': ')),file=f)

### Displaying Results
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path)
subprocess.call('python RestaurantExtractor.py',shell=True)

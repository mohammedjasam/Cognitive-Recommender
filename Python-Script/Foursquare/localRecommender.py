# import os
import json
import subprocess
import foursquare
from foursquare import Foursquare
import os
import UID
#
# dir_path = os.path.dirname(os.path.realpath(__file__))
# os.chdir(dir_path)

# ll='37.9485,-91.7715'


def extract_ll(la,lo,code):
    la=la[:-3]
    lo=lo[1:-3]
    if code == 1:
        return la+","+lo
    elif code == 2:
        return la+",-"+lo
    elif code == 3:
        return "-"+la+","+lo
    elif code == 4:
        return "-"+la+",-"+lo


# s="17.3850° N, 78.4867° E" # HYD
# s="37.7749° N, 122.4194° W" # San Francisco
# s="28.5562° N, 77.1000° E" # DEL AIRPORT
#s='34.3416° N, 108.9398° E' # Jenny's Home
# s='40.7128° N, 74.0059° W' # NYC
s="39.9042° N, 116.4074° E" #london
la,lo = s.split(',')
if la[-1]=='N':
    if lo[-1]=='E':
        location=extract_ll(la,lo,1)
    else:
        location=extract_ll(la,lo,2)
else:
    if lo[-1]=='E':
        location=extract_ll(la,lo,3)
    else:
        location=extract_ll(la,lo,4)


location = UID.get_location() #### Dynamic Location!



# ll=la+","+lo

### Creating a Client to Access the API
client = foursquare.Foursquare(client_id='Q3JHJJ1TS1XOR0TK1LTF5OFJHQC4OTLHMBIK1GYOIYXORSMB', client_secret='C54VXQIUTU2PKCPZI3RJQICLYNLYKZQ40ST1T50PYN5YVHUO')

## Accessing the app from the user!
client.users('9650322')
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path+'\\DataSets\\')
import UID
## Extracting the data by querying!
with open('Restaurants.json', 'w') as f:
    print(json.dumps(client.venues.search(params={'ll':location,'radius':'10000','categoryId':'4d4b7105d754a06374d81259'}),sort_keys=True, indent=4, separators=(',', ': ')),file=f)

### Displaying Results
# dir_path = os.path.dirname(os.path.realpath(__file__))
# os.chdir(dir_path)
# subprocess.call('python RestaurantExtractor.py',shell=True)

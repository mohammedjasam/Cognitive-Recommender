from __future__ import print_function
import json
from clarifai.rest import ClarifaiApp
import subprocess
import sys
from clarifai.rest import Image as ClImage

app = ClarifaiApp("21IgMMOfoj5kD4gAOUtKjJN3sNWbwpkCxpzwRH3b", "JHFF2_adodG9-Av7AJEk4vyBKnPu3e-rZ_S0cTh0")

model = app.models.get('ProfileGen')


# image = app.inputs.create_image_from_filename('c:\Users\Stark\Desktop\Programming\Android Development\CognitiveRecommender\Python Script\Images\mj.jpg')    #sClImage(file_obj=open('c:\Users\Stark\Desktop\Programming\Android Development\CognitiveRecommender\Python Script\Images\mj.jpg', 'rb'))
s= model.predict_by_url(url='http://i.telegraph.co.uk/multimedia/archive/03332/g7-kruen-obama-bee_3332810b.jpg')


ss = json.dumps(s, indent=4, sort_keys=True);

with open('op.json', 'w') as f:
    print(ss, file=f)
f.close()
subprocess.call('python print.py')

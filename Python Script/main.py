from __future__ import print_function
import json
from clarifai.rest import ClarifaiApp

app = ClarifaiApp("21IgMMOfoj5kD4gAOUtKjJN3sNWbwpkCxpzwRH3b", "JHFF2_adodG9-Av7AJEk4vyBKnPu3e-rZ_S0cTh0")

model = app.models.get('ProfileGen')

s= model.predict_by_url(url='https://8296-presscdn-0-77-pagely.netdna-ssl.com/wp-content/uploads/images/headline/140117_cigarettes.jpg')

ss = json.dumps(s, indent=4, sort_keys=True);

with open('op.json', 'w') as f:
    print(ss, file=f)

from clarifai import rest
from clarifai.rest import ClarifaiApp
import sys
import json
# CLARIFAI_APP_ID = '1YO3Mv6APzD0LlEavQuRo13vkqQcQcoIGZbAh3EO'
# CLARIFAI_APP_SECRET = 'YvaCBTKd4dcuNFE3pJlp4wRELtbpugfDKN6s4YnI'
#
#
# app = ClarifaiApp(CLARIFAI_APP_ID, CLARIFAI_APP_SECRET)
app = ClarifaiApp("b9Eaq-zthkRQ6_5N0LpyXGfMkgnt4qLRr0KOILHC", "mh3-rEVbYqVaQ2cKHMRUECtClG572XRGf2Emcp1v") #Version 2 mdjasam1171

del sys.argv[0]
s=sys.argv[0]
s.replace(' \\ ',' \ ')
model = app.models.get('ProfileGen')
ret= model.predict_by_filename(s)
print json.dumps(ret, indent=4, sort_keys=True);


# print(s)



# To execute type python2 BasicPrediction.py Images\<image-name>

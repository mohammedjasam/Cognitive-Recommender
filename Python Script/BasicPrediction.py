from clarifai import rest
from clarifai.rest import ClarifaiApp
import sys

CLARIFAI_APP_ID = '1YO3Mv6APzD0LlEavQuRo13vkqQcQcoIGZbAh3EO'
CLARIFAI_APP_SECRET = 'YvaCBTKd4dcuNFE3pJlp4wRELtbpugfDKN6s4YnI'


app = ClarifaiApp(CLARIFAI_APP_ID, CLARIFAI_APP_SECRET)

del sys.argv[0]
ret = app.tag_files(sys.argv)
print json.dumps(ret, indent=4, sort_keys=True);



# To execute type python2 BasicPrediction.py Images\<image-name>

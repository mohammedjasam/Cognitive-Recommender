from clarifai import rest
from clarifai.rest import ClarifaiApp


CLARIFAI_APP_ID = '1YO3Mv6APzD0LlEavQuRo13vkqQcQcoIGZbAh3EO'
CLARIFAI_APP_SECRET = 'YvaCBTKd4dcuNFE3pJlp4wRELtbpugfDKN6s4YnI'


app = ClarifaiApp(CLARIFAI_APP_ID, CLARIFAI_APP_SECRET)


# # import a few labeld images
# app.inputs.create_image_from_url(url="https://samples.clarifai.com/dog1.jpeg", concepts=["cute dog"], not_concepts=["cute cat"])
# app.inputs.create_image_from_url(url="https://samples.clarifai.com/dog2.jpeg", concepts=["cute dog"], not_concepts=["cute cat"])
#
# app.inputs.create_image_from_url(url="https://samples.clarifai.com/cat1.jpeg", concepts=["cute cat"], not_concepts=["cute dog"])
# app.inputs.create_image_from_url(url="https://samples.clarifai.com/cat2.jpeg", concepts=["cute cat"], not_concepts=["cute dog"])
#
# model = app.models.create(model_id="pets", concepts=["cute cat", "cute dog"])
#
# model = model.train()
#
# # predict with samples
# a= model.predict_by_url(url="https://samples.clarifai.com/dog3.jpeg")
# print a
# print 'HIiiiiiiiiiiiiiiiiiiiiiiiii'
# # print model.predict_by_url(url="https://samples.clarifai.com/cat3.jpeg")

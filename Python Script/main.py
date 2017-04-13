# from clarifai import rest
# from clarifai.rest import ClarifaiApp
#
# # CLARIFAI_APP_ID = '1YO3Mv6APzD0LlEavQuRo13vkqQcQcoIGZbAh3EO'
# # CLARIFAI_APP_SECRET = 'YvaCBTKd4dcuNFE3pJlp4wRELtbpugfDKN6s4YnI'
# #
# # app = ClarifaiApp(CLARIFAI_APP_ID, CLARIFAI_APP_SECRET)
# # # s={}
# # s = app.tag_urls(['https://samples.clarifai.com/metro-north.jpg'])
# # llistt= str(s).split(',')
# # # s=list(s)
# # for x in range(len(llistt)):
# #     j= llistt[x]
# #     llistt[x]=j[3:]
# #     # l[x] = l[x][]# + "\n"
# #     # print x
# #
# # for x in llistt:
# #     print x


from clarifai.rest import ClarifaiApp

app = ClarifaiApp("21IgMMOfoj5kD4gAOUtKjJN3sNWbwpkCxpzwRH3b", "JHFF2_adodG9-Av7AJEk4vyBKnPu3e-rZ_S0cTh0")

# get the general model
# model = app.models.get("general-v1.3")

# import a few labeld images

# app.inputs.create_image_from_url(url="https://samples.clarifai.com/dog1.jpeg", concepts=["cute dog"], not_concepts=["cute cat"])
# app.inputs.create_image_from_url(url="https://samples.clarifai.com/dog2.jpeg", concepts=["cute dog"], not_concepts=["cute cat"])
#
# app.inputs.create_image_from_url(url="https://samples.clarifai.com/cat1.jpeg", concepts=["cute cat"], not_concepts=["cute dog"])
# app.inputs.create_image_from_url(url="https://samples.clarifai.com/cat2.jpeg", concepts=["cute cat"], not_concepts=["cute dog"])

# model = app.models.create(model_id="pets", concepts=["cute cat", "cute dog"])

model = app.models.get('ProfileGen')
#
# model = model.train()

# model = app.models.get('pets')
# predict with the model


s= model.predict_by_url(url='https://8296-presscdn-0-77-pagely.netdna-ssl.com/wp-content/uploads/images/headline/140117_cigarettes.jpg')
s=str(s)
print json.dumps(s, indent=4, sort_keys=True);
import json
with open('data.txt', 'w') as outfile:
    json.dump(s, outfile)

#
# CLARIFAI_APP_ID = '1YO3Mv6APzD0LlEavQuRo13vkqQcQcoIGZbAh3EO'
# CLARIFAI_APP_SECRET = 'YvaCBTKd4dcuNFE3pJlp4wRELtbpugfDKN6s4YnI'
#
#
# app = ClarifaiApp(CLARIFAI_APP_ID, CLARIFAI_APP_SECRET)
#
#
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
# model = app.models.get('pets')
# # predict with samples
# a= model.predict_by_url(url="https://samples.clarifai.com/dog3.jpeg")
# print a
# print 'HIiiiiiiiiiiiiiiiiiiiiiiiii'
# # print model.predict_by_url(url="https://samples.clarifai.com/cat3.jpeg")

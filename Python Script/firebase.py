# from firebase import firebase
import pyrebase
# firebase = firebase.FirebaseApplication('https://cognitive-recommender.firebaseio.com/', None)
# result = firebase.get('/User', None)
# print (result)
#

config = {
  "apiKey": "AIzaSyCuygRGzgjeLAZcu5NJasnL3DK8GMweuh4",
  "authDomain": "cognitive-recommender.firebaseapp.com",
  "databaseURL": "https://cognitive-recommender.firebaseio.com/",
  "storageBucket": "cognitive-recommender.appspot.com"
}

firebase = pyrebase.initialize_app(config)
storage  = firebase.storage()
db = firebase.database()
# db.child("User")

users = db.child("User").get()

s= storage.child("images/atm5.jpg").get_url(None)
print(users.val())
print(s)# {"Morty": {"name": "Mortimer 'Morty' Smith"}, "Rick": {"name": "Rick Sanchez"}}

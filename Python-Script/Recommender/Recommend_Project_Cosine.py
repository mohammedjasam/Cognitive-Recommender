import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from surprise import *
from surprise import accuracy
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import classification_report,confusion_matrix
import math
from scipy.spatial import distance
from math import *
import csv
import os
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path)



with open("userprofile_Jenny.csv", 'r') as csvfile:
    User_Profile = pd.read_csv(csvfile)

with open("Testing_Binary.csv", 'r') as csvfile:
    Test_User_Profile = pd.read_csv(csvfile)

df = pd.read_csv('userprofile_Jenny.csv')
df1 = pd.read_csv('rating_final.csv')
df2 = pd.read_csv('geoplaces2.csv')


df2_select = df2[['placeID','name']]
df1_select = df1[['userID', 'placeID']]
user_restaurant = pd.merge(df,df1_select)
user_restaurant_final = pd.merge(user_restaurant,df2_select)


def get_lastUID(csv_filename):
    with open(csv_filename, 'r') as f:
        lastrow = None
        for lastrow in csv.reader(f): pass
        s=lastrow[0]
        n=int(s[1:])
        s=s[0]+str(n)
        return s
distance_columns = ['smoker', 'drink_level', 'dress_preference', 'ambience', 'activity', 'budget']

# Select only the numeric columns from the NBA dataset
User_numeric = User_Profile[distance_columns]

# Normalize all of the numeric columns
User_normalized = (User_numeric - User_numeric.mean()) / User_numeric.std()

# Fill in NA values in nba_normalized
User_normalized.fillna(0, inplace=True)

# Select only the numeric columns from the NBA dataset
Test_User_numeric = Test_User_Profile[distance_columns]

# Normalize all of the numeric columns
Test_User_normalized = (Test_User_numeric - Test_User_numeric.mean()) / Test_User_numeric.std()

# Fill in NA values in nba_normalized
Test_User_numeric.fillna(0, inplace=True)

# Find the normalized vector for lebron james.
Test_User_current_normalized = Test_User_normalized[Test_User_Profile["userID"] == get_lastUID("Testing_Binary.csv")]

# Find the distance between lebron james and everyone else.
cosine_distances = User_normalized.apply(lambda row: distance.cosine(row, Test_User_current_normalized), axis=1)

# Create a new dataframe with distances.
distance_frame = pd.DataFrame(data={"dist": cosine_distances, "idx": cosine_distances.index})
# print (distance_frame)
distance_frame.sort("dist", inplace=True)

# sns.heatmap(distance_frame['dist'])
# plt.show()
Users=[]
Restraunts = []
UserRes={}




for i in range(5):
    d=distance_frame.iloc[i+1]["idx"]
    u=User_Profile.loc[int(d)]['userID']
    r=user_restaurant_final.loc[user_restaurant_final['userID'] == u]
    Users.append(u)
    Restraunts+=list(r['name'])
    UserRes[u]=list(r['name'])


print("\n")
print("===================================")
print("||The User Being tested is: "+ get_lastUID("Testing_Binary.csv")+"||")
print("===================================")
print()
print("The following are the top 5 Users similar to the user in the picture and their corresponding restaurant choices!")
print("================================================================================================================\n")
# print(Users)
# print()
# print(Restraunts)
# print("\n")
# print()
# print(len(Restraunts))
# print()
# print(len(set(Restraunts)))
#
import json

# # load from file:
# with open('/path/to/my_file.json', 'r') as f:
#     try:
#         data = json.load(f)
#     # if the file is empty the ValueError will be thrown
#     except ValueError:
#         data = {}

# save to file:
sss=json.dumps(UserRes)#, indent=4, sort_keys=True)
# print(sss)
with open('Restraunts.json', 'w') as f:
    json.dump(UserRes,f,indent=4,ensure_ascii=False)
    # print(sss,f)
#     # data['new_key'] = [1, 2, 3]
#     sss=json.dump(UserRes,indent=4, sort_keys=True)
#
#     json.dump(UserRes,indent=4, sort_keys=True, f)
# #
# with open('Restraunts.csv','w') as f:
#     # for k,v in UserRes.items():
#     #     print((k,v),f)
#     print(Restraunts,f)

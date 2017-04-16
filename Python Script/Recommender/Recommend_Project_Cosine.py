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

with open("/Users/manish/Documents/UniversityofMissouri/Spring2017/Spatial/Project/Start/userprofile_Jenny.csv", 'r') as csvfile:
    User_Profile = pd.read_csv(csvfile)


with open("/Users/manish/Documents/UniversityofMissouri/Spring2017/Spatial/Project/Start/Testing_Binary.csv", 'r') as csvfile:
    Test_User_Profile = pd.read_csv(csvfile)
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
Test_User_current_normalized = Test_User_normalized[Test_User_Profile["userID"] == "U1139"]

# Find the distance between lebron james and everyone else.
cosine_distances = User_normalized.apply(lambda row: distance.cosine(row, Test_User_current_normalized), axis=1)

# Create a new dataframe with distances.
distance_frame = pd.DataFrame(data={"dist": cosine_distances, "idx": cosine_distances.index})
# print (distance_frame)
distance_frame.sort("dist", inplace=True)

# sns.heatmap(distance_frame['dist'])
# plt.show()

# Find the most similar user to selected user (the lowest distance to lebron is lebron, the second smallest is the most similar non-lebron player)
second_smallest = distance_frame.iloc[1]["idx"]
third_smallest = distance_frame.iloc[2]["idx"]
fourth_smallest = distance_frame.iloc[3]["idx"]
fifth_smallest = distance_frame.iloc[4]["idx"]
most_similar_to_selected_user = User_Profile.loc[int(second_smallest)]["userID"]
thirdmost_similar_to_selected_user = User_Profile.loc[int(third_smallest)]["userID"]
fourthmost_similar_to_selected_user = User_Profile.loc[int(fourth_smallest)]["userID"]
fifthmost_similar_to_selected_user = User_Profile.loc[int(fifth_smallest)]["userID"]

print(most_similar_to_selected_user)
print(thirdmost_similar_to_selected_user)
print(fourthmost_similar_to_selected_user)
print(fifthmost_similar_to_selected_user)

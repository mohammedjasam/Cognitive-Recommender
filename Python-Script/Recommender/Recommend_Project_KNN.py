import pandas as pd
import numpy as np
import matplotlib.pylab as plt
import seaborn as sns
from sklearn.preprocessing import StandardScaler
import sklearn.metrics
from sklearn.model_selection import ShuffleSplit
from sklearn.cross_validation import StratifiedKFold
from sklearn.model_selection import KFold
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import classification_report
from sklearn.metrics import confusion_matrix
from sklearn.tree import DecisionTreeClassifier
import IPython
from sklearn import tree
#from StringIO import StringIO
from io import StringIO
#from StringIO import StringIO
from IPython.display import Image
import time
from sklearn.metrics import accuracy_score, f1_score
from scipy.spatial import distance


df = pd.read_csv('userprofile_Jenny.csv')
df1 = pd.read_csv('rating_final.csv')
df2 = pd.read_csv('geoplaces2.csv')
# print (df.head())
# print (df1.head())
df2_select = df2[['placeID','name']]
df1_select = df1[['userID', 'placeID']]
user_restaurant = pd.merge(df,df1_select)
user_restaurant_final = pd.merge(user_restaurant,df2_select)

User_Profile = user_restaurant_final.drop(['name','placeID'],axis=1)
# print (User_Profile.head())

with open("Testing_Binary.csv", 'r') as csvfile:
    Test_User_Profile = pd.read_csv(csvfile)
Test_User_Profile_final = Test_User_Profile.drop('userID',axis=1)
x_columns = ['smoker', 'drink_level', 'dress_preference', 'ambience', 'activity', 'budget']

# The column that we want to predict.
y_column = ['name']

from sklearn.neighbors import KNeighborsClassifier
# Create the knn model.
# Look at the five closest neighbors.
knn = KNeighborsClassifier(n_neighbors=5)
# Fit the model on the training data.
knn.fit(User_Profile[x_columns], user_restaurant_final[y_column])
# Make point predictions on the test set using the fit model.
predictions = knn.predict(Test_User_Profile_final[x_columns])

print (predictions)

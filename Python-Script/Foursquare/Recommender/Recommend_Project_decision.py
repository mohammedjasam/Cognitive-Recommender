import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from surprise import *
from surprise import accuracy
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import classification_report,confusion_matrix

df = pd.read_csv('userprofile_Jenny.csv')

df_test = pd.read_csv('Testing_Binary.csv')
# print(df_test)

df_rating = pd.read_csv('rating_final.csv')
df_RestData = pd.read_csv('geoplaces2.csv')

df_RestData_select = df_RestData[['placeID','name']]
# print(df_RestData_select)

df_rating_select = df_rating[['userID', 'placeID']]
# print(df_rating_select)
df_initial = pd.merge(df,df_rating_select)
# print(df_initial)
df_final = pd.merge(df_initial,df_RestData_select)
# print(df_final.drop(['userID','name','placeID'],axis=1))
dtree = DecisionTreeClassifier()

dtree.fit(df_final.drop(['userID','name','placeID'],axis=1), df_final['name'])

predictions = dtree.predict(df_test.drop(['userID'],axis=1))

print (predictions)

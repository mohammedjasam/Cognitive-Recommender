import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from surprise import *
from surprise import accuracy
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import classification_report,confusion_matrix
from sklearn.ensemble import RandomForestClassifier
#from StringIO import StringIO
from io import StringIO
#from StringIO import StringIO
from IPython.display import Image
from sklearn import tree

df = pd.read_csv('userprofile_Jenny.csv')

df_test = pd.read_csv('Testing_Binary.csv')

df_rating = pd.read_csv('rating_final.csv')
df_RestData = pd.read_csv('geoplaces2.csv')

df_RestData_select = df_RestData[['placeID','name']]
df_rating_select = df_rating[['userID', 'placeID']]
df_initial = pd.merge(df,df_rating_select)
df_final = pd.merge(df_initial,df_RestData_select)

# print (df_final.head())


dtree = DecisionTreeClassifier()

dtree = dtree.fit(df_final.drop(['userID','name','placeID'],axis=1), df_final['name'])

predictions = dtree.predict(df_test.drop(['userID'],axis=1))
print("\n")
print("=================================")

print("||Prediction from Decision Tree||")
print("=================================")

for x in range(len(predictions)):
    print ("%s. " %(x+1) +predictions.item(x))
print("=================================")

print()

#Displaying the decision tree

out = StringIO()
tree.export_graphviz(dtree, out_file=out)
import pydotplus
graph=pydotplus.graph_from_dot_data(out.getvalue())
Image(graph.write_pdf("graph.pdf"))


###########Random Forest implementation############
rfc = RandomForestClassifier(n_estimators = 200)

rfc.fit(df_final.drop(['userID','name','placeID'],axis=1), df_final['name'])

rfc_rediction = rfc.predict(df_test.drop(['userID'],axis=1))

l=[]
l=rfc_rediction
print("\n")
print("==========================================")

print("||Prediction from RandomForestClassifier||")
print("==========================================")

for x in range(len(rfc_rediction)):
    print ("%s. " %(x+1) +rfc_rediction.item(x) )

print("==========================================")


print()

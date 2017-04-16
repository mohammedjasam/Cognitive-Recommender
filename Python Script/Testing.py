import csv

def get_last_row(csv_filename):
    with open(csv_filename, 'r') as f:
        lastrow = None
        for lastrow in csv.reader(f): pass
        s=lastrow[0]
        n=int(s[1:])+1
        s=s[0]+str(n)
        return s

print(get_last_row(r'C:/Users/Stark/Desktop/Programming/Android Development/CognitiveRecommender/Python Script/Recommender/Testing_Binary.csv'))

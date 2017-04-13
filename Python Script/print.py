import json
from pprint import pprint
from ast import literal_eval
with open('op.json') as data_file:
    data = json.load(data_file)

# data = [item for value in data for item in literal_eval(value)]
# pprint(data["u'concepts"])

data = dict(data)
ll = data['outputs'][0]['data']['concepts']
# pprint(ll[0])

for x in ll:
    print(x['name'],str(x['value']*100)+"%")
# print mail_accounts[0]["i"]

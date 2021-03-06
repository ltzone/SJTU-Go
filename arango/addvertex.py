# coding=utf-8
from pyArango.connection import *
import xml.etree.ElementTree as ET
from coordTransform_utils import wgs84_to_gcj02
def transform(lon,lat):
    gcj_lon, gcj_lat = wgs84_to_gcj02(lon,lat)
    return (gcj_lon,gcj_lat)
conn = Connection(arangoURL='http://47.92.147.237:8529', username="root", password="sjtugo")
db = conn["_system"]
tree = ET.parse('highway2.xml')
root = tree.findall('node')
c1 = db['vertex']
c2 = db['park']
count=0
for i in range(len(root)):

    count+=1
    data = root[i]
    park = False
    place = False
    pp=data.findall('tag')
    attr = data.attrib
    node_id = attr['id'][1:]

    lat = attr['lat']
    lon = attr['lon']
    array = []
    array.append(float(lon))
    array.append(float(lat))

    array[0],array[1]=transform(array[0],array[1])
    doc=c1.creatDocument()


    if( len(pp)>0):
        for ii in range(len(pp)):
            tag = pp[ii]
            att=tag.attrib
            if att['k']=='parking_spot':
                park = True
                doc1=c2.createDocument()
                doc1['name'] = att['v']
                doc1['location'] = array
                doc1.save()
                doc['name']=att['v']
                print(park,att['v'])
    doc['isPark']=park
    doc['isPlace']=place

    doc['location']=array
    doc._key = node_id[1:]
    doc.save()
    print(node_id,'collection count after insert: %s' % c1.count())

""" #change location
    if count>0:
        v = c1[node_id]
        nnn=(v['location'][0]==array[0] and v['location'][1]==array[1])
        print(nnn,count)
        if (not nnn):

            nnn1 = (v['location'][0] == array[0] and v['location'][1] == array[1])
            print(nnn1)
            print(v['location'], node_id,array, count)
            v['location']=array

            v.save()

"""


'''#example
doc = c1.createDocument()
doc['location'] = ['31.0292108','121.4381921']
doc._key = '-110717'
doc['parking'] = '新图书馆'
doc.save()

doc1 = c1.createDocument()
doc1['location'] = ['31.0261311','121.4370098']
doc1._key = '-110788'
doc1['parking'] = '二餐'
doc1.save()

doc2 = c1.createDocument()
doc2['location'] = ['31.025364','121.4321087']
doc2._key = '-110760'
doc2['parking'] = '一餐'
doc2.save()
'''
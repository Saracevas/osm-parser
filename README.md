# OpenStreet Map XML Parser
This tool does a couple of things, firstly, it parses the exported OpenStreetMap XML data and constructs a network of vehicular roads. It then takes the parsed data and constructs a dictionary of all unique street names and their corresponding IDs.

The data can then be given to a path finding algorithm, such as Dijkstra, to find the shortest path between the two streets. The choice of the algorithm will impact the amount of time it will take to calculate the shortest path.

### Example
##### 1. Data is retreived from OSM in an XML format
```
...
 <way id="3988157" visible="true" version="7" changeset="11834941" timestamp="2012-06-08T12:52:33Z" user="Chris Parker" uid="51722">
  <nd ref="20835478"/>
  <nd ref="1780340306"/>
  <nd ref="20835480"/>
  <nd ref="24644055"/>
  <tag k="abutters" v="retail"/>
  <tag k="highway" v="unclassified"/>
  <tag k="name" v="Hotel Street"/>
  <tag k="oneway" v="yes"/>
 </way>
 ...
```
##### 2. Tool outputs data.txt which contains a road, it's length and all of the connecting roads
```
{
  "78457189":{
    "distance":45,
    "name":"Upper Nelson Street",
    "id":"78457189",
    "connections":[
      {"id":"152865381"},
      {"id":"78457187"}
    ]
  },
  "78457187":{
    "distance":161,
    "name":"Regent Street",
    "id":"78457187",
    "connections":[
      {"id":"78457189"},
      {"id":"4409992"}
    ]
  }
 ...
```
##### 3. It then goes through the parsed data and build a JSON file containing all of the unique roads
```
...
  "Deacon Street":"48812592",
  "Stanley Road":"37621340",
  "Marquis Street":"19202695",
  "Biddulph Street":"23019040",
  "Romway Road":"13961889",
  "Stoneygate Road":"27753748"
...
```
### Issues
1. It appears that the distance of the road is sometimes not calculated correctly, as the tool stops adding the distances if it cannot find any more connecting paths.

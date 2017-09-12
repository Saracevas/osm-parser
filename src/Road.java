import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.io.Serializable;
import java.util.*;
import java.util.Map;

/**
 * Created by GLaDOS on 29/04/2015.
 */
public class Road {

    private String id, name;
    private int weight;
    MapNode result;
    JSONArray connectionsArray, temp;
    private String s = "";
    private LinkedHashMap<String, MapNode> nodes = new LinkedHashMap<String, MapNode>();
    private HashMap<String, Road> connections = new HashMap<String, Road>();

    public Road(String id) {
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public HashMap<String, MapNode> getNodes() {
        return nodes;
    }

    public void addNode(MapNode n) {
        nodes.put(n.getId(), n);
    }

    public void removeNode(MapNode n) {
        nodes.remove(n.getId());
    }

    public int getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setWeight() {
        MapNode first = getFirstNode();
        MapNode last = getLastNode();
        Double distance = distance(first.getLat(), first.getLon(), last.getLat(), last.getLon());
        this.weight = distance.intValue();
    }

    public int nodesSize() {
        return nodes.size();
    }

    public Road getConnection(String s) {
        return connections.get(s);
    }

    public void addConnecton(Road r) {
        connections.put(r.getId(), r);
    }

    public int getConnectionSize() {
        return connections.size();
    }

    public String getConnections() {
        String s = "";
        for (String con : connections.keySet()) {
            s = s + " " + con;
        }
        return s;
    }

    public static double distance(double lat1, double lng1, double lat2, double lng2){
        double a = (lat1-lat2)*distPerLat(lat1);
        double b = (lng1-lng2)*distPerLng(lat1);
        return Math.sqrt(a*a+b*b);
    }

    private static double distPerLng(double lat){
        return 0.0003121092*Math.pow(lat, 4)
                +0.0101182384*Math.pow(lat, 3)
                -17.2385140059*lat*lat
                +5.5485277537*lat+111301.967182595;
    }

    private static double distPerLat(double lat){
        return -0.000000487305676*Math.pow(lat, 4)
                -0.0033668574*Math.pow(lat, 3)
                +0.4601181791*lat*lat
                -1.4558127346*lat+110579.25662316;
    }

    public MapNode getFirstNode() {
        ArrayList<MapNode> nodeArray = new ArrayList<MapNode>();
        for (String s : nodes.keySet()) {
            MapNode tempNode = nodes.get(s);
            nodeArray.add(tempNode);
        }
        return nodeArray.get(0);
    }

    public MapNode getLastNode() {
        ArrayList<MapNode> nodeArray = new ArrayList<MapNode>();
        for (String s : nodes.keySet()) {
            MapNode tempNode = nodes.get(s);
            nodeArray.add(tempNode);
        }
        return nodeArray.get(nodeArray.size()-1);
    }


    @Override
    public String toString() {
        connectionsArray = new JSONArray();
//        for (String rd : connections.keySet()) {
//            Road rod = connections.get(rd);
//            temp = new JSONArray();
//            temp.add(rod.getId());
//            connectionsArray.addAll(temp);
//        }

        for (String rd : connections.keySet()) {
            JSONObject connectionsObj = new JSONObject();
            Road rod = connections.get(rd);
            connectionsObj.put("id", rod.getId());
            connectionsArray.add(connectionsObj);
        }
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("distance", getWeight());
        json.put("connections", connectionsArray);
        return json.toJSONString();
    }
}
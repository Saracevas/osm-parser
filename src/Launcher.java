/**
 * Created by Sylvester on 02/05/2015.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.json.simple.JSONValue;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Launcher {
    int count;
    String readLat, readLon;
    double lat, lon;
    String tempNodeId;

    public Launcher() {

    }

    public static HashMap<String, MapNode> nodes = new HashMap<String, MapNode>();
    public static HashMap<String, Road> roads = new HashMap<String, Road>();
    public ArrayList<String> deleteRoads = new ArrayList<String>();

    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        System.out.println(distance(52.628219, -1.115286, 52.626545, -1.115608));
        launcher.firstParse();
        launcher.fixMapNodes();
        launcher.secondParse();
        launcher.getBadRoads();
        launcher.clean();
        ExtractNames extractNames = new ExtractNames();
        extractNames.getNames();


        String json = JSONValue.toJSONString(roads);
        System.out.println(roads.get("23018806").getConnections());
        System.out.println(roads.get("23018806").getNodes().get("248185914"));

        // SAVE JSON TO A FILE
        try {
            PrintWriter out = new PrintWriter("data.txt");
            out.println(json);
            out.close();
        }catch (FileNotFoundException e) { e.printStackTrace(); }
    }

    public void getBadRoads() {
        for (String rd : roads.keySet()) {
            Road road = roads.get(rd);
            if (road.getConnectionSize() == 0) {
                deleteRoads.add(rd);
            }
        }
    }

    public void clean() {
        for (String s : deleteRoads) {
            roads.remove(s);
        }
    }

    public void firstParse() {

        try {
            File inputFile = new File("map.osm-2.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            //traversing WAY
            NodeList nList = doc.getElementsByTagName("way");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    //traversing REF
                    NodeList mapNodeList = eElement.getChildNodes();
                    Road road = new Road(eElement.getAttribute("id"));
                    for (int i=0; i < mapNodeList.getLength(); i++) {
                        Node ref = mapNodeList.item(i);
                        if (ref.getNodeType() == Node.ELEMENT_NODE) {
                            if (ref.getNodeName().equals("nd")){
                                Element eElement2 = (Element) ref;

                                MapNode tempNode;

                                if (nodes.containsKey(eElement2.getAttribute("ref"))) {
                                    tempNode = nodes.get(eElement2.getAttribute("ref"));
                                    tempNode.addCount();
                                    nodes.replace(eElement2.getAttribute("ref"), tempNode);
                                } else {
                                    tempNode = new MapNode(eElement2.getAttribute("ref"));
                                    nodes.put(eElement2.getAttribute("ref"), tempNode);
                                }
                                road.addNode(tempNode);
                            }else {
                                Element eElement3 = (Element) ref;
                                if (eElement3.getAttribute("k").equals("name") && !(eElement3.getAttribute("k").equals("railway"))) {
                                    road.setName(eElement3.getAttribute("v"));
                                    roads.put(road.getId(), road);
                                }
                                //System.out.println("Type: " + eElement3.getAttribute("k"));
                            }
                        }
                    }


                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void secondParse() {

        for (String road : roads.keySet()) {
            Road tempRoad = roads.get(road);
            HashMap<String, MapNode> tempNodes = tempRoad.getNodes();
            for (String node : tempNodes.keySet()) {
                MapNode tempNode = tempNodes.get(node);
                int currentCount = tempNode.getCount();
                if (currentCount >= 2) {

                    for (String road2 : roads.keySet()) {

                        Road tempRoad2 = roads.get(road2);
                        tempRoad2.setWeight();
                        HashMap<String, MapNode> tempNodes2 = tempRoad2.getNodes();

                        for (String node2 : tempNodes2.keySet()) {
                            MapNode tempNode2 = tempNodes2.get(node2);
                            if (tempNode2.getId().equals(tempNode.getId()) && !(tempRoad.getId().equals(tempRoad2.getId()))) {
                                tempRoad.addConnecton(tempRoad2);
                            }
                        }

                    }

                }

            }
        }
    }

    public void fixMapNodes(){
        try {
            File inputFile = new File("map.osm-2.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            //traversing WAY
            NodeList nList2 = doc.getElementsByTagName("node");
            for (int temp = 0; temp < nList2.getLength(); temp++) {
                Node nNode = nList2.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    tempNodeId = eElement.getAttribute("id");
                    readLat = eElement.getAttribute("lat");
                    lat = Double.parseDouble(readLat);
                    //System.out.println(lat);
                    //System.out.println(readLat);
                    readLon = eElement.getAttribute("lon");
                    lon = Double.parseDouble(readLon);
                    for (String rd : roads.keySet()) {
                        //System.out.println(rd);
                        Road fixRoad = roads.get(rd);

                        //System.out.println(fixNode.toString());
                        for (String mapNodes : fixRoad.getNodes().keySet()) {
                            //count++;
                            //System.out.println("lel");
                            MapNode fixNode = fixRoad.getNodes().get(mapNodes);
                            if (fixNode.getId().equals(tempNodeId)) {
                                fixNode.setLat(lat);
                                fixNode.setLon(lon);
                            }
                            //System.out.println("Node ID "+fixNode.getId()+" Lat "+fixNode.getLat()+" Lon "+fixNode.getLon());
                        }
                    }
                }
            }
            System.out.println(count);
        }catch (Exception e) {}
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


}

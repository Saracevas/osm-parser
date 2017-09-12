/**
 * Created by Sylvester on 02/05/2015.
 */
import org.json.simple.JSONObject;

public class MapNode {

    private int count = 1;
    private String id;
    private double lat, lon;

    public MapNode(String id, double lat, double lon) {
        this.id=id;
        this.lat=lat;
        this.lon=lon;
    }

    public MapNode(String id) {
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        count++;
    }

    public String toString() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("lat", lat);
        json.put("lon", lon);
        return json.toJSONString();
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}


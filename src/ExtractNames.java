import org.json.JSONObject;
import sun.security.provider.certpath.Vertex;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Sylvester on 04/05/2015.
 */
public class ExtractNames {

    public ExtractNames() {

    }
    String content;
    HashMap<String, String> export = new HashMap<String, String>();


    public void getNames() {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();

        try {
            content = new Scanner(new File("data.txt")).useDelimiter("\\Z").next();

            JSONObject jObject = new JSONObject(content);
            Iterator<?> keys = jObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();

                if (jObject.get(key) instanceof JSONObject) {
                    JSONObject jObj = (JSONObject) jObject.get(key);
                    export.put(jObj.getString("name"), jObj.getString("id"));
                }
            }

            org.json.simple.JSONObject json = new org.json.simple.JSONObject();
            json.putAll(export);

            FileWriter file = new FileWriter("names.txt");
            file.write(json.toJSONString());
            file.flush();
            file.close();

            System.out.println(export.size() + " vertices");
            System.out.println(export.toString());
        }catch (Exception e) {
            System.out.println("Exception e");}
    }


}

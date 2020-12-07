package gameClient;

import api.*;
import com.google.gson.*;
import gameClient.util.Point3D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class loadForGames {
    static directed_weighted_graph LoadGraphFromJson(String str) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(directed_weighted_graph.class, new graphJsonDeserializer());
        Gson gson = builder.create();
        directed_weighted_graph graph = gson.fromJson(str, directed_weighted_graph.class);
        return graph;
    }
    public static ArrayList<CL_Pokemon> LoadPokemonsFromJson(String fs) {
        ArrayList<CL_Pokemon> ans = new  ArrayList<CL_Pokemon>();
        try {
            JSONObject ttt = new JSONObject(fs);
            JSONArray ags = ttt.getJSONArray("Pokemons");
            for(int i=0;i<ags.length();i++) {
                JSONObject pp = ags.getJSONObject(i);
                JSONObject pk = pp.getJSONObject("Pokemon");
                int t = pk.getInt("type");
                double v = pk.getDouble("value");
                //double s = 0;//pk.getDouble("speed");
                String p = pk.getString("pos");
                CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, null);
                ans.add(f);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
        return ans;
    }
    public static ArrayList<CL_Agent> LoadAgentsFromJson(String aa, directed_weighted_graph gg) {
        ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
        try {
            JSONObject ttt = new JSONObject(aa);
            JSONArray ags = ttt.getJSONArray("Agents");
            for(int i=0;i<ags.length();i++) {
                CL_Agent c = new CL_Agent(gg,0);
                c.update(ags.get(i).toString());
                ans.add(c);
            }
            //= getJSONArray("Agents");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ans;
    }

















    /////////////////////////////////////////////////////////////////////////////////////


    private static class graphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {

        @Override
        public  directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray nodes=jsonObject.get("Nodes").getAsJsonArray();
            node_data currNode;
            directed_weighted_graph g0=new DWGraph_DS();
            for(JsonElement curr: nodes){
                currNode=new NodeData(curr.getAsJsonObject().get("id").getAsInt(),posToDouble(curr.getAsJsonObject().get("pos").getAsString()));
                g0.addNode(currNode);
            }
            JsonArray edges=jsonObject.get("Edges").getAsJsonArray();
            for(JsonElement curr :edges){
                int src=curr.getAsJsonObject().get("src").getAsInt();
                int dest=curr.getAsJsonObject().get("dest").getAsInt();
                double w=curr.getAsJsonObject().get("w").getAsDouble();
                g0.connect(src,dest,w);
            }
            return g0;
        }
    }
    private static geoLocation posToDouble(String str){
        String[] a = str.split(",");
        geoLocation ans =new geoLocation(Double.parseDouble(a[0]),Double.parseDouble(a[1]),Double.parseDouble(a[2]));
        return ans;
    }


}
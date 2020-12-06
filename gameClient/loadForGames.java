package gameClient;

import api.*;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class loadForGames {
    static directed_weighted_graph LoadGraphFromJson(String str) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(directed_weighted_graph.class, new graphJsonDeserializer());
        Gson gson = builder.create();
        directed_weighted_graph graph = gson.fromJson(str, directed_weighted_graph.class);
        return graph;
    }
    static List<CL_Pokemon> LoadPocimonsFromJson(String str){
        return null;
    }
    static List<CL_Agent> LoadAgentstsFromJson (String str){return null;}
















    /////////////////////////////////////////////////////////////////////////////////////


    private static class graphJsonDeserializer implements JsonDeserializer<directed_weighted_graph>, gameClient.stringJsonDeserializer {

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
            System.out.println(g0);
            return g0;
        }
    }
    private static geoLocation posToDouble(String str){
        String[] a = str.split(",");
        geoLocation ans =new geoLocation(Double.parseDouble(a[0]),Double.parseDouble(a[1]),Double.parseDouble(a[2]));
        return ans;
    }
}
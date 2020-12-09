package gameClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;

public class Ex2 {
    private Arena arena;















    ///////////////////////////////////////


    private static void updateAgents(String json,ArrayList<CL_Agent> agents) {
        try {
            JSONObject ttt = new JSONObject(json);
            JSONArray ags = ttt.getJSONArray("Agents");
            for(int i=0;i<ags.length();i++) {
                agents.get(i).update(ags.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}












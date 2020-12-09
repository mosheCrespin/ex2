package gameClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;

public class Ex2 {
    private Arena arena;

    ///////////////////////////////////////

    private static int numberOfAgents(String str) {
        try {
            JSONObject ttt = new JSONObject(str);
            JSONObject object = ttt.getJSONObject("GameServer");
            return object.getInt("agents");

        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }
    private static void updateagents(String json,ArrayList<CL_Agent> agents) {
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












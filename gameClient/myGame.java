package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;

public class myGame {

    public static void main(String[] args) throws FileNotFoundException, JSONException {
        game_service game = Game_Server_Ex2.getServer(0);
        directed_weighted_graph g0 = loadForGames.LoadGraphFromJson(game.getGraph());
        int numberOfAgents = numberOfAgents(game.toString());
        ArrayList<CL_Pokemon> pokemons = loadForGames.LoadPokemonsFromJson(game.getPokemons());
        for (CL_Pokemon curr : pokemons) {//upadate in which edge the Pokemon sitting rn
            Arena.updateEdge(curr, g0);
        }
        game.addAgent(1);
        game.startGame();
        ArrayList<CL_Agent> agents = loadForGames.LoadAgentsFromJson(game.getAgents(), g0);
        HashMap<CL_Agent, pokimonForAgents> mission = new HashMap<>();
        for (CL_Agent agent : agents) {
            mission.put(agent, new pokimonForAgents(pokemons, g0));
        }
        boolean flag=false;
        while (game.isRunning()) {
            for (Map.Entry<CL_Agent, pokimonForAgents> a : mission.entrySet()) {
                updateagents(game.getAgents(), agents);//should be inside thread
                if(a.getKey().getNextNode()==-1) {//its not working---i think that there is problem with the server
                    flag=true;
                    int b=a.getValue().whereShouldIGoNow(a.getKey().getSrcNode());
                    System.out.println(b+"..................");
                    game.chooseNextEdge(a.getKey().getID(),b);
                }
            }
            if(flag) {
                System.out.println(game.move());
                flag=false;
            }

        }
//        System.out.println(game.toString());

    }


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
                System.out.println(agents.get(i).getID());
//                System.out.println(ags.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}












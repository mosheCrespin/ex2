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
import java.util.ArrayList;
import java.util.Map;
import java.util.SplittableRandom;

public class myGame {

    public static void main(String[] args) throws FileNotFoundException, JSONException {
        game_service game = Game_Server_Ex2.getServer(16);
        game.startGame();
        directed_weighted_graph g0=loadForGames.LoadGraphFromJson(game.getGraph());
        int numberOfAgents = numberOfAgents(game.toString());
        ArrayList<CL_Pokemon>pokemons=loadForGames.LoadPokemonsFromJson(game.getPokemons());
        int i=0;
        for(CL_Pokemon curr:pokemons){//upadate in which edge the Pokemon sitting rn
            Arena.updateEdge(curr,g0);
        }
        int []array=new int [numberOfAgents];
    }

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
}












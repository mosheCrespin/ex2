package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.game_service;
import api.node_data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class threadAgents implements Runnable{
    private final Arena arena;
    private final DWGraph_Algo graphAlgo;
    private CL_Agent agent;
    private game_service game;


    public threadAgents(DWGraph_Algo graph_algo,Arena arena,CL_Agent agent,game_service game){
        this.arena=arena;
        this.graphAlgo=graph_algo;
        this.agent=agent;
        this.game=game;
    }
    @Override
    public void run() {
        whereShouldIGo(this.agent);
        int nextNode=agent.getNextNodeViaIterator();
        game.chooseNextEdge(agent.getID(), nextNode);
        while (game.isRunning()) {
           while(nextNode!=-1) {
                try {
                        for (int i = 0; i < 3; i++) {//should work on that one
                            Thread.sleep((long) agent.getSpeed() / 5 * 1000);
                            updateAgent(game.getAgents());
                        }
                    }
                 catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(agent.dest==-1) {
                    nextNode = agent.getNextNodeViaIterator();
                    if(nextNode!=-1)
                         game.chooseNextEdge(agent.getID(), nextNode);
                }
            }
               arena.setPokemons(game.getPokemons());
               whereShouldIGo(agent);
               nextNode=agent.getNextNodeViaIterator();
               game.chooseNextEdge(agent.getID(), nextNode);

           }
        }




    private double timeToGetToPokimon(CL_Pokemon pokemon,CL_Agent agent){
        double weights=graphAlgo.shortestPathDist(agent.getSrcNode(),pokemon.get_edge().getSrc());
        if(weights==-1) return -1;
        weights+=pokemon.get_edge().getWeight();
        return weights/agent.getSpeed();
    }
    private void whereShouldIGo(CL_Agent agent){
        ArrayList<CL_Pokemon> pokemons=arena.getPokemons();
        CL_Pokemon min=pokemons.get(0);
        double tempSDT;
        double minSDT=Double.MAX_VALUE;
        for(int i=0;i<pokemons.size();i++){
            if(!pokemons.get(i).isBusy()) {//check if the pokimon is busy
                tempSDT = timeToGetToPokimon(pokemons.get(i), agent);
                if ((tempSDT < minSDT)&&tempSDT>=0) {
                    min = pokemons.get(i);
                    minSDT = tempSDT;
                }
            }
        }
        agent.set_curr_fruit(min);
        min.setIsBusy(true);
        List<node_data> path=this.graphAlgo.shortestPath(agent.getSrcNode(),min.get_edge().getSrc());
        path.add(arena.getGraph().getNode(min.get_edge().getDest()));
        agent.setCurrPath(path);
    }



    private void updateAgent(String json) {
        try {
            JSONObject ttt = new JSONObject(json);
            JSONArray ags = ttt.getJSONArray("Agents");
            agent.update(ags.get(agent.getID()).toString());
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

}
}

package gameClient;
import api.DWGraph_Algo;
import api.edge_data;
import api.game_service;
import api.node_data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class threadAgents implements Runnable{
    private final Arena arena;
    private final DWGraph_Algo graphAlgo;
    private CL_Agent agent;
    private game_service game;
    private double [][] distanceArr;
    private List<node_data> [][]pathes;
    private List<node_data> currPath;
    private Iterator<node_data> currIterator;

    public threadAgents(List<node_data> [][]path ,double[][] distanceArr,DWGraph_Algo graph_algo,Arena arena,CL_Agent agent,game_service game){
        this.arena=arena;
        this.graphAlgo=graph_algo;
        this.agent=agent;
        this.game=game;
        this.distanceArr= distanceArr;
        this.pathes=path;
    }
    @Override
    public void run() {
        whereShouldIGo(this.agent);
        game.chooseNextEdge(agent.getID(), this.currIterator.next().getKey());
        while (game.isRunning()) {
            while (this.currIterator.hasNext()&&this.agent.get_curr_fruit().isStillFood()) {//there is still path to this pokemon
                updateAgent(game.getAgents());
                    if (!agent.isMoving()) {//check if this agent is not moving RN
                            int nextNode = this.currIterator.next().getKey();
                            isTherePokemonInThisEdge(agent.getSrcNode(), nextNode);
                            game.chooseNextEdge(agent.getID(), nextNode);
                    }
            }
            //just eat the pokemon so now find a new one to eat
            agent.get_curr_fruit().setIsBusy(false);
            agent.get_curr_fruit().setIsStillFood(false);
            arena.setPokemons(game.getPokemons());
            whereShouldIGo(agent);
            game.chooseNextEdge(agent.getID(), this.currIterator.next().getKey());

        }
    }
      private void isTherePokemonInThisEdge(int src, int dest){
          edge_data currEdge=graphAlgo.getGraph().getEdge(src,dest);
          for(CL_Pokemon currP: arena.getPokemons()){
              if(currP.get_edge()==currEdge) {
                  currP.setIsStillFood(false);
              }
          }
      }
    private double timeToGetToPokemon(CL_Pokemon pokemon, CL_Agent agent){
        double weights = this.distanceArr[agent.getSrcNode()][pokemon.get_edge().getSrc()];
        if (weights == -1) return -1;
            weights += pokemon.get_edge().getWeight();
            return weights / agent.getSpeed();
    }
    private double value(CL_Pokemon pokemon,CL_Agent agent){
        double time= timeToGetToPokemon(pokemon,agent);
            return time /pokemon.getValue();
    }
    private void whereShouldIGo(CL_Agent agent){
        ArrayList<CL_Pokemon> pokemons=arena.getPokemons();
        CL_Pokemon min=pokemons.get(0);
        double tempSDT;
        double minSDT=Double.MAX_VALUE;
        for (CL_Pokemon pokemon : pokemons) {
            if (!pokemon.isBusy()) {//check if the pokimon is busy
                tempSDT = value(pokemon, agent);
                if ((tempSDT < minSDT) && tempSDT >= 0) {
                    min = pokemon;
                    minSDT = tempSDT;
                }
            }
        }
        agent.set_curr_fruit(min);
        min.setIsBusy(true);
        min.setIsStillFood(true);
        this.currPath=this.pathes[agent.getSrcNode()][min.get_edge().getSrc()];
        this.currPath.add(arena.getGraph().getNode(min.get_edge().getDest()));
        this.currIterator=this.currPath.listIterator();
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

package gameClient;

import api.DWGraph_Algo;
import api.edge_data;
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
    private double [][] distanceArr;



    public threadAgents(double[][] distanceArr,DWGraph_Algo graph_algo,Arena arena,CL_Agent agent,game_service game){
        this.arena=arena;
        this.graphAlgo=graph_algo;
        this.agent=agent;
        this.game=game;
        this.distanceArr= distanceArr;
    }
    @Override
    public void run() {
        whereShouldIGo(this.agent);
        int nextNode=agent.getNextNodeViaIterator();
        game.chooseNextEdge(agent.getID(), nextNode);
        while (game.isRunning()) {
           while(nextNode!=-1) {
               updateAgent(game.getAgents());
                if(!agent.isMoving()) {//check if this agent is not moving RN
                    if(this.agent.get_curr_fruit().isBusy()) {//check if the Pokemon still exist, false means some other agent eat this pokemon
                        nextNode = agent.getNextNodeViaIterator();
                        if (nextNode != -1) {//there is still path to this pokemon
//                            if(isTherePokemonInThisEdge(agent.getSrcNode(),nextNode)) {
                            isTherePokemonInThisEdge(agent.getSrcNode(), nextNode);
                            game.chooseNextEdge(agent.getID(), nextNode);
                            game.move();
//                                game.chooseNextEdge(agent.getID(), nextNode);
//                                game.move();
//                                while(agent.isMoving()){
//                                    try {
//                                        game.move();
//                                        updateAgent(game.getAgents());
//                                        Thread.sleep(30);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }

//                        }
                        }
                    }
                    else{//some agent did eat this pokemon
                                arena.setPokemons(game.getPokemons());//update the pokemons
                                whereShouldIGo(agent);//find me a new path
                                nextNode = agent.getNextNodeViaIterator();
                                game.chooseNextEdge(agent.getID(), nextNode);
                            }
                            nextNode = agent.getNextNodeViaIterator();
                        }
                    }
            }//just eat the pokemon so now find a new one to eat
               agent.get_curr_fruit().setIsBusy(false);
               arena.setPokemons(game.getPokemons());
               whereShouldIGo(agent);
               nextNode=agent.getNextNodeViaIterator();
               game.chooseNextEdge(agent.getID(), nextNode);

           }

      private void isTherePokemonInThisEdge(int src, int dest){
          edge_data currEdge=graphAlgo.getGraph().getEdge(src,dest);
//          boolean flag=false;
          for(CL_Pokemon currP: arena.getPokemons()){
              if(currP.get_edge()==currEdge) {
                  currP.setIsBusy(false);
//                 flag=true;
              }
          }
      }



      private void eatAnotherPokemon(int src,int dest){
          edge_data currEdge=graphAlgo.getGraph().getEdge(src,dest);
          for(CL_Pokemon currP: arena.getPokemons()){
              if(currP.get_edge()==currEdge)
                  currP.setIsBusy(false);
          }
      }

    private double timeToGetToPokimon(CL_Pokemon pokemon,CL_Agent agent){
        double weights = this.distanceArr[agent.getSrcNode()][pokemon.get_edge().getSrc()];
        if (weights == -1) return -1;
            weights += pokemon.get_edge().getWeight();
            return weights / agent.getSpeed();
    }
    private double value(CL_Pokemon pokemon,CL_Agent agent){
        double time=timeToGetToPokimon(pokemon,agent);
        if(time==-1) return -1;
            return time/pokemon.getValue();
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
//        List path=this.pathArr[agent.getSrcNode()][min.get_edge().getSrc()];

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

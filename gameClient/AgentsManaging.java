package gameClient;
import api.DWGraph_Algo;
import api.edge_data;
import api.game_service;
import api.node_data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AgentsManaging implements Runnable{
    private final Arena arena;
    private final DWGraph_Algo graphAlgo;
    private CL_Agent agent;
    private game_service game;
    private double [][] distanceArr;
    private List<node_data> [][]pathes;
    private ArrayList<Integer> repeat;
    private ArrayList<node_data> _currPath;
    private int iterator;

    /**
     * constructor for agents that get the data about the agents and keep it.
     * @param path
     * @param distanceArr
     * @param graph_algo
     * @param arena
     * @param agent
     * @param game
     */
    public AgentsManaging(List<node_data> [][]path , double[][] distanceArr, DWGraph_Algo graph_algo, Arena arena, CL_Agent agent, game_service game){
        this.arena=arena;
        this.graphAlgo=graph_algo;
        this.agent=agent;
        this.game=game;
        this.distanceArr= distanceArr;
        this.pathes=path;
        this.repeat=new ArrayList<>();
    }

    /**
     ** this method run all the time the game is running and check for
     * the agents where to go to eat pokemons.
     */
    @Override
    public void run() {
        int nextNode;
        whereShouldIGo();
        while (game.isRunning()) {
            while (iterator<this._currPath.size()&&this.agent.get_curr_fruit().isStillFood()) {//there is still path to this pokemon
                updateAgent(game.getAgents());
                    if (!agent.isMoving()) {//check if this agent is not moving RN
                            nextNode=this._currPath.get(iterator).getKey();
                            iterator++;
                            game.chooseNextEdge(agent.getID(), nextNode);
                            repeat.add(nextNode);
                            isTherePokemonInThisEdge(agent.getSrcNode(), nextNode);
                            if(repeat.size()==6){
                                strike();
                            }
                        System.out.println("agent :" + agent.getID()+ " moved from: " + agent.getSrcNode()
                                    + " to: " +nextNode );
                    }
            }
            //just eat the pokemon so now find a new one to eat
            updateAgent(game.getAgents());
            while(agent.isMoving()){
                updateAgent(game.getAgents());
            }
            agent.get_curr_fruit().setIsBusy(false);
            agent.get_curr_fruit().setIsStillFood(false);
            whereShouldIGo();
            if(iterator<this._currPath.size()) {
                nextNode = this._currPath.get(iterator).getKey();
                game.chooseNextEdge(agent.getID(), nextNode);
                this.iterator++;
                System.out.println("agent :" + agent.getID() + " moved from: " + agent.getSrcNode()
                        + " to: " + nextNode);
                isTherePokemonInThisEdge(agent.getSrcNode(), nextNode);
            }
        }
    }
//TODO
    /**
     * this method get src and dest  run over all the pokemons in the graph and check if
     * there is a pokemon between the src and the dest.if so this pokemon become   .
     * @param src
     * @param dest
     */
      private void isTherePokemonInThisEdge(int src, int dest){
          edge_data currEdge=graphAlgo.getGraph().getEdge(src,dest);
          for(CL_Pokemon currP: arena.getPokemons()) {
              if (currP != this.agent.get_curr_fruit()) {
                  if (currP.get_edge() == currEdge) {
                      currP.setIsStillFood(false);
                      currP.setIsBusy(false);
                  }
              }
          }
      }

     private void strike() {
         if (repeat.get(0) == repeat.get(2) && repeat.get(2) == repeat.get(4))
             if (repeat.get(1) == repeat.get(3) && repeat.get(3) == repeat.get(5)){
                 whereShouldIGo();
                 int next=this._currPath.get(iterator).getKey();
                 game.chooseNextEdge(agent.getID(),next);
                 }
         this.repeat=new ArrayList<>();
     }

    /**
     * this method calculate how much time it's take to the agent to eat the pokemon.
     * this calculate by the weights / agent.getSpeed().
     * @param pokemon
     * @return
     */
    private double timeToGetToPokemon(CL_Pokemon pokemon){
        double weights = this.distanceArr[agent.getSrcNode()][pokemon.get_edge().getSrc()];
        if (weights == -1) return -1;
            weights += pokemon.get_edge().getWeight();
            return weights / agent.getSpeed();
    }

    /**
     * this method return the value the pokemon equal by the time it's will take to the agents to get the pokemon divide
     * the pokemon's value.
     * @param pokemon
     * @return
     */
    private double value(CL_Pokemon pokemon){
        double time= timeToGetToPokemon(pokemon);
            return time /pokemon.getValue();
    }

    /**
     * the 'whereShouldIGo()' method run over all the pokemons that existent in this moment and check
     * whice of them is closest and also free.the method say to the agent to go to the this pokemon.
     */
    private void whereShouldIGo(){
        arena.setPokemons(game.getPokemons());
        ArrayList<CL_Pokemon> pokemons=arena.getPokemons();
        CL_Pokemon min =null;
        double tempSDT;
        double minSDT=Double.MAX_VALUE;
        for (CL_Pokemon pokemon : pokemons) {
            if (!pokemon.isBusy()) {//check if the pokimon is busy
                tempSDT = value(pokemon);
                if ((tempSDT < minSDT) && tempSDT >= 0) {
                    min = pokemon;
                    minSDT = tempSDT;
                }
            }
        }
        if(min!=null) {
            agent.set_curr_fruit(min);
            min.setIsBusy(true);
            min.setIsStillFood(true);
            updateAgent(game.getAgents());
            this._currPath=new ArrayList<>(this.pathes[agent.getSrcNode()][min.get_edge().getSrc()]);
            this._currPath.add(arena.getGraph().getNode(min.get_edge().getDest()));
            this.iterator=1;
        }
        else{
            this._currPath=new ArrayList<>();
        }
    }

    /**
     * this method get String json and update the agent to CL_Agent.
     * @param json
     */
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

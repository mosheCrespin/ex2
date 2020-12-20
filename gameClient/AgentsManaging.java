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
     * this method is managing the single agent while the game is running
     * the algorithm goes like that-
     * 1. check where is the best pokemon for this agent is allocate (using `whereShouldIGo` method)
     * 2. now there is a target for this agent so enter start to do these simple things:
     * 3. while the game is running there are 2 possible options:
     * 3.1 this agent still has the old pokemon so when the agent is not moving do these things:
     * 3.1.1 first tell the server to move forward the pokemon (one node for every call)
     * 3.1.2 check if there is another pokemon on this edge using `isTherePokemonInThisEdge` method.
     * 3.2 this agent just ate his pokemon or some other agent eat this pokemon
     * 3.2.1 wait until this agent will eat the pokemon
     * 3.2.2 find a new pokemon to eat (using `whereShouldIGo` method)
     * 3.2.3 tell the server to move forward the new pokemon
     */
    @Override
    public void run() {
        int nextNode;
        whereShouldIGo();
        while (game.isRunning()) {
            while (iterator<this._currPath.size()) {//there is still path to this pokemon
                updateAgent(game.getAgents());
                if (agent.getDest() == -1) {//check if this agent is not moving right now
                    nextNode = this._currPath.get(iterator).getKey();
                    iterator++;
                    game.chooseNextEdge(agent.getID(), nextNode);
                    repeat.add(nextNode);
                    isTherePokemonInThisEdge(agent.getSrcNode(), nextNode, false);
                    if (repeat.size() == 6) {
                        strike();
                    }
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
                isTherePokemonInThisEdge(agent.getSrcNode(), nextNode, false);//check if there is another pokemon on this edge
            }
        }
    }
    /**
     * this method get src and dest and convert them into an edge
     * this method responsible to check if there is another pokemon in this edge
     * there are 2 options for this edge
     * option 1-(flag==false)
     * while the agent is moving forward his target, in every move he check if there is another pokemon on this edge
     * if so, then tell the another agent that his pokemon is not relevant any more
     * option 2- (flag==true)
     * when the agent found via the method `whereShouldIGo()` a new target then he check if there is another pokemon on the edge of his pokemon
     * if so, then he also take this pokemon as a target.
     * @param src the src of the edge
     * @param dest the dest of the edge
     * @param flag for option 1 flag==false, for option 2 flag==true
     */
      private void isTherePokemonInThisEdge(int src, int dest,boolean flag){
          edge_data currEdge=graphAlgo.getGraph().getEdge(src,dest);
          if(currEdge==null) return;
          for(CL_Pokemon currP: arena.getPokemons()) {
              if (currP != this.agent.get_curr_fruit()) {
                  if(currP.get_edge().getSrc() == currEdge.getSrc()&&currP.get_edge().getDest()==currEdge.getDest()){
                      currP.setIsStillFood(flag);
                      currP.setIsBusy(flag);
                  }
              }
          }
      }


    /**
     * this method check if the agent is moving on the same edge for more then 3 times
     * this.repeat is responsible to remember the last six moves of the agent
     * if repeat[0]==repeat[2]==repeat[4] and repeat[1]==repeat[3]==repeat[5]
     * then it means the agent goes back and forth on the same edge
     * if so then find a new target to strike
     */

    private void strike() {
         if (repeat.get(0) == repeat.get(2) && repeat.get(2) == repeat.get(4))
             if (repeat.get(1) == repeat.get(3) && repeat.get(3) == repeat.get(5)) {
                 whereShouldIGo();
             }
         this.repeat=new ArrayList<>();
     }

    /**
     * this method get a pokemon and check How long will it take for the agent to reach this Pokemon
     * this method is using the shortest path Distance algorithm (Dijkstra algorithm) to find how much weight it take for the agent to reach this pokemon
     * if there is no path then return -1.
     * else return the `Time` calculate by (the path in weights for this pokemon) / (speed of this agent)
     * @param pokemon
     * @return  -1 or `Time`
     */
    private double timeToGetToPokemon(CL_Pokemon pokemon){
        double weights = this.distanceArr[agent.getSrcNode()][pokemon.get_edge().getSrc()];
        if (weights == -1) return -1;
            weights += pokemon.get_edge().getWeight();
            return weights / agent.getSpeed();
    }

    /**
     * this method calculate the `General Grade` for this pokemon
     * the `General Grade` is the result of the following calculation
     * if the value<=0 then the `General Grade` will be -1
     * else the `General Grade` will be this calculation: (time to get to this pokemon)/ (the value of this pokemon)
     * `time to get to this pokemon` is the return value of `timeToGetToPokemon()` method
     * @param pokemon
     * @return  `General Grade`
     */
    private double value(CL_Pokemon pokemon){
        if(pokemon.getValue()<=0) return -1;// if this pokemon is not worth it
        double time= timeToGetToPokemon(pokemon);
            return time /pokemon.getValue();
    }

    /**
     * this method run over all the Pokemons that is not busy(meaning that there is no another agent that moving forward this pokemon)
     * this method checks which of these pokemons has the best Grade
     * the Grade is the return value of `value()` method
     * the best pokemon is the one that has the `lowest` Grade
     * if there is no pokemon then the agent will wait for another pokemon
     * if a pokemon were found then:
     * 1. Determine the agent that this pokemon is his target
     * 2. set this.path to be the path for this pokemon and set iterator(int)=2
     * 3. tell the server to move forward this pokemon
     */
    private void whereShouldIGo(){
        arena.setPokemons(game.getPokemons());//update all the pokemons
        ArrayList<CL_Pokemon> pokemons=arena.getPokemons();
        CL_Pokemon min =null;
        double tempSDT;
        double minSDT=Double.MAX_VALUE;
        for (CL_Pokemon pokemon : pokemons) {
            if (!pokemon.isBusy()) {//check if the pokemon is busy
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
            int nextNode= this._currPath.get(iterator).getKey();
            game.chooseNextEdge(agent.getID(), nextNode);//tell the server to move forward this pokemon
            iterator++;//this._currPath[0]== curr location of this agent, this._currPath[1]== next location of this agent (just told the server to go there)
            isTherePokemonInThisEdge(min.get_edge().getSrc(),min.get_edge().getDest(),true);//check if there is another pokemon on this pokemon edge
        }
        else{
            this._currPath=new ArrayList<>();
        }
    }

    /**
     * this method get the curr update of all the agents by calling the server via game.getAgents()
     * the server return the update via Json
     * then this method takes the relevant information of the curr agent (using ID)
     * the information sends to the method `update` of this agent
     * @param json `game.getAgents`
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

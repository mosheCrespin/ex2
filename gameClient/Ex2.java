package gameClient;
import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.game_service;
import api.node_data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;

public class Ex2 implements Runnable {
    private Arena arena;
    private DWGraph_Algo graphAlgo;


    public static void main(String[] args) {
        Thread main = new Thread(new Ex2());
        main.start();
    }

    @Override
    public void run() {
        int level = 20;
        game_service game = Game_Server_Ex2.getServer(level);
        init(game);
        Thread[] arrThreadOfAgents = new Thread[arena.getNumberOfAgents()];
        for (int i = 0; i < arrThreadOfAgents.length; i++) {
            arrThreadOfAgents[i] = new Thread(new threadAgents(this.graphAlgo, arena, arena.getAgents().get(i), game));
        }
        for (Thread arrThreadOfAgent : arrThreadOfAgents) arrThreadOfAgent.start();
        Thread move = new Thread(new moveMethod(arena, game));
        move.start();
    }

    private void init(game_service game) {
        this.arena = new Arena();
        this.graphAlgo = new DWGraph_Algo();
        this.arena.setGraph(Arena.LoadGraphFromJson(game.getGraph()));
        this.graphAlgo.init(this.arena.getGraph());

        this.arena.setPokemons(game.getPokemons());
        this.arena.setNumberOfAgents(game.toString());
        int[] ArrayAgents = new int[this.arena.getNumberOfAgents()];
        int j = 0;
        while (ArrayAgents.length != j) {
            CL_Pokemon MinPok = PokemonToRun();
            game.addAgent(MinPok.get_edge().getSrc());//init the Agent.
            ArrayAgents[j] = MinPok.get_edge().getDest();
            game.chooseNextEdge(j, ArrayAgents[j]);//start to move
            j++;
        }
        this.arena.setAgents(Arena.getAgents(game.getAgents(), arena.getGraph()));
        game.startGame();
        System.out.println(game.move());
    }


    public CL_Pokemon PokemonToRun() {
        List<CL_Pokemon> PokemonsList=new ArrayList<CL_Pokemon>();
        PokemonsList = this.arena.getPokemons();
        CL_Pokemon TakeThisPokemon = PokemonsList.get(0);
        int i = 1;
        while (PokemonsList.size() != i) {//run over all the Pokemons and find the worthwhile Pokemon to run.
            if (!PokemonsList.get(i).isBusy()) {//if the Pokemon isn't busy ,check if he worthwhile for the agent.
                if (PokemonsList.get(i).get_edge().getWeight() / PokemonsList.get(i).getValue() < TakeThisPokemon.get_edge().getWeight() / TakeThisPokemon.getValue())
                    TakeThisPokemon = PokemonsList.get(i);
            }
            i++;
        }
        TakeThisPokemon.setIsBusy(true);//update that this Pokemon is busy now.
        return TakeThisPokemon;//return where to init the Agent.
    }


//    private void init(game_service game) {
//        this.arena=new Arena();
//        List<CL_Pokemon> list=new ArrayList<CL_Pokemon>();
//        this.arena.setGraph(Arena.LoadGraphFromJson(game.getGraph()));
//        this.arena.setPokemons(game.getPokemons());
//        this.arena.setNumberOfAgents(game.toString());
//        this.graphAlgo=new DWGraph_Algo();
//        this.graphAlgo.init(arena.getGraph());
//        for(int i=0;i<arena.getNumberOfAgents();i++){
//            game.addAgent(i);
//        }
//        List<CL_Agent> agents=Arena.getAgents(game.getAgents(), arena.getGraph());
//        arena.setAgents(agents);
//        game.startGame();
//
//
//        //game.getAgents()
//    }


}







//
//    private double timeToGetToPokimon(CL_Pokemon pokemon,CL_Agent agent){
//        double weights=graphAlgo.shortestPathDist(agent.get_curr_edge().getSrc(),pokemon.get_edge().getSrc());
//        if(weights==-1) return -1;
//        weights+=pokemon.get_edge().getWeight();
//        return weights/agent.getSpeed();
//    }
//    private void whereShouldIGo(CL_Agent agent){
//        ArrayList<CL_Pokemon> pokemons=arena.getPokemons();
//        CL_Pokemon min=pokemons.get(0);
//        double tempSDT;
//        double minSDT=Double.MAX_VALUE;
//        for(int i=0;i<pokemons.size();i++){
//            if(!pokemons.get(i).isBusy()) {//check if the pokimon is busy
//                tempSDT = timeToGetToPokimon(pokemons.get(i), agent);
//                if ((tempSDT < minSDT)&&tempSDT>=0) {
//                    min = pokemons.get(i);
//                    minSDT = tempSDT;
//                }
//            }
//        }
//        agent.set_curr_fruit(min);
//        min.setIsBusy(true);
//        List<node_data> path=this.graphAlgo.shortestPath(agent.get_curr_edge().getSrc(),min.get_edge().getSrc());
//        path.add(arena.getGraph().getNode(min.get_edge().getDest()));
//        agent.setCurrPath(path);
//    }

















    ///////////////////////////////////////













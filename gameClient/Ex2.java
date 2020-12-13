package gameClient;
import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.game_service;
import api.node_data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Ex2 implements Runnable {
    private Arena arena;
    private DWGraph_Algo graphAlgo;
    private static MyJFrame _win;
    private double [][] distance;
    private List[][] path;



    public static void main(String[] args) {
        Thread main = new Thread(new Ex2());
        main.start();
    }

    @Override
    public void run() {
        int level = 11;
        game_service game = Game_Server_Ex2.getServer(level);
        System.out.println(game.getPokemons());
        init(game);
        Thread[] arrThreadOfAgents = new Thread[arena.getNumberOfAgents()];
        for (int i = 0; i < arrThreadOfAgents.length; i++) {
            arrThreadOfAgents[i] = new Thread(new threadAgents(this.distance, this.graphAlgo, arena, arena.getAgents().get(i), game));
        }
        for (Thread arrThreadOfAgent : arrThreadOfAgents) arrThreadOfAgent.start();
        Thread move = new Thread(new moveMethod(arena, game));
        move.start();

        int ind = 0;
        long dt =20;
        while (game.isRunning()) {
            try {
                   _win.repaint();
                    Thread.sleep(dt);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }



    private void init(game_service game) {
        this.arena = new Arena();
        this.graphAlgo = new DWGraph_Algo();
        this.arena.setGraph(Arena.LoadGraphFromJson(game.getGraph()));
        this.graphAlgo.init(this.arena.getGraph());
        distnaceArr();
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
        //
        _win = new MyJFrame("test Ex2",arena);
        _win.setSize(800, 600);
        _win.show();
        _win.setTitle("Catch me if U can!" + game.toString());
        //
        game.startGame();
        System.out.println(game.move());
    }


    public CL_Pokemon PokemonToRun() {
        List<CL_Pokemon> PokemonsList;
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

    private void distnaceArr(){
        int nodeSize=graphAlgo.getGraph().nodeSize();
        this.distance=new double[nodeSize][nodeSize];
        this.path=new List[nodeSize][nodeSize];
        for(int i=0;i<nodeSize;i++)
            for(int j=0;j<nodeSize;j++) {
                this.distance[i][j] = graphAlgo.shortestPathDist(i, j);
            }
        if(!graphAlgo.isConnected()) not_a_trap();
    }
    private void not_a_trap(){
        int nodeSize=graphAlgo.getGraph().nodeSize();
        for(int i=0;i<nodeSize;i++)
            for(int j=0;j<nodeSize;j++)
                if(((this.distance[i][j]==-1)&&(this.distance[j][i]!=-1))||(this.distance[j][i]==-1&&this.distance[i][j]!=-1)) {
                    this.distance[i][j] = -1;
                    this.distance[j][i]=-1;
                }

    }

}













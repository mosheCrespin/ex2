package gameClient;
import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.game_service;


import javax.swing.*;
import java.util.List;

public class Ex2 implements Runnable {
    private Arena arena;
    private DWGraph_Algo graphAlgo;
    private static MyJFrame _win;
    private double [][] distance;
    private List[][] path;
    private int level;
    private int id;
    MyLoginPage entrancePage;





    public static void main(String[] args) {
        Thread main = new Thread(new Ex2());
        main.start();
    }


    @Override
    public void run() {
        entrancePage();
        game_service game = Game_Server_Ex2.getServer(this.level);
        if(this.id!=-1)
            game.login(this.id);
        System.out.println(game.getPokemons());
        init(game);
        Thread[] arrThreadOfAgents = new Thread[this.arena.getNumberOfAgents()];
        for (int i = 0; i < arrThreadOfAgents.length; i++) {
            arrThreadOfAgents[i] = new Thread(new threadAgents(this.path,this.distance, this.graphAlgo, arena, arena.getAgents().get(i), game));
        }
        for (Thread arrThreadOfAgent : arrThreadOfAgents) arrThreadOfAgent.start();
        Thread move = new Thread(new moveMethod(arena, game));
        move.start();
        long dt =20;
        while (game.isRunning()) {
            try {
                   this._win.repaint();
                    Thread.sleep(dt);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(game.toString());
        System.exit(0);
    }


    public void entrancePage(){
        this.entrancePage=new MyLoginPage();
        while(!this.entrancePage.get_user_successfuly_connected()){
            System.out.println("waiting for input");
        }
        System.out.println("starting game...");
        this.level=entrancePage.getLevel_num();
        this.id=entrancePage.getId_num();
        if(!entrancePage.get_user_enterd_id()) this.id=-1;
        entrancePage.setVisible(false);
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
        MyPanel panel=new MyPanel(arena);
        _win.add(panel);
        _win.show();
        _win.setTitle("Catch me if U can!" + game.toString());
        _win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //
        game.startGame();
        System.out.println(game.move());
        game.move();
        game.move();
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
        pathArr(nodeSize);
        this.distance=new double[nodeSize][nodeSize];
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
    private void pathArr(int nodeSize){
        this.path=new List[nodeSize][nodeSize];
        for(int i=0;i<nodeSize;i++)
            for(int j=0;j<nodeSize;j++) {
                this.path[i][j] = graphAlgo.shortestPath(i, j);
            }
    }

}













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
    private static int _level;
    private static int _id;
    MyLoginPage entrancePage;
    private static boolean cmdInput;

    public static void main(String[] args) {
        Thread main = new Thread(new Ex2());

        if((args.length==0)||(args.length==1))
            cmdInput=false;
        else{
            String id=args[0];
            String level=args[1];
            if(id.length()>10||level.length()>10)
            {
               cmdInput=false;//not entered integer
            }
            if(level.length()==0)
            {
                cmdInput=false;//level number required
            }
            boolean IdIsNumeric = id.chars().allMatch( Character::isDigit );
            boolean LevelIsNumeric=level.chars().allMatch(Character::isDigit);
            if(LevelIsNumeric&&IdIsNumeric){//check if id and level are numbers
                _id=Integer.parseInt(id);
                _level=Integer.parseInt(level);
                cmdInput= _level >= 0 && _level <= 23;
            }
        }
        if(!cmdInput &&(args.length!=0) )
            System.out.println("invalid input, going to the Entrance Page");
        if(cmdInput)
            System.out.println("Starting Game!");
        main.start();
    }

    @Override
    public void run() {
        if(!cmdInput)
            entrancePage();
        game_service game = Game_Server_Ex2.getServer(_level);
        if(_id!=-1)
            game.login(_id);
        init(game);
        Thread[] arrThreadOfAgents = new Thread[this.arena.getNumberOfAgents()];
        game.startGame();
        for (int i = 0; i < arrThreadOfAgents.length; i++) {
            arrThreadOfAgents[i] = new Thread(new threadAgents(this.path,this.distance, this.graphAlgo, arena, arena.getAgents().get(i), game));
        }
        for (Thread arrThreadOfAgent : arrThreadOfAgents) arrThreadOfAgent.start();
        game.move();
        long dt =100;
        while (game.isRunning()) {
            try {
                    game.move();
                   _win.repaint();
                    Thread.sleep(dt);
                    arena.updateInfo(game.toString(), (int) (game.timeToEnd()/1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(game.toString());
        System.exit(0);
    }

    public void entrancePage(){
        this.entrancePage=new MyLoginPage();
        while(!this.entrancePage.get_user_successfully_connected()){
            System.out.print("");
        }
        System.out.println("starting game...");
        _level=entrancePage.getLevel_num();
        _id=entrancePage.getId_num();
        if(!entrancePage.get_user_entered_id()) _id=-1;
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
        for(CL_Pokemon pokemon: arena.getPokemons()) {
            game.addAgent(pokemon.get_edge().getSrc());
        }
        this.arena.setAgents(Arena.getAgents(game.getAgents(), arena.getGraph()));
        _win = new MyJFrame("Pokemon Game",arena);
        _win.setSize(800, 600);
        MyPanel panel=new MyPanel(arena);
        _win.add(panel);
        _win.show();
        _win.setTitle("Catch me if U can!");
        _win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        arena.setInfo(game.toString());
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













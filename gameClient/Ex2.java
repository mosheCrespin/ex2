package gameClient;
import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.game_service;
import javax.swing.*;
import java.util.*;

public class Ex2 implements Runnable {
    private Arena arena;
    private DWGraph_Algo graphAlgo;
    private static JFrame _win;
    private double [][] distance;
    private List[][] path;
    private static int _level;
    private static int _id;
    private MyLoginPage entrancePage;
    private static boolean cmdInput;

    /**
     * this main() method get String from the user the ID and Level number via the Command Line.
     * this main() method check if the data in the String [] args about the id and level is correlate to the demand,if so
     * the game start,else the login page will show.
     * @param args
     */
    public static void main(String[] args) {
        Thread main = new Thread(new Ex2());
        if(args.length==0)
            cmdInput=false;
        else {
            String id = args[0];
            String level = args[1];
            if (id.length() > 10 || level.length() > 10) {
                cmdInput = false;//not entered integer
            } else {
                boolean IdIsNumeric = id.chars().allMatch(Character::isDigit);
                boolean LevelIsNumeric = level.chars().allMatch(Character::isDigit);
                if (LevelIsNumeric && IdIsNumeric) {//check if id and level are numbers
                    _id = Integer.parseInt(id);
                    _level = Integer.parseInt(level);
                    cmdInput = true;
                }
            }
        }
        if(!cmdInput &&(args.length!=0) )
            System.out.println("invalid input, going to the Entrance Page");
        if(cmdInput)
            System.out.println("Starting Game!");
        main.start();
    }

    /**
     * this method run all the time the game is running.
     *  first check if there were input via the cmd
     * if so the game starts else the entrance panel show up
     * also responsible to init the game.
     * also responsible to start the threads of the agents
     * there is a a while loop inside this method that responsible to update the panel and the move method of the server.
     * 	at the end of the game this method will print the results.
     */
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
            arrThreadOfAgents[i] = new Thread(new AgentsManaging(this.path,this.distance, this.graphAlgo, arena, arena.getAgents().get(i), game));
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


    /**
     * this method show an Entrance frame.
     * responsible to take an input from the user
     * after the input received the frame close and the frame fo the game will show up
     */
    private void entrancePage(){
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

    /**
     *this method init all the information of the game
     * including the graph of this level, the Pokemons and the agents
     * also init the frame of the game.
     * also starts the method distanceArr
     * this method also responsible to put the first agents at their location using this algorithm
     * 1. put all the pokemons inside a priority queue with a comparator
     * 2. while the q is not empty and there is more agents to put then pull a pokemon and determine a agent on this pokemon.
     * 3. if the graph is not connected then send the rest of the agents as reinforcement to the part that the graph is connected
     * @param game
     */

    private void init(game_service game) {
        this.arena = new Arena();
        this.graphAlgo = new DWGraph_Algo();
        this.arena.setGraph(Arena.LoadGraphFromJson(game.getGraph()));
        this.graphAlgo.init(this.arena.getGraph());
        distnaceArr();
        this.arena.setPokemons(game.getPokemons());
        this.arena.setNumberOfAgents(game.toString());
        int i=0;
        int ReinforcementSrc=0;
        int ReinforcementDest=0;
        Queue<CL_Pokemon> q = new PriorityQueue<>(Comparator.comparingDouble(CL_Pokemon::getValue));
        q.addAll(arena.getPokemons());
        boolean flag=false;
        CL_Pokemon curr;
        while(!flag&&!q.isEmpty()){//check if there is still agents to add
            curr=q.poll();
            if(this.distance[curr.get_edge().getSrc()][curr.get_edge().getDest()]!=-1) {//not a trap
                game.addAgent(curr.get_edge().getSrc());
                game.chooseNextEdge(i,curr.get_edge().getDest());
                i++;
                ReinforcementSrc=curr.get_edge().getSrc();
                ReinforcementDest=curr.get_edge().getDest();
            }
            if(i== arena.getNumberOfAgents()) {
                flag = true;
            }
        }
        while(i<arena.getNumberOfAgents())//if the graph is not connected then send reinforcement to the other agents
        {
            game.addAgent(ReinforcementSrc);
            game.chooseNextEdge(i,ReinforcementDest);
            i++;
        }

        this.arena.setAgents(Arena.getAgents(game.getAgents(), arena.getGraph()));
        _win=new JFrame();
        _win.setSize(800, 600);
        MyPanel panel=new MyPanel(arena);
        _win.add(panel);
        _win.setVisible(true);
        _win.setTitle("Catch me if U can!");
        _win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        arena.setInfo(game.toString());
    }

    /**
     * this method run over all the nodes in the graph and calculate the distance to all other nodes in the graph.
     * all the distances is stored in 2d matrix
     * if this graph isn't connected so call the not_a_trap() method.
     * running time will take O(n*n*logV(V+E)).
     */
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

    /**
     * this method run over the distances matrix
     * checke if distance[i][j] == -1 so  set distance[j][i] to -1.
     * running time of this method is O(n*n).
     */
    private void not_a_trap(){
        int nodeSize=graphAlgo.getGraph().nodeSize();
        for(int i=0;i<nodeSize;i++)
            for(int j=0;j<nodeSize;j++)
                if(((this.distance[i][j]==-1)&&(this.distance[j][i]!=-1))||(this.distance[j][i]==-1&&this.distance[i][j]!=-1)) {
                    this.distance[i][j] = -1;
                    this.distance[j][i]=-1;
                }

    }

    /**
     * this method get number of nodes in the graph and update the array path[][] that contains the list nodes that need to over
     * if the agent want to over from one node to another in te shortest way.
     * running time is O(n*n*logV(V+E)).
     * @param nodeSize
     */
    private void pathArr(int nodeSize){
        this.path=new List[nodeSize][nodeSize];
        for(int i=0;i<nodeSize;i++)
            for(int j=0;j<nodeSize;j++) {
                this.path[i][j] = graphAlgo.shortestPath(i, j);
            }
    }

}













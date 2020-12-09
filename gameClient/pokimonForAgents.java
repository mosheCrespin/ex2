package gameClient;

import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.node_data;

import java.util.*;

public class pokimonForAgents {
    Comparator<pokemonPath> comp;
    private ArrayList<pokemonPath> list;
    private int currLocationOfAgent;
    private directed_weighted_graph graph;
    private DWGraph_Algo graphAlgo;
    private List<node_data> currPath;
    private Iterator<node_data> currPathIterator;
//    private CL_Pokemon currPokimon;
//    private int currIndex;




    public pokimonForAgents(ArrayList<CL_Pokemon> pokimons,directed_weighted_graph graph){
        this.graph=graph;
        this.graphAlgo=new DWGraph_Algo();
        this.graphAlgo.init(this.graph);
        this.list=new ArrayList<>();
        this.comp= Comparator.comparingDouble(pokemonPath::getDistance);
        updatePokimons(pokimons);
        }


    private void updatecurrLocation(int curr){
//        if(this.currLocationOfAgent==curr) return;
        this.currLocationOfAgent=curr;
        updateList();
    }
    private void updateList(){
        for(pokemonPath curr: this.list)
        {
            curr.setDistance(this.currLocationOfAgent);
        }
        this.list.sort(comp);
        setCurrPath();
    }
    private void setCurrPath(){
        int i=0;
        while(list.get(i).getPokimon().isBusy()){
            i++;
        }
        list.get(i).getPokimon().setIsBusy(true);
        this.currPath= list.get(i).getPath(this.currLocationOfAgent);
        this.currPathIterator=this.currPath.listIterator();
//        this.currPokimon=list.get(i).getPokimon();
//        this.currIndex=i;
    }
//    public CL_Pokemon getCurrPokimon(){return this.currPokimon;}

    public int whereShouldIGoNow(int currLocationOfAgent){//i need to think if its good to get location
        if(currPathIterator==null) {//this is the first time
            updatecurrLocation(currLocationOfAgent);
        }

        if(currPathIterator.hasNext())
            return currPathIterator.next().getKey();
        else{
            return -1;//need to update pokimons
        }
    }
    public void updatePokimons(ArrayList<CL_Pokemon> pokimons) {//after update edges
        this.list=new ArrayList<>();
        for (CL_Pokemon pokemon : pokimons) {
            list.add(new pokemonPath(pokemon, this.graphAlgo));
        }
        this.currPathIterator=null;
    }







    //////////////////////////////////////////////////
    public static class pokemonPath{
        private CL_Pokemon pokimon;
        private double distance;
        private List<node_data> path;
        private dw_graph_algorithms graphAlgo;


        public pokemonPath(CL_Pokemon pokemon,DWGraph_Algo graphAlgo){
            this.pokimon=pokemon;
            this.graphAlgo=graphAlgo;
        }
        public CL_Pokemon getPokimon() {
            return this.pokimon;
        }
        public double getDistance(){
            return this.distance;
        }
        public void setDistance(int currLocation){
            this.distance=this.graphAlgo.shortestPathDist(currLocation,this.pokimon.get_edge().getSrc())+this.pokimon.get_edge().getWeight();
        }
//        public void setPath(int currLocation){
//            this.path=this.graphAlgo.shortestPath(currLocation,this.pokimon.get_edge().getSrc());
//            if (path!=null)
//                this.path.add(graphAlgo.getGraph().getNode(this.pokimon.get_edge().getDest()));
//        }
        public List<node_data> getPath(int currLocation){
            this.path=this.graphAlgo.shortestPath(currLocation,this.pokimon.get_edge().getSrc());
            if (path!=null)
                this.path.add(graphAlgo.getGraph().getNode(this.pokimon.get_edge().getDest()));
            return path;
        }

    }
}

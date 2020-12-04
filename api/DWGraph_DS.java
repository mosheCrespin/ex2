package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> myGraph;
    private int numberOfNodes;
    private int numberOfEdges;
    private int amountOfChanges;
    private HashMap<Integer, HashMap<Integer, edge_data>> myGraphEdges;//src-->dest
    private HashMap<Integer, HashSet<Integer>> pointersToSrc;//dest-->src

        //constructor
    public DWGraph_DS() {
        this.myGraph = new HashMap<>();
        this.myGraphEdges=new HashMap<>();
        this.pointersToSrc=new HashMap<>();
        this.numberOfNodes = 0;
        this.numberOfEdges = 0;
        this.amountOfChanges = 0;
    }

    public node_data getNode(int key) {
        return myGraph.get(key);
    }



    private HashMap<Integer,edge_data> getNi(int src){
            return myGraphEdges.get(src);
    }


    public edge_data getEdge(int src, int dest) {
        if (getNi(src) == null) return null;//src does not exist;
        return getNi(src).get(dest);
    }
        public void addNode(node_data n) {
        //if the node doesn't exist ,make a new node and add him to the graph.
        if (!myGraph.containsKey(n.getKey())) {
            myGraph.put(n.getKey(), n);
            myGraphEdges.put(n.getKey(),new HashMap<>());
            pointersToSrc.put(n.getKey(),new HashSet<>());//dest-->src
            amountOfChanges++;
            numberOfNodes++;
        }
    }


    public void connect(int src, int dest, double w) {
        //check if the nodes are exist and also if the src and dest aren't same node,else do noting.
        if (src != dest && myGraph.containsKey(src) && myGraph.containsKey(dest)) {
            if (!getNi(src).containsKey(dest)) {//if there is no already edge between the nodes.
                myGraphEdges.get(src).put(dest,new EdgeData(src,dest,w));//src-->dest
                pointersToSrc.get(dest).add(src);//dest-->src
                amountOfChanges++;
                numberOfEdges++;

            }
        }
    }

    public Collection<node_data> getV() {
        return myGraph.values();
    }

    public Collection<edge_data> getE(int node_id) {
        if(getNode(node_id)==null) return null;
        return getNi(node_id).values();
    }

    public node_data removeNode(int key) {
        node_data RemovingNode = myGraph.get(key);
        if (RemovingNode == null) return null;
        myGraph.remove(key);
        numberOfEdges-=myGraphEdges.get(key).size();
        myGraphEdges.remove(key);
        //remove all nodes that point to key
        for (int i : pointersToSrc.get(key)) {
            myGraphEdges.get(i).remove(key);
            numberOfEdges--;
        }
        pointersToSrc.remove(key);
        amountOfChanges++;
        numberOfNodes--;
        return RemovingNode;
    }


    public edge_data removeEdge(int src, int dest) {
        //if there is a edge between the nodes.
        if (getNode(src)==null||getNode(dest)==null) return null;
        if(!getNi(src).containsKey(dest)) return null;
            //save the edge data before delete.
            edge_data ReturnEdge = getNi(src).get(dest);
            //delete the edge from the 2 hashmaps.
            myGraphEdges.get(src).remove(dest);
            pointersToSrc.get(dest).remove(src);
            //apply changes
            numberOfEdges--;
            amountOfChanges++;
            return ReturnEdge;

    }

    public int nodeSize() {
        return numberOfNodes;
    }

    public int edgeSize() {
        return numberOfEdges;
    }

    public int getMC() {
        return amountOfChanges;
    }
}

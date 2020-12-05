package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap< Integer ,  node_data> myGraph;
    private transient int numberOfNodes;
    private transient int numberOfEdges;
    private transient int amountOfChanges;
    private HashMap<Integer, HashMap<Integer, edge_data>> My_graph_edges;//src-->dest
    private transient HashMap<Integer, HashSet<Integer>> pointersToDest;//dest-->src

        //constructor
    public DWGraph_DS() {
        this.My_graph_edges = new HashMap<>();
        this.pointersToDest = new HashMap<>();
        this.myGraph = new HashMap<>();
        this.numberOfNodes = 0;
        this.numberOfEdges = 0;
        this.amountOfChanges = 0;
    }

    public node_data getNode(int key) {
        return myGraph.get(key);
    }

    private HashMap<Integer,edge_data> getNi(int src){
            return My_graph_edges.get(src);
    }


    public edge_data getEdge(int src, int dest) {
        if (getNi(src) == null) return null;//src does not exist;
        return getNi(src).get(dest);
    }
        public void addNode(node_data n) {
            //if the node doesn't exist ,make a new node and add him to the graph.
            if (!myGraph.containsKey(n.getKey())) {
                myGraph.put(n.getKey(), n);
                My_graph_edges.put(n.getKey(), new HashMap<>());
                pointersToDest.put(n.getKey(), new HashSet<>());
                numberOfNodes++;
                }
            }


    public void connect(int src, int dest, double w) {
        //check if the nodes are exist and also if the src and dest aren't same node,else do noting.
        if (src != dest && myGraph.containsKey(src) && myGraph.containsKey(dest)) {
            if (!My_graph_edges.get(src).containsKey(dest)) {//if there is no already edge between the nodes.
                My_graph_edges.get(src).put(dest,new EdgeData(src, dest, w));//dest-->src
                pointersToDest.get(dest).add(src);
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
        numberOfEdges-=My_graph_edges.get(key).size();
        My_graph_edges.remove(key);
        //remove all nodes that point to key
        for (int i : pointersToDest.get(key)) {
            My_graph_edges.get(i).remove(key);
            numberOfEdges--;
        }
        pointersToDest.remove(key);
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
        My_graph_edges.get(src).remove(dest);
        pointersToDest.get(dest).remove(src);
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

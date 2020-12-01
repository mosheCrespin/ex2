package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> My_graph;
    private int numberOfNodes;
    private int numberOfEdges;
    private int amountOfChanges;

    private HashMap<Integer,HashMap<Integer,edge_data>>My_graph_edges;

    //constructor
    public DWGraph_DS() {
        this.My_graph = new HashMap<>();
        this.numberOfNodes = 0;
        this.numberOfEdges = 0;
        this.amountOfChanges = 0;
    }

    public node_data getNode(int key) {
        return My_graph.get(key);
    }

    public edge_data getEdge(int src, int dest) {
        //if there is edge between the nodes, return the edge.
        if(My_graph_edges.get(src).containsKey(dest))
           return My_graph_edges.get(src).get(dest);
        else
        return null;
    }

    public void addNode(node_data n) {

    }

    public void connect(int src, int dest, double w) {

    }

    public Collection<node_data> getV() {
        return null;
    }

    public Collection<edge_data> getE(int node_id) {
        return null;
    }

    public node_data removeNode(int key) {
        return null;
    }

    public edge_data removeEdge(int src, int dest) {
        return null;
    }

    public int nodeSize() {
        return 0;
    }

    public int edgeSize() {
        return 0;
    }

    public int getMC() {
        return 0;
    }
}

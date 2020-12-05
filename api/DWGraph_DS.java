package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> My_graph;
    private int numberOfNodes;
    private int numberOfEdges;
    private int amountOfChanges;

    private HashMap<Integer, HashMap<Integer, edge_data>> My_graph_edges;//src-->dest
    private HashMap<Integer, HashSet<Integer>> pointersToDest;//dest-->src

        //constructor
    public DWGraph_DS() {
        this.My_graph_edges = new HashMap<>();
        this.pointersToDest = new HashMap<>();
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
        if (My_graph_edges.get(src).containsKey(dest))
            return My_graph_edges.get(src).get(dest);
        else
            return null;
    }

    public void addNode(node_data n) {
        //if the node doesn't exist ,make a new node and add him to the graph.
        if (!My_graph.containsKey(n.getKey())) {
            My_graph.put(n.getKey(), n);
            My_graph_edges.put(n.getKey(),new HashMap<>());
            pointersToDest.put(n.getKey(),new HashSet<>());
            amountOfChanges++;
            numberOfNodes++;
        }
    }

    public void connect(int src, int dest, double w) {
        //check if the nodes are exist and also if the src and dest aren't same node,else do noting.
        if (src != dest && My_graph.containsKey(src) && My_graph.containsKey(dest)) {
            if (!My_graph_edges.get(src).containsKey(dest)) {//if there is no already edge between the nodes.
                My_graph_edges.get(src).put(dest,new EdgeData(src, dest, w));
                pointersToDest.get(dest).add(src);
                amountOfChanges++;
                numberOfEdges++;

            }
        }
    }

    public Collection<node_data> getV() {
        return My_graph.values();
    }

    public Collection<edge_data> getE(int node_id) {
        //all the edges getting out of the given node.
        //todo check the return value
        return My_graph_edges.get(node_id).values();
    }

    public node_data removeNode(int key) {
        if (My_graph.containsKey(key)) {
            node_data newnode = My_graph.get(key);
            //init the number of edges after remove node
            numberOfEdges -= (My_graph_edges.get(key).size());

            //remove all nodes that point to key
            for (int i : pointersToDest.get(key)) {
                My_graph_edges.get(i).remove(key);
            }
            //remove the edge in pointersToDest hashmap
            for (int i : My_graph_edges.get(key).keySet()) {
                pointersToDest.get(i).remove(key);
            }

            //clear all the nodes that there src is key.todo to it 2 times check
            My_graph_edges.get(key).clear();
            My_graph_edges.remove(key);

            //remove all the edges there dest is key
            pointersToDest.get(key).clear();
            pointersToDest.remove(key);

            amountOfChanges++;

            //remove from My_graph
            My_graph.remove(key);
            return newnode;
        } else return null;
    }

    public edge_data removeEdge(int src, int dest) {
        //if there is a edge between the nodes.
        if (My_graph_edges.get(src).containsKey(dest)) {
            //save the edge data before delete.
            edge_data ReturnEdge = My_graph_edges.get(src).get(dest);
            //delete the edge from the 2 hashmaps.
            My_graph_edges.get(src).remove(dest);
            pointersToDest.get(dest).remove(src);

            //init changes
            numberOfEdges--;
            amountOfChanges++;

            return ReturnEdge;
        }
        //do else
        return null;
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

package api;

import java.util.*;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap< Integer ,  node_data> myGraph;
    private transient int numberOfNodes;
    private transient int numberOfEdges;
    private transient int amountOfChanges;
    private HashMap<Integer, HashMap<Integer, edge_data>> My_graph_edges;//src-->dest
    private transient HashMap<Integer, HashSet<Integer>> pointersToDest;//dest-->src

    /**
     * constructor for DWGraph_DS
     * This method initialize the hashmaps and update the numberOfNodes,numberOfEdges,amountOfChanges to 0.
     */
    public DWGraph_DS() {
        this.My_graph_edges = new HashMap<>();
        this.pointersToDest = new HashMap<>();
        this.myGraph = new HashMap<>();
        this.numberOfNodes = 0;
        this.numberOfEdges = 0;
        this.amountOfChanges = 0;
    }
    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data , null if none.
     */
    public node_data getNode(int key) {
        return myGraph.get(key);
    }

    /**
     * Returns a Hashmap that contains-key that hold id number of the node the src node connect to,
     * and hold in the value of this Hashmap the data of the edge (src,dest).
     * @param src- the node's id.
     * @return HashMap.
     */
    private HashMap<Integer,edge_data> getNi(int src){
        return My_graph_edges.get(src);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     * @param src- the node's id that the edge start from.
     * @param dest-the node's id that the edge end from.
     * @return edge_data
     */
    public edge_data getEdge(int src, int dest) {
        if (getNi(src) == null) return null;//src does not exist;
        return getNi(src).get(dest);
    }
    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method  run in O(1) time.
     * this method add the new node to 3 hashmaps,one contains the 'n' node in her key and in the value the dest .
     * My_graph_edges contains the 'n' id  in the key of the hashmap and in the value contains another hashmap that
     * contains key and value ,in the key there is a id dest from the 'n' node,and in the value there id the edge that connect between the nodes.
     * pointersToDest contains in the key of this hashmap the node 'n' and in the value of this hashmap there is hashset that contains
     * all the dest nodes that start from 'n' node.
     *  where the node start from, which node connect to 'n' node
     *
     * @param n
     */
    public void addNode(node_data n) {
        //if the node doesn't exist ,make a new node and add him to the graph.
        if (!myGraph.containsKey(n.getKey())) {
            myGraph.put(n.getKey(), n);
            My_graph_edges.put(n.getKey(), new HashMap<>());
            pointersToDest.put(n.getKey(), new HashSet<>());
            numberOfNodes++;
            amountOfChanges++;
        }
    }


    /**
     * Connects an edge with weight w between node src to node dest.
     *  Note: this method run in O(1) time.
     * this method update the hashmaps that the nodes are connects.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
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
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method run in O(1) time.
     * @return Collection<node_data>
     */
    public Collection<node_data> getV() {
        return myGraph.values();
    }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method run in O(1) time.
     * @return Collection<edge_data>
     */
    public Collection<edge_data> getE(int node_id) {
        if(getNode(node_id)==null) return null;
        return getNi(node_id).values();
    }
    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method run in O(k), V.degree=k, as all the edges should be removed.
     * This method remove the node from all the hashmaps.
     * @return the data of the removed node (null if none).
     * @param key
     */
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

    /**
     * Deletes the edge from the graph,
     * Note: this method run in O(1) time.
     * This method update My_graph_edges and pointersToDest hashmaps.
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
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
    /** Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return 'numberOfNodes'.
     */
    public int nodeSize() {
        return numberOfNodes;
    }
    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     * @return 'numberOfEdges'.
     */
    public int edgeSize() {
        return numberOfEdges;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return 'amountOfChanges'.
     */
    public int getMC() {
        return amountOfChanges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWGraph_DS that = (DWGraph_DS) o;
        boolean flag = false;
        if (numberOfNodes == that.numberOfNodes &&
                numberOfEdges == that.numberOfEdges) {
            flag = true;
            for (int src : myGraph.keySet())
                for (int dest : My_graph_edges.get(src).keySet())
                    if (((DWGraph_DS) o).getEdge(src, dest).getSrc() != src && ((DWGraph_DS) o).getEdge(src, dest).getDest() != dest)
                        flag = false;
        }

        return flag;

    }

    /**
     * This method return a String that contains the numberOfNodes,numberOfEdges and getMC(number of changes).
     * @return String.
     */
    public String toString(){
        StringBuilder str = new StringBuilder();
        for (node_data curr : myGraph.values()) {
            str.append(curr.getKey()).append("->\n");
            for (edge_data currNi : getNi(curr.getKey()).values())
                str.append(" ").append(currNi.toString());
            str.append("________________ \n");
        }
        return str + "\n" + "nodes " + numberOfNodes + ", edges: " + numberOfEdges + ", changes: " + getMC();
    }

}
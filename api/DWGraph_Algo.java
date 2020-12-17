package api;

import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;



public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph myGraph;

    /**
     * init the class's graph.
     */
    public DWGraph_Algo() {
        this.myGraph = new DWGraph_DS();
    }
    /**
     * Init the graph on which this set of algorithms operates on with given graph g.
     * @param g
     */
    public void init(directed_weighted_graph g) {
        this.myGraph = g;
    }
    /**
     * Return the graph this class works.
     * @return myGraph
     */
    public directed_weighted_graph getGraph() {
        return myGraph;
    }
    /**
     * Do a deep copy of this weighted graph.
     * This method make a new graph call 'copiedGraph' and do deep copy from the graph on which this set of algorithms
     * operates on and copy all the nodes,and all the edges.
     * @return copiedGraph
     */
    public directed_weighted_graph copy() {
        directed_weighted_graph copiedGraph = new DWGraph_DS();
        node_data temp;
        for (node_data curr : myGraph.getV()) {
            copiedGraph.addNode(new NodeData(curr));
        }
        for (node_data curr : myGraph.getV())
            for (edge_data currNi : myGraph.getE(curr.getKey())) {
                copiedGraph.connect(currNi.getSrc(), currNi.getDest(), curr.getWeight());
            }
        return copiedGraph;
    }

    /**
     * Do a deep copy of this weighted graph but the edges he copy in reverse .
     * This method make a new graph call 'copiedGraph' and do deep copy from the graph on which this set of algorithms
     * operates on and copy all the nodes,and all the edges in reverse.
     * @return copiedGraph
     */

    private  synchronized directed_weighted_graph  deepCopyOppositGraph() {
        directed_weighted_graph copiedGraph = new DWGraph_DS();
        node_data temp;
        for (node_data curr : myGraph.getV()) {
            copiedGraph.addNode(new NodeData(curr));
        }
        for (node_data curr : myGraph.getV())
            for (edge_data currNi : myGraph.getE(curr.getKey())) {
                copiedGraph.connect(currNi.getDest(), currNi.getSrc(), curr.getWeight());
            }
        return copiedGraph;
    }

    /**
     * this method run over all the nodes and update there tag to -1.
     * @param g
     */
    private void initTags(directed_weighted_graph g) {
        for (node_data curr : g.getV()) {
            curr.setTag(-1);
        }
    }

    /**
     * this method run over all the nodes and init there weight to -1.
     */
    private void initWeight(directed_weighted_graph g){
        for(node_data curr: myGraph.getV()){
            curr.setWeight(-1);
        }
    }

    /**
     * this method check if this graph is connected using BFS algorithm if so, the method check also if the deepCopyOppositGraph()
     *is connected if true return true else false.
     * running time is O(V+E)//todo
     * @return true if the graph is connected, `false` otherwise
     */

    public  boolean isConnected() {
        directed_weighted_graph graphPointer = myGraph;
        int Node_size = graphPointer.nodeSize();
        if (Node_size < 2)//if the number of nodes is less than 2 the graph is connected
            return true;
        //if the graph is connected it should have at least 2(n-1) edges
        if (graphPointer.edgeSize() < 2 * Node_size - 2)
            return false;
        if (graphPointer.edgeSize() == (graphPointer.nodeSize() * (graphPointer.nodeSize() - 1)))
            return true;
        int[] counters = new int[2];//counter for two situation;
        //we should start from some place
        node_data start = graphPointer.getV().iterator().next();//should start from somewhere
        for (int i = 0; i < 2; i++) {
            initTags(graphPointer);//initializes all the tags to -1
            node_data curr;
            node_data Ni;
            Queue<node_data> q = new LinkedList<>();
            q.add(start);
            graphPointer.getNode(start.getKey()).setTag(1);
            counters[i] = 1;
            while (!q.isEmpty()) {
                curr = q.poll();
                for (edge_data Eni : graphPointer.getE(curr.getKey())) {

                    Ni = graphPointer.getNode(Eni.getDest());
                    if (Ni.getTag() == -1) {
                        q.add(Ni);
                        Ni.setTag(1);
                        counters[i]++;
                    }
                }
            }
            if (counters[i] != graphPointer.nodeSize()) return false;
            graphPointer = deepCopyOppositGraph();

        }
        //if the number of the seen vertices is equal to nodeSize than the graph is connected
        return counters[1] == myGraph.nodeSize();//we already checked counters[0]
    }

    /**
     * this method get 2 nodes and calculate the shortest path between them.
     * if there is a path than the method returns the sum of the weights of the shortest path
     * if there is no path or if one of the nodes does not exist in the graph, than returns -1.
     * if src==dest than returns 0
     * this method using Dijkstra algorithm
     * running time is O(logV(V+E))
     * @param src - start node
     * @param dest - end (target) node
     * @return the distance
     */
    public  double shortestPathDist(int src, int dest) {

        HashMap<node_data, Double> distances = new HashMap<>();
        Queue<node_data> q = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        initTags(myGraph);//init all the tags to -1
        node_data start = myGraph.getNode(src);
        node_data end = myGraph.getNode(dest);
        if (start == null || end == null) return -1;
        if (src == dest) {//if true then returns a list with only the start node
            return 0;
        }
        node_data Ni_node;
        node_data curr;
        distances.put(start, 0.0);//the distance between node to itself is 0
        q.add(start);
        boolean flag = false;
        while (!q.isEmpty()&&!flag) {
            curr = q.poll();//take a node
            if(curr.getKey()==dest)
                flag=true;
            for (edge_data edge : myGraph.getE(curr.getKey())) {//run for all of his Ni
                Ni_node = myGraph.getNode(edge.getDest());
                if (Ni_node.getTag() == -1) {//if the Ni never got visited
                    distances.put(Ni_node, (distances.get(curr) + edge.getWeight()));//the tag of this node is his father tag(recursive)+the weight of the edge who connects between the father to him.
                    q.add(Ni_node);//O(logV)
                    Ni_node.setTag(1);
                } else {//if the Ni already got visited
                    //take the minimum between the Ni tag to the new path that found.
                    double temp = Math.min(distances.get(Ni_node), distances.get(curr) + edge.getWeight());
                    if (temp != distances.get(Ni_node)) {//if the new path is better
                        distances.put(Ni_node, temp);//set the new path of Ni
                        q.add(Ni_node);//for update the list, yes i know there will be duplicate nodes inside the q
                    }
                }
            }

        }
        if (!flag) {//if there is no path then return null
            return -1;
        }
        return distances.get(myGraph.getNode(dest));
    }

    /**
     * this method get 2 nodes and calculate the shortest path between them.
     * if there is a path than the method returns the the actual path using List</node_info>
     * if there is no path then the method returns an empty list.
     * if one of the nodes does not exist in the graph, than returns null.
     * if src==dest than returns a list with only the src node
     * this method using Dijkstra algorithm
     * running time is O(logV(V+E))
     * @param src - start node
     * @param dest - end (target) node
     * @return list with the actual path
     */
    public  List<node_data> shortestPath(int src, int dest) {
        HashMap<node_data, Double> distances = new HashMap<>();
        Queue<node_data> q = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        HashMap<node_data, node_data> father = new HashMap<>();//this hashmap is using to recover the path
        initTags(myGraph);//init all the tags to -1
        node_data start = myGraph.getNode(src);
        node_data end = myGraph.getNode(dest);
        if (start == null || end == null) return null;
        if (src == dest) {//if true then returns a list with only the start node
            List<node_data> temp = new LinkedList<>();
            temp.add(start);
            return temp;
        }
        node_data Ni_node;
        node_data curr;
        distances.put(start, 0.0);//the distance between node to itself is 0
        start.setTag(1);
        q.add(start);
        boolean flag = false;
        while (!q.isEmpty()&&!flag) {
            curr = q.poll();//take a node
            if(curr.getKey()==dest)
                flag=true;
            for (edge_data edge : myGraph.getE(curr.getKey())) {//run for all of his Ni
                Ni_node = myGraph.getNode(edge.getDest());
                if (Ni_node.getTag() == -1) {//if the Ni never got visited
                    distances.put(Ni_node, (distances.get(curr) + edge.getWeight()));//the tag of this node is his father tag(recursive)+the weight of the edge who connects between the father to him.
                    father.put(Ni_node, curr);//the HashMap builds in this path--> <the neighbor, his father>
                    q.add(Ni_node);//O(logV)
                    Ni_node.setTag(1);
                } else {//if the Ni already got visited
                    //take the minimum between the Ni tag to the new path that found.
                    double temp = Math.min(distances.get(Ni_node), distances.get(curr) + edge.getWeight());
                    if (temp != distances.get(Ni_node)) {//if the new path is better
                        father.put(Ni_node, curr);//set the new father of Ni
                        distances.put(Ni_node, temp);//set the new path of Ni
                        q.add(Ni_node);//for update the list, yes i know there will be duplicate nodes inside the q
                    }
                }
            }

        }
        if (!flag) {//if there is no path then return null
            return new LinkedList<>();
        }
        return buildPath(father, end);//builds path using `buildPath` and return this list
    }

    /**
     * this method makes a conversion from HashMap that holds a path, to a List of nodes
     * running time is O(k) while k is the number of the nodes in the path
     * @param father the HashMap who holds the path
     * @param dest   the key of the dest node
     * @return a list of the path
     */
    private LinkedList<node_data> buildPath(HashMap<node_data, node_data> father, node_data dest) {
        LinkedList<node_data> ans = new LinkedList<>();
        int end=dest.getKey();
        ans.add(dest);
        dest = father.get(dest);//O(1)
        while (dest != null&&dest.getKey()!=end) {//O(K)
            ans.addFirst(dest);
            dest = father.get(dest);
        }
        return ans;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    public boolean save(String file) {
        Gson gson= new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this.myGraph);
        try{
            PrintWriter pw = new PrintWriter(new File(file));
            pw.write(json);
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    public boolean load(String file) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class, new graphJsonDeserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            directed_weighted_graph graph = gson.fromJson(reader, directed_weighted_graph.class);
            if (graph != null)
                this.myGraph = graph;
            else return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private class graphJsonDeserializer implements JsonDeserializer<directed_weighted_graph>{
        /**
         * This method get JsonElements and init g0 graph with the values that write in the jsonElements this action call deserialize
         * the method return the g0 graph with all the values about the nodes and edges.
         * @param jsonElement
         * @param type
         * @param jsonDeserializationContext
         * @return g0
         * @throws JsonParseException
         */
        @Override
        public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject nodes=jsonObject.get("myGraph").getAsJsonObject();
            node_data currNode;
            directed_weighted_graph g0=new DWGraph_DS();
            for(Map.Entry<String, JsonElement> node :nodes.entrySet()){
                JsonObject curr= node.getValue().getAsJsonObject();
                JsonObject currLocation=curr.get("Location").getAsJsonObject();
                geoLocation geoLocation=new geoLocation(currLocation.get("x").getAsDouble(),currLocation.get("y").getAsDouble(),currLocation.get("z").getAsDouble());
                currNode=new NodeData(curr.get("id").getAsInt(),curr.get("tag").getAsInt(),curr.get("weight").getAsDouble(),curr.get("info").getAsString(),geoLocation);
                g0.addNode(currNode);
            }
            JsonObject edges=jsonObject.get("My_graph_edges").getAsJsonObject();
            for(Map.Entry<String, JsonElement> edge : edges.entrySet()){
                for(Map.Entry<String, JsonElement> currEdge:edge.getValue().getAsJsonObject().entrySet()){
                    JsonObject edgeData=currEdge.getValue().getAsJsonObject();
                    int src=edgeData.get("src").getAsInt();
                    int dest=edgeData.get("dest").getAsInt();
                    double weight=edgeData.get("weight").getAsDouble();
                    g0.connect(src,dest,weight);
                    g0.getEdge(src,dest).setInfo(edgeData.get("info").getAsString());
                    g0.getEdge(src,dest).setTag(edgeData.get("tag").getAsInt());
                }
            }
            return g0;
        }
    }

}

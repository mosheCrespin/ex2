package api;

import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;


public class DWGraph_Algo implements dw_graph_algorithms{

    private directed_weighted_graph myGraph;

    public DWGraph_Algo() {
        this.myGraph = new DWGraph_DS();
    }

    public void init(directed_weighted_graph g) {
        this.myGraph = g;
    }

    public directed_weighted_graph getGraph() {
        return myGraph;
    }

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

    private directed_weighted_graph deepCopyOppositGraph() {
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

    private void initTags(directed_weighted_graph g) {
        for (node_data curr : g.getV()) {
            curr.setTag(-1);
        }
    }
    private void initWeight(){
        for(node_data curr: myGraph.getV()){
            curr.setWeight(-1);
        }
    }



    public boolean isConnected() {
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
/*
    public double shortestPathDist(int src, int dest) {
       //this comparator using the field tag of every node to compare

       HashMap<Integer,Double >distances= new HashMap<>();
        Comparator<node_data> nodeInfoComparator= Comparator.comparingDouble(distances::get);
        Queue<node_data> q=new PriorityQueue<>(nodeInfoComparator);
        initTags();//init all the tags to -1
        node_data start=myGraph.getNode(src);
        node_data end=myGraph.getNode(dest);
        if(start==null||end==null) return -1;//if one or two of the nodes does not exist in the graph
        if(src==dest) {//if true then returns a 0
            return 0;
        }
        node_data curr;
        distances.put(src,0.0);//the distance between node to itself is 0
        //start.setTag(0);//the distance between node to itself is 0
        q.add(myGraph.getNode(src));
        while(!q.isEmpty()&&myGraph.getNode(dest).getTag()!=0){
            curr=q.poll();//take a node
            curr.setTag(0);//change his tag to know we run over him.
            for( edge_data Ni: myGraph.getE(curr.getKey())) {//run for all of his Edges. O(logE)
                if (!distances.containsKey(Ni.getDest())||distances.get(Ni.getDest())==0)//if the node isn't exist in hashmap or his distance is 0.
                {
                    distances.put(Ni.getDest(), distances.get(Ni.getSrc())+Ni.getWeight() );
                    //Ni.setTag(0);//the tag of this node is his father tag(recursive)+the weight of the edge who connects between the father to him.
                    //myGraph.getNode(curr.getKey())

                    q.add(myGraph.getNode(Ni.getDest()));//add this node to the queue
                //the node already got visited
                    //take the minimum between the old path to the new one
                    //Ni.setTag(Math.min(Ni.getTag(), curr.getTag() + myGraph.getEdge(Ni.getKey(), curr.getKey())));
                }
                else //check if now the distance is smaller
                {
                    if (distances.get(Ni.getDest())>distances.get(Ni.getSrc())+Ni.getWeight())
                        distances.put(Ni.getDest(),distances.get(Ni.getSrc())+Ni.getWeight());//change to the smaller distance

                }

            }
        }*/
public double shortestPathDist(int src, int dest) {
    //this comparator using the field tag of every node to compare
    Comparator<node_data> nodeInfoComparator= Comparator.comparingDouble(node_data::getWeight);
    Queue<node_data> q=new PriorityQueue<>(nodeInfoComparator);
    initTags(myGraph);//init all the tags to -1
    initWeight();//init all the weight to -1
    node_data start=myGraph.getNode(src);
    node_data end=myGraph.getNode(dest);
    if(start==null||end==null) return -1;//if one or two of the nodes does not exist in the graph
    if(src==dest) {//if true then returns a 0
        return 0;
    }
    node_data curr;
    start.setWeight(0);
    start.setTag(0);
    q.add(start);
    while(!q.isEmpty()&&end.getTag()==-1){
        curr=q.poll();//take a node
        for(edge_data Ni: myGraph.getE(curr.getKey())) {//run for all of his Ni. O(logE)
            if (myGraph.getNode(Ni.getDest()).getWeight()==-1||curr.getWeight()+Ni.getWeight()<myGraph.getNode(Ni.getDest()).getWeight())//if the node never got visited
            {
                curr.setTag(0);
                myGraph.getNode(Ni.getDest()).setWeight(curr.getWeight()+Ni.getWeight());//the tag of this node is his father tag(recursive)+the weight of the edge who connects between the father to him.
                q.add(myGraph.getNode(Ni.getDest()));
            }
        }
    }
    return end.getWeight();
}
    public List<node_data> shortestPath(int src, int dest) {
        HashMap<node_data, node_data> father = new HashMap<>();//this hashmap is using to recover the path
        //this comparator using the field tag of every node to compare
        List<node_data> temp = new LinkedList<>();
        Comparator<node_data> nodeInfoComparator= Comparator.comparingDouble(node_data::getWeight);
        Queue<node_data> q=new PriorityQueue<>(nodeInfoComparator);
        initTags(myGraph);//init all the tags to -1
        initWeight();//init all the weight to -1
        node_data start=myGraph.getNode(src);
        node_data end=myGraph.getNode(dest);
        temp.add(start);
        if(start==null||end==null) return null;//if one or two of the nodes does not exist in the graph
        if(src==dest) {//if true then returns a 0
            return temp;
        }
        node_data curr;
        start.setWeight(0);
        start.setTag(0);
        q.add(start);
        while(!q.isEmpty()&&end.getTag()==-1){
            curr=q.poll();//take a node
            for(edge_data Ni: myGraph.getE(curr.getKey())) {//run for all of his Ni. O(logE)
                if (myGraph.getNode(Ni.getDest()).getWeight()==-1||curr.getWeight()+Ni.getWeight()<myGraph.getNode(Ni.getDest()).getWeight())//if the node never got visited
                {
                    father.put(myGraph.getNode(Ni.getDest()),curr);
                    curr.setTag(0);
                    myGraph.getNode(Ni.getDest()).setWeight(curr.getWeight()+Ni.getWeight());//the tag of this node is his father tag(recursive)+the weight of the edge who connects between the father to him.
                    q.add(myGraph.getNode(Ni.getDest()));
                }
            }
        }
        if (!father.containsKey(end)) {//if there is no path then return null
            return null;
        }
        return buildPath(father, end);//builds path using `buildPath` and return this list
}

    /**
     * this method makes a conversion from HashMap that holds a path, to a List of nodes
     * running time is O(k) while k is the number of the nodes in the path
     *
     * @param father the HashMap who holds the path
     * @param dest   the key of the dest node
     * @return a list of the path
     */
    private LinkedList<node_data> buildPath(HashMap<node_data, node_data> father, node_data dest) {
        LinkedList<node_data> ans = new LinkedList<>();
        ans.add(dest);
        dest = father.get(dest);//O(1)
        while (dest != null) {//O(K)
            ans.addFirst(dest);
            dest = father.get(dest);
        }
        return ans;
    }


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

    public boolean load(String file) {
        GsonBuilder builder=new GsonBuilder();
//        builder.registerTypeAdapter(directed_weighted_graph.class,new )
//        Type type = new TypeToken<HashMap<Integer, Employee>>(){}.getType();
//        HashMap<Integer, Employee> clonedMap = gson.fromJson(jsonString, type);
        return false;
    }


    private class graphJsonDeserializer implements JsonDeserializer<directed_weighted_graph>{

        @Override
        public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return null;

        }
    }

}

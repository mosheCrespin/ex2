package api;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
public class DWGraph_Algo implements dw_graph_algorithms{
    private directed_weighted_graph myGraph;
    public DWGraph_Algo(){
        this.myGraph=new DWGraph_DS();
    }
    public void init(directed_weighted_graph g) {
        this.myGraph=g;
    }

    public directed_weighted_graph getGraph() {
        return myGraph;
    }

    public directed_weighted_graph copy() {
        directed_weighted_graph copiedGraph=new DWGraph_DS();
        node_data temp;
        for(node_data curr: myGraph.getV()){
            copiedGraph.addNode(new NodeData(curr));
        }
        for(node_data curr:myGraph.getV())
            for(edge_data currNi : myGraph.getE(curr.getKey())){
                copiedGraph.connect(currNi.getSrc(), currNi.getDest(), curr.getWeight());
            }
        return copiedGraph;
    }
    private directed_weighted_graph deepCopyOppositGraph(){
        directed_weighted_graph copiedGraph=new DWGraph_DS();
        node_data temp;
        for(node_data curr: myGraph.getV()){
            copiedGraph.addNode(new NodeData(curr));
        }
        for(node_data curr:myGraph.getV())
            for(edge_data currNi : myGraph.getE(curr.getKey())){
                copiedGraph.connect(currNi.getDest(), currNi.getSrc(), curr.getWeight());
            }
        return copiedGraph;
    }
    private void initTags(){
        for(node_data curr: myGraph.getV()){
            curr.setTag(-1);
        }
    }

    public boolean isConnected() {
        directed_weighted_graph graphPointer=myGraph;
        int Node_size = graphPointer.nodeSize();
        if (Node_size < 2)//if the number of nodes is less than 2 the graph is connected
            return true;
        //if the graph is connected it should have at least 2(n-1) edges
        if(graphPointer.edgeSize()<2*Node_size-2)
            return false;
        if(graphPointer.edgeSize()== (graphPointer.nodeSize()*(graphPointer.nodeSize()-1)))
            return true;
        int[] counters=new int[2];//counter for two situation;
        //we should start from some place
        node_data start = graphPointer.getV().iterator().next();//should start from somewhere
        for(int i=0;i<2;i++) {
            initTags();//initializes all the tags to -1
            node_data curr;
            node_data Ni;
            Queue<node_data> q = new LinkedList<>();
            q.add(start);
            start.setTag(1);
            counters[i] = 1;
            while (!q.isEmpty()) {
                curr = q.poll();
                for (edge_data Eni : graphPointer.getE(curr.getKey())) {
                    Ni = graphPointer.getNode(Eni.getTag());
                    if (Ni.getTag() == -1) {
                        q.add(Ni);
                        Ni.setTag(1);
                        counters[i]++;
                    }
                }
            }
            if(counters[i]!= graphPointer.nodeSize()) return false;
            graphPointer=deepCopyOppositGraph();

        }
        //if the number of the seen vertices is equal to nodeSize than the graph is connected
        return counters[1] == myGraph.nodeSize();//we already checked counters[0]
    }

    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    public boolean save(String file) {
        return false;
    }

    public boolean load(String file) {
        return false;
    }
}

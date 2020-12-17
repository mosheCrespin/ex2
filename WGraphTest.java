import api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
public class WGraphTest {

    @Test
    void removeNodeAndEdges() {
        directed_weighted_graph g0 = GraphForTest();
        Assertions.assertEquals(13, g0.edgeSize());
        Assertions.assertEquals(10, g0.nodeSize());
        g0.removeNode(3);//this remove node remove 3 edges that connect to node 3.
        Assertions.assertEquals(9,g0.nodeSize());//check if the number of nodes change.
        Assertions.assertEquals(10, g0.edgeSize());
        edge_data edgeThatDeleted=g0.removeEdge(2, 6);//remove edge that doesn't exist.
        Assertions.assertEquals(null, edgeThatDeleted);//check if the return from the function remove is null.
        Assertions.assertEquals(10, g0.edgeSize());
        g0.addNode(new NodeData(10));//add new node to g0.
        Assertions.assertEquals(10,g0.nodeSize());//check if the number of nodes change after the addNode function.
    }

    @Test
    void AddAndRemoveDoubleNodesAndEdges() {
        directed_weighted_graph g0 = GraphForTest();
        double i = 0.3;
        Assertions.assertEquals(13, g0.edgeSize());
        g0.connect(0, 6, 20);//this edge already exist.
        g0.connect(0, 1, 1);//this edge already exist.
        Assertions.assertEquals(10, g0.nodeSize());
        Assertions.assertEquals(13, g0.edgeSize());
        g0.removeEdge(0, 1);
        g0.removeEdge(0, 6);
        g0.removeNode(2);
        g0.removeNode(2);
        Assertions.assertEquals(9, g0.edgeSize());//check if ths edges that connect to node 2 also deleted.
        Assertions.assertEquals(9, g0.nodeSize());
    }


    @Test
    void getv() {
        directed_weighted_graph g0 = small_graph_creator();
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(true, g0.getV().contains(g0.getNode(i)));//check if in g0 there are all the nodes with id from 0-4
        }
    }

    @Test
    void CheckWeight() {
        directed_weighted_graph g1 = graph_creator();
        for (int i = 0; i < 9; i++) {
            Assertions.assertEquals(g1.getEdge(i, i + 1).getWeight(), i);
        }
    }

    @Test
    void checkMC() {
        directed_weighted_graph g0 = small_graph_creator();
        Assertions.assertEquals(12, g0.getMC());
        g0.removeEdge(3, 4);//remove node that does not exist.
        Assertions.assertEquals(12, g0.getMC());//check if mc updates when removing edge
        g0.removeNode(0);//even this node connect all the other node this count only one changes.
        Assertions.assertEquals(13, g0.getMC());
        g0.connect(1, 3, 0.2);//check if mc updates when connecting between two nodes
        Assertions.assertEquals(14, g0.getMC());
    }
    @Test
    void getE(){
        directed_weighted_graph graph=NodeThatConnectToAllNodes();
        int arr[] = new int[10];
        arr[0]=1;
        int IdDest;
        for(edge_data edge:graph.getE(0)){//if node 0 connect to this node put in the array 1 in the edge.dest place.
            IdDest=edge.getDest();
            arr[IdDest]=1;
        }

        for(int i:arr){//test if node 0 connect to all other nodes
            Assertions.assertEquals(1,arr[i]);
        }
    }




    public directed_weighted_graph graph_creator() {//the edges are from i to i+1
        directed_weighted_graph g1 = new DWGraph_DS();
        for (int i = 0; i < 10; i++) {
            g1.addNode(new NodeData(i));
        }
        for (int i = 0; i < 9; i++) {
            g1.connect(i, i + 1, i);
        }
        return g1;
    }

    public static directed_weighted_graph small_graph_creator() {
        directed_weighted_graph g0 = new DWGraph_DS();
        for (int i = 0; i < 5; i++) {
            g0.addNode(new NodeData());
        }
        double i = 0.3;
        g0.connect(0, 1, i);
        g0.connect(1, 2, i);
        g0.connect(0, 3, i);
        g0.connect(4, 3, i);
        g0.connect(2, 3, i);
        g0.connect(0, 2, i);
        g0.connect(2, 0, i);
        return g0;
    }
    public static directed_weighted_graph GraphForTest(){
        directed_weighted_graph graph= new DWGraph_DS();
        for(int i=0;i<10;i++){
            graph.addNode(new NodeData(i));
        }
        graph.connect(7,0,1);
        graph.connect(0,1,1);
        graph.connect(0,7,1);
        graph.connect(1,3,1);
        graph.connect(3,1,1);
        graph.connect(3,8,1);
        graph.connect(4,9,1);
        graph.connect(4,5,1);
        graph.connect(2,5,1);
        graph.connect(2,9,1);
        graph.connect(8,6,1);
        graph.connect(6,8,1);
        graph.connect(0,6,20);
        return graph;

    }
    public directed_weighted_graph NodeThatConnectToAllNodes() {//the edges are from i to i+1
        directed_weighted_graph g2 = new DWGraph_DS();
        for (int i = 0; i < 10; i++) {
            g2.addNode(new NodeData(i));
        }
        for (int i = 0; i < 9; i++) {
            g2.connect(0, i, i);
        }
        return g2;
    }
}

import api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

public class WGraphAlgoTest {
    @Test
    void isConnectedSmallGraph() {
        directed_weighted_graph g0 = new DWGraph_DS();
        dw_graph_algorithms ga = new DWGraph_Algo();
        dw_graph_algorithms gb = new DWGraph_Algo();
        g0.addNode(new NodeData());
        g0.addNode(new NodeData());
        ga.init(g0);
        Assertions.assertFalse(ga.isConnected());//this graph isn't connected.
        g0.connect(0, 1, 1.1);
        g0.connect(1, 0, 1);
        gb.init(g0);
        Assertions.assertTrue(gb.isConnected());//this graph is connected.
        g0.removeEdge(1,0);
        Assertions.assertFalse(ga.isConnected());//this graph isn't connected,after remove the edge.

    }

    @Test
    void shortestPathDist() {
        directed_weighted_graph g0 = WGraphTest.GraphForTest();
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(g0);
        Assertions.assertEquals(4,ga.shortestPathDist(0,6));//the shortest path from 0 to 6 is 4.
        Assertions.assertEquals(-1,ga.shortestPathDist(0,2));//there is no path from 0 to 2.
        g0.removeEdge(0,1);
        Assertions.assertEquals(20,ga.shortestPathDist(0,6));//after remove the edge between 0 to 1 the dist from 0 to 6 changed to 20.
        g0.connect(0,8,0);
        Assertions.assertEquals(1,ga.shortestPathDist(0,6));
    }
    @Test
    void shortestPath() {
        directed_weighted_graph g0 = WGraphTest.GraphForTest();
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(g0);
        List<node_data> list1 =new ArrayList<node_data>();
        List<node_data> list2 =new ArrayList<node_data>();
        list1.add(g0.getNode(0));
        list1.add(g0.getNode(1));
        list1.add(g0.getNode(3));
        list1.add(g0.getNode(8));
        list1.add(g0.getNode(6));
        list2=ga.shortestPath(0,6);
        int i=0;
        while(list1.size()>i&&list2.size()>i){
            Assertions.assertEquals(list1.get(i),list2.get(i));//check if the list1 and list 2 are same.
            i++;
        }

    }

    @Test
    void million_nodes_ten_million_edge_runtime_test() {
        directed_weighted_graph graph_test = new DWGraph_DS();
        int mil = 1000000;
        double run_time_s;
        long endTime, finalTime, startTime = System.currentTimeMillis();
        boolean check = false;
        node_data curr;
        for (int i = 0; i < mil; i++) {
            graph_test.addNode(new NodeData());

        }
        for (int i = 0, j = mil - 1; i < mil; i++) {
            for (int k = 0; k < 10; k++) {

                graph_test.connect(i, j - k, 2);
            }
        }
        endTime = System.currentTimeMillis();
        finalTime = endTime - startTime;
        run_time_s = ((double) finalTime) / 1000;
        if (run_time_s < 10.0)
            check = true;
        System.out.println("node size: " + graph_test.nodeSize() + " edge size: "
                + graph_test.edgeSize() + " runtime seconds: " + run_time_s);
        Assertions.assertTrue(check);
    }


    @Test
    void copy() {
        directed_weighted_graph g0 = new DWGraph_DS();
        dw_graph_algorithms gg = new DWGraph_Algo();
        directed_weighted_graph copyWeightedGraph = new DWGraph_DS();
        //dw_graph_algorithms copyAlgo=new DWGraph_Algo();
        for (int i = 0; i < 10; i++) {
            g0.addNode(new NodeData(i));
        }
        g0.connect(0, 8, 1.1);
        g0.connect(0, 2, 1);
        g0.connect(2, 5, 0.3);
        g0.connect(5, 4, 0.3);
        g0.connect(4, 7, 0.3);
        g0.connect(0, 3, 0);
        g0.connect(3, 7, 0);
        g0.connect(3, 6, 1);
        g0.connect(0, 1, 0.5);
        g0.connect(5, 3, 0.1);
        g0.connect(7, 8, 1);
        gg.init(g0);
        copyWeightedGraph = gg.copy();
        Assertions.assertTrue(copyWeightedGraph.equals(g0));//check if the directed graph have same nodes and connection after the copy.

    }


   // @Test
   /* void save_load() throws FileNotFoundException {
//        directed_weighted_graph g0 = connected_graph_creator(80);
        directed_weighted_graph g0=
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(g0);
        String str = "g0.obj";
        ga.save(str);
        ga.load(str);
        assertEquals(ga.getGraph(), g0);
        ga.getGraph().removeNode(33);
        Assertions.assertNotEquals(ga.getGraph(), g0);
        weighted_graph g1 = ga.getGraph();
        boolean flag = ga.load("non_exiting_file.obj");
        assertFalse(flag);
        assertSame(ga.getGraph(), g1);
    }*/
}
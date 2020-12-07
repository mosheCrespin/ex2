import api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class WGraphAlgoTest {
    @Test
    void isConnectedSmallGraph(){
        directed_weighted_graph g0 = WGraphTest.small_graph_creator();
        dw_graph_algorithms ga=new DWGraph_Algo();
        ga.init(g0);
        Assertions.assertFalse(ga.isConnected());
        g0.connect(0,4,1);
        Assertions.assertFalse(ga.isConnected());
        g0.connect(3,0,1);
        Assertions.assertTrue(ga.isConnected());
    }
    @Test
    void shortestPath(){
        directed_weighted_graph g0 = new DWGraph_DS();
        for(int i=0;i<10;i++){
            g0.addNode(new NodeData());
        }
        g0.connect(0,8,1.1);
        g0.connect(0,2,1);
        g0.connect(2,5,0.3);
        g0.connect(5,4,0.3);
        g0.connect(4,7,0.3);
        g0.connect(0,3,0);
        g0.connect(3,7,0);
        g0.connect(3,6,1);
        g0.connect(0,1,0.5);
        g0.connect(5,3,0.1);
        g0.connect(7,8,1);
        dw_graph_algorithms ga=new DWGraph_Algo();
        ga.init(g0);
//        List<node_data> list=ga.shortestPathDist(0,4);
//        ga.save("file.json");
//        Assertions.assertTrue(ga.load("file.json"));
        System.out.println(ga.shortestPathDist(0,4));
        System.out.println(ga.shortestPath(0,4));

    }
    @Test
    void million_nodes_ten_million_edge_runtime_test() {
        directed_weighted_graph graph_test = new DWGraph_DS();
        int mil = 1000000;
        double run_time_s;
        long endTime,finalTime,startTime = System.currentTimeMillis();
        boolean check=false;
        node_data curr;
        for (int i = 0; i <mil;i++)
        {
            graph_test.addNode(new NodeData());

        }
        for (int i = 0, j=mil-1; i <mil;i++)
        {
            for (int k=0;k<10;k++ ) {

                graph_test.connect(i, j-k, 2);
            }
        }
        endTime = System.currentTimeMillis();
        finalTime = endTime - startTime;
        run_time_s =((double)finalTime)/1000;
        if (run_time_s<10.0)
            check=true;
        System.out.println("node size: " + graph_test.nodeSize() +" edge size: "
                + graph_test.edgeSize() + " runtime seconds: " + run_time_s);
        Assertions.assertTrue(check);
    }
}

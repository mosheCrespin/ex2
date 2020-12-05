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
        System.out.println(ga.shortestPathDist(0,8));
        System.out.println(ga.shortestPath(0,8));




    }
}

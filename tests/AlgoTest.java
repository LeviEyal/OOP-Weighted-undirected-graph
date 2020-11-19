package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AlgoTest {

    private static WGraph_Algo ga = new WGraph_Algo();
    private static Random _rnd;


    @BeforeEach
    void setUp() {
        ga.getGraph().addNode(0);
        ga.getGraph().addNode(1);
        ga.getGraph().addNode(2);
        ga.getGraph().addNode(3);
        ga.getGraph().addNode(4);
        ga.getGraph().connect(0,4,8.0);
        ga.getGraph().connect(4,3,3.0);
        ga.getGraph().connect(3,0,7.0);
        ga.getGraph().connect(3,1,4.0);
        ga.getGraph().connect(3,2,2.0);
        ga.getGraph().connect(1,0,3.0);
        ga.getGraph().connect(2,1,1.0);
    }

    @Test
    void emptyGraph(){
        weighted_graph g0 = graph_creator(0, 0, 1);
//        System.out.println(g0);
        weighted_graph_algorithms ga0 = new WGraph_Algo();
        ga0.init(g0);
        boolean b = ga0.isConnected();
        assertTrue(b);
    }

    @Test
    void singleNodeGraph(){
        weighted_graph g0 = graph_creator(1, 0, 1);
//        System.out.println(g0);
        weighted_graph_algorithms ga0 = new WGraph_Algo();
        ga0.init(g0);
        boolean b = ga0.isConnected();
        assertTrue(b);
    }

    @Test
    void twoNodesNoEdge(){
        weighted_graph g0 = graph_creator(2, 0, 1);
        System.out.println(g0);
        weighted_graph_algorithms ga0 = new WGraph_Algo();
        ga0.init(g0);
        boolean b = ga0.isConnected();
        assertFalse(b);
    }

    @Test
    void twoNodesWithEdge(){
        weighted_graph g0 = graph_creator(2, 1, 1);
        System.out.println(g0);
        weighted_graph_algorithms ga0 = new WGraph_Algo();
        ga0.init(g0);
        boolean b = ga0.isConnected();
        assertTrue(b);
    }

    @Test
    void dependency(){
        weighted_graph g0 = graph_creator(10, 15, 1);
        weighted_graph_algorithms ga2 = new WGraph_Algo();
        ga2.init(g0);
        weighted_graph g1 = ga2.copy();
        node_info[] a = nodes(g0);
        g0.removeNode(a[1].getKey());
        g0.removeNode(a[4].getKey());
        g1.removeNode(a[7].getKey());
        g1.addNode(a[a.length-1].getKey()+1);
        g0.addNode(a[a.length-1].getKey()+2);
        g1.addNode(a[a.length-1].getKey()+7);
//        System.out.println("g0: remove: "+n1);
//        System.out.println("g1: add after: "+n2);
        System.out.println("g0: "+g0);
        System.out.println("g1: "+g1);
        boolean b1 = (g1.getV().size() == 11) && (g0.getV().size() == 9);
        boolean b2 = (g1.nodeSize() == 11) && (g0.nodeSize() == 9);
        boolean b3 = (g1.edgeSize() == 15) && (g0.edgeSize() == 12);
        assertTrue(b1);
        assertTrue(b2);
    }

    @Test
    void graph1(){
        weighted_graph g10 = graph_creator(10,30,2);
            System.out.println(g10);
        node_info[] nodes = nodes(g10);
        int a0 = nodes[0].getKey();
        int a1 = nodes[1].getKey();
        int a2 = nodes[2].getKey();
        g10.removeEdge(a0,a1);
        System.out.println("remove edge: "+a0+"-"+a1);
        g10.removeEdge(a2,a0);
        System.out.println("remove edge: "+a2+"-"+a0);
        g10.removeEdge(a2,a1);
        System.out.println("remove edge: "+a2+"-"+a1);
        g10.removeNode(a2);
        System.out.println("remove node: "+a2);
        g10.removeNode(a2);
        int re = 9;
        System.out.println("remove edge: "+a0+"-"+a1);
        System.out.println(g10);
        assertEquals(re, g10.nodeSize());
        re = 22;
        assertEquals(re, g10.edgeSize());
        System.out.println(g10);
    }

    @Test
    void performance(){
        int times = 1000000;
        int maxWeight = 100;
        long start_local = new Date().getTime();
        weighted_graph g0 = graph_creator(0, 0, 1);
        for (int i = 0; i < times; i++) {
            g0.addNode(i);
        }
        _rnd = new Random(31);
        node_info[] nodes = nodes(g0);
        for (int i = 0; i < times; i++) {
            int a = nextRnd(0,times);
            int b = nextRnd(0,times);
            int w = nextRnd(0,maxWeight);
            int k = nodes[a].getKey();
            int j = nodes[b].getKey();
            g0.connect(a,b,w);
        }
        Collection<node_info> trash;
        for (int i = 0; i < times; i++) {
            trash = g0.getV();
        }
        node_info temp;
        for (int i = 0; i < times; i++) {
            temp = g0.getNode(nodes[nextRnd(0,times)].getKey());
            trash = g0.getV(temp.getKey());
        }
        long end_local = new Date().getTime();
        double dt_local = (end_local-start_local)/1000.0;
        boolean b = dt_local<10;
        assertTrue(b);
    }

    @Test
    void isConnected() {
        assertTrue(ga.isConnected());
        ga.getGraph().removeEdge(2,3);
        ga.getGraph().removeEdge(1,2);
        assertFalse(ga.isConnected());
        ga.getGraph().connect(2,3,2.9);
        ga.getGraph().connect(2,1,3.5);
        assertTrue(ga.isConnected());
        ga.getGraph().removeNode(1);
        ga.getGraph().removeNode(3);
        assertFalse(ga.isConnected());

    }

    @Test
    void shortestPathDist() {
        assertEquals(5, ga.shortestPathDist(4,2));
        assertEquals(6, ga.shortestPathDist(4,1));
        assertEquals(4, ga.shortestPathDist(0,2));
        assertEquals(-1, ga.shortestPathDist(4,8));
    }

    @Test
    void shortestPath() {
        ga.shortestPath(4,2);
        ga.shortestPath(0,2);
        ga.shortestPath(3,1);
        ga.shortestPath(3,4);
        ga.shortestPath(3,3);
    }

    @Test
    void saveAndLoad() {
        System.out.println(ga.getGraph());
        ga.save("save1");
        weighted_graph_algorithms t = new WGraph_Algo();
        t.load("save1");
        System.out.println(t.getGraph());
        assertEquals(ga.getGraph().toString(), t.getGraph().toString());
    }

    /************************** PRIVATE METHODS**************************/

    private static weighted_graph graph_creator(int v_size, int e_size, int seed) {
        WGraph_DS g = new WGraph_DS();
        _rnd = new Random(seed);
        for(int i=0;i<v_size;i++) {
            g.addNode(i);
        }
        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        node_info[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            int w = nextRnd(0,100);
            int i = nodes[a].getKey();
            int j = nodes[b].getKey();
            g.connect(i,j,w);
        }
        return g;
    }
    /*
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an  Iterator<node_edge> to be fixed in Ex1
     */
    private static node_info[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        return nodes;
    }

    private static int nextRnd(int min, int max) {
        return (int)nextRnd(min+0.0,max);
    }
    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        return d*dx+min;
    }


}
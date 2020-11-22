package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    private WGraph_DS g = new WGraph_DS();

    @BeforeEach
    void setup() {
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.connect(0,4,8.0);
        g.connect(4,3,3.0);
        g.connect(3,0,7.0);
        g.connect(3,1,4.0);
        g.connect(3,2,2.0);
        g.connect(1,0,3.0);
        g.connect(2,1,1.0);
    }
    @Test
    void priority() {
        g.getNode(0).setTag(0);
        g.getNode(1).setTag(32);
        g.getNode(2).setTag(2);
        g.getNode(3).setTag(43);
        g.getNode(4).setTag(4);
        Queue<node_info> q = new PriorityQueue<>();
        q.add(g.getNode(0));
        q.add(g.getNode(1));
        q.add(g.getNode(2));
        q.add(g.getNode(3));
        q.add(g.getNode(4));
        while(!q.isEmpty())
            System.out.print(q.remove()+" ");
    }

    @Test
    void hasEdge() {
        assertTrue(g.hasEdge(3,0));
        assertTrue(g.hasEdge(3,1));
        assertTrue(g.hasEdge(4,0));
        assertTrue(g.hasEdge(1,2));

        assertFalse(g.hasEdge(4,1));
        assertFalse(g.hasEdge(4,2));
        assertFalse(g.hasEdge(0,2));
        System.out.println(g);
    }

    @Test
    void getEdge() {
        assertEquals(g.getEdge(3,0),7.0);
        g.removeEdge(3,0);
        g.connect(0,3,6.0);
        assertEquals(g.getEdge(0,3),6.0);
        assertEquals(g.getEdge(0,1),3.0);
    }

    @Test
    void nodeSize() {
        int size = g.nodeSize();
        assertEquals(size, 5);
    }

    @Test
    void edgeSize() {
        int edges = g.edgeSize();
        assertEquals(edges, 7);
    }

    @Test
    void getMC() {
        assertEquals(12, g.getMC());
        g.removeEdge(1,3);
        assertEquals(13, g.getMC());
        g.removeNode(0);
        assertEquals(17, g.getMC());
    }
}
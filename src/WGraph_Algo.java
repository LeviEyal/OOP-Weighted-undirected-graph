package ex1.src;

import java.io.*;
import java.util.*;

/**
 * This class represents the "regular" Graph Theory algorithms including:
 * 0. clone();
 * 1. init(String file_name);
 * 2. save(String file_name);
 * 3. isConnected();
 * 5. int shortestPathDist(int src, int dest);
 * 6. List<Node> shortestPath(int src, int dest);
 * @see weighted_graph_algorithms
 *
 * @author Eyal Levi
 * https://github.com/LeviEyal
 */
public class WGraph_Algo implements weighted_graph_algorithms {

    private weighted_graph g;
    private static final int VISITED = 1;
    private static final int NOT_VISITED = 0;
    private static final double INFINITY = Double.POSITIVE_INFINITY;
    /**
     * Construct a graph-algorithms object and set its init graph with an empty new graph.
     */
    public WGraph_Algo() {
        this.g = new WGraph_DS();
    }

    /**
     * Init this set of algorithms on a given graph
     * @param g a given graph to init this set of algorithms on.
     */
    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }

    /**
     * @return the underlying graph of which this class works
     */
    @Override
    public weighted_graph getGraph() {
        return g;
    }

    /**
     * Computes a deep copy of this graph by turning to the copy-constructor of Graph_DS class.
     * @return a deep copy of this graph
     */
    @Override
    public weighted_graph copy() {
        setAllTags(0);
        return new WGraph_DS(this.g);
    }

    /**
     * This algorithm traverse the graph and check
     * connectivity of all nodes.
     * The algorithm use BFS method for graph traversal.
     * The algorithm works as followed:
     * 1) Mark all nodes as not visited by setting their tags to 0
     * 2) Pick some node - call it v
     * 3) Mark v as visited by set its tag to 1
     * 4) add v to a queue
     * 5) while the queue not empty commit:
     *      - set v as the outcome of the queue pop
     *      - for all neighbor n of v:
     *          -- if not visited - add it to the queue and mark it as visited
     * 6) traverse the graph and return false if their is a node who still marked as not visited.
     *
     * @return TRUE if the graph is connected.
     */
    @Override
    public boolean isConnected() {
        if (g.nodeSize() == 0 || g.nodeSize() == 1) return true;
        setAllTags(NOT_VISITED);
        Queue<node_info> q = new LinkedList<>();
        node_info v = g.getV().iterator().next(); //pick some node
        v.setTag(VISITED);
        q.add(v);
        while (!q.isEmpty()) {
            v = q.remove();
            for (node_info n : g.getV(v.getKey())) {
                if (n.getTag() == NOT_VISITED) {
                    q.add(n);
                    n.setTag(VISITED);
                }
            }
        }
        for (node_info n : g.getV())
            if (n.getTag() == NOT_VISITED)
                return false;
        return true;
    }

    //Sets al nodes' tags value to a given integer number t
    private void setAllTags(double t) {
        for (node_info n : g.getV())
            n.setTag(t);
    }

    /**
     * This algorithm searching for the shortest path between given source and destination.
     * The algorithm use Dijkstra method for graph traversal.
     * The algorithm works as followed:
     * 1) set all tags to infinity.
     * 3) set source node tag to 0 and add to the queue
     * 4) add v to a queue
     * 5) while the queue not empty do:
     *      - set v as the outcome of the queue pop
     *      - for all neighbor n of v:
     *          -- if not visited - add it to the queue and mark it as visited
     * 6) traverse the graph and return false if their is a node who still marked as not visited.
     *
     * @param src - start node
     * @param dest - end (target) node
     * @return the shortest path between given source and destination.
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (src == dest) return 0;

        node_info source = g.getNode(src);
        node_info destination = g.getNode(dest);
        if (source == null || destination == null)
            return -1;

        setAllTags(INFINITY);

        Queue<node_info> q = new PriorityQueue<>();
        source.setTag(0);
        q.add(source);

        while (!q.isEmpty()) {
            node_info v = q.poll();
            for (node_info n : g.getV(v.getKey())) {
                double w = g.getEdge(v.getKey(), n.getKey()); //weight (v<->n)
                double weightFromSrc = v.getTag() + w;
                if (weightFromSrc < n.getTag()) {
                    q.add(n);
                    n.setTag(weightFromSrc);
                }
            }
        }
        double t = destination.getTag();
        return (t == INFINITY)? -1 : t;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * The algorithm is as the algorithms above, based on Dijkstra algorithm, but modified
     * in order to preserve the path and retrieve it as a list of nodes.
     * @param src - source node
     * @param dest - destination node
     * @return A list of nodes of the path between source and destination in order
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        node_info source = g.getNode(src);
        node_info destination = g.getNode(dest);
        if (source == null || destination == null) return null;

        List<node_info> path = new ArrayList<>();
        if (src == dest) {
            path.add(source);
            return path;
        }
        HashMap<Integer, Integer> map = new LinkedHashMap<>();
        map.put(src, -1);
        //dijkstra:
        setAllTags(INFINITY);

        Queue<node_info> q = new PriorityQueue<>();
        source.setTag(0);
        q.add(source);

        while (!q.isEmpty()) {
            node_info v = q.poll();
            for (node_info n : g.getV(v.getKey())) {
                double w = g.getEdge(v.getKey(), n.getKey()); //weight (v<->n)
                double weightFromSrc = v.getTag() + w;
                if (weightFromSrc < n.getTag()) {
                    map.put(n.getKey(), v.getKey());
                    q.add(n);
                    n.setTag(weightFromSrc);
                }
            }
        }
        while (dest != -1) {
            path.add(g.getNode(dest));
            dest = map.get(dest);
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     *
     * @param fileName - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String fileName) {
        try {
            FileOutputStream file_out = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file_out);
            out.writeObject(g);
            out.close();
            file_out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param fileName - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String fileName) {
        try {
            FileInputStream file_in = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file_in);
            g = (weighted_graph) in.readObject();
            file_in.close();
            in.close();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}

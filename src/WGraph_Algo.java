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
 * @see ex0.graph_algorithms
 *
 * @author Eyal Levi
 * https://github.com/LeviEyal
 */
public class WGraph_Algo implements weighted_graph_algorithms {

    private weighted_graph g;

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
        setAllTags(0);
        Queue<node_info> q = new LinkedList<>();
        node_info v = g.getV().iterator().next(); //pick some node
        v.setTag(1);
        q.add(v);
        while (!q.isEmpty()) {
            v = q.remove();
            for (node_info n : g.getV(v.getKey())) {
                if (n.getTag() == 0) {
                    q.add(n);
                    n.setTag(1);
                }
            }
        }
        for (node_info n : g.getV())
            if (n.getTag() == 0)
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
     * The algorithm use BFS method for graph traversal.
     * The algorithm works as followed:
     * 1) Mark all nodes as not visited by setting their tags to 0
     * 3) Mark v as visited by set its tag to 1
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

        dijkstra(source);
        double t = destination.getTag();
        return t == Double.POSITIVE_INFINITY ? -1 : t;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * The algorithm is as the algorithms above, based on BFS algorithm, but modified
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
        setAllTags(Double.POSITIVE_INFINITY);

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
        System.out.println(path);
        return path;
    }

    private void dijkstra(node_info start) {
        setAllTags(Double.POSITIVE_INFINITY);

        Queue<node_info> q = new PriorityQueue<>();
        start.setTag(0);
        q.add(start);

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
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String fileName) {
        try {
            FileOutputStream file_out = new FileOutputStream("src\\SavedGraphs\\" + fileName + ".txt");
            ObjectOutputStream out = new ObjectOutputStream(file_out);
            out.writeObject(g);
            out.close();
            file_out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String fileName) {
        try {
            FileInputStream file_in = new FileInputStream("src\\SavedGraphs\\" + fileName + ".txt");
            ObjectInputStream in = new ObjectInputStream(file_in);
            g = (weighted_graph) in.readObject();
            file_in.close();
            in.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}

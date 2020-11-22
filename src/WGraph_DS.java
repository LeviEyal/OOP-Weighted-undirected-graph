package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * This class represents an undirected unweighted graph.
 * The implementation based on adjacency list representation -
 * created with HashMap data structure for accessing vertices quickly.
 * The class implements the interface graph -
 * for more documentation visit this interface.
 * @see weighted_graph
 *
 * @author Eyal Levi
 * https://github.com/LeviEyal
 */
public class WGraph_DS implements weighted_graph, Serializable {

    private HashMap<Integer, node_info> v = new HashMap<>();
    private HashMap<Integer, HashMap<node_info, Double>> e = new HashMap<>();
    private int nodeSize, edgeSize, mc;

    public WGraph_DS() {
        nodeSize = 0;
        edgeSize = 0;
        mc = 0;
    }

    /**
     * Creates a new graph that is a deep copy of a given other graph.
     * The constructor copy each node of the other graph to the new graph
     * and than seek for any possible edge in the other graph and
     * connect the parallel nodes in the new graph.
     * The method runs in O(n^2) time.
     * @param other graph to copy from
     */
    public WGraph_DS(weighted_graph other) {
        for (node_info n : other.getV()) {
            v.put(n.getKey(), new NodeData(n));
            e.put(n.getKey(), new HashMap<>());
        }
        for (int i : v.keySet()) {
            for (int j : v.keySet()) {
                if (hasEdge(i,j))
                    connect(i, j, other.getEdge(i, j));
            }
        }
        nodeSize = other.nodeSize();
        mc = other.getMC();
        edgeSize = other.edgeSize();
    }

    /**
     * return the node_data by the node_id.
     * The method runs in O(1) time.
     * @param key The Key that the desired node associated with.
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        return v.get(key);
    }

    /**
     * Checks if two given nodes are connected.
     * The method runs in O(1) time.
     * @param key1 the first node's key
     * @param key2 the second node's key
     * @return true if the two nodes are connected.
     */
    @Override
    public boolean hasEdge(int key1, int key2) {
        node_info n1 = getNode(key1);
        node_info n2 = getNode(key2);
        if(n1==null || n2==null || key1==key2)
            return false;
        return e.get(key1).containsKey(n2);
    }

    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     *
     * @param node1 node1
     * @param node2 node2
     * @return the weight of the edge between node1 - node2
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {
            return e.get(node1).get(getNode(node2));
        }
        return -1;
    }

    /**
     * Adds a given node to the graph.
     * If the node is already in the graph then
     * the method does nothing.
     * The method runs in O(1) time.
     * @param key The node to be added to the graph
     */
    @Override
    public void addNode(int key) {
        if (!v.containsKey(key)) {
            v.put(key, new NodeData(key));
            e.put(key, new HashMap<>());
            nodeSize++;
            mc++;
        }
    }

    /**
     * Connects two given nodes by their key.
     * If they already connected, the method does nothing.
     * The method runs in O(1) time.
     * @param key1 The first node's key
     * @param key2 The second node's key
     * @param w weight
     */
    @Override
    public void connect(int key1, int key2, double w) {
        node_info n1 = getNode(key1);
        node_info n2 = getNode(key2);
        if(n1==null || n2==null || key1==key2)
            return;
        if (!hasEdge(key1, key2)) {
            e.get(key1).put(n2, w);
            e.get(key2).put(n1, w);
            edgeSize++;
            mc++;
        }
    }

    /**
     * This method return a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * The method runs in O(1) time.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return v.values();
    }

    /**
     * Returns a collection representing all the nodes connected to node associated with key.
     * The method runs in O(1) time.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV(int key) {
        return e.get(key).keySet();
    }

    /**
     * Remove a node associated with a given key,
     * and remove all of the edges that involves this node.
     * The method runs in O(n) time, where n stands for the amount of this graph's vertices.
     * @param key A key of the node to be removed
     * @return The deleted node if succeeded. otherwise return null.
     */
    @Override
    public node_info removeNode(int key) {
        if (v.containsKey(key)) {
            Collection<node_info> t = getV(key);
            while (!t.isEmpty())
                removeEdge(t.iterator().next().getKey(), key);
            mc++;
            nodeSize--;
            return v.remove(key);
        }
        return null;
    }

    /**
     * Disconnect two node associated with two given keys.
     * If they already disconnected, the method does nothing.
     * @param key1 The key to the first node
     * @param key2 The key to the second node
     */
    @Override
    public void removeEdge(int key1, int key2) {
        if (hasEdge(key1, key2)) {
            e.get(key1).remove(getNode(key2));
            e.get(key2).remove(getNode(key1));
            mc++;
            edgeSize--;
        }
    }

    /**
     * @return The number of nodes of this graph.
     * The method runs in O(1) time.
     */
    @Override
    public int nodeSize() {
        return nodeSize;
    }

    /**
     * @return The number of edges of this graph.
     * The method runs in O(1) time.
     */
    @Override
    public int edgeSize() {
        return edgeSize;
    }

    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     * @return The counts
     */
    @Override
    public int getMC() {
        return mc;
    }

    /**
     * Returns a string representation of this graph as an adjacency list.
     * @return A string representation of this graph
     */
    @Override
    public String toString() {
        String s = "Vertices: " + nodeSize() + " Edges: " + edgeSize() + " MC: " + getMC() + "\n";
        for (int key : v.keySet()) {
            s += key + ": ";
            for (node_info n : getV(key)) {
                s += n + "(w=" + e.get(key).get(n) + "), ";
            }
            s += "\n";
        }
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS other = (WGraph_DS) o;
        if (nodeSize != other.nodeSize && edgeSize == other.edgeSize)
            return false;
        for (int i : v.keySet()) {
            if (!e.get(i).equals(other.e.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(v, e, nodeSize, edgeSize);
    }

    /*****************************  private classes  ******************************/

    private static class NodeData implements node_info, Comparable<NodeData>, Serializable {

        private final int key;
        private String info = "";
        private double tag = 0;

        public NodeData(node_info other) {
            key = other.getKey();
            info = other.getInfo();
            tag = other.getTag();
        }

        public NodeData(int key) {
            this.key = key;
        }

        /**
         * Return the key (id) associated with this node.
         * Note: each node_data should have a unique key.
         *
         * @return the unique key of this node
         */
        @Override
        public int getKey() {
            return key;
        }

        /**
         * return the remark (meta data) associated with this node.
         *
         * @return A string of this node information
         */
        @Override
        public String getInfo() {
            return info;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         *
         * @param s the new information string
         */
        @Override
        public void setInfo(String s) {
            info = s;
        }

        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         *
         * @return this node tag
         */
        @Override
        public double getTag() {
            return tag;
        }

        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            tag = t;
        }

        @Override
        public String toString() {
            return "#" + key;
        }

        @Override
        public int compareTo(NodeData o) {
            return o.tag < tag ? 1 : -1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeData nodeData = (NodeData) o;
            return key == nodeData.key &&
                    Objects.equals(info, nodeData.info);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, info);
        }
    }

}

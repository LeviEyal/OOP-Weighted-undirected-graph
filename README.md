![enter image description here](https://camo.githubusercontent.com/c6cb93c822ddc0d3d2504cacb8f4f5b591fcba38/68747470733a2f2f6d656469612e6c6963646e2e636f6d2f646d732f696d6167652f433444304241514744376e704d506f58796d772f636f6d70616e792d6c6f676f5f3230305f3230302f303f653d3231353930323434303026763d6265746126743d545a3855623355326a435a6741315f6b4178365345386a6d4163534a6b62695a41754e364b6e315f6c5730)

**wolcome to EX1!**

This is the second assignment in the course OOP at the Ariel University to year 2020.

> Made by Eyal Levi
>
> GitHub page: [github.com/LeviEyal](github.com/LeviEyal)


## Subject: Weighted - undirected graph

The project contains classes represents an  weighted - undirected graph and its vertices. it also contains the class Graph_Algo that holds methods that perform some operations on a given garph.


## The operations:

 - isConnected: check if the given graph is connected, means that for any vertex their is a path to every other vertex.
 - shortestPathDist: for some two given vertices, compute the size of the shortest path from one to another.
- shortestPath: for some two given vertices, return a list holds the vertices of the shortest path from one to another.

## How to use?

 Create some Test class in the package ex0 and run the code below (**for example**):
 

    public static void main(String[] args) {  
  
	    graph graph = new Graph_DS();  
	    for (int i = 0; i <= 4; i++) {  
	        node_data node = new NodeData();  
	        graph.addNode(node);  
	    }  
	    graph.connect(4, 1);  
	    graph.connect(1, 3);  
	    graph.connect(2, 3);  
	    graph.connect(4, 3);  
	    graph.connect(0, 4);  
	    graph.connect(0, 1);  
	    graph.connect(1, 2);  
	    Graph_Algo ga = new Graph_Algo();  
	    ga.init(graph);  
  
	    System.out.println(ga.isConnected());  
	    System.out.println(ga.shortestPathDist(0, 2));  
	    System.out.println(ga.shortestPath(0, 2));  
    }
The code creates the graph:

![enter image description here](https://media.geeksforgeeks.org/wp-content/uploads/undirectedgraph.png)

The output of this example program shoud be:

    true
    2
    [#0 , #1 , #2 ]

## Algorithms used:
For those three mentioned operations i used some variations of the BFS algorithm and dijkstra algorithm.

## External info
What is graph: [https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)](https://en.wikipedia.org/wiki/Graph_%28discrete_mathematics%29)

Connectivity of a graph: [https://en.wikipedia.org/wiki/Connectivity_(graph_theory)](https://en.wikipedia.org/wiki/Connectivity_%28graph_theory%29)

The shortest path problem: [https://en.wikipedia.org/wiki/Shortest_path_problem](https://en.wikipedia.org/wiki/Shortest_path_problem)

The BFS algorithm: [https://en.wikipedia.org/wiki/Breadth-first_search](https://en.wikipedia.org/wiki/Breadth-first_search)

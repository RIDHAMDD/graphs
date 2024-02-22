import java.util.*;

public class Graph<T> { // The Graph class represents a generic undirected graph
    // <T> the type of the nodes in the graph
    public Map<T, Map<T, Integer>> map = new HashMap<>();
    // The map stores the adjacency list representation of the graph
    public double[] pageRank; //Array for pageRank Algorithm
	public double[] contribution; //Array for pageRank Algorithm
	public double[] old_pageRank; //Array for pageRank Algorithm

    public void addVertex(T node) { // Adds a vertex (node) to the graph.
        map.put(node, new HashMap<>());
    }

    public void addEdge(T r1, T r2, int w) { // Adds an undirected edge between two nodes with a specified weight
        if (!map.containsKey(r1)) { // Check if the first node exists in the graph, if not, add it
            addVertex(r1);
        }
        if (!map.containsKey(r2)) { // Check if the second node exists in the graph, if not, add it
            addVertex(r2);
        }
        // Add the edge by updating the maps of both nodes with the corresponding weight
        map.get(r1).put(r2, w);
        // map.get(r2).put(r1, w); Uncomment this line to make the complete graph Undirected
    }

    public String toString() {
        // Returns a string representation of the graph.
        if (map.isEmpty())
            // Check if the graph is empty
            return new String("The Graph is Empty");

        StringBuilder builder = new StringBuilder();
        // Iterate over each vertex in the map
        for (T v : map.keySet()) {
            builder.append(v.toString() + ": ");
            // Iterate over each neighboring node and its corresponding edge weight
            for (T knb : map.get(v).keySet()) {
                builder.append(knb.toString() + " " + map.get(v).get(knb) + "  ");
            }
            builder.append("\n");
        }

        return (builder.toString() + "\n"); // return the string
    }

    public Boolean isConnected() { // Checks whether the graph is connected or not.
        // returns true if the graph is connected, false otherwise
        if (map.isEmpty()) {// Check if the graph is empty
            return false; // An empty graph is considered not connected
        }
        // Initialize the set to track visited vertices
        Set<T> visited = new HashSet<>();
        // Initialize the queue for breadth-first search traversal
        Queue<T> queue = new LinkedList<>();
        // Get the set of all vertices in the graph
        Set<T> keySet = map.keySet();
        // Create an iterator to traverse the keySet
        Iterator<T> it = keySet.iterator();
        // Start the traversal from the first vertex in the keySet
        queue.add(it.next());
        visited.add(it.next());
        // Perform breadth-first search traversal
        while (!queue.isEmpty()) {
            T currVer = queue.poll(); // Get the current vertex from the queue
            // Iterate over the neighbors of the current vertex
            for (T neighbor : map.get(currVer).keySet()) {
                // If the neighbor is not visited, add it to the queue and mark it as visited
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }
        // Check if all vertices have been visited
        return visited.size() == map.size();
    }

    /**
     * Checks whether the graph contains cycles.
     * If a cycle is found, it prints a message indicating the presence of a cycle.
     * If no cycles are found, it prints a message indicating the absence of cycles.
     */
    public void isCyclic() {
        if (map.isEmpty()) { // Check if the graph is empty
            System.out.println("The Graph is Empty");
            return;
        }
        // Initialize the set to track visited vertices
        Set<T> visited = new HashSet<>();
        // Iterate over each vertex in the graph
        for (T vertex : map.keySet()) {
            // Check if the vertex has not been visited yet
            if (!visited.contains(vertex)) {
                // Check if the vertex is part of a cycle
                if (isCyclic2(vertex, visited, null)) {
                    System.out.println("Graph contains cycle" + "\n");
                    return;
                }
            }
        }
        // If no cycles are found, print a message indicating the absence of cycles
        System.out.println("Graph doesn't contain cycle" + "\n");
        return;
        // return false;
    }

    // Helper method to check if the graph contains a cycle.
    private Boolean isCyclic2(T vertex, Set<T> visited, T parent) {
        // Mark the current vertex as visited
        visited.add(vertex);
        // Iterate over the neighbors of the current vertex
        for (T neighbour : map.get(vertex).keySet()) {
            // If the neighbor has not been visited, recursively check if it is part of a
            // cycle
            if (!visited.contains(neighbour))
                if (isCyclic2(neighbour, visited, vertex)) {
                    return true;
                    // If the neighbor has been visited and is not the parent of the current vertex,
                    // a cycle is detected
                } else if ((!neighbour.equals(parent)))
                    return true;
        }
        return false; // No cycles found in the current traversal path
    }

    public List<T> Dijkstara(T t1, T t2) {

        List<T> path = new LinkedList<>();
        // Check if both t1 and t2 are present in the graph
        if (!map.containsKey(t1) || !map.containsKey(t2)) {
            System.out.println("One of the Given Vertexes are not present in the Graph");
            path.add(null);
            return path;
        }
        // Initialize distance and previous node maps
        Map<T, Integer> distance = new HashMap<>();
        Map<T, T> prevNode = new HashMap<>();
        // Comparator class to compare vertices based on their distances
        class GComparator implements Comparator<T> {
            public int compare(T t1, T t2) {
                if (distance.get(t1) > distance.get(t2))
                    return 1;
                else if (distance.get(t1) < distance.get(t2))
                    return -1;
                else
                    return 0;
            }
        }
        // Priority queue to store vertices based on their distances
        PriorityQueue<T> q = new PriorityQueue<>(new GComparator());
        // Initialize distance and previous node maps, and add vertices to the priority
        // queue
        for (T v : map.keySet()) {
            distance.put(v, Integer.MAX_VALUE); // Set initial distance to infinity
            prevNode.put(v, null); // Set initial previous node as null
            q.add(v); // Add vertex to the priority queue
        }
        distance.put(t1, 0); // Set distance of source vertex t1 as 0

        // Dijkstra's algorithm
        while (!q.isEmpty()) {
            T curr = q.poll(); // Remove vertex with minimum distance from the priority queue
            for (T v : map.get(curr).keySet()) {
                if (q.contains(v) || v.equals(t2)) {
                    // update distance and previous node if a shorter path is found
                    if (map.get(curr).get(v) < distance.get(v) - distance.get(curr)) {
                        distance.put(v, distance.get(curr) + map.get(curr).get(v));
                        prevNode.put(v, curr);
                    }
                }
            }
        }
        // Build the shortest path from t1 to t2
        path.add(t2);
        T curr = prevNode.get(t2);

        while (curr != t1) {
            path.add(curr);
            curr = prevNode.get(curr);
        }

        path.add(t1);
        Collections.reverse(path); // Reverse the path to get it from t1 to t2
        return path;
    }

    public static <T> boolean isBipartite(Graph<T> graph) {// Checks whether the given graph is bipartite or not.
        if (graph.map.isEmpty())// Check if the graph is empty
            return false;// An empty graph is considered not bipartite
        // Initialize the set to track visited vertices
        Set<T> visited = new HashSet<>();
        // Initialize the color map to assign colors to vertices
        Map<T, Integer> colorMap = new HashMap<>();

        // Iterate over each vertex in the graph
        for (T vertex : graph.map.keySet()) {
            // Check if the vertex has not been visited yet
            if (!visited.contains(vertex)) {
                // Check if the graph is bipartite starting from the current vertex
                if (isBipartite2(graph, vertex, visited, colorMap)) {
                    System.out.println("Graph is bipartite");
                    return true; // Graph is bipartite
                }
            }
        }
        // If the method reaches this point, the graph is not bipartite
        System.out.println("Graph is not bipartite");
        return false; //Graph is not bipartite
    }

    // Helper method to check if the graph is bipartite starting from a specific
    // vertex.
    public static <T> boolean isBipartite2(Graph<T> graph, T vertex, Set<T> visited, Map<T, Integer> colorMap) {
        // Create a queue for breadth-first search traversal
        Queue<T> queue = new LinkedList<>();
        // Add the current vertex to the queue, mark it as visited, and assign color 0
        queue.add(vertex);
        visited.add(vertex);
        colorMap.put(vertex, 0);
        // Perform breadth-first search traversal
        while (!queue.isEmpty()) {
            // Get the current vertex from the queue
            T currVer = queue.poll();
            int currColor = colorMap.get(currVer);
            // Iterate over the neighbors of the current vertex
            for (T neighbor : graph.map.get(currVer).keySet()) {
                // If the neighbor has not been visited, assign a color and add it to the queue
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    colorMap.put(neighbor, 1 - currColor); // Assign opposite color to that of current vertex
                }
                // If the neighbor has been visited and has the same color as the current
                // vertex, the graph is not bipartite
                else if (colorMap.get(neighbor) == currColor) {
                    return false;
                }
            }
        }
        return true;// The graph is bipartite
    }

    public void initializePageRank(){
        // Get the total number of vertices in the graph
        int totalVertex = map.keySet().size();
        // Calculate the initial rank for each vertex
        double initialRank = 1.0 / totalVertex;
        // Initialize arrays to store page ranks, old page ranks, and contributions
        pageRank = new double[totalVertex];
	    old_pageRank = new double[totalVertex];
	    contribution = new double[totalVertex];
        // Set all page ranks to the initial rank
        Arrays.fill(pageRank, initialRank);
        Arrays.fill(old_pageRank, initialRank);
        // Calculate the contribution of each vertex
        for(int i=0;i<totalVertex;i++){
            contribution[i] = map.get(i).size();
        }
    }

    public void PageRank(int round) {
        // Initialize page ranks and other variables
        initializePageRank();
        int totalVertex = map.keySet().size();
        double[] newPageRankArray = new double[totalVertex];
        // Perform the PageRank algorithm for the specified number of rounds
        for(int k =0;k<round-1;k++){
            // Initialize the new page rank array
            Arrays.fill(newPageRankArray,0);
            // Copy the current page ranks to the old page rank array
            old_pageRank = Arrays.copyOf(pageRank, pageRank.length);
            // Perform the PageRank calculation for each vertex
            for(T v : map.keySet()){
                for(T u : map.keySet()){
                    // Check if there is an edge from u to v
                    if(map.get(u).containsKey(v)){
                        // Update the new page rank for v based on the old page rank of u and its contribution
                        newPageRankArray[(int)v] += old_pageRank[(int)u]/contribution[(int)u];// Assumption: We are given a Integer Graph
                    }
                }
            }
            // Update the page rank array with the new page ranks
            pageRank = Arrays.copyOf(newPageRankArray, newPageRankArray.length);
        }
        System.out.println("Round " + round + " = ");
        System.out.println(Arrays.toString(pageRank));
    }

    public void maximumFlow() { // Bose moved this method to next HomeWork
        if (map.isEmpty()) {
            System.out.println("The Graph is Empty");
            return;
        }
    }

    public static void main(String[] args) {
        Graph<String> graph1 = new Graph<>();
        graph1.addVertex("A");
        graph1.addVertex("B");
        graph1.addEdge("A", "B", 2);
        graph1.addEdge("B", "A", 2);
        graph1.addEdge("A", "C", 4);
        graph1.addEdge("C", "A", 4);
        graph1.addVertex("D");
        System.out.println(" isConnected graph1 = "+graph1.isConnected()); // prints False
        graph1.addEdge("D", "C", 7);
        graph1.addEdge("C", "D", 7);
        System.out.println(" isConnected graph1 = "+ graph1.isConnected()); // prints True
        System.out.println("graph1"); // toString test
        System.out.println(graph1); // toString test
        System.out.println(" isCyclic graph 1 =");
        graph1.isCyclic();

        Graph<String> graph2 = new Graph<>();
        System.out.println(" isConnected graph2 " + graph2.isConnected()); // Empty graph is assumed to be not connected
        System.out.println("graph2");
        System.out.println(graph2);
        System.out.println("isCyclic graph2 = ");
        graph2.isCyclic();

        Graph<String> graph3 = new Graph<>();
        graph3.addVertex("A");
        graph3.addVertex("B");
        graph3.addEdge("A", "B", 2);
        graph3.addEdge("B", "A", 2);
        graph3.addVertex("C");
        graph3.addVertex("D");
        System.out.println("isConnected graph3 = " + graph3.isConnected());
        graph3.addEdge("B", "C", 5);
        graph3.addEdge("C", "B", 5);
        graph3.addEdge("C", "A", 10);
        graph3.addEdge("A", "C", 10);
        graph3.addEdge("D", "A", 8);
        graph3.addEdge("A", "D", 8);
        System.out.println("graph3");
        System.out.println(graph3);
        System.out.println("isCyclic graph3 = ");
        graph3.isCyclic();

        Graph<String> graph4 = new Graph<>(); // Grapht to test Dijkstara's Algotrithm
        graph4.addVertex("A");
        graph4.addVertex("B");
        graph4.addEdge("A", "B", 10);
        graph4.addEdge("B", "A", 10);
        graph4.addVertex("C");
        graph4.addVertex("D");
        graph4.addEdge("A", "C", 15);
        graph4.addEdge("C", "A", 15);
        graph4.addEdge("D", "B", 12);
        graph4.addEdge("B", "D", 12);
        graph4.addEdge("F", "B", 15);
        graph4.addEdge("B", "F", 15);
        graph4.addEdge("C", "E", 10);
        graph4.addEdge("E", "C", 10);
        graph4.addEdge("D", "F", 1);
        graph4.addEdge("F", "D", 1);
        graph4.addEdge("D", "E", 2);
        graph4.addEdge("E", "D", 2);
        graph4.addEdge("F", "E", 5);
        graph4.addEdge("E", "F", 5);
        System.out.println("graph4");
        System.out.println(graph4);
        System.out.println("Dijkstara graph 4 " + graph4.Dijkstara("A", "E"));
        System.out.println("Dijkstara graph 4 " + graph4.Dijkstara("F", "E"));

        Graph<String> graph5 = new Graph<>(); // Grapht to test Bipartite
        graph5.addVertex("A");
        graph5.addVertex("B");
        graph5.addEdge("A", "B", 1);
        graph5.addEdge("B", "A", 1);
        graph5.addVertex("C");
        graph5.addVertex("D");
        graph5.addEdge("B", "C", 1);
        graph5.addEdge("C", "B", 1);
        graph5.addEdge("C", "D", 1);
        graph5.addEdge("D", "C", 1);
        graph5.addEdge("D", "E", 1);
        graph5.addEdge("E", "D", 1);
        graph5.addEdge("E", "F", 1);
        graph5.addEdge("F", "E", 1);
        graph5.addEdge("F", "A", 1);
        graph5.addEdge("A", "F", 1);
        System.out.println("graph5");
        System.out.println(graph5);
        System.out.println("isBipartite graph5 = ");
        isBipartite(graph5);

        Graph<Integer> graph6 = new Graph<>(); // integer Graph for Page Rank Algorithm
        graph6.addEdge(0, 1, 1); //Example 1 from Bose's slides for PageRank Algorithm
        graph6.addEdge(1, 0, 1);
        graph6.addEdge(1, 2, 1);
        graph6.addEdge(2, 3, 1);
        graph6.addEdge(3, 1, 1);
        graph6.addEdge(4, 3, 1);
        System.out.println("graph6");
        System.out.println(graph6);
        System.out.println("PageRank graph 6 = ");
        graph6.PageRank(8);

        Graph<Integer> graph7 = new Graph<>(); // integer Graph for Page Rank Algorithm
        graph7.addEdge(0, 2, 1); //Example 3 from Bose's slides for PageRank Algorithm
        graph7.addEdge(2, 3, 1); //PageRank Example with a Rank Trap
        graph7.addEdge(3, 2, 1);
        graph7.addEdge(1, 0, 1);
        graph7.addEdge(1, 3, 1);
        System.out.println("graph7");
        System.out.println(graph7);
        System.out.println("PageRank graph 7 = ");
        graph7.PageRank(5);

    }
}
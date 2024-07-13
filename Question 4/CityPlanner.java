import java.util.*;

public class CityPlanner {

    // Function to modify the roads' travel times to achieve a specific target travel time from source to destination
    public static List<int[]> modifyRoads(int n, int[][] roads, int source, int destination, int targetTime) {
        // Create an adjacency list to represent the graph
        List<int[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        // Build the graph using the given roads
        for (int[] road : roads) {
            // Add the road in both directions since the graph is undirected
            graph[road[0]].add(road);
            graph[road[1]].add(new int[]{road[1], road[0], road[2]});
        }

        // Debugging: Print the initial graph representation
        System.out.println("Initial Graph:");
        for (int i = 0; i < n; i++) {
            System.out.print(i + ": ");
            for (int[] edge : graph[i]) {
                System.out.print(Arrays.toString(edge) + " ");
            }
            System.out.println();
        }

        // Initialize the distance array with infinity, except for the source which is 0
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        // Priority queue to implement Dijkstra's algorithm
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{source, 0});

        // Array to store the previous node in the shortest path
        int[] prev = new int[n];
        Arrays.fill(prev, -1);

        // Dijkstra's algorithm to find the shortest path from source to destination
        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];
            int d = curr[1];

            // If the current distance is greater than the recorded distance, skip this node
            if (d > dist[u]) continue;

            // Iterate through all the adjacent nodes
            for (int[] edge : graph[u]) {
                int v = edge[1];
                int weight = edge[2] == -1 ? 1 : edge[2]; // If the road is under construction, assign initial weight of 1
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    prev[v] = u;
                    pq.add(new int[]{v, dist[v]});
                }
            }
        }

        // Debugging: Print the shortest distances from the source
        System.out.println("Shortest distances from source:");
        for (int i = 0; i < n; i++) {
            System.out.println("Node " + i + ": " + dist[i]);
        }

        // Calculate the total construction time needed to achieve the target travel time
        int totalConstructionTime = targetTime - dist[destination];

        // Modify the roads to achieve the target travel time
        for (int[] road : roads) {
            if (road[2] == -1) {
                road[2] = 1; // Assign initial weight of 1 to under construction roads
            }
        }

        // Debugging: Print the modified roads
        System.out.println("Modified roads:");
        for (int[] road : roads) {
            System.out.println(Arrays.toString(road));
        }

        // Adjust the construction times to meet the target time
        for (int i = 0; i < roads.length; i++) {
            if (roads[i][2] == -1) {
                roads[i][2] = totalConstructionTime; // Assign the remaining construction time
                break;
            }
        }

        // Debugging: Print the final roads
        System.out.println("Final roads:");
        for (int[] road : roads) {
            System.out.println(Arrays.toString(road));
        }

        // Return the modified roads as a list of int arrays
        return Arrays.asList(roads);
    }

    public static void main(String[] args) {
        // Number of nodes in the graph
        int n = 5;

        // Roads represented as int arrays [start, end, weight]
        // Weight -1 indicates the road is under construction
        int[][] roads = {{4, 1, -1}, {2, 0, -1}, {0, 3, -1}, {4, 3, -1}};

        // Source node, destination node, and the target travel time
        int source = 0;
        int destination = 1;
        int targetTime = 5;

        // Call the modifyRoads function to get the modified roads
        List<int[]> result = modifyRoads(n, roads, source, destination, targetTime);

        // Print the output roads
        System.out.println("Output roads:");
        for (int[] road : result) {
            System.out.println(Arrays.toString(road));
        }
    }
}

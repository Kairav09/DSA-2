
import java.util.*;

public class CO4_casestudy {

    static final String[] NAMES = {"MJC", "KEM", "JAY", "KOR", "WHF", "HBR", "MRT"};
    // index:                        0      1      2      3      4      5      6

    static class Edge {

        int u, v, weight;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.weight = w;
        }
    }

    // 11 directed edges (weight = travel time + surge penalty, minutes)
    static List<Edge> graphEdges() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 8));   // MJC -> KEM
        edges.add(new Edge(0, 2, 5));   // MJC -> JAY
        edges.add(new Edge(0, 3, 12));  // MJC -> KOR
        edges.add(new Edge(1, 5, 7));   // KEM -> HBR
        edges.add(new Edge(1, 4, 10));  // KEM -> WHF
        edges.add(new Edge(2, 3, 4));   // JAY -> KOR
        edges.add(new Edge(3, 6, 9));   // KOR -> MRT
        edges.add(new Edge(4, 5, 3));   // WHF -> HBR
        edges.add(new Edge(4, 6, -3));  // WHF -> MRT  (negative, IT-corridor bonus)
        edges.add(new Edge(5, 6, 11));  // HBR -> MRT
        edges.add(new Edge(3, 4, 6));   // KOR -> WHF
        return edges;
    }

    // ---- (a) Dijkstra (naive, treats -3 edge as 0) ----
    static int[] dijkstra(int n, List<Edge> edges, int source) {
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (Edge e : edges) {
            int w = (e.weight < 0) ? 0 : e.weight;  // treat -3 as 0 for this part
            adj.get(e.u).add(new int[]{e.v, w});
        }

        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        boolean[] visited = new boolean[n];

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.add(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0];
            if (visited[u]) {
                continue;
            }
            visited[u] = true;
            for (int[] nb : adj.get(u)) {
                int v = nb[0], w = nb[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.add(new int[]{v, dist[v]});
                }
            }
        }
        return dist;
    }

    // ---- (b) Bellman-Ford, V-1 iterations, table per iteration ----
    static int[][] bellmanFordTrace(int n, List<Edge> edges, int source) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        int[][] table = new int[n - 1][n]; // one row per iteration

        for (int iter = 0; iter < n - 1; iter++) {
            for (Edge e : edges) {
                if (dist[e.u] != Integer.MAX_VALUE && dist[e.u] + e.weight < dist[e.v]) {
                    dist[e.v] = dist[e.u] + e.weight;
                }
            }
            table[iter] = dist.clone();
        }
        return table;
    }

    // ---- (c) Bellman-Ford with negative-cycle detection (boilerplate) ----
    static int[] bellmanFord(int n, List<Edge> edges, int source) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        // V-1 iterations of relaxation.
        for (int iter = 0; iter < n - 1; iter++) {
            // TODO 1: scan every edge; if dist[u] != MAX_VALUE and
            //         dist[u] + weight < dist[v], relax dist[v]
            for (Edge e : edges) {
                if (dist[e.u] != Integer.MAX_VALUE && dist[e.u] + e.weight < dist[e.v]) {
                    dist[e.v] = dist[e.u] + e.weight;
                }
            }
        }

        // One more pass: any relaxation now indicates a negative cycle.
        for (Edge e : edges) {
            // TODO 2: if dist[e.u] != MAX_VALUE and dist[e.u] + e.weight < dist[e.v],
            //         throw new RuntimeException("negative cycle reachable from source")
            if (dist[e.u] != Integer.MAX_VALUE && dist[e.u] + e.weight < dist[e.v]) {
                throw new RuntimeException("negative cycle reachable from source");
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        int n = 7;
        List<Edge> edges = graphEdges();

        // ---- (a) Dijkstra (treating -3 as 0) ----
        System.out.println("=== (a) Dijkstra from MJC (treating -3 edge as 0) ===");
        int[] dijkDist = dijkstra(n, edges, 0);
        for (int i = 0; i < n; i++) {
            System.out.println("  " + NAMES[0] + " -> " + NAMES[i] + " : " + dijkDist[i]);
        }
        System.out.println();

        // ---- (b) Bellman-Ford trace table ----
        System.out.println("=== (b) Bellman-Ford from MJC - dist[] after each iteration ===");
        int[][] table = bellmanFordTrace(n, edges, 0);
        System.out.print("Iter |");
        for (String name : NAMES) {
            System.out.printf(" %4s |", name);
        }
        System.out.println();
        for (int it = 0; it < table.length; it++) {
            System.out.printf("  %2d |", it + 1);
            for (int v = 0; v < n; v++) {
                String val = (table[it][v] == Integer.MAX_VALUE) ? " INF" : String.valueOf(table[it][v]);
                System.out.printf(" %4s |", val);
            }
            System.out.println();
        }
        System.out.println();

        // ---- (c) Bellman-Ford with negative-cycle detection ----
        System.out.println("=== (c) Bellman-Ford with negative-cycle detection ===");
        try {
            int[] bfDist = bellmanFord(n, edges, 0);
            System.out.println("No negative cycle detected. Final shortest distances from MJC:");
            for (int i = 0; i < n; i++) {
                System.out.println("  " + NAMES[0] + " -> " + NAMES[i] + " : " + bfDist[i]);
            }
        } catch (RuntimeException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }
}

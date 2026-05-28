
import java.util.*;

public class CO3_casestudy {

    static class UnionFindNaive {

        int[] parent;

        UnionFindNaive(int n) {
            parent = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {                      // NO path compression
            while (parent[x] != x) {
                x = parent[x];
            }
            return x;
        }

        boolean union(int x, int y) {          // NO union-by-rank
            int rx = find(x), ry = find(y);
            if (rx == ry) {
                return false;
            }
            parent[rx] = ry;
            return true;
        }
    }

    static List<int[]> kruskalNaive(int n, int[][] edges) {
        Arrays.sort(edges, Comparator.comparingInt(e -> e[0])); // sort by weight asc

        UnionFindNaive uf = new UnionFindNaive(n);
        List<int[]> mst = new ArrayList<>();

        for (int[] e : edges) {               // e = {w, u, v}
            if (uf.union(e[1], e[2])) {
                mst.add(e);
            }
        }

        return mst;
    }

    public static void main(String[] args) {
        String[] names = {"M", "K", "W", "S", "E", "Y", "H"};

        int[][] edges = {
            {4, 4, 3}, // E-S
            {5, 1, 2}, // K-W
            {6, 2, 3}, // W-S
            {7, 0, 4}, // M-E
            {8, 1, 4}, // K-E
            {9, 0, 5}, // Y-M
            {9, 5, 6}, // Y-H
            {10, 3, 0}, // S-M
            {11, 0, 6}, // H-M
            {12, 0, 2}, // M-W
            {14, 1, 6}, // K-H
            {8, 2, 4}, // W-E
        };

        List<int[]> mst = kruskalNaive(7, edges);

        System.out.println("MST Edges:");
        int total = 0;
        for (int[] e : mst) {
            System.out.println("  " + names[e[1]] + " - " + names[e[2]] + "  cost=" + e[0]);
            total += e[0];
        }
        System.out.println("Total cost: " + total + " crore");
    }
}

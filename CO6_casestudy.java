
import java.util.*;

public class CO6_casestudy {

    static final String[] NAMES = {"A", "B", "C", "D", "E", "F", "G", "H"};
    static final int[] WEIGHTS = {5, 8, 3, 10, 4, 6, 7, 2};
    static final int[] VALUES = {40, 50, 20, 70, 30, 35, 45, 15};
    static final int CAPACITY = 24;

    // ---- (a) Greedy attempt by v/w ratio (flawed for 0/1 Knapsack) ----
    static void greedyAttempt() {
        int n = WEIGHTS.length;
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) {
            idx[i] = i;
        }
        // sort by value/weight descending
        Arrays.sort(idx, (a, b) -> Double.compare(
                (double) VALUES[b] / WEIGHTS[b], (double) VALUES[a] / WEIGHTS[a]));

        System.out.println("Order considered (by v/w ratio descending):");
        int remaining = CAPACITY, totalWeight = 0, totalValue = 0;
        List<String> packed = new ArrayList<>();
        for (int i : idx) {
            double ratio = (double) VALUES[i] / WEIGHTS[i];
            if (WEIGHTS[i] <= remaining) {
                remaining -= WEIGHTS[i];
                totalWeight += WEIGHTS[i];
                totalValue += VALUES[i];
                packed.add(NAMES[i]);
                System.out.printf("  %s  w=%-3d v=%-3d ratio=%.2f  -> PACKED (remaining cap=%d)%n",
                        NAMES[i], WEIGHTS[i], VALUES[i], ratio, remaining);
            } else {
                System.out.printf("  %s  w=%-3d v=%-3d ratio=%.2f  -> SKIPPED (doesn't fit, cap=%d)%n",
                        NAMES[i], WEIGHTS[i], VALUES[i], ratio, remaining);
            }
        }
        System.out.println("Greedy packed items: " + packed);
        System.out.println("Greedy total weight: " + totalWeight + " t,  total value: " + totalValue + " k");
    }

    // ---- (b) and (c): DP with item recovery (boilerplate filled) ----
    /**
     * Returns indices (1-based) of items included in the optimal subset.
     */
    static List<Integer> knapsack01(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[][] dp = new int[n + 1][W + 1];
        // dp[0][*] = 0 by Java default

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                // TODO 1: skip-item case: dp[i][w] = dp[i-1][w]
                int skip = dp[i - 1][w];

                // TODO 2: if weights[i-1] <= w, also consider taking item i:
                //         dp[i-1][w - weights[i-1]] + values[i-1]
                int take = Integer.MIN_VALUE;
                if (weights[i - 1] <= w) {
                    take = dp[i - 1][w - weights[i - 1]] + values[i - 1];
                }

                // TODO 3: dp[i][w] = max of the two
                dp[i][w] = Math.max(skip, take);
            }
        }

        System.out.println("DP table row i=" + n + " (final row), w=0.." + W + ":");
        System.out.print("  ");
        for (int w = 0; w <= W; w++) {
            System.out.print(dp[n][w] + " ");
        }
        System.out.println();
        System.out.println("Optimal value dp[" + n + "][" + W + "] = " + dp[n][W]);

        // Back-trace: walk from dp[n][W] to determine which items are taken.
        List<Integer> chosen = new ArrayList<>();
        int w = W;
        for (int i = n; i >= 1; i--) {
            // TODO 4: if dp[i][w] != dp[i-1][w], item i was taken.
            //         Add i to `chosen`, decrement w by weights[i-1].
            if (dp[i][w] != dp[i - 1][w]) {
                chosen.add(i);
                w -= weights[i - 1];
            }
        }
        Collections.reverse(chosen);
        return chosen;
    }

    public static void main(String[] args) {
        System.out.println("=== (a) Greedy attempt by v/w ratio (flawed for 0/1 Knapsack) ===");
        greedyAttempt();
        System.out.println();

        System.out.println("=== (b)/(c) 0/1 Knapsack DP with item recovery ===");
        List<Integer> chosen = knapsack01(WEIGHTS, VALUES, CAPACITY);
        System.out.print("Items included in optimal subset: ");
        int totalW = 0, totalV = 0;
        for (int i : chosen) {
            System.out.print(NAMES[i - 1] + " ");
            totalW += WEIGHTS[i - 1];
            totalV += VALUES[i - 1];
        }
        System.out.println();
        System.out.println("Total weight: " + totalW + " t,  Total value: " + totalV + " k");
    }
}

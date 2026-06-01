
public class CO2_casestudy {

    static class SegTreeLazy {

        long[] tree;
        long[] lazy;
        int n;

        SegTreeLazy(int n) {
            this.n = n;
            tree = new long[4 * n];
            lazy = new long[4 * n];
        }

        void pushDown(int node) {
            if (lazy[node] != 0) {
                tree[2 * node] += lazy[node];
                lazy[2 * node] += lazy[node];
                tree[2 * node + 1] += lazy[node];
                lazy[2 * node + 1] += lazy[node];
                lazy[node] = 0;
            }
        }

        // TODO: handle no-overlap, full-overlap (lazy mark + return), partial-overlap.
        void updateRange(int node, int lo, int hi, int l, int r, long delta) {
            if (lo > r || hi < l) {
                return;
            }
            if (lo >= l && hi <= r) {
                tree[node] += delta;
                lazy[node] += delta;
                return;
            }
            pushDown(node);
            int mid = (lo + hi) / 2;
            updateRange(2 * node, lo, mid, l, r, delta);
            updateRange(2 * node + 1, mid + 1, hi, l, r, delta);
            tree[node] = Math.max(tree[2 * node], tree[2 * node + 1]);
        }

        long queryMax(int node, int lo, int hi, int l, int r) {
            if (lo > r || hi < l) {
                return Long.MIN_VALUE;
            }
            if (lo >= l && hi <= r) {
                return tree[node];
            }
            pushDown(node);
            int mid = (lo + hi) / 2;
            return Math.max(queryMax(2 * node, lo, mid, l, r), queryMax(2 * node + 1, mid + 1, hi, l, r));
        }

        void printState() {
            System.out.print("  Zone values: [ ");
            for (int i = 1; i <= n; i++) {
                System.out.printf("%.1f ", queryMax(1, 1, n, i, i) / 10.0);
            }
            System.out.println("]");
        }
    }

    public static void main(String[] args) {
        int N = 16;
        SegTreeLazy sg = new SegTreeLazy(N);
        for (int i = 1; i <= N; i++) {
            sg.updateRange(1, 1, N, i, i, 10);
        }

        System.out.println("Initial: all zones = 1.0");
        System.out.println();

        System.out.println("Update 1: update [3, 9] += 0.5  (M.G. Road event ends)");
        sg.updateRange(1, 1, N, 4, 10, 5);
        sg.printState();
        System.out.println("  Lazy marker set on internal nodes fully covering [3,9].");
        System.out.println();

        System.out.println("Update 2: update [7, 14] += 0.3  (Whitefield IT shift ends)");
        sg.updateRange(1, 1, N, 8, 15, 3);
        sg.printState();
        System.out.println("  Lazy marker set on internal nodes fully covering [7,14].");
        System.out.println();

        System.out.println("Query 1: query max [0, 15]");
        double q1 = sg.queryMax(1, 1, N, 1, N) / 10.0;
        System.out.println("  Path: root[0,15] -> full overlap -> answer = " + q1);
        System.out.println("  query max [0,15] = " + q1);
        System.out.println();

        System.out.println("Update 3: update [2, 6] += 0.7  (Cricket stadium emptying)");
        sg.updateRange(1, 1, N, 3, 7, 7);
        sg.printState();
        System.out.println("  Lazy marker set on internal nodes fully covering [2,6].");
        System.out.println();

        System.out.println("Query 2: query max [4, 10]");
        double q2 = sg.queryMax(1, 1, N, 5, 11) / 10.0;
        System.out.println("  Path: root[0,15] -> [0,7] -> [4,7] -> ... push down lazy, merge max");
        System.out.println("  query max [4,10] = " + q2);
        System.out.println();

        System.out.println("Worst-case internal nodes visited per operation: O(4 * log2(16)) = O(16)");
        System.out.println("Time complexity per operation as function of n: O(log n)");
    }
}

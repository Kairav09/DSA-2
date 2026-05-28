
public class CO2_casestudy {

    static class SegTreeLazy {

        long[] tree;   // max value at each node
        long[] lazy;   // pending add for each node
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

        /**
         * Add delta to every leaf in [l, r]. lo, hi is current node's segment.
         */
        // TODO: handle no-overlap, full-overlap (lazy mark + return), partial-overlap.
        void updateRange(int node, int lo, int hi, int l, int r, long delta) {
            // No-overlap
            if (lo > r || hi < l) {
                return;
            }

            // Full-overlap: mark lazy and update max, then return
            if (lo >= l && hi <= r) {
                tree[node] += delta;
                lazy[node] += delta;
                return;
            }

            // Partial-overlap: push down, recurse, pull up max
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
            return Math.max(
                    queryMax(2 * node, lo, mid, l, r),
                    queryMax(2 * node + 1, mid + 1, hi, l, r)
            );
        }
    }

    public static void main(String[] args) {
        int N = 16;
        SegTreeLazy sg = new SegTreeLazy(N);
        for (int i = 1; i <= N; i++) {
            sg.updateRange(1, 1, N, i, i, 10);
        }

        sg.updateRange(1, 1, N, 4, 10, 5);
        sg.updateRange(1, 1, N, 8, 15, 3);
        System.out.println("Query max [0,15] = " + sg.queryMax(1, 1, N, 1, N) / 10.0);

        sg.updateRange(1, 1, N, 3, 7, 7);
        System.out.println("Query max [4,10] = " + sg.queryMax(1, 1, N, 5, 11) / 10.0);
    }
}

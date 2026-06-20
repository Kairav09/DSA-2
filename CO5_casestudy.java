
public class CO5_casestudy {

    static class Delivery {

        int over, ball;

        Delivery(int over, int ball) {
            this.over = over;
            this.ball = ball;
        }

        public String toString() {
            return "(" + over + "," + ball + ")";
        }
    }

    // ---- (a) Counting sort applied twice: first by ball, then stably by over ----
    static Delivery[] countingSortByBall(Delivery[] in) {
        final int K = 12; // ball range 1..12 (pathological overs)
        int[] count = new int[K + 1];
        for (Delivery d : in) {
            count[d.ball]++;
        }
        for (int i = 1; i <= K; i++) {
            count[i] += count[i - 1];
        }

        Delivery[] out = new Delivery[in.length];
        for (int i = in.length - 1; i >= 0; i--) {
            Delivery d = in[i];
            out[--count[d.ball]] = d;
        }
        return out;
    }

    static Delivery[] countingSortByOver(Delivery[] in) {
        final int K = 49; // over range 0..49
        int[] count = new int[K + 1];
        for (Delivery d : in) {
            count[d.over]++;
        }
        for (int i = 1; i <= K; i++) {
            count[i] += count[i - 1];
        }

        Delivery[] out = new Delivery[in.length];
        for (int i = in.length - 1; i >= 0; i--) {
            Delivery d = in[i];
            out[--count[d.over]] = d;
        }
        return out;
    }

    // ---- (b) Boilerplate: stable counting sort by `over` field ----
    static Delivery[] countingSortByOverBoilerplate(Delivery[] in) {
        final int K = 50;
        int[] count = new int[K + 1];
        // TODO 1: count occurrences: for each d in `in`, count[d.over]++
        for (Delivery d : in) {
            count[d.over]++;
        }

        // TODO 2: convert to prefix sums so count[i] = #elements with over <= i
        for (int i = 1; i <= K; i++) {
            count[i] += count[i - 1];
        }

        Delivery[] out = new Delivery[in.length];
        // TODO 3: walk `in` in REVERSE order to preserve stability.
        //         For each d: out[--count[d.over]] = d.
        for (int i = in.length - 1; i >= 0; i--) {
            Delivery d = in[i];
            out[--count[d.over]] = d;
        }
        return out;
    }

    public static void main(String[] args) {
        // (a) 10 deliveries, composite key (over, ball)
        Delivery[] deliveries = {
            new Delivery(2, 4), new Delivery(1, 1), new Delivery(3, 6),
            new Delivery(1, 5), new Delivery(2, 2), new Delivery(3, 1),
            new Delivery(1, 3), new Delivery(2, 6), new Delivery(3, 4),
            new Delivery(1, 2)
        };

        System.out.println("=== (a) Counting sort applied twice (LSD radix-style) ===");
        System.out.print("Input (unsorted): ");
        for (Delivery d : deliveries) {
            System.out.print(d + " ");
        }
        System.out.println();

        Delivery[] afterBall = countingSortByBall(deliveries);
        System.out.print("Pass 1 - sorted by ball: ");
        for (Delivery d : afterBall) {
            System.out.print(d + " ");
        }
        System.out.println();

        Delivery[] finalSorted = countingSortByOver(afterBall);
        System.out.print("Pass 2 - stably sorted by over: ");
        for (Delivery d : finalSorted) {
            System.out.print(d + " ");
        }
        System.out.println();
        System.out.println();

        // (b) Boilerplate counting sort by over only
        System.out.println("=== (b) Stable Counting Sort by `over` field (K=50) ===");
        Delivery[] sortedByOver = countingSortByOverBoilerplate(deliveries);
        System.out.print("Sorted by over: ");
        for (Delivery d : sortedByOver) {
            System.out.print(d + " ");
        }
        System.out.println();
    }
}

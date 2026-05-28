
public class CO1_casestudy {

    static class AVLNode {

        int key;
        AVLNode left, right;
        int height = 1;

        AVLNode(int key) {
            this.key = key;
        }
    }

    static int height(AVLNode n) {
        return n == null ? 0 : n.height;
    }

    static int balance(AVLNode n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    static void updateHeight(AVLNode n) {
        if (n != null) {
            n.height = 1 + Math.max(height(n.left), height(n.right));
        }
    }

    // TODO 1: perform a right rotation around y; return the new subtree root.
    static AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;
        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    // TODO 2: perform a left rotation around x; return the new subtree root.
    static AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;
        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    // TODO 3: standard BST insert + rebalance using the four cases (LL, LR, RL, RR).
    static AVLNode insert(AVLNode node, int key) {
        if (node == null) {
            return new AVLNode(key);
        }
        if (key < node.key) {
            node.left = insert(node.left, key); 
        }else if (key > node.key) {
            node.right = insert(node.right, key); 
        }else {
            return node;
        }

        updateHeight(node);
        int bf = balance(node);

        // LL case
        if (bf > 1 && key < node.left.key) {
            return rotateRight(node);
        }
        // RR case
        if (bf < -1 && key > node.right.key) {
            return rotateLeft(node);
        }
        // LR case
        if (bf > 1 && key > node.left.key) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // RL case
        if (bf < -1 && key < node.right.key) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    static void inorder(AVLNode n) {
        if (n == null) {
            return;
        }
        inorder(n.left);
        System.out.print(n.key + "(bf=" + balance(n) + ") ");
        inorder(n.right);
    }

    public static void main(String[] args) {
        int[] keys = {20, 30, 35, 40, 45, 50, 60, 65, 70, 75, 80, 85, 90};
        AVLNode root = null;
        for (int k : keys) {
            root = insert(root, k);
        }

        System.out.print("In-order: ");
        inorder(root);
        System.out.println();
        System.out.println("Height: " + (root.height - 1) + " edges");
    }
}

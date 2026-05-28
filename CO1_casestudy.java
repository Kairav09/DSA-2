
public class CO1_casestudy {

    static class BSTNode {

        int key;
        BSTNode left, right;

        BSTNode(int key) {
            this.key = key;
        }
    }

    static BSTNode bstInsert(BSTNode root, int key) {
        if (root == null) {
            return new BSTNode(key);
        }
        if (key < root.key) {
            root.left = bstInsert(root.left, key);
        } else {
            root.right = bstInsert(root.right, key);
        }
        return root;
    }

    static int bstHeight(BSTNode root) {
        if (root == null) {
            return -1;
        }
        return 1 + Math.max(bstHeight(root.left), bstHeight(root.right));
    }

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

    // TODO 1: right rotation
    static AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left, T2 = x.right;
        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);
        System.out.println("  [LL] rotateRight pivot=" + y.key + " -> new root: " + x.key);
        return x;
    }

    // TODO 2: left rotation
    static AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right, T2 = y.left;
        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        System.out.println("  [RR] rotateLeft  pivot=" + x.key + " -> new root: " + y.key);
        return y;
    }

    // TODO 3: insert + rebalance
    static AVLNode insert(AVLNode node, int key) {
        if (node == null) {
            return new AVLNode(key);
        }
        if (key < node.key) {
            node.left = insert(node.left, key);
        } else if (key > node.key) {
            node.right = insert(node.right, key);
        } else {
            return node;
        }
        updateHeight(node);
        int bf = balance(node);
        if (bf > 1 && key < node.left.key) {
            return rotateRight(node);
        }
        if (bf < -1 && key > node.right.key) {
            return rotateLeft(node);
        }
        if (bf > 1 && key > node.left.key) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (bf < -1 && key < node.right.key) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    static void avlInorder(AVLNode n) {
        if (n == null) {
            return;
        }
        avlInorder(n.left);
        System.out.print(n.key + " ");
        avlInorder(n.right);
    }

    public static void main(String[] args) {
        int[] keys = {20, 30, 35, 40, 45, 50, 60, 65, 70, 75, 80, 85, 90};

        // BST
        BSTNode bst = null;
        for (int k : keys) {
            bst = bstInsert(bst, k);
        }
        System.out.println("BST constructed from insertion sequence, height = " + bstHeight(bst));
        System.out.println();

        // AVL insertions with rotations
        System.out.println("AVL tree insertions with rotation messages:");
        AVLNode avl = null;
        for (int k : keys) {
            System.out.println("Insert " + k + ":");
            avl = insert(avl, k);
        }
        System.out.println();

        // Final in-order
        System.out.print("Final AVL tree in-order traversal: ");
        avlInorder(avl);
        System.out.println();
    }
}

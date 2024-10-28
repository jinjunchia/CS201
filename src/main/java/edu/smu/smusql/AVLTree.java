package edu.smu.smusql;

import java.util.ArrayList;
import java.util.List;

public class AVLTree<E> {

    // Inner class representing a node in the AVL Tree
    class AVLNode {
        private int key;
        private int height;
        private AVLNode left;
        private AVLNode right;
        private E element;

        AVLNode(int key, E element) {
            this.element = element;
            this.key = key;
            this.height = 1;
        }
    }

    private AVLNode root;

    /* ================= Utility Methods ================= */

    private int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    private int balanceFactor(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(AVLNode node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /* ================= Rotation Methods ================= */

    private AVLNode rightRotate(AVLNode y) {
        if (y == null || y.left == null) return y; // Additional null check

        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        if (x == null || x.right == null) return x; // Additional null check

        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    /* ================= Insert Operations ================= */

    public void insert(E element, int key) {
        root = insertRec(root, key, element);
    }

    private AVLNode insertRec(AVLNode node, int key, E element) {
        if (node == null) {
            return new AVLNode(key, element);
        }

        if (key < node.key) {
            node.left = insertRec(node.left, key, element);
        } else if (key > node.key) {
            node.right = insertRec(node.right, key, element);
        } else {
            return node;
        }

        updateHeight(node);
        return balanceNode(node, key);
    }

    /* ================= Delete Operations ================= */

    public void delete(int key) {
        root = deleteRec(root, key);
    }

    private AVLNode deleteRec(AVLNode node, int key) {
        if (node == null) {
            return node;
        }

        if (key < node.key) {
            node.left = deleteRec(node.left, key);
        } else if (key > node.key) {
            node.right = deleteRec(node.right, key);
        } else {
            if (node.left == null) return node.right;
            else if (node.right == null) return node.left;

            AVLNode temp = minValueNode(node.right);
            node.key = temp.key;
            node.element = temp.element;
            node.right = deleteRec(node.right, temp.key);
        }

        updateHeight(node);
        return balanceNode(node, key);
    }

    /* ================= Search Operations ================= */

    public E search(int key) {
        AVLNode node = searchRecursive(root, key);
        return node != null ? node.element : null;
    }

    private AVLNode searchRecursive(AVLNode node, int key) {
        if (node == null || node.key == key) {
            return node;
        }
        return (key < node.key) ? searchRecursive(node.left, key) : searchRecursive(node.right, key);
    }

    /* ================= Traversal Operations ================= */

    public List<E> inorderTraversal() {
        List<E> elements = new ArrayList<>();
        inorderTraversalRec(root, elements);
        return elements;
    }

    private void inorderTraversalRec(AVLNode node, List<E> elements) {
        if (node != null) {
            inorderTraversalRec(node.left, elements);
            elements.add(node.element);
            inorderTraversalRec(node.right, elements);
        }
    }

    /* ================= Balancing Method ================= */

    private AVLNode balanceNode(AVLNode node, int key) {
        int balance = balanceFactor(node);

        // Left-Left case
        if (balance > 1 && node.left != null && key < node.left.key) {
            return rightRotate(node);
        }

        // Right-Right case
        if (balance < -1 && node.right != null && key > node.right.key) {
            return leftRotate(node);
        }

        // Left-Right case
        if (balance > 1 && node.left != null && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right-Left case
        if (balance < -1 && node.right != null && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }
}
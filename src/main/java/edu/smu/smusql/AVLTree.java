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

    // Get the height of the node
    private int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    // Calculate the balance factor of the node
    private int balanceFactor(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // Update the height of the node based on its children
    private void updateHeight(AVLNode node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    // Find the node with the smallest key in a subtree
    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /* ================= Rotation Methods ================= */

    // Perform a right rotation to balance the tree
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Perform a left rotation to balance the tree
    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    /* ================= Insert Operations ================= */

    // Insert a key and associated element into the AVL tree
    public void insert(E element, int key) {
        root = insertRec(root, key, element);
    }

    // Recursive function to insert a key in the subtree rooted at node
    private AVLNode insertRec(AVLNode node, int key, E element) {
        if (node == null) {
            return new AVLNode(key, element);
        }

        if (key < node.key) {
            node.left = insertRec(node.left, key, element);
        } else if (key > node.key) {
            node.right = insertRec(node.right, key, element);
        } else {
            // Duplicate keys are not allowed
            return node;
        }

        // Update the height of this ancestor node
        updateHeight(node);
        return balanceNode(node, key);
    }

    /* ================= Delete Operations ================= */

    // Delete a key from the AVL tree
    public void delete(int key) {
        root = deleteRec(root, key);
    }

    // Recursive function to delete a key in the subtree rooted at node
    private AVLNode deleteRec(AVLNode node, int key) {
        if (node == null) {
            return node;
        }

        if (key < node.key) {
            node.left = deleteRec(node.left, key);
        } else if (key > node.key) {
            node.right = deleteRec(node.right, key);
        } else {
            // Node with only one child or no child
            if (node.left == null) return node.right;
            else if (node.right == null) return node.left;

            // Node with two children
            AVLNode temp = minValueNode(node.right);
            node.key = temp.key;
            node.element = temp.element;
            node.right = deleteRec(node.right, temp.key);
        }

        // Update the height of the current node
        updateHeight(node);
        return balanceNode(node, key);
    }

    /* ================= Search Operations ================= */

    // Search for an element by key
    public E search(int key) {
        AVLNode node = searchRecursive(root, key);
        return node != null ? node.element : null;
    }

    // Recursive function to search for a key in the subtree rooted at node
    private AVLNode searchRecursive(AVLNode node, int key) {
        if (node == null || node.key == key) {
            return node;
        }
        return (key < node.key) ? searchRecursive(node.left, key) : searchRecursive(node.right, key);
    }

    /* ================= Traversal Operations ================= */

    // Public method to start in-order traversal
    public List<E> inorderTraversal() {
        List<E> elements = new ArrayList<>();
        inorderTraversalRec(root, elements);
        return elements;
    }

    // Helper method to perform in-order traversal
    private void inorderTraversalRec(AVLNode node, List<E> elements) {
        if (node != null) {
            inorderTraversalRec(node.left, elements); // Traverse left subtree
            elements.add(node.element); // Visit node
            inorderTraversalRec(node.right, elements); // Traverse right subtree
        }
    }

    /* ================= Balancing Method ================= */

    // Balance the node after insertion or deletion
    private AVLNode balanceNode(AVLNode node, int key) {
        int balance = balanceFactor(node);

        // Left-Left case
        if (balance > 1 && key < node.left.key) {
            return rightRotate(node);
        }

        // Right-Right case
        if (balance < -1 && key > node.right.key) {
            return leftRotate(node);
        }

        // Left-Right case
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right-Left case
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node; // Node is balanced
    }
}
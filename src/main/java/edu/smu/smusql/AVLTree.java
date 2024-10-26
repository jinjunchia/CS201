package edu.smu.smusql;

import java.util.ArrayList;
import java.util.List;

public class AVLTree<E> {

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

    // Get height of the node
    private int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    // Get balance factor of the node
    private int balanceFactor(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // Update the height of the node
    private void updateHeight(AVLNode node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    // Right rotation
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Left rotation
    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // Insert a key into the AVL tree
    public void insert(E element, int key) {
        root = insertRec(root, key);
    }

    private AVLNode insertRec(AVLNode node, int key) {
        if (node == null) {
            return new AVLNode(key, null);
        }

        if (key < node.key) {
            node.left = insertRec(node.left, key);
        } else if (key > node.key) {
            node.right = insertRec(node.right, key);
        } else {
            // Duplicate keys not allowed
            return node;
        }

        updateHeight(node);
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

        return node;
    }

    // Delete a key from the AVL tree
    public void delete(int key) {
        root = deleteRec(root, key);
    }

    private AVLNode deleteRec(AVLNode node, int key) {
        if (node == null) {
            return node;
        }

        // Perform standard BST deletion
        if (key < node.key) {
            node.left = deleteRec(node.left, key);
        } else if (key > node.key) {
            node.right = deleteRec(node.right, key);
        } else {
            // Node with only one child or no child
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // Node with two children
            AVLNode temp = minValueNode(node.right);
            node.key = temp.key;
            node.element = temp.element;
            node.right = deleteRec(node.right, temp.key);
        }

        // Update height of the current node
        updateHeight(node);

        // Balance the node
        int balance = balanceFactor(node);

        // Left Left Case
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rightRotate(node);
        }

        // Left Right Case
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return leftRotate(node);
        }

        // Right Left Case
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Search function to find the element with the given key
    public E search(int key) {
        AVLNode node = searchRecursive(root, key);
        return node != null ? node.element : null;
    }

    // Recursive search function to traverse the tree
    private AVLNode searchRecursive(AVLNode node, int key) {
        // Base case: node is null or key is found
        if (node == null || node.key == key) {
            return node;
        }

        // Traverse the left subtree if key is smaller than node's key
        if (key < node.key) {
            return searchRecursive(node.left, key);
        }

        // Traverse the right subtree if key is greater than node's key
        return searchRecursive(node.right, key);
    }

    // Find the node with the smallest key
    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

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
}
package edu.smu.smusql;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
        if (node != null) {
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
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
        
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

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
            return node; // Duplicate keys are not allowed
        }

        updateHeight(node);
        return balanceNode(node);
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
        return balanceNode(node);
    }

    /* ================= Balancing Method ================= */

    private AVLNode balanceNode(AVLNode node) {
        int balance = balanceFactor(node);

        // Left-heavy
        if (balance > 1) {
            if (balanceFactor(node.left) >= 0) {
                
                return rightRotate(node); // Left-Left case
            } else {
                
                node.left = leftRotate(node.left); // Left-Right case
                return rightRotate(node);
            }
        }

        // Right-heavy
        if (balance < -1) {
            if (balanceFactor(node.right) <= 0) {
                
                return leftRotate(node); // Right-Right case
            } else {
                
                node.right = rightRotate(node.right); // Right-Left case
                return leftRotate(node);
            }
        }

        return node; // Node is balanced
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

    // Level-order traversal to show the tree structure
    public void levelOrderTraversal() {
        if (root == null) return;

        Queue<AVLNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            while (levelSize > 0) {
                AVLNode currentNode = queue.poll();
                System.out.print("[" + currentNode.key + "] ");
                if (currentNode.left != null) queue.add(currentNode.left);
                if (currentNode.right != null) queue.add(currentNode.right);
                levelSize--;
            }
            System.out.println();
        }
    }
}
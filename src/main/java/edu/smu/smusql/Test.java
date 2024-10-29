package edu.smu.smusql;

import java.util.List;

public class Test {
 public static void main(String[] args) {
        AVLTree<String> avlTree = new AVLTree<>();

        System.out.println("Inserting elements into AVL Tree...");

        // Insert elements to set up the AVL tree
        System.out.println("Inserting elements to create a balanced AVL Tree...");
        avlTree.insert("Node10", 10);
        avlTree.insert("Node20", 20);
        avlTree.insert("Node30", 30);  // Causes Right-Right rotation
        avlTree.insert("Node5", 5);
        avlTree.insert("Node15", 15);
        avlTree.insert("Node25", 25);
        avlTree.insert("Node35", 35);
        avlTree.insert("Node40", 40);  // Further balancing

        //----------------------------------------Insert Test-----------------------------------------------/
        // simple check by commenting off all inserts after Node30 to see the right-right rotation
        System.out.println("In-order Traversal of AVL Tree (should be sorted):");
        List<String> inorder = avlTree.inorderTraversal();
        for (String value : inorder) {
            System.out.print(value + " ");
        }
        System.out.println("\n");

        System.out.println("Level-order Traversal of AVL Tree (shows structure and balance):");
        avlTree.levelOrderTraversal();

        //--------------------------------------------------------------------------------------------------/




        //----------------------------------------Delete Test-----------------------------------------------/
        // System.out.println("Initial Level-order Traversal (Balanced AVL Tree):");
        // avlTree.levelOrderTraversal();
        // System.out.println();

        // // Delete a node to cause rebalancing
        // int deleteKey = 20;  // Deleting 20 will require the tree to rebalance
        // avlTree.delete(deleteKey);

        // System.out.println("\nLevel-order Traversal after deleting " + deleteKey + " (should trigger rebalancing):");
        // avlTree.levelOrderTraversal();
        // System.out.println("In-order Traversal (should remain sorted):");
        // System.out.println(avlTree.inorderTraversal());
    //------------------------------------------------------------------------------------------------------/

    }   
}

package edu.smu.smusql;
import java.util.Map;

public class AVLTree<E> {
    //This AVL Tree represent the structure of the Table

    private static Node root = null;

    public AVLTree(){

    }

    public Node update(String searchString, Map<String, String> newRow){
        Node nodeToUpdate = search(root, searchString);
        nodeToUpdate.setRow(newRow);
        return nodeToUpdate;
    }

    public static Node search(Node node, String searchString){
        if (node == null){
            return null; //note not found
        }
        if (getFirstColumn(node).equals(searchString)){
            return node; //null means that node already exists
        }

        if (getFirstColumn(node).compareTo(searchString) < 0){ //if inputString is larger than current node
            return search(node.getRightNode(), searchString);
        } else { //if inputString is smaller than current node
            return search(node.getLeftNode(), searchString);
        }
    }

    public static Node getNodeToInsert(Node node, String searchString){
        if (getFirstColumn(node).equals(searchString)){
            return null; //null means that node already exists
        }

        if (getFirstColumn(node).compareTo(searchString) < 0){ //if inputString is larger than current node
            if (node.getRightNode() != null){
                return search(node.getRightNode(), searchString);
            } else {
                return node;
            }
        } else { //if inputString is smaller than current node
            if (node.getLeftNode() != null){
                return search(node.getLeftNode(), searchString);
            } else {
                return node;
            }
        }
    }

    public static Node getBiggestLeftNode(Node node){
        if (node.getRightNode() == null){
            return node;
        } else{
            return getBiggestLeftNode(node.getRightNode());
        }
    }

    public static void delete(String searchString){
        Node nodeToDelete = search(root, searchString);

        if (nodeToDelete == null){
            return;
        }

        Node parentNode = nodeToDelete.getParentNode();
        Node nodeToBalance = null;
        
        if (nodeToDelete.getRightNode() != null && nodeToDelete.getLeftNode() != null){ //2 children
            Node largestLeftNode = getBiggestLeftNode(nodeToDelete.getLeftNode());
            Map<String,String> newRow = largestLeftNode.getRow();
            delete(getFirstColumn(largestLeftNode));
            nodeToDelete.setRow(newRow);
        } else if(nodeToDelete.getRightNode() == null){ //node has 1 left child
            nodeToBalance = nodeToDelete.getLeftNode();
        } else if(nodeToDelete.getLeftNode() == null){ // node has 1 right child
            nodeToBalance = nodeToDelete.getRightNode();
        }

        if (parentNode.getLeftNode() == nodeToDelete){
            parentNode.setLeftNode(nodeToBalance);
        } else {
            parentNode.setRightNode(nodeToBalance);
        }

        //balance below
        if (nodeToBalance == null){
            nodeToBalance = parentNode;
        }
        balanceTheTree(nodeToBalance);

        return;
    }

    public void insert(Node node){
        if (root == null){
            root = node;
            return;
        }
        
        String firstColumnString = getFirstColumn(node);
        Node parentNode = getNodeToInsert(root, firstColumnString);
        
        if (getFirstColumn(parentNode) == firstColumnString)return; //can't insert with the same first column value

        //inserting the node
        if (getFirstColumn(parentNode).compareTo(firstColumnString) < 0){
            parentNode.setRightNode(node);
        } else {
            parentNode.setLeftNode(node);
        }
        node.setParentNode(parentNode);

        //balancing the tree
        balanceTheTree(node);
    }

    public static void balanceTheTree(Node node){
        while(node.getParentNode() != null){ //checks until reach root node because rootnode parent is null
            node = node.getParentNode();
            if (getBalance(node) > 1){ //left heavy
                if (getHeight(node.getLeftNode()) < getHeight(node.getRightNode())){ //LR
                    rotateLeft(node.getLeftNode());
                    rotateRight(node);
                } else { //LL
                    rotateRight(node);
                }
            } else if (getBalance(node) < -1){ //right heavy
                if (getHeight(node.getLeftNode()) > getHeight(node.getRightNode())){ //RL
                    rotateRight(node.getRightNode());
                    rotateLeft(node);
                } else { //RR
                    rotateLeft(node);
                }
            }
        }
    }

    public static void rotateRight(Node node){ //arg is the imbalanced node
        Node temp = node;
        node = node.getLeftNode();
        if (node.getRightNode() != null){
            temp.setLeftNode(node.getRightNode());
        }
        node.setRightNode(temp);
    }

    public static void rotateLeft(Node node){ //arg is the imbalanced node
        Node temp = node;
        node = node.getRightNode();
        if (node.getLeftNode() != null){
            temp.setRightNode(node.getLeftNode());
        }
        node.setLeftNode(temp);
    }

    public static String getFirstColumn(Node node){
        Map<String, String> rowMap = node.getRow();
        return (String)rowMap.keySet().toArray()[0];
    }

    public static int getBalance(Node node){
        int leftHeight = getHeight(node.getLeftNode());
        int rightHeight = getHeight(node.getRightNode());
        return leftHeight - rightHeight;
    }

    public static int getHeight(Node node){
        if (node.isleafNode()){
            return 0;
        }
        return 1 + Math.max(getHeight(node.getLeftNode()), getHeight(node.getRightNode()));
    }

}

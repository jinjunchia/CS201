package edu.smu.smusql;
import java.util.Map;

public class Node{
    //one Node contains one HashMap
    //one HashMap represents one row
    //therefore one Node is one Row
    private Node parentNode = null;
    private Node leftNode = null;
    private Node rightNode = null;

    private Map<String,String> row = null;

    public void setRow(Map<String, String> row) {
        this.row = row;
    }

    public Node(Map<String, String> row){
        this.row = row;
    }

    public void setLeftNode(Node leftNode){
        this.leftNode = leftNode;
    }

    public void setRightNode(Node rightnode){
        this.rightNode = rightnode;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public boolean isleafNode(){
        if (this.getLeftNode() == null && this.getRightNode() == null){
            return true;
        }
        return false;
    }

    public Map<String, String> getRow() {
        return row;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

}

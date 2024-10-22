package edu.smu.smusql;

import java.util.HashMap;
import java.util.Map;
import edu.smu.smusql.AVLTree;

public class Table {
    
    private String name;
    //key is the columnName
    //value is the columnValue
    private AVLTree<Map<String,String>> table = new AVLTree<>();
    
    public String getName() {
        return name;
    }
    
    public Map<String, String> getTable() {
        return table;
    }

    
}

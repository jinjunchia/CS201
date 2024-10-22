package edu.smu.smusql;

import java.util.HashMap;
import java.util.Map;

public class Table {
    
    private String name;
    private Map<String,String> table = new HashMap<>();
    
    public String getName() {
        return name;
    }
    public Map<String, String> getTable() {
        return table;
    }

    
}

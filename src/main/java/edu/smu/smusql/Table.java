package edu.smu.smusql;

import java.util.List;
import java.util.Map;

public class Table {
    private String name;
    private List<String> columns;
    AVLTree<Map<String, String>> dataList = new AVLTree<>();

    public Table(String name, List<String> columns) {
        this.dataList = new AVLTree<>();
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public AVLTree<Map<String, String>> getDataList() {
        return dataList;
    }

    public void setDataList(AVLTree<Map<String, String>> dataList) {
        this.dataList = dataList;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void addRow(Map<String, String> new_row){
        int id = Integer.parseInt(new_row.get("id"));
        dataList.insert(new_row, id);
    }


    

}

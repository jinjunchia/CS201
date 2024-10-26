package edu.smu.smusql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Engine {
    public AVLTree<Table> dataList = new AVLTree<>();
    public String executeSQL(String query) {
        
        String[] tokens = query.trim().split("\\s+");
        String command = tokens[0].toUpperCase();

        switch (command) {
            case "CREATE":
                return create(tokens);
            case "INSERT":
                return insert(tokens);
            case "SELECT":
                return select(tokens);
            case "UPDATE":
                return update(tokens);
            case "DELETE":
                return delete(tokens);
            default:
                return "ERROR: Unknown command";
        }
    }

    public String insert(String[] tokens) {
       if (!tokens[1].equalsIgnoreCase("INTO")) {
            return "ERROR: Invalid INSERT INTO syntax";
        }

        String tableName = tokens[2];

        // Search for the table in the BST
        Table table = dataList.search(tableName.hashCode());
        if (table == null) {
            return "ERROR: Table not found";
        }

        // Extract column names and values
        //INSERT INTO table_name (column1, column2, column3, ...) VALUES (value1, value2, value3, ...);
        // String columnList = queryBetweenParentheses(tokens, 4);
        // List<String> columns = Arrays.asList(columnList.split(","));
        // columns.replaceAll(String::trim);

        String valueList = queryBetweenParentheses(tokens, 4);
        List<String> values = Arrays.asList(valueList.split(","));
        values.replaceAll(String::trim);
        List<String> columns = table.getColumns();
       
        if (columns.size() != values.size()) {
            return "ERROR: Column count does not match value count";
        }

        // Create a new row as a map
        Map<String, String> newRow = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            newRow.put(columns.get(i), values.get(i));
        }
        int id = Integer.parseInt(newRow.get("id"));
        if (table.getDataList().search(id) != null) {
            return "ERROR: Duplicate id";
        }
        // Insert the new row into the table
        table.addRow(newRow);

        return "Row inserted successfully into " + tableName;
    }

    public String delete(String[] tokens) {
        if (!tokens[1].equalsIgnoreCase("FROM")) {
            return "ERROR: Invalid DELETE FROM syntax";
        }

        String tableName = tokens[2];

        // Search for the table in the BST
        Table table = dataList.search(tableName.hashCode());
        if (table == null) {
            return "ERROR: Table not found";
        }

        // Check for WHERE clause
        String whereClause = null;
        for (int i = 3; i < tokens.length; i++) {
            if (tokens[i].equalsIgnoreCase("WHERE")) {
                whereClause = String.join(" ", Arrays.copyOfRange(tokens, i + 1, tokens.length));
                break;
            }
        }

        if (whereClause == null) {
            return "ERROR: Missing WHERE clause";
        }

        // Retrieve and delete the rows that match the conditions
        List<Map<String, String>> rows = table.getDataList().inorderTraversal();
        List<Map<String, String>> rowsToDelete = filterRows(rows, whereClause);

        for (Map<String, String> row : rowsToDelete) {
            int id = Integer.parseInt(row.get("id"));
            table.getDataList().delete(id);
        }

        return "Rows deleted successfully from " + tableName;
    }

    public String select(String[] tokens) {
        if (!tokens[1].equals("*") || !tokens[2].toUpperCase().equals("FROM")) {
            return "ERROR: Invalid SELECT syntax";
        }

        String tableName = tokens[3];
        
        // Search for the table in the BST
        Table table = dataList.search(tableName.hashCode());
        
        if (table == null) {
            return "ERROR: Table not found";
        }

        String whereClause = null;
        for (int i = 4; i < tokens.length; i++) {
            if (tokens[i].equalsIgnoreCase("WHERE")) {
                whereClause = String.join(" ", Arrays.copyOfRange(tokens, i + 1, tokens.length));
                break;
            }
        }

        // Retrieve and format the data from the table
        StringBuilder result = new StringBuilder();
        result.append("Table: ").append(tableName).append("\n");
        result.append(String.join(", ", table.getColumns())).append("\n");

        List<Map<String, String>> rows = table.getDataList().inorderTraversal();
        if (whereClause != null) {
            rows = filterRows(rows, whereClause);
        }
        // Assuming the table has a method to get all rows as a list of maps
        for (Map<String, String> row : rows) {
            for (String column : table.getColumns()) {
                result.append(row.get(column)).append(", ");
            }
            result.setLength(result.length() - 2); // Remove trailing comma and space
            result.append("\n");
        }

        return result.toString();
    }

    public String update(String[] tokens) {
        if (!tokens[2].equalsIgnoreCase("SET")) {
            return "ERROR: Invalid UPDATE syntax";
        }

        String tableName = tokens[1];

        // Search for the table in the BST
        Table table = dataList.search(tableName.hashCode());
        if (table == null) {
            return "ERROR: Table not found";
        }
        int whereIndex = -1;
        for (int i = 2; i < tokens.length; i++) {
            if (tokens[i].equalsIgnoreCase("WHERE")) {
                whereIndex = i;
                break;
            }
        }
        if (whereIndex == -1) {
            return "ERROR: Missing WHERE clause";
        }

        // Extract column names and values to update
        String setClause = String.join(" ", Arrays.copyOfRange(tokens, 2, whereIndex));
        String[] setPairs = setClause.split(",");
        Map<String, String> updates = new HashMap<>();
        for (String pair : setPairs) {
            String[] keyValue = pair.split("=");
            updates.put(keyValue[0].trim(), keyValue[1].trim());
        }

        // Check for WHERE clause
        String whereClause = String.join(" ", Arrays.copyOfRange(tokens, whereIndex + 1, tokens.length));
        // Retrieve and update the rows that match the conditions
        List<Map<String, String>> rows = table.getDataList().inorderTraversal();
        List<Map<String, String>> rowsToUpdate = filterRows(rows, whereClause);

        for (Map<String, String> row : rowsToUpdate) {
            for (Map.Entry<String, String> entry : updates.entrySet()) {
                row.put(entry.getKey(), entry.getValue());
            }
            int id = Integer.parseInt(row.get("id"));
            table.getDataList().delete(id);
            table.addRow(row);
        }

        return "Rows updated successfully in " + tableName;
    }
    public String create(String[] tokens) {
       
        if (!tokens[1].equalsIgnoreCase("TABLE")) {
            return "ERROR: Invalid CREATE TABLE syntax";
        }

        String tableName = tokens[2];

        if (dataList.search(tableName.hashCode()) != null) {
            return "ERROR: Table already exists";
        }

        String columnList = queryBetweenParentheses(tokens, 3);
        List<String> columns = Arrays.asList(columnList.split(","));
        columns.replaceAll(String::trim);
        Table newTable = new Table(tableName, columns);
        dataList.insert(newTable, tableName.hashCode());
        
        return "Table " + tableName + " created successfully";
    }
    private List<Map<String, String>> filterRows(List<Map<String, String>> rows, String whereClause) {
        Predicate<Map<String, String>> predicate = createPredicate(whereClause);
        return rows.stream().filter(predicate).collect(Collectors.toList());
    }
    private Predicate<Map<String, String>> createPredicate(String whereClause) {
        String[] conditions = whereClause.split(" AND ");
        return row -> {
            for (String condition : conditions) {
                String[] parts = condition.split(" ");
                String attribute = parts[0];
                String operator = parts[1];
                String value = parts[2];

                String rowValue = row.get(attribute);
                if (rowValue == null) {
                    return false;
                }

                switch (operator) {
                    case "=":
                        if (!rowValue.equals(value)) {
                            return false;
                        }
                        break;
                    case ">":
                        if (!(Double.parseDouble(rowValue) > Double.parseDouble(value))) {
                            return false;
                        }
                        break;
                    case "<":
                        if (!(Double.parseDouble(rowValue) < Double.parseDouble(value))) {
                            return false;
                        }
                        break;
                    // Add more operators as needed
                    default:
                        return false;
                }
            }
            return true;
        };
    }
    private String queryBetweenParentheses(String[] tokens, int startIndex) {
        StringBuilder sb = new StringBuilder();
        boolean openParenthesisFound = false;
        for (int i = startIndex; i < tokens.length; i++) {
            if (tokens[i].contains("(")) {
                openParenthesisFound = true;
                sb.append(tokens[i].substring(tokens[i].indexOf("(") + 1));
            } else if (tokens[i].contains(")")) {
                sb.append(" ").append(tokens[i], 0, tokens[i].indexOf(")"));
                break;
            } else if (openParenthesisFound) {
                sb.append(" ").append(tokens[i]);
            }
        }
        return sb.toString().trim();
    }

}

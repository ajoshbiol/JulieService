package com.julieservice.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import java.util.*;

/*
 * MySQL handler
 *
 */
public class MySQL
{
    private static boolean isInitialized = false; 
    private static String dbUrl = null;
    private static String dbUsername = null;
    private static String dbPassword = null;
    private static Connection connection = null;
    
    /*
     *  Start weight related functions
     */

    // Function to retrieve weight data within the start and 
    // end date parameters
    public static ArrayList<Weight> getWeights(String startDate, 
        String endDate) {
        
        ensureInit();

        try {
        
            if (!connection.isValid(0)) {
                connect();
            }
       
            String query = "SELECT * FROM weights ";

            if (startDate != null) {
                query = query + "WHERE datetime >= ?";
            }

            if (endDate != null) {
                query = query + " and datetime <= ?";
            }

            query += " ORDER BY datetime DESC;"; 

            try (PreparedStatement pStmt = 
                    connection.prepareStatement(query)) {

                if (startDate != null) {
                    pStmt.setString(1, startDate);
                }
                if (endDate != null) {
                    pStmt.setString(2, endDate);
                }
                
                try (ResultSet rs = pStmt.executeQuery()) {
                
                    ArrayList<Weight> weights = new ArrayList<Weight>();
                    while (rs.next()) {
                        Weight weight = new Weight();
                        weight.setWeight(rs.getDouble("weightInLbs"));
                        weight.setDate(rs.getDate("datetime"));
                        weight.setId(rs.getInt("id"));

                        weights.add(weight);
                    }
                    
                    return weights;
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex); 
        }  

        return null;
    }

    // Function to add new weight data
    public static Weight addWeight(String date, String weight) {
    
        ensureInit();

        try {
        
            if (!connection.isValid(0)) {
                connect();
            }

            double d = Double.parseDouble(weight);
            try(CallableStatement cStmt = 
                connection.prepareCall("{call `addWeight`(?, ?)};")) {

                cStmt.setString("_date", date);
                cStmt.setDouble("_weight", d);

                boolean success = cStmt.execute();

                if (!success) {
                    return null;
                }

                try (ResultSet rs = cStmt.getResultSet()) {
                
                    rs.next();
                    Weight w = new Weight();
                    w.setWeight(d);
                    w.setDate(Utils.convertStringToDate(date));
                    w.setId(rs.getInt("id"));

                    return w;
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex); 
        }

        return null;
    }

    // Function to delete weight data
    public static boolean deleteWeight(String id) {
        
        ensureInit();

        try {
        
            if (!connection.isValid(0)) {
                connect();
            }

            int i = Integer.parseInt(id);

            String query = "DELETE FROM weights WHERE id = ?;";

            try (PreparedStatement pStmt = 
                    connection.prepareStatement(query)) {
            
                pStmt.setInt(1, i);
                pStmt.executeUpdate();

                return true;
            }
        }
        catch (Exception ex) {
            System.out.println(ex); 
        }

        return false;
    }

    // Function to update existing weight data
    public static boolean updateWeight(String id, String date, String weight) {
    
        ensureInit();

        try {
        
            if (!connection.isValid(0)) {
                connect();
            }

            int i = Integer.parseInt(id);
            double d = Double.parseDouble(weight);

            try(CallableStatement cStmt = 
                connection.prepareCall("{call `updateWeight`(?, ?, ?)};")) {

                cStmt.setInt(1, i);
                cStmt.setString(2, date);
                cStmt.setDouble(3, d);

                cStmt.execute();
                return true;
            }
        }
        catch (SQLException sqlEx) {
            System.out.println(sqlEx);
        }

        return false;
    }

    /*
     *  Start todo list related functions
     */

    public static int getIncompleteTodoCount() {
    
        ensureInit();

        try {
            
            if (!connection.isValid(0)) {
                connect();
            }

            String query = 
                "SELECT COUNT(*) FROM todos WHERE completionDate is null;";

            try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            
                rs.next();
                return rs.getInt("COUNT(*)");
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

        return -1;
    }

    public static ArrayList<Todo> getTodos() {
        return getTodos(0); 
    }

    public static ArrayList<Todo> getTodos(int id) {
        
        ensureInit();

        try {
             
            if (!connection.isValid(0)) {
                connect();
            }

            String query = "SELECT * FROM todos WHERE id > ? LIMIT 20;";

            try (PreparedStatement pStmt = 
                connection.prepareStatement(query)) {
            
                pStmt.setInt(1, id);    
                
                try (ResultSet rs = pStmt.executeQuery()) {
                
                    ArrayList<Todo> todos = new ArrayList<Todo>();
                    while (rs.next()) {
                    
                        Todo todo = new Todo();
                        todo.setId(rs.getInt("id"));
                        todo.setTask(rs.getString("task"));
                        todo.setCreationDate(
                            rs.getTimestamp("creationDate").getTime());
                        Timestamp ts = rs.getTimestamp("completionDate");
                        if (ts != null) {
                            todo.setCompletionDate(ts.getTime());
                        }
                        todos.add(todo);
                    }

                    return todos;
                } 
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        } 

        return null;
    }

    // Function to add a new task; Returns a new Todo object
    public static Todo addTodo(String task) {
    
        ensureInit();

        try {
        
            if (!connection.isValid(0)) {
                connect();
            }

            try(CallableStatement cStmt = 
                connection.prepareCall("{call `addTodo`(?)};")) {

                cStmt.setString("_task", task);

                boolean success = cStmt.execute();

                if (!success) {
                    return null;
                }

                try (ResultSet rs = cStmt.getResultSet()) {
                
                    rs.next();
                    Todo t = new Todo();
                    t.setId(rs.getInt("id"));
                    t.setTask(task);
                    t.setCreationDate(
                        rs.getTimestamp("creationDate").getTime());

                    return t;
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex); 
        }

        return null;
    }

    // Function to delete a todo item
    public static boolean deleteTodo(String id) {
    
        ensureInit();

        try {
        
            if (!connection.isValid(0)) {
                connect();
            }

            int i = Integer.parseInt(id);

            String query = "DELETE FROM todos WHERE id = ?;";

            try (PreparedStatement pStmt = 
                    connection.prepareStatement(query)) {
            
                pStmt.setInt(1, i);
                pStmt.executeUpdate();

                return true;
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

        return false;
    }


    // Function to update existing todo item
    public static boolean updateTodo(String id, String task, 
        String creationDate, String completionDate) {
    
        ensureInit();

        try {
        
            if (!connection.isValid(0)) {
                connect();
            }

            int i = Integer.parseInt(id);

            try(CallableStatement cStmt = 
                connection.prepareCall("{call `updateTodo`(?, ?, ?, ?)};")) {

                cStmt.setInt(1, i);
                cStmt.setString(2, task);
                cStmt.setString(3, creationDate);
                cStmt.setString(4, completionDate);

                cStmt.execute();
                return true;
            }
        }
        catch (SQLException sqlEx) {
            System.out.println(sqlEx);
        }

        return false;
    }

    // Function to connect to our database
    private static void connect() {
    
        try {

            connection = DriverManager.getConnection(dbUrl, 
                dbUsername, 
                dbPassword);

            System.out.println("Database connected!");
        } 
        catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    // Function to ensure that our member variables have been assigned their
    // configured values.
    private static void ensureInit() {
        
        if (isInitialized) {
            return;
        } 

        Properties configs = App.getConfigs();

        dbUrl = configs.getProperty("mysqlDb");
        dbUsername = configs.getProperty("mysqlUser");
        dbPassword = configs.getProperty("mysqlPassword");

        connect();

        isInitialized = true;
    }
}

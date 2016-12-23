package com.julieservice.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.PreparedStatement;

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
                connection.prepareCall("{call `addWeight`(?, ?)}")) {

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

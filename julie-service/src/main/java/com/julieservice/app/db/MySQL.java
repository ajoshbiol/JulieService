package com.julieservice.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

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
                query = query + "WHERE datetime >= '" + startDate + "'";
            }

            if (endDate != null) {
                query = query + " and datetime <= '" + endDate + "'";
            }

            query += " ORDER BY datetime DESC;"; 

            try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
                
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
        catch (Exception ex) {
            System.out.println(ex); 
        }  

        return null;
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

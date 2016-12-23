package com.julieservice.app;

import static spark.Spark.*;
import com.fasterxml.jackson.core.*; 
import com.fasterxml.jackson.databind.*; 

import java.io.*;
import java.lang.*;
import java.util.*;
import java.text.*;

/**
 * Entry point of our service
 *
 */
public class App 
{
    private static boolean isInitialized = false;
    private static Properties configs = null;
    private static ObjectWriter ow = null;

    // Our App entry point
    public static void main(String[] args) {

        ensureInit();

        get("/health/weight", (req, res) -> { 

            String startDate = req.queryParams("startDate");
            String endDate = req.queryParams("endDate"); 

            MyHealth myHealth = new MyHealth();
            SvcResponse serviceRes = myHealth.getWeights(startDate, endDate);

            res.status(serviceRes.status);

            String json = ow.writeValueAsString(serviceRes);
            return json;
        });

        // Creates a new weight entry
        post("/health/weight", (req, res) -> {
        
            String date = req.queryParams("date");
            String weight = req.queryParams("weight");

            MyHealth myHealth = new MyHealth();
            SvcResponse serviceRes = myHealth.addWeight(date, weight);

            res.status(serviceRes.status);

            String json = ow.writeValueAsString(serviceRes);
            return json;
        });
    }

    // Allows access to config values
    public static Properties getConfigs() {

        return configs;
    }

    // Function to ensure that our app and configs has been initialized
    private static void ensureInit() {

        if (isInitialized) {
            return;
        }

        configs = new Properties();

        try (InputStream input = new FileInputStream("config.properties")) {

            configs.load(input);
        }
        catch (IOException ex) {
            ex.printStackTrace(); 
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(df);
        ow = om.writer().withDefaultPrettyPrinter();

        isInitialized = true;
    }
}

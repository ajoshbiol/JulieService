package com.julieservice.app;

import static spark.Spark.*;
import com.fasterxml.jackson.core.*; 
import com.fasterxml.jackson.databind.*; 

import java.io.*;
import java.lang.*;
import java.util.*;


/**
 * Entry point of our service
 *
 */
public class App 
{
    private static boolean isInitialized = false;
    private static Properties configs = null;

    // Our App entry point
    public static void main(String[] args) {

        ensureInit();

        get("/health/weight", (req, res) -> { 

            String startDate = req.queryParams("startDate");
            String endDate = req.queryParams("endDate"); 

            // TODO Verify parameters

            MyHealth myHealth = new MyHealth();
            HealthGetWeightRes myRes = myHealth.getWeights(startDate, endDate);

            ObjectWriter ow = 
                new ObjectMapper().writer().withDefaultPrettyPrinter();

            String json = ow.writeValueAsString(myRes);
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

        isInitialized = true;
    }
}

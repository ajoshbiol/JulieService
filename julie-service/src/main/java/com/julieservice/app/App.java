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

            Object serviceRes = null;
            if (!Utils.isValidDate(startDate) ||
               (endDate != null && !Utils.isValidDate(endDate))) {

                res.status(401);
                SvcResponse failedRes = new SvcResponse();

                failedRes.status = 401;
                failedRes.message = "Invalid input.";

                serviceRes = failedRes;
            }
            else {
                MyHealth myHealth = new MyHealth();
                HealthGetWeightRes goodRes = 
                    myHealth.getWeights(startDate, endDate);

                serviceRes = goodRes; 
            }

            ObjectWriter ow = 
                new ObjectMapper().writer().withDefaultPrettyPrinter();

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

        isInitialized = true;
    }
}

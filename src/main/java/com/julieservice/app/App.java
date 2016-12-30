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

        // Makes sure that the requestor is valid
        before((req, res) -> {

            if (!configs.getProperty("serviceAuthentication").equals(
                req.headers("Authorization"))) {

                SvcResponse serviceRes = new SvcResponse();
                serviceRes.setStatus(402);
                halt(serviceRes.status, ow.writeValueAsString(serviceRes));
            }
        });

        // Gets weight entries
        get("/health/weight", (req, res) -> { 

            String startDate = req.queryParams("startDate");
            String endDate = req.queryParams("endDate"); 

            MyHealth myHealth = new MyHealth();
            SvcResponse serviceRes = myHealth.getWeights(startDate, 
                    endDate);

            res.status(serviceRes.status);

            String json = ow.writeValueAsString(serviceRes);
            return json;
        });

        // Creates a new weight entry
        post("/health/weight", (req, res) -> {
        
            String date = req.queryParams("date");
            String weight = req.queryParams("weight");

            MyHealth myHealth = new MyHealth();
            SvcResponse serviceRes = myHealth.addWeight(date, 
                    weight);

            res.status(serviceRes.status);

            String json = ow.writeValueAsString(serviceRes);
            return json;
        });

        // Deletes a weight entry
        delete("/health/weight", (req, res) -> {
            
            String id = req.queryParams("id");

            MyHealth myHealth = new MyHealth();
            SvcResponse serviceRes = myHealth.deleteWeight(id);

            res.status(serviceRes.status);

            String json = ow.writeValueAsString(serviceRes);
            return json;
        });

        // Updates a weight entry
        put("/health/weight", (req, res) -> {
            
            String id = req.queryParams("id");
            String date = req.queryParams("date");
            String weight = req.queryParams("weight");

            MyHealth myHealth = new MyHealth();
            SvcResponse serviceRes = myHealth.updateWeight(id, 
                    date, 
                    weight);

            res.status(serviceRes.status);

            String json = ow.writeValueAsString(serviceRes);
            return json;
        });

        // Processes todo related get queries
        get("/productivity/todo", (req, res) -> {
        
            String query = req.queryParams("query");

            MyProductivity myProd = new MyProductivity();
            SvcResponse serviceRes = null;

            if (query == null) {
                serviceRes = new SvcResponse();
                serviceRes.setStatus(401);
            }
            else {
                switch(query) {
                
                    case "incompleteTodoCount":
                        serviceRes = myProd.getIncompleteTodoCount();
                        res.status(serviceRes.status);
                        break;
                    case "todos":
                        String lastId = req.queryParams("lastId");
                        serviceRes = myProd.getTodos(lastId);
                        res.status(serviceRes.status);
                        break;
                    default:
                        serviceRes = new SvcResponse();
                        serviceRes.setStatus(401);
                        break; 
                }
            }            

            String json = ow.writeValueAsString(serviceRes);
            return json;
        });

        // Creates a todo item
        post("/productivity/todo", (req, res) -> {
        
            String task = req.queryParams("task");

            MyProductivity myProd = new MyProductivity();
            SvcResponse serviceRes = myProd.createTodo(task);
            res.status(serviceRes.status);

            String json = ow.writeValueAsString(serviceRes);
            return json;
        });

        // Deletes a todo item
        delete("/productivity/todo", (req, res) -> {
        
            String id = req.queryParams("id");

            MyProductivity myProd = new MyProductivity();
            SvcResponse serviceRes = myProd.deleteTodo(id);
            res.status(serviceRes.status);

            String json = ow.writeValueAsString(serviceRes);
            return json;
        });

        // Updates a todo item
        put("productivity/todo", (req, res) -> {
        
            String id = req.queryParams("id");
            String task = req.queryParams("task");
            String creationDate = req.queryParams("creationDate");
            String completionDate = req.queryParams("completionDate");

            MyProductivity myProd = new MyProductivity();
            SvcResponse serviceRes = myProd.updateTodo(id, 
                    task, 
                    creationDate, 
                    completionDate);
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

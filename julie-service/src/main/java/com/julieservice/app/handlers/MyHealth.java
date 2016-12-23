package com.julieservice.app;

import org.json.*;
import java.lang.*;
import java.util.*;

/*
 * Class containing logic to handle anything health related
 *
 */
public class MyHealth 
{
    public MyHealth() {}

    // Function to handle retrieving weight data
    public SvcResponse getWeights(String startDate, String endDate) {

        HealthWeightRes res = new HealthWeightRes();
        if (!Utils.isValidDate(startDate) || 
            (endDate != null && !Utils.isValidDate(endDate))) {
            
            res.status = 401;
            res.message = "Invalid input.";
        }
        else {
        
            ArrayList<Weight> weights = MySQL.getWeights(startDate, endDate);
        
            if (weights == null) {
                res.status = 500;
                res.message = "MySQL internal error.";
            }
            else {
                res.status = 200;
                res.weights = weights;
            }
        }

        return res;        
    }

    // Function to handle adding weight data; Returns response data
    public SvcResponse addWeight(String date, String weight) {
        
        HealthWeightRes res = new HealthWeightRes();
        if (!Utils.isValidDate(date) || !Utils.isValidDouble(weight)) {
            // Failed
            res.status = 401;
            res.message = "Invalid input."; 
        }
        else {
             
            Weight newWeight = MySQL.addWeight(date, weight);

            if (newWeight == null) {
                res.status = 500;
                res.message = "MySQL internal error.";
            }
            else {
                res.status = 200;
                res.weight = newWeight;
            }
        }

        return res;
    }

    // Function to handle deleting weight data
    public SvcResponse deleteWeight(String id) {
        
        HealthWeightRes res = new HealthWeightRes();
        if (!Utils.isValidInt(id)) {
            // Failed
            res.status = 401;
            res.message = "Invalid input."; 
        }
        else {
            boolean success = MySQL.deleteWeight(id);

            if (!success) {
                res.status = 500;
                res.message = "MySQL internal error.";
            }
            else {
                res.status = 200;
            }
        }
         
        return res;
    }

    // Function to handle updating a weight entry
    public SvcResponse updateWeight(String id, String date, String weight) {
    
        HealthWeightRes res = new HealthWeightRes();
        if (!Utils.isValidInt(id) || 
            !Utils.isValidDate(date) || 
            !Utils.isValidDouble(weight)) {
        
            // Failed
            res.status = 401;
            res.message = "Invalid input."; 
        }
        else {
        
            boolean success = MySQL.updateWeight(id, date, weight);

            if (!success) {
                res.status = 500;
                res.message = "MySQL internal error.";
            }
            else {
                res.status = 200;
            }
        }

        return res;
    }
}

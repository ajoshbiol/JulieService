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
    public HealthGetWeightRes getWeights(String startDate, String endDate) {

        ArrayList<Weight> weights = MySQL.getWeights(startDate, endDate);

        HealthGetWeightRes res = new HealthGetWeightRes();

        if (weights == null) {
            res.status = 401;
        }
        else {
            res.status = 200;
            res.weights = weights;
        }

        return res;        
    }
}

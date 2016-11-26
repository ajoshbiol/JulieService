package com.julieservice.app;

import java.util.ArrayList;

/*
 * Response object for the GET health/weight query
 *
 */
public class HealthGetWeightRes extends SvcResponse {

    public ArrayList<Weight> weights;
}

package com.julieservice.app;

import java.util.ArrayList;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;

/*
 * Response object used for health related requests
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthWeightRes extends SvcResponse {

    @JsonProperty
    public ArrayList<Weight> weights;

    @JsonProperty
    public Weight weight;
}

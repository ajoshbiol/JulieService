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
public class ProductivityTodoRes extends SvcResponse {

    @JsonProperty
    public ArrayList<Todo> todos;

    @JsonProperty
    public int count;
}

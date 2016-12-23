package com.julieservice.app;

import com.fasterxml.jackson.annotation.*;

/*
 * Service response base class
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SvcResponse 
{
    @JsonProperty
    public int status;

    @JsonProperty
    public String message;
}

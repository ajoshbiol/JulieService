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

    public void setStatus(int status) {
    
        this.status = status;

        switch (status) {
        
            case 200:
                message = "OK";
                break;
            case 401:
                message = "Invalid request.";
                break;
            case 402:
                message = "You are not welcome here!";
                break;
            case 502:
                message = "Database internal error.";
                break;
            default:
                message = null;
                break;
        }
    }
}

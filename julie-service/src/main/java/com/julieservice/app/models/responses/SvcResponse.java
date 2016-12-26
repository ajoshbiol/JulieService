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
        
            case 401:
                message = "Invalid request.";
                break;
            case 402:
                message = "you are not welcome here!";
                break;
            default:
                message = null;
                break;
        }
    }
}

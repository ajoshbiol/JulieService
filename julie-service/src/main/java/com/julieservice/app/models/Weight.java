package com.julieservice.app;

import java.util.Date;

/*
 * A simple class containing weight data
 */
public class Weight 
{
    private Date date;
    private int weight; 

    public void setDate(Date date) {
        this.date = date;
    }

    public void setWeight(int weight) {
        this.weight = weight; 
    }

    public Date getDate() {
        return date;
    }

    public int getWeight() {
        return weight;
    }
}

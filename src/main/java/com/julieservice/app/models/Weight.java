package com.julieservice.app;

import java.util.Date;

/*
 * A simple class containing weight data
 */
public class Weight 
{
    private int id;
    private Date date;
    private double weight; 

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setWeight(double weight) {
        this.weight = weight; 
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public double getWeight() {
        return weight;
    }
}

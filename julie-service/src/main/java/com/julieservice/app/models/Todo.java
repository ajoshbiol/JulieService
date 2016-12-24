package com.julieservice.app;

import java.util.Date;

/*
 * Class containing todo item data
 */
public class Todo {

    private int id;
    private String task;
    private Date creationDate;
    private Date completionDate;

    public void setId(int id) {
        this.id = id;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }
}

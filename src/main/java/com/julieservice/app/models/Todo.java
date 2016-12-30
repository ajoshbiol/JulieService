package com.julieservice.app;

import java.util.Date;

/*
 * Class containing todo item data
 */
public class Todo {

    private Integer id;
    private String task;
    private long creationDate;
    private long completionDate;

    public void setId(int id) {
        this.id = id;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public void setCompletionDate(long completionDate) {
        this.completionDate = completionDate;
    }

    public Integer getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public long getCompletionDate() {
        return completionDate;
    }
}

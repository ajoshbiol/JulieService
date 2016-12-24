package com.julieservice.app;

/*
 * Class containing logic to handle productivity related requests
 *
 */
public class MyProductivity {

    public SvcResponse getIncompleteTaskCount() {
    
        ProductivityTodoRes res = new ProductivityTodoRes();

        int count = MySQL.getIncompleteTodoCount();

        if (count == -1) {
            res.status = 500;
            res.message = "MySQL internal error.";
        }
        else {
            res.status = 200;
            res.count = count;
        }

        return res;
    }
}

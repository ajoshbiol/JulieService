package com.julieservice.app;

import java.util.*;

/*
 * Class containing logic to handle productivity related requests
 *
 */
public class MyProductivity {

    public SvcResponse getIncompleteTodoCount() {
    
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

    public SvcResponse getTodos(String lastId) {
    
        ProductivityTodoRes res = new ProductivityTodoRes();

        ArrayList<Todo> todos = null;
        if (lastId == null) {
            todos = MySQL.getTodos(); 
        }
        else {
            if (!Utils.isValidInt(lastId)) {
                res.setStatus(401);
                return res;
            }

            int i = Integer.parseInt(lastId);
            todos = MySQL.getTodos(i);
        }

        if (todos == null) {
            res.status = 500;
            res.message = "MySQL internal error.";
        }
        else {
            res.status = 200;
            res.todos = todos;
        }

        return res;
    }
}

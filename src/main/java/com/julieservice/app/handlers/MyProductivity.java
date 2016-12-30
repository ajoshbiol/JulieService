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
            res.setStatus(502);
        }
        else {
            res.setStatus(200);
            res.todos = todos;
        }

        return res;
    }

    public SvcResponse createTodo(String task) {
        
        ProductivityTodoRes res = new ProductivityTodoRes();

        if (Utils.isNullOrEmpty(task)) {
            res.setStatus(401);
        }
        else {

            Todo t = MySQL.addTodo(task);

            if (t == null) {
                res.setStatus(502);
            }
            else {
                res.setStatus(200);
                res.todos = new ArrayList<Todo>();
                res.todos.add(t); 
            }
        }

        return res;
    } 

    public SvcResponse deleteTodo(String id) {
    
        ProductivityTodoRes res = new ProductivityTodoRes();

        if (!Utils.isValidInt(id)) {
            res.setStatus(401);    
        }
        else {
        
            boolean success = MySQL.deleteTodo(id);

            if (!success) {
                res.setStatus(502);
            }
            else {
                res.setStatus(200);
            }
        }

        return res;
    }

    public SvcResponse updateTodo(String id, String task, String creationDate,
        String completionDate) {
    
        ProductivityTodoRes res = new ProductivityTodoRes();

        if (!Utils.isValidInt(id) ||
            Utils.isNullOrEmpty(task) ||
            !Utils.isValidDate(creationDate) ||
            !Utils.isValidDate(completionDate)) {
            
            res.setStatus(401); 
        }
        else {
        
            boolean success = MySQL.updateTodo(id, 
                    task, 
                    creationDate, 
                    completionDate);

            if (!success) {
                res.setStatus(502);
            }
            else {
                res.setStatus(200);
            }
        }

        return res;
    }
}

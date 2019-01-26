package com.example.todolist;

import java.io.Serializable;
import java.util.HashMap;

public class ToDo implements Serializable {

    private String task;
    private String date;

    public ToDo() {

    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String geTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }


    public HashMap<String,String> toFirebaseObject() {
        HashMap<String,String> todo =  new HashMap<String,String>();
        todo.put("task", task);
        todo.put("date", date);

        return todo;
    }

}

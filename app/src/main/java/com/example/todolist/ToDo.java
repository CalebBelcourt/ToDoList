package com.example.todolist;

import java.io.Serializable;
import java.util.HashMap;

public class ToDo implements Serializable {

    private String task;
    private String date;
    private String time;

    public ToDo() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public HashMap<String,String> toFirebaseObject() {
        HashMap<String,String> todo =  new HashMap<String,String>();
        todo.put("task", task);
        todo.put("date", date);
        todo.put("time", time);
        return todo;
    }

}

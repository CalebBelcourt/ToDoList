package com.example.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private List<Task> tasks;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_activity);

        rv = (RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        //Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        tasks = new ArrayList<>();
        tasks.add(new Task("Do this", "Today 10pm"));
        tasks.add(new Task("Do that", "Wed 11am"));
        tasks.add(new Task("Finish this", "Sun 12pm"));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(tasks);
        rv.setAdapter(adapter);
    }




}

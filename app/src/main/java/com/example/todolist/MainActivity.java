package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton t_fab = findViewById(R.id.text_fab);
        FloatingActionButton v_fab = findViewById(R.id.voice_fab);
        t_fab.bringToFront();
        v_fab.bringToFront();


        t_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });

        v_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open up text to speech

                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");

                myRef.setValue("Hello, World!");
            }
        });

    }


}

package com.example.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class CardView extends Activity {

    CheckBox task;
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        task = (CheckBox) findViewById(R.id.task_check);
        date = (TextView)findViewById(R.id.task_date);

        task.setText("Do WHAT?!");
        date.setText("WHEN?!");
    }
}
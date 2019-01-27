package com.example.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener mDateSetListener;

    EditText taskEditText;
    EditText dateEditText;
    EditText timeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        ImageButton calendarButton = (ImageButton) findViewById(R.id.calendar_button);
        taskEditText = (EditText)findViewById(R.id.taskEditText);
        dateEditText = (EditText)findViewById(R.id.dateEditText);
        timeEditText = (EditText)findViewById(R.id.timeEditText);


        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TaskActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet (DatePicker datePicker, int year, int month, int day){
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                dateEditText.setText(date);
                }
            };

        //Speech-to-test result
        Intent intent = getIntent();
        String str = intent.getStringExtra("result");
        taskEditText.setText(str);



        }

    //Create the checkmark in the top right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu, menu);
        return true;
    }

    //When the checkmark in the top right is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        saveTodo();
        Intent intent = new Intent(TaskActivity.this, ListActivity.class);
        TaskActivity.this.startActivity(intent);
        return true;
    }

    //Saves (writes) task to Firebase
    void saveTodo() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("todoList").push().getKey();

        ToDo todo = new ToDo();
        todo.setTask(taskEditText.getText().toString());
        todo.setDate(dateEditText.getText().toString());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( key, todo.toFirebaseObject());
        database.getReference("todoList").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    finish();
                }
            }
        });

    }


}

package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener dateSetListener;

    EditText taskEditText;
    EditText dateEditText;
    EditText timeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        ImageButton calendarButton = (ImageButton) findViewById(R.id.calendar_button);
        ImageButton timeButton = (ImageButton) findViewById(R.id.time_button);
        ImageButton micButton = (ImageButton) findViewById(R.id.mic_button);
        ImageButton dcancelButton = (ImageButton) findViewById(R.id.d_cancel_button);
        ImageButton tcancelButton = (ImageButton) findViewById(R.id.t_cancel_button);

        taskEditText = (EditText)findViewById(R.id.taskEditText);
        dateEditText = (EditText)findViewById(R.id.dateEditText);
        timeEditText = (EditText)findViewById(R.id.timeEditText);

        //user can decide how many times they want task to repeat
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.repeat_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Calendar picker widget pops up if user taps calendar imageButton
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
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //Format date and set it into the date textbox
        dateSetListener = new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet (DatePicker datePicker, int year, int month, int day){
                month = month + 1;

                String date = year + "/" + month + "/" + day;
                dateEditText.setText(date);
                }
            };

        //Speech-to-text result
        Intent intent = getIntent();
        String str = intent.getStringExtra("result");
        taskEditText.setText(str);

        //initializes voice input when mic button is tapped
        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vInput();
            }
        });

        //Time picker widget pops up if user taps time button
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(TaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeEditText.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });

        //removes text in date textbox
        dcancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateEditText.setText("");
            }
        });

        //removes text in time textbox
        tcancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeEditText.setText("");
            }
        });

        }
        //speech-to-text
    private void vInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What do you need to do?");
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
        }
    }

    //set result of voice input to be text in the task textbox
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    taskEditText.setText(result.get(0));
                }
                break;
            }

        }
    }

    //Create the checkmark in the top right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu, menu);
        return true;
    }

    //When the checkmark in the top right is selected, task is saved
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
        todo.setTime(timeEditText.getText().toString());

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

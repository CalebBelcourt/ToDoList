package com.example.todolist;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

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
                vInput();

            }
        });

    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //send speech-to-text result to editText in TaskActivity
                    Intent v_intent = new Intent(MainActivity.this, TaskActivity.class);
                    v_intent.putExtra("result", result.get(0));
                    startActivity(v_intent);
                }
                break;
            }

        }
    }
    }




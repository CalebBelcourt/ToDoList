package com.example.todolist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {

    RecycleAdapter adapter;
    ArrayList<ToDo> todoList;

    //get current date and time
    Calendar cal = Calendar.getInstance();
    Date d = cal.getTime();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    String currentTime = dateFormat.format(d);
    String currentDate = new SimpleDateFormat("yyyy/M/dd", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        FloatingActionButton l_fab = (FloatingActionButton) findViewById(R.id.list_fab);

        l_fab.bringToFront();

        //tapping fab lets you enter another task
        l_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(ListActivity.this,TaskActivity.class);
                ListActivity.this.startActivity(newIntent);
            }
        });

        todoList = new ArrayList<>();

        //initializes recyclerview
        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        adapter = new RecycleAdapter();
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    //When app resumes itself, refresh Firebase database
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference("todoList").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        todoList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            ToDo todo = data.getValue(ToDo.class);
                            todoList.add(todo);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private class RecycleAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return todoList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            SimpleItemViewHolder pvh = new SimpleItemViewHolder(v);
            return pvh;
        }

        //sets date, time and task fields with data from the database
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SimpleItemViewHolder viewHolder = (SimpleItemViewHolder) holder;
            ToDo todo = todoList.get(position);
            viewHolder.date.setText(todo.getDate());
            viewHolder.task.setText(todo.getTask());
            viewHolder.time.setText(todo.getTime());

            //if task is overdue, colour the time red
            if(currentDate.compareTo((String)viewHolder.date.getText()) >= 0
                    && currentTime.compareTo((String) viewHolder.time.getText()) >= 0){
                viewHolder.time.setTextColor(Color.parseColor("#D02130"));
                viewHolder.date.setTextColor(Color.parseColor("#D02130"));
            }else if(currentDate.compareTo((String)viewHolder.date.getText()) > 0){
                viewHolder.time.setTextColor(Color.parseColor("#D02130"));
                viewHolder.date.setTextColor(Color.parseColor("#D02130"));
            }

        }

        //initializes item in recyclerview
        public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            CheckBox task;
            TextView date;
            TextView time;
            public SimpleItemViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv);
                task = (CheckBox) itemView.findViewById(R.id.task_check);
                date = (TextView)itemView.findViewById(R.id.task_date);
                time = (TextView) itemView.findViewById(R.id.task_time);
            }
        }
    }

}
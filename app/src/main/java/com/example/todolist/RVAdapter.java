package com.example.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.TaskViewHolder>{

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        CheckBox task;
        TextView date;

        TaskViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            task = (CheckBox) itemView.findViewById(R.id.task_check);
            date = (TextView)itemView.findViewById(R.id.task_date);
        }
    }

    List<Task> tasks;

    RVAdapter(List<Task> tasks){
        this.tasks = tasks;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        TaskViewHolder pvh = new TaskViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder personViewHolder, int i) {
        personViewHolder.task.setText(tasks.get(i).task);
        personViewHolder.date.setText(tasks.get(i).date);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
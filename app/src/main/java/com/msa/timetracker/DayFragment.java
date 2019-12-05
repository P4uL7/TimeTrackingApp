package com.msa.timetracker;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DayFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    FloatingActionButton add_task;
    //chrono stuff
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    private View v;
    private String new_task_name = "";
    private Button start, pause, stop;

    public DayFragment(FirebaseAuth mAuth, FirebaseUser currentUser, DatabaseReference myRef, FirebaseDatabase database) {
        this.mAuth = mAuth;
        this.currentUser = currentUser;
        this.myRef = myRef;
        this.database = database;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_day, container, false);

        // buttons
        start = v.findViewById(R.id.start_timer);
        start.setOnClickListener(this);
        pause = v.findViewById(R.id.pause_timer);
        pause.setOnClickListener(this);
        stop = v.findViewById(R.id.stop_timer);
        stop.setOnClickListener(this);
        add_task = v.findViewById(R.id.add_task);
        add_task.setOnClickListener(this);

        //chrono
        chronometer = v.findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        chronometer.setOnChronometerTickListener(chronometer -> {
            // check stuff
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_timer:
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }
                break;
            case R.id.pause_timer:
                if (running) {
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
                break;
            case R.id.stop_timer:
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
                break;
            case R.id.add_task:


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter task name:");

                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", (dialog, which) -> {
                    new_task_name = input.getText().toString();
                    Toast.makeText(getActivity(), "New task: " + new_task_name, Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();

                break;

        }
    }


}

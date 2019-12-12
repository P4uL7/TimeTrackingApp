package com.msa.timetracker;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskFragment extends Fragment implements View.OnClickListener {
    long elapsedMillis = 0;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    //chrono stuff
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running = false, task_done = false;
    private View v;
    private TextView taskLabel, status_label;
    private Button start, stop;
    private String taskName;

    public TaskFragment(String taskName) {
        this.taskName = taskName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_task, container, false);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // buttons
        start = v.findViewById(R.id.start_timer);
        start.setOnClickListener(this);
        stop = v.findViewById(R.id.stop_timer);
        stop.setOnClickListener(this);

        //set task header
        taskLabel = v.findViewById(R.id.task_title);
        taskLabel.setText(taskName);
        status_label = v.findViewById(R.id.is_paused);

        //chrono
        chronometer = v.findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        chronometer.setOnChronometerTickListener(chronometer -> {
            // check stuff
        });

        return v;
    }

    private void addTaskToDB() {
        if (currentUser != null) {
            currentUser = mAuth.getCurrentUser();
            String userID = currentUser.getUid();
            String userEmail = currentUser.getEmail();

//            myRef.child(userID).child("email").setValue(userEmail);
            myRef.child(userID).child(getCurrentDate()).child(taskName).setValue(elapsedMillis);

            Toast.makeText(getActivity(), "Task added to DB!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Failed to save task.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_timer:
                if (!running && !task_done) {
                    status_label.setText("Task in progress");
                    start.setText("Pause");
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                } else if (running && !task_done) {
                    status_label.setText("paused");
                    start.setText("resume");
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
                break;
            case R.id.stop_timer:
                if (!task_done) {
                    chronometer.stop();
                    status_label.setText("Task done!");
                    running = false;
                    task_done = true;
                    elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                    addTaskToDB();

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        // Do something after delay
                        Fragment fragment = new DayFragment();
                        replaceFragment(fragment);
                    }, 800);

                }
                break;

        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}

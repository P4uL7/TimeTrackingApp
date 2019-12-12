package com.msa.timetracker;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DayFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private View v;
    private String new_task_name = "";
    private FloatingActionButton add_task;

    private ArrayList<DataModel> todaysTasks = new ArrayList<>();

    public DayFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_day, container, false);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // Read from the database
        DatabaseReference userRef = myRef.child(currentUser.getUid() + "/" + getCurrentDate()); // get today's tasks
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                todaysTasks = new ArrayList<>();
//                Toast.makeText(getActivity(), currentUser.getUid(), Toast.LENGTH_SHORT).show();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    todaysTasks.add(new DataModel(ds.getKey(), ds.getValue().toString()));
                    System.out.println("added to datamodel:" + ds.getKey());
                }
//                Toast.makeText(getActivity(), todaysTasks.size() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getActivity(), "Error reading from DB", Toast.LENGTH_SHORT).show();
            }
        });

        // buttons
        add_task = v.findViewById(R.id.add_task);
        add_task.setOnClickListener(this);


        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            // populate list
            ListView list = v.findViewById(R.id.listView);
            CustomAdapter adapter = new CustomAdapter(todaysTasks, getActivity());
            list.setAdapter(adapter);
        }, 800);


        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_task:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter task name:");

                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", (dialog, which) -> {
                    new_task_name = input.getText().toString();
                    Toast.makeText(getActivity(), "New task: " + new_task_name, Toast.LENGTH_SHORT).show();
                    Fragment fragment = new TaskFragment(new_task_name);
                    replaceFragment(fragment);

                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
                break;
        }
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }

    private void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

package com.msa.timetracker;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OverviewFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private View v;

    private long currentDayTime = 0;
    private ArrayList<DataModel> allTasks = new ArrayList<>();

    public OverviewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_overview, container, false);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // Read from the database
        DatabaseReference userRef = myRef.child(currentUser.getUid()); // get all tasks
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allTasks = new ArrayList<>();
                for (DataSnapshot day : dataSnapshot.getChildren()) {
                    currentDayTime = 0;
                    for (DataSnapshot task : day.getChildren()) {
                        long temp = Long.parseLong(task.getValue().toString());
                        currentDayTime += temp;
                    }

                    allTasks.add(new DataModel(day.getKey(), currentDayTime + ""));
                    System.out.println("added to datamodel: " + day.getKey() + "  -> " + currentDayTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getActivity(), "Error reading from DB", Toast.LENGTH_SHORT).show();
            }
        });


        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            // populate list
            ListView list = v.findViewById(R.id.overview_listView);
            CustomOverviewAdapter adapter = new CustomOverviewAdapter(allTasks, getActivity());
            list.setAdapter(adapter);
        }, 800);


        return v;
    }


    @Override
    public void onClick(View v) {

    }
}

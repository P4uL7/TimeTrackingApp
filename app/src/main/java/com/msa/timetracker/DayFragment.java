package com.msa.timetracker;

import android.os.Bundle;
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
import com.google.android.material.snackbar.Snackbar;
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
    FloatingActionButton add_task;

    String[] tasks;
    String[] durations;

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
                Toast.makeText(getActivity(), currentUser.getUid(), Toast.LENGTH_SHORT).show();
                tasks = new String[(int) dataSnapshot.getChildrenCount()];
                durations = new String[(int) dataSnapshot.getChildrenCount()];
                int i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    tasks[i] = ds.getKey();
                    durations[i] = ds.getValue().toString();
                    i++;
                }

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


        // populate list
        ListView list = v.findViewById(R.id.listView);
        ArrayList<DataModel> dataModels = new ArrayList<>();

        dataModels.add(new DataModel("Apple Pie", "Android 1.0", "1", "September 23, 2008"));
        dataModels.add(new DataModel("Banana Bread", "Android 1.1", "2", "February 9, 2009"));
        dataModels.add(new DataModel("Cupcake", "Android 1.5", "3", "April 27, 2009"));
        dataModels.add(new DataModel("Donut", "Android 1.6", "4", "September 15, 2009"));
        dataModels.add(new DataModel("Eclair", "Android 2.0", "5", "October 26, 2009"));
        dataModels.add(new DataModel("Froyo", "Android 2.2", "8", "May 20, 2010"));
        dataModels.add(new DataModel("Gingerbread", "Android 2.3", "9", "December 6, 2010"));
        dataModels.add(new DataModel("Honeycomb", "Android 3.0", "11", "February 22, 2011"));
        dataModels.add(new DataModel("Ice Cream Sandwich", "Android 4.0", "14", "October 18, 2011"));
        dataModels.add(new DataModel("Jelly Bean", "Android 4.2", "16", "July 9, 2012"));
        dataModels.add(new DataModel("Kitkat", "Android 4.4", "19", "October 31, 2013"));
        dataModels.add(new DataModel("Lollipop", "Android 5.0", "21", "November 12, 2014"));
        dataModels.add(new DataModel("Marshmallow", "Android 6.0", "23", "October 5, 2015"));

        CustomAdapter adapter = new CustomAdapter(dataModels, getActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {

            DataModel dataModel = dataModels.get(position);

            Snackbar.make(view, dataModel.getName() + "\n" + dataModel.getType() + " API: " + dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
        });

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

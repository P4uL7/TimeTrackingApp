package com.msa.timetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class MainFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private Button bDay, bOverview, bProfile, bExit;
    private View v;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);
        bDay = v.findViewById(R.id.bDay);
        bOverview = v.findViewById(R.id.bOverview);
        bProfile = v.findViewById(R.id.bProfile);
        bExit = v.findViewById(R.id.bExit);
        bDay.setOnClickListener(this);
        bOverview.setOnClickListener(this);
        bProfile.setOnClickListener(this);
        bExit.setOnClickListener(this);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        return v;
    }

    @Override
    public void onClick(View v) {
        System.out.println("YOlo asd: " + v.getId());
        switch (v.getId()) {
            case R.id.bDay:
                replaceFragment(new DayFragment());
                break;

            case R.id.bOverview:
                replaceFragment(new OverviewFragment());
                break;

            case R.id.bProfile:
                replaceFragment(new ProfileFragment());
                break;

            case R.id.bExit:
                exitApp();
                break;
        }
    }

    public void replaceFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragment_container_main, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void exitApp() {
        new AlertDialog.Builder(getContext())
                .setTitle("Exit App")
                .setMessage("Are you sure you wish to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("No", null)
                .show();
    }

    private void getCurrentUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            Toast.makeText(getActivity(), "Logged in as: " + email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

}

package com.msa.timetracker;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class MainFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    Button bClick1, bClick2, bClick3, bRandom;
    private View v;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);
        bClick1 = v.findViewById(R.id.bClick1);
        bClick2 = v.findViewById(R.id.bClick2);
        bClick3 = v.findViewById(R.id.bClick3);
        bRandom = v.findViewById(R.id.bRandom);
        bClick1.setOnClickListener(this);
        bClick2.setOnClickListener(this);
        bClick3.setOnClickListener(this);
        bRandom.setOnClickListener(this);

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
            case R.id.bClick1:
                getCurrentUser();
                break;
            case R.id.bClick2:
            case R.id.bClick3:
//                addJunkToDB();
                Toast.makeText(getActivity(), "Deprecated.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bRandom:
                // showAlertDialog("Random Dialog");
                Toast.makeText(getActivity(), "Dead :(", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getCurrentUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Name, email address, and profile photo Url
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            Uri photoUrl = currentUser.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = currentUser.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = currentUser.getUid();

            System.out.println(name + "  " + email + "  \n" + photoUrl + " \n" + emailVerified + " \n" + uid);
            Toast.makeText(getActivity(), "Logged in as: " + email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addJunkToDB() {
        if (currentUser != null) {
            Toast.makeText(getActivity(), "User: " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            currentUser = mAuth.getCurrentUser();
            String userID = currentUser.getUid();
            String userEmail = currentUser.getEmail();

            Date currentTime = Calendar.getInstance().getTime();

            myRef.child(userID).child("Time").setValue(currentTime.toString());
            myRef.child(userID).child("email").setValue(userEmail);
        } else {
            Toast.makeText(getActivity(), "User is null.", Toast.LENGTH_SHORT).show();
        }
    }

}

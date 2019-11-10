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

import java.util.Calendar;
import java.util.Date;

public class MainFragment extends Fragment implements View.OnClickListener {
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
                Toast.makeText(getActivity(), "Adding junk to db.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bClick3:
                Toast.makeText(getActivity(), "Launching dayActivity.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bRandom:
                // showAlertDialog("Random Dialog");
                Toast.makeText(getActivity(), "Dead :(", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getCurrentUser() {
        MainActivity.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (MainActivity.currentUser != null) {
            // Name, email address, and profile photo Url
            String name = MainActivity.currentUser.getDisplayName();
            String email = MainActivity.currentUser.getEmail();
            Uri photoUrl = MainActivity.currentUser.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = MainActivity.currentUser.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = MainActivity.currentUser.getUid();

            System.out.println(name + "  " + email + "  \n" + photoUrl + " \n" + emailVerified + " \n" + uid);
            Toast.makeText(getActivity(), "Logged in as: " + email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addJunkToDB() {
        if (MainActivity.currentUser != null) {
            MainActivity.currentUser = MainActivity.mAuth.getCurrentUser();
            String userID = MainActivity.currentUser.getUid();
            String userEmail = MainActivity.currentUser.getEmail();

            Date currentTime = Calendar.getInstance().getTime();

            MainActivity.myRef.child(userID).child("Time").setValue(currentTime.toString());
            MainActivity.myRef.child(userID).child("email").setValue(userEmail);
        } else {
            Toast.makeText(getActivity(), "User is null.", Toast.LENGTH_SHORT).show();
        }
    }

}

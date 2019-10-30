package com.msa.timetracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        System.out.println("Current user: " + currentUser);
    }

    @Override
    public void onBackPressed() {
    }

    public void clicked(View view) {

        TextView tView = findViewById(R.id.tName);

        switch (view.getId()) {
            case R.id.bClick1:
                tView.setText("Showing user info.");
                getCurrentUser();
                break;
            case R.id.bClick2:
                tView.setText("Adding junk to db.");
                addJunkToDB();
                break;
            case R.id.bClick3:
                tView.setText("Launching dayActivity.");
                launchDayActivity();
                break;
            case R.id.bRandom:
                tView.setText("Pressed the random button!");
                showAlertDialog("Random Dialog");
                break;
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                launchLoginActivity();
                break;
        }
    }


    private void addJunkToDB() {
        currentUser = mAuth.getCurrentUser();
        String userID = currentUser.getUid();
        String userEmail = currentUser.getEmail();

        Date currentTime = Calendar.getInstance().getTime();

        myRef.child(userID).child("Time").setValue(currentTime.toString());
        myRef.child(userID).child("email").setValue(userEmail);
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
            Toast.makeText(MainActivity.this, "Logged in as: " + email,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Not logged in.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void showAlertDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        PopupDialogFragment alertDialog = new PopupDialogFragment(message, getApplicationContext(), "not ok", "OK");
        alertDialog.show(fm, "fragment_alert");
    }

    private void launchDayActivity() {
        Intent intent = new Intent(this, DayActivity.class);
        startActivity(intent);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}

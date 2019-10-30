package com.msa.timetracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        System.out.println("Current user: " + currentUser);
        updateUI(currentUser);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog("Are you sure you wish to exit?");
    }

    private void showAlertDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        PopupDialogFragment alertDialog = new PopupDialogFragment(message, getApplicationContext(), "No", "Yes");
        alertDialog.show(fm, "exit_app");
    }

    private void updateUI(FirebaseUser user) {
        if (user != null)
            System.out.println("Got user: " + user.toString());
        else
            System.out.println("User is NULL!");
    }

    @SuppressLint("SetTextI18n")
    public void clicked_login(View view) {

        TextView tView = findViewById(R.id.login_message);
        EditText email_field = findViewById(R.id.email_login);
        EditText password_field = findViewById(R.id.password_login);

        String email = email_field.getText().toString();
        String password = password_field.getText().toString();

        switch (view.getId()) {
            case R.id.loginButton:
                tView.setText("Pressed login.");
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Password cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    signIn(email, password);
                }
                break;
            case R.id.goToRegisterButton:
                tView.setText("Pressed register.");
                launchRegisterActivity();
                break;
            case R.id.goToMainButton:
                tView.setText("Pressed goToMain.");
                launchMainActivity();
                break;
            case R.id.googleLoginButton:
                tView.setText("Pressed Google.");
                getCurrentUser();
                break;
            case R.id.facebookLoginButton:
                tView.setText("Pressed Facebook.");
                break;

        }
    }


    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void launchRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    private void signIn(String email, String password) {
        System.out.println("Enter sign in.");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication succeeded.",
                                    Toast.LENGTH_SHORT).show();

                            currentUser = mAuth.getCurrentUser();
                            Log.d(TAG, "signInWithEmail:success");
                            updateUI(currentUser);

                            launchMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            updateUI(null);
                            System.out.println("Authentication failed.");

                        }

                        // ...
                    }
                });
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
            Toast.makeText(LoginActivity.this, "Logged in as: " + email,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "Not logged in.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

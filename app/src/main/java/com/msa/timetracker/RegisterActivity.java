package com.msa.timetracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity ";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        System.out.println("Current user: " + currentUser);
    }


    public void clicked_register(View view) {
        EditText user = findViewById(R.id.user);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText confirm_password = findViewById(R.id.confirm_password);

        String user_string = user.getText().toString().trim();
        String email_string = email.getText().toString().trim();
        String password_string = password.getText().toString();
        String confirm_string = confirm_password.getText().toString();

        if (user_string.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Username cannot be empty.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (email_string.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Email cannot be empty.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (password_string.isEmpty() || confirm_string.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Password cannot be empty.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password_string.equals(confirm_string)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (password_string.length() < 6) {
            Toast.makeText(RegisterActivity.this, "Password should be at least 6 characters.",
                    Toast.LENGTH_SHORT).show();
        } else {
            createAccount(email_string, password_string, user_string);
        }
    }

    private void createAccount(String email, String password, String user_string) {
        System.out.println("Enter create with: " + email + "  " + password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registration succeeded.",
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "createUserWithEmail:success");
                        currentUser = mAuth.getCurrentUser();
                        // set username
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(user_string).build();
                        currentUser.updateProfile(profileUpdates);
                        //
                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            launchMainActivity();
                        }, 1000);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed.",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());

                    }
                });
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

package com.msa.timetracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        initializeFacebookButton();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        System.out.println("Current user: " + currentUser);
        if (currentUser != null) {
            System.out.println("Launching mainActivity for user: " + currentUser);
            launchMainActivity();
        }
    }

    //sign-in with email
    private void signIn(String email, String password) {
        System.out.println("Enter sign in.");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Authentication OK.",
                                Toast.LENGTH_SHORT).show();
                        currentUser = mAuth.getCurrentUser();
                        Log.d(TAG, "signInWithEmail:success");
                        launchMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        System.out.println("Authentication failed.");
                    }
                });
    }

    // sign-in with google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        Toast.makeText(LoginActivity.this, "Authentication OK.",
                                Toast.LENGTH_SHORT).show();
                        currentUser = mAuth.getCurrentUser();
                        launchMainActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication Failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //sign-in with facebook
    private void initializeFacebookButton() {
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult.toString());
                Toast.makeText(LoginActivity.this, "Authentication OK.",
                        Toast.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());

                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (currentUser != null) {
                        System.out.println("Success: Launching mainActivity for user: " + currentUser);
                        launchMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "user is null?",
                                Toast.LENGTH_SHORT).show();
                    }
                }, 2000);

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(LoginActivity.this, "Authentication Cancel.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(LoginActivity.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        currentUser = mAuth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override // google sign in
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(Objects.requireNonNull(account));
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("SetTextI18n")
    public void clicked_login(View view) {

        EditText email_field = findViewById(R.id.email_login);
        EditText password_field = findViewById(R.id.password_login);

        String email = email_field.getText().toString();
        String password = password_field.getText().toString();

        switch (view.getId()) {
            case R.id.loginButton:
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
                launchRegisterActivity();
                break;

            case R.id.googleLoginButton:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
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

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void launchRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
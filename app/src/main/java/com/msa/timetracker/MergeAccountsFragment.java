package com.msa.timetracker;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MergeAccountsFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private View v;

    EditText email_merge, password_merge;
    Button merge_login, merge_google, merge_facebook, status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_merge, container, false);


        email_merge = v.findViewById(R.id.merge_email_login);
        password_merge = v.findViewById(R.id.merge_password_login);

        merge_login = v.findViewById(R.id.merge_loginButton);
        merge_login.setOnClickListener(this);

        merge_google = v.findViewById(R.id.merge_googleLoginButton);
        merge_google.setOnClickListener(this);

        merge_facebook = v.findViewById(R.id.merge_buttonFacebookLogin);
        merge_facebook.setOnClickListener(this);

        status = v.findViewById(R.id.merge_showUserStatus);
        status.setOnClickListener(this);


        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.merge_loginButton:
                Toast.makeText(getActivity(), "login", Toast.LENGTH_SHORT).show();
                mergeEmail();
                break;
            case R.id.merge_googleLoginButton:
                Toast.makeText(getActivity(), "google", Toast.LENGTH_SHORT).show();
                break;
            case R.id.merge_buttonFacebookLogin:
                Toast.makeText(getActivity(), "facebook", Toast.LENGTH_SHORT).show();
                break;
            case R.id.merge_showUserStatus:
                getCurrentUser();
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

            String uid = currentUser.getUid();

            System.out.println(name + "  " + email + "  \n" + photoUrl + " \n" + emailVerified + " \n" + uid);
            Toast.makeText(getActivity(), "Logged in as: " + email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void mergeEmail() {
        AuthCredential credential = EmailAuthProvider.getCredential("asd@asd.com", "asdasd");
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Auth ok", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
//                        handleMerge(credential);

                    }
                });
    }

    private void handleMerge(AuthCredential credential) {
        FirebaseUser prevUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser currentUser = task.getResult().getUser();
                        // Merge prevUser and currentUser accounts and data
                    }
                });
    }
}

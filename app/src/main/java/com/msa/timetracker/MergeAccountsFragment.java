package com.msa.timetracker;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class MergeAccountsFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private View v;

    private EditText email_merge, password_merge;
    private Button merge_login, merge_google, merge_facebook, status;

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
        boolean disabled = true;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (disabled) {
            Toast.makeText(getActivity(), "Feature disabled", Toast.LENGTH_SHORT).show();
        } else {
            switch (v.getId()) {
                case R.id.merge_loginButton:
                    //start dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                    builder.setTitle("Please re-enter password:");

                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);

                    builder.setPositiveButton("OK", (dialog, which) -> {
                        AuthCredential current_credential = EmailAuthProvider.getCredential(currentUser.getEmail(), input.getText().toString());
                        currentUser.reauthenticate(current_credential);
                        AuthCredential credential = EmailAuthProvider.getCredential(email_merge.getText().toString(), password_merge.getText().toString());
                        mergeEmail(credential);
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // Check if edittext is empty
                            if (TextUtils.isEmpty(s)) {
                                // Disable ok button
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                            } else {
                                // Something into edit text. Enable the button.
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            }
                        }
                    });
                    //end dialog

                    break;
                case R.id.merge_googleLoginButton:
                    //get google token
                    // AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
                    // Toast.makeText(getActivity(), credential != null ? credential.getProvider() : "null", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.merge_buttonFacebookLogin:
                    AccessToken token = AccessToken.getCurrentAccessToken();
                    AuthCredential current_credential = FacebookAuthProvider.getCredential(token.getToken());
                    currentUser.reauthenticate(current_credential);
                    AuthCredential credential = EmailAuthProvider.getCredential(email_merge.getText().toString(), password_merge.getText().toString());
                    mergeEmail(credential);

                    break;
                case R.id.merge_showUserStatus:
                    getCurrentUser();
                    break;
            }
        }
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

    private void mergeEmail(AuthCredential credential) {
        mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                FirebaseUser user = task.getResult().getUser();
            } else {
                Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                System.out.println(" >>> MERGE_ERROR: " + task.getException().toString());
            }
        });
    }

    public void showDialog() {

    }
}

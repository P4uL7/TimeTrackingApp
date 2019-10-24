package com.msa.timetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void clicked_login(View view) {

        TextView tView = findViewById(R.id.login_message);

        switch (view.getId()) {
            case R.id.loginButton:
                tView.setText("Pressed login.");
                break;
            case R.id.registerButton:
                tView.setText("Pressed register.");
                break;
            case R.id.goToMainButton:
                tView.setText("Pressed goToMain.");
                launchActivity();
                break;
        }
    }

    private void launchActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

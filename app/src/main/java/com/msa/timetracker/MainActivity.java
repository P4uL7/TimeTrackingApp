package com.msa.timetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getApplicationContext();

    }

    public void clicked(View view) {

        TextView tView = findViewById(R.id.tName);

        switch (view.getId()) {
            case R.id.bClick1:
                tView.setText("Pressed button1.");
                break;
            case R.id.bClick2:
                tView.setText("Pressed button2.");
                break;
            case R.id.bClick3:
                tView.setText("Launching dayActivity.");
                launchActivity();
                break;
            case R.id.bRandom:
                tView.setText("Pressed the random button!");
                showAlertDialog("Random Dialog");
                break;
        }
    }

    private void showAlertDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        PopupDialogFragment alertDialog = new PopupDialogFragment(message, getApplicationContext());
        alertDialog.show(fm, "fragment_alert");
    }

    private void launchActivity() {
        Intent intent = new Intent(this, DayActivity.class);
        startActivity(intent);
    }
}

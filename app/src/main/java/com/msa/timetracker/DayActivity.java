package com.msa.timetracker;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Button mBtGoBack = findViewById(R.id.bt_go_back);

        mBtGoBack.setOnClickListener(view -> finish());

    }
}

package com.msa.timetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DayFragment extends Fragment implements View.OnClickListener {
    private View v;
    private Button btn_goBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_day, container, false);
        btn_goBack = v.findViewById(R.id.bt_go_back);
        btn_goBack.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_go_back:
                Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

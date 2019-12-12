package com.msa.timetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String nameList[];
    String durationList[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] nameList, String[] durationList) {
        this.context = applicationContext;
        this.nameList = nameList;
        this.durationList = durationList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return nameList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView name = view.findViewById(R.id.list_task_name);
        TextView duration = view.findViewById(R.id.list_task_duration);
        name.setText(nameList[i]);
        duration.setText(durationList[i]);
        return view;
    }
}
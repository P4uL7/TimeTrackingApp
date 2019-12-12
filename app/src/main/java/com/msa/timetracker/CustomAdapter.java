package com.msa.timetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel> {

    Context mContext;

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.activity_listview, data);
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag


        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_listview, parent, false);
            viewHolder.taskName = convertView.findViewById(R.id.list_task_name);
            viewHolder.taskDuration = convertView.findViewById(R.id.list_task_duration);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.taskName.setText(dataModel.getName());
        viewHolder.taskDuration.setText(dataModel.getDuration());
        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView taskName;
        TextView taskDuration;
    }
}
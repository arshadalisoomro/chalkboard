package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ghofrani.classapp.R;

public class TimetableListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private final String[] inputClasses;

    public TimetableListAdapter(Context context, String[] inputClasses) {

        this.inputClasses = inputClasses;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {

        return inputClasses.length;

    }

    @Override
    public Object getItem(int position) {

        return inputClasses[position];

    }

    @Override
    public long getItemId(int position) {

        return position;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null)
            view = inflater.inflate(R.layout.view_list_child, null);

        String[] splitClasses = inputClasses[position].split(",");

        TextView text = (TextView) view.findViewById(R.id.list_item_text);
        text.setText(splitClasses[0]);

        TextView time = (TextView) view.findViewById(R.id.list_item_time);
        time.setText(splitClasses[1]);

        TextView location = (TextView) view.findViewById(R.id.list_item_location);
        location.setText(splitClasses[2]);

        return view;

    }
}
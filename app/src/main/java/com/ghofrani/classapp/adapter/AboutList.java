package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghofrani.classapp.R;

import java.util.ArrayList;

public class AboutList extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final ArrayList<String> aboutTitlesArrayList;
    private final Context context;

    public AboutList(Context context) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        aboutTitlesArrayList = new ArrayList<>();
        this.context = context;

    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return aboutTitlesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {

        return !(position == 0 || position == 1);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.view_about_list_child, null);

        final ImageView listChildIconImageView = (ImageView) convertView.findViewById(R.id.view_about_list_child_icon_image_view);
        final TextView listChildTitleTextView = (TextView) convertView.findViewById(R.id.view_about_list_child_title_text_view);

        switch (position) {

            case 0:

                listChildIconImageView.setImageResource(R.drawable.developer);
                listChildTitleTextView.setText("Armin Ghofrani");

                break;

            case 1:

                listChildIconImageView.setImageResource(R.drawable.version);
                listChildTitleTextView.setText("Version " + context.getString(R.string.app_version));

                break;

            case 2:

                listChildIconImageView.setImageResource(R.drawable.changelog);
                listChildTitleTextView.setText("View Changelog");

                break;

            case 3:

                listChildIconImageView.setImageResource(R.drawable.licenses);
                listChildTitleTextView.setText("View Licenses");

                break;

            case 4:

                listChildIconImageView.setImageResource(R.drawable.rate);
                listChildTitleTextView.setText("Rate on Google Play");

                break;

            case 5:

                listChildIconImageView.setImageResource(R.drawable.bugs);
                listChildTitleTextView.setText("Report Bugs on GitHub");

                break;

        }

        return convertView;

    }

}
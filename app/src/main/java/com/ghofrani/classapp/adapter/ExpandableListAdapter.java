package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ghofrani.classapp.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final HashMap<String, List<String>> nextClassesHM;
    private final List<String> nextClasses;

    public ExpandableListAdapter(Context context, HashMap<String,
            List<String>> nextClassesHM, List<String> nextClasses) {

        this.context = context;
        this.nextClassesHM = nextClassesHM;
        this.nextClasses = nextClasses;

    }

    @Override
    public int getGroupCount() {
        return nextClasses.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return nextClassesHM.get(nextClasses.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return nextClasses.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return nextClassesHM.get(nextClasses.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String groupTitle = (String) getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, parent, false);

        }

        TextView groupTextView = (TextView) convertView.findViewById(R.id.list_group_text);

        groupTextView.setText(groupTitle);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String childInformation = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_child, parent, false);

        }

        String[] childInformationArray = childInformation.split(",");
        String childTitle = childInformationArray[0];
        String childSubtitle = childInformationArray[1];
        String childLocation = childInformationArray[2];

        TextView childTitleTextView = (TextView) convertView.findViewById(R.id.list_item_text);
        childTitleTextView.setText(childTitle);

        TextView childSubtitleTextView = (TextView) convertView.findViewById(R.id.list_item_time);
        childSubtitleTextView.setText(childSubtitle);

        TextView childLocationTextView = (TextView) convertView.findViewById(R.id.list_item_location);
        childLocationTextView.setText(childLocation);

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

}
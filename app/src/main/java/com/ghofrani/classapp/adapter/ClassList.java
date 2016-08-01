package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.StandardClass;

import java.util.LinkedList;

public class ClassList extends BaseExpandableListAdapter {

    private final LinkedList<StandardClass> classesLinkedList;
    private final String groupTitle;
    private final LayoutInflater layoutInflater;

    public ClassList(Context context, LinkedList<StandardClass> classesLinkedList, String groupTitle) {

        this.classesLinkedList = classesLinkedList;
        this.groupTitle = groupTitle;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return classesLinkedList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return classesLinkedList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return classesLinkedList.get(childPosition);
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

        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.view_list_group, parent, false);

        TextView listGroupTitleTextView = (TextView) convertView.findViewById(R.id.view_list_group_text);
        listGroupTitleTextView.setText(groupTitle);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.view_list_child, parent, false);

        StandardClass standardClass = classesLinkedList.get(childPosition);

        TextView listChildTitleTextView = (TextView) convertView.findViewById(R.id.view_list_child_text);
        listChildTitleTextView.setText(standardClass.getName());

        TextView listChildSubtitleTextView = (TextView) convertView.findViewById(R.id.view_list_child_time);
        listChildSubtitleTextView.setText(standardClass.getStartTimeString() + " - " + standardClass.getEndTimeString());

        TextView listChildLocationTextView = (TextView) convertView.findViewById(R.id.view_list_child_location);
        listChildLocationTextView.setText(standardClass.getLocation());

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
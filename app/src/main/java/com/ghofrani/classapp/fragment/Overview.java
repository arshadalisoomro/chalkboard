package com.ghofrani.classapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.ClassList;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.modules.DataStore;

import java.util.LinkedList;

public class Overview extends Fragment {

    private final BroadcastReceiver updateProgressBarReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            getView().post(new Runnable() {

                @Override
                public void run() {

                    updateProgressBar();

                }

            });

        }

    };
    private ExpandableListView expandableListViewNextClasses;
    private LinkedList<StandardClass> nextClassesLinkedList;
    private ExpandableListView expandableListViewTomorrowClasses;
    private LinkedList<StandardClass> tomorrowClassesLinkedList;
    private final BroadcastReceiver updateUI = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            getView().post(new Runnable() {

                @Override
                public void run() {

                    updateUI();

                }

            });

        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_overview, container, false);

    }

    @Override
    public void onStart() {

        super.onStart();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateProgressBarReceiver, new IntentFilter("update_progress_bar"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateUI, new IntentFilter("update_UI"));

        updateUI();

    }

    @Override
    public void onStop() {

        super.onStop();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateProgressBarReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateUI);

    }

    private void updateUI() {

        if (DataStore.isCurrentClass()) {

            StandardClass currentClass = DataStore.getCurrentClass();

            TextView currentClassTitleTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_title);
            currentClassTitleTextView.setText(currentClass.getName());

            TextView currentClassLocationTeacherTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_location_teacher);
            String locationTeacher = currentClass.getTeacher() + ", " + currentClass.getLocation();
            currentClassLocationTeacherTextView.setText(locationTeacher);

            TextView currentClassStartTimeTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_start_time);
            currentClassStartTimeTextView.setText(currentClass.getStartTimeString());

            TextView currentClassEndTimeTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_end_time);
            currentClassEndTimeTextView.setText(currentClass.getEndTimeString());

            updateProgressBar();

        }

        if (DataStore.isNextClasses()) {

            nextClassesLinkedList = DataStore.getNextClassesLinkedList();

            configureExpandableListViewNextClasses();

            expandableListViewNextClasses.post(new Runnable() {

                @Override
                public void run() {

                    expandableListViewNextClasses.setIndicatorBoundsRelative(expandableListViewNextClasses.getRight() - getPixelFromDP(32), expandableListViewNextClasses.getWidth());

                }

            });

        }

        if (DataStore.isTomorrowClasses() && PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("tomorrow_classes", true)) {

            tomorrowClassesLinkedList = DataStore.getTomorrowClassesLinkedList();

            configureExpandableListViewTomorrowClasses();

            expandableListViewTomorrowClasses.post(new Runnable() {

                @Override
                public void run() {

                    expandableListViewTomorrowClasses.setIndicatorBoundsRelative(expandableListViewTomorrowClasses.getRight() - getPixelFromDP(32), expandableListViewTomorrowClasses.getWidth());


                }

            });

        }

        setMarginsVisibility();

    }

    private void updateProgressBar() {

        final ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.overview_current_class_card_progress_bar);
        progressBar.setIndeterminate(false);

        TextView progressTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_progress_percentage);
        progressTextView.setText(DataStore.getProgressBarText());

        progressBar.setProgress(DataStore.getProgressBarProgress());

    }

    private void setMarginsVisibility() {

        if (DataStore.isCurrentClass()) {

            CardView noClassesCard = (CardView) getView().findViewById(R.id.overview_no_classes_card);
            noClassesCard.setVisibility(View.GONE);

            CardView currentClassCard = (CardView) getView().findViewById(R.id.overview_current_class_card);
            currentClassCard.setVisibility(View.VISIBLE);

            if (DataStore.isNextClasses()) {

                RelativeLayout.LayoutParams layoutParamsCCC;
                layoutParamsCCC = (RelativeLayout.LayoutParams) currentClassCard.getLayoutParams();
                layoutParamsCCC.setMargins(getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(4));
                currentClassCard.setLayoutParams(layoutParamsCCC);

                CardView nextClassesCard = (CardView) getView().findViewById(R.id.overview_next_classes_card);
                nextClassesCard.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams layoutParamsNCC;
                layoutParamsNCC = (RelativeLayout.LayoutParams) nextClassesCard.getLayoutParams();
                layoutParamsNCC.setMargins(getPixelFromDP(16), getPixelFromDP(4), getPixelFromDP(16), getPixelFromDP(16));
                nextClassesCard.setLayoutParams(layoutParamsNCC);

                if (DataStore.isTomorrowClasses() && PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("tomorrow_classes", true)) {

                    layoutParamsNCC.setMargins(getPixelFromDP(16), getPixelFromDP(4), getPixelFromDP(16), getPixelFromDP(4));
                    nextClassesCard.setLayoutParams(layoutParamsNCC);

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.overview_tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams layoutParamsTCC;
                    layoutParamsTCC = (RelativeLayout.LayoutParams) tomorrowClassesCard.getLayoutParams();
                    layoutParamsTCC.setMargins(getPixelFromDP(16), getPixelFromDP(4), getPixelFromDP(16), getPixelFromDP(16));
                    tomorrowClassesCard.setLayoutParams(layoutParamsTCC);

                } else {

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.overview_tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.GONE);

                }

            } else {

                CardView nextClassesCard = (CardView) getView().findViewById(R.id.overview_next_classes_card);
                nextClassesCard.setVisibility(View.GONE);

                if (DataStore.isTomorrowClasses() && PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("tomorrow_classes", true)) {

                    RelativeLayout.LayoutParams layoutParamsCCC;
                    layoutParamsCCC = (RelativeLayout.LayoutParams) currentClassCard.getLayoutParams();
                    layoutParamsCCC.setMargins(getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(4));
                    currentClassCard.setLayoutParams(layoutParamsCCC);

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.overview_tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams layoutParamsTCC;
                    layoutParamsTCC = (RelativeLayout.LayoutParams) tomorrowClassesCard.getLayoutParams();
                    layoutParamsTCC.setMargins(getPixelFromDP(16), getPixelFromDP(4), getPixelFromDP(16), getPixelFromDP(16));
                    tomorrowClassesCard.setLayoutParams(layoutParamsTCC);

                } else {

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.overview_tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.GONE);

                }

            }

        } else {

            CardView currentClassCard = (CardView) getView().findViewById(R.id.overview_current_class_card);
            currentClassCard.setVisibility(View.GONE);

            if (DataStore.isNextClasses()) {

                CardView noClasses = (CardView) getView().findViewById(R.id.overview_no_classes_card);
                noClasses.setVisibility(View.GONE);

                CardView nextClassesCard = (CardView) getView().findViewById(R.id.overview_next_classes_card);
                nextClassesCard.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams layoutParamsNCC;
                layoutParamsNCC = (RelativeLayout.LayoutParams) nextClassesCard.getLayoutParams();
                layoutParamsNCC.setMargins(getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16));
                nextClassesCard.setLayoutParams(layoutParamsNCC);

                if (DataStore.isTomorrowClasses() && PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("tomorrow_classes", true)) {

                    layoutParamsNCC.setMargins(getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(4));
                    nextClassesCard.setLayoutParams(layoutParamsNCC);

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.overview_tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams layoutParamsTCC;
                    layoutParamsTCC = (RelativeLayout.LayoutParams) tomorrowClassesCard.getLayoutParams();
                    layoutParamsTCC.setMargins(getPixelFromDP(16), getPixelFromDP(4), getPixelFromDP(16), getPixelFromDP(16));
                    tomorrowClassesCard.setLayoutParams(layoutParamsTCC);

                } else {

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.overview_tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.GONE);

                }

            } else {

                CardView nextClassesCard = (CardView) getView().findViewById(R.id.overview_next_classes_card);
                nextClassesCard.setVisibility(View.GONE);

                if (DataStore.isTomorrowClasses() && PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("tomorrow_classes", true)) {

                    CardView noClasses = (CardView) getView().findViewById(R.id.overview_no_classes_card);
                    noClasses.setVisibility(View.GONE);

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.overview_tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams layoutParamsTCC;
                    layoutParamsTCC = (RelativeLayout.LayoutParams) tomorrowClassesCard.getLayoutParams();
                    layoutParamsTCC.setMargins(getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16));
                    tomorrowClassesCard.setLayoutParams(layoutParamsTCC);

                } else {

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.overview_tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.GONE);

                    CardView noClasses = (CardView) getView().findViewById(R.id.overview_no_classes_card);
                    noClasses.setVisibility(View.VISIBLE);

                }

            }

        }

    }

    private void configureExpandableListViewNextClasses() {

        expandableListViewNextClasses = (ExpandableListView) getView().findViewById(R.id.overview_next_classes_list);

        ClassList classListAdapter = new ClassList(getActivity(), nextClassesLinkedList, "Next classes");
        expandableListViewNextClasses.setAdapter(classListAdapter);

        setListViewHeightBasedOnChildren(expandableListViewNextClasses, false);

        expandableListViewNextClasses.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            public boolean onGroupClick(ExpandableListView expandableListViewGroupListener, View view, int groupPosition, long id) {

                expandableListViewNextClasses = expandableListViewGroupListener;

                if (!expandableListViewNextClasses.isGroupExpanded(0))
                    expandableListViewNextClasses.expandGroup(0);
                else
                    expandableListViewNextClasses.collapseGroup(0);

                setListViewHeightBasedOnChildren(expandableListViewNextClasses, true);

                return true;

            }

        });

    }

    private void configureExpandableListViewTomorrowClasses() {

        expandableListViewTomorrowClasses = (ExpandableListView) getView().findViewById(R.id.overview_tomorrow_classes_list);

        ClassList classListAdapter = new ClassList(getActivity(), tomorrowClassesLinkedList, "Tomorrow's classes");
        expandableListViewTomorrowClasses.setAdapter(classListAdapter);

        setListViewHeightBasedOnChildren(expandableListViewTomorrowClasses, false);

        expandableListViewTomorrowClasses.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            public boolean onGroupClick(ExpandableListView expandableListViewGroupListener, View view, int groupPosition, long id) {

                expandableListViewTomorrowClasses = expandableListViewGroupListener;

                if (!expandableListViewTomorrowClasses.isGroupExpanded(0))
                    expandableListViewTomorrowClasses.expandGroup(0);
                else
                    expandableListViewTomorrowClasses.collapseGroup(0);

                setListViewHeightBasedOnChildren(expandableListViewTomorrowClasses, true);

                return true;

            }

        });

    }

    private void setListViewHeightBasedOnChildren(ExpandableListView listView, boolean changeParams) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null)
            return;

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        final ViewGroup.LayoutParams listViewLayoutParams = listView.getLayoutParams();

        if (changeParams) {

            TextView groupText = (TextView) listView.findViewById(R.id.view_list_group_text);
            LinearLayout.LayoutParams groupTextLayoutParams = (LinearLayout.LayoutParams) groupText.getLayoutParams();

            if (listView.isGroupExpanded(0)) {

                final RelativeLayout parentLayout = (RelativeLayout) listView.getParent();
                final int listViewLayoutParamsHeight = totalHeight + getPixelFromDP(8) + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

                Animation expandAnimation = new Animation() {

                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                        FrameLayout.LayoutParams relativeLayoutParams = (FrameLayout.LayoutParams) parentLayout.getLayoutParams();
                        relativeLayoutParams.height = (int) (getPixelFromDP(36) + interpolatedTime * (listViewLayoutParamsHeight - getPixelFromDP(36)));

                        parentLayout.requestLayout();

                    }
                };

                expandAnimation.setDuration(100);
                parentLayout.startAnimation(expandAnimation);

                listViewLayoutParams.height = listViewLayoutParamsHeight;

                groupTextLayoutParams.setMargins(0, getPixelFromDP(8), 0, getPixelFromDP(16));
                groupText.setLayoutParams(groupTextLayoutParams);

            } else {

                final RelativeLayout parentLayout = (RelativeLayout) listView.getParent();
                final int listViewLayoutParamsHeight = listViewLayoutParams.height;

                Animation collapseAnimation = new Animation() {

                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                        FrameLayout.LayoutParams relativeLayoutParams = (FrameLayout.LayoutParams) parentLayout.getLayoutParams();
                        relativeLayoutParams.height = (int) (getPixelFromDP(36) + (1 - interpolatedTime) * (listViewLayoutParamsHeight - getPixelFromDP(36)));

                        parentLayout.requestLayout();

                    }
                };

                collapseAnimation.setDuration(100);
                parentLayout.startAnimation(collapseAnimation);

                listViewLayoutParams.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

                groupTextLayoutParams.setMargins(0, getPixelFromDP(8), 0, getPixelFromDP(8));
                groupText.setLayoutParams(groupTextLayoutParams);

            }

        } else {

            if (!listView.isGroupExpanded(0))
                listViewLayoutParams.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            else
                listViewLayoutParams.height = totalHeight + getPixelFromDP(8) + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        }

        listView.setLayoutParams(listViewLayoutParams);
        listView.requestLayout();

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

}
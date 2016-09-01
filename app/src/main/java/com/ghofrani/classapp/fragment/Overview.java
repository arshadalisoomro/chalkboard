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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ViewClass;
import com.ghofrani.classapp.adapter.ClassList;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.modules.DataStore;

import java.util.ArrayList;

public class Overview extends Fragment {

    private ClassList classListAdapterTomorrow;
    private ClassList classListAdapterNext;
    private ExpandableListView expandableListViewNextClasses;
    private ProgressBar progressBar;
    private TextView progressTextView;
    private TextView currentClassTitleTextView;
    private TextView currentClassLocationTeacherTextView;
    private TextView currentClassStartTimeTextView;
    private TextView currentClassEndTimeTextView;
    private ImageView currentClassColorIndicator;
    private BroadcastReceiver updateProgressBarBroadcastReceiver = new BroadcastReceiver() {

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
    private ArrayList<StandardClass> nextClassesArrayList;
    private ExpandableListView expandableListViewTomorrowClasses;
    private final BroadcastReceiver collapseExpandableListViewsBroadcastReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            getView().post(new Runnable() {

                @Override
                public void run() {

                    if (expandableListViewNextClasses != null) {

                        if (expandableListViewNextClasses.isGroupExpanded(0)) {

                            expandableListViewNextClasses.collapseGroup(0);

                            setListViewHeightBasedOnChildrenNext(true, false);

                        }

                    }

                    if (expandableListViewTomorrowClasses != null) {

                        if (expandableListViewTomorrowClasses.isGroupExpanded(0)) {

                            expandableListViewTomorrowClasses.collapseGroup(0);

                            setListViewHeightBasedOnChildrenTomorrow(true, false);

                        }

                    }

                }

            });

        }

    };
    private ArrayList<StandardClass> tomorrowClassesArrayList;
    private BroadcastReceiver updateUIBroadcastReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            getView().post(new Runnable() {

                @Override
                public void run() {

                    if (expandableListViewNextClasses != null) {

                        if (expandableListViewNextClasses.isGroupExpanded(0)) {

                            expandableListViewNextClasses.collapseGroup(0);

                            setListViewHeightBasedOnChildrenNext(true, true);

                        }

                    }

                    if (expandableListViewTomorrowClasses != null) {

                        if (expandableListViewTomorrowClasses.isGroupExpanded(0)) {

                            expandableListViewTomorrowClasses.collapseGroup(0);

                            setListViewHeightBasedOnChildrenTomorrow(true, true);

                        }

                    }

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
    public void onResume() {

        super.onResume();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(updateProgressBarBroadcastReceiver, new IntentFilter("update_progress_bar"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(updateUIBroadcastReceiver, new IntentFilter("update_UI"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(collapseExpandableListViewsBroadcastReceiver, new IntentFilter("collapse_lists"));

        updateUI();

    }

    @Override
    public void onStop() {

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(updateProgressBarBroadcastReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(updateUIBroadcastReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(collapseExpandableListViewsBroadcastReceiver);

        if (expandableListViewNextClasses != null) {

            if (expandableListViewNextClasses.isGroupExpanded(0)) {

                expandableListViewNextClasses.collapseGroup(0);

                setListViewHeightBasedOnChildrenNext(true, false);

            }

        }

        if (expandableListViewTomorrowClasses != null) {

            if (expandableListViewTomorrowClasses.isGroupExpanded(0)) {

                expandableListViewTomorrowClasses.collapseGroup(0);

                setListViewHeightBasedOnChildrenTomorrow(true, false);

            }

        }

        super.onStop();

    }

    @Override
    public void onDestroyView() {

        updateProgressBarBroadcastReceiver = null;
        updateUIBroadcastReceiver = null;
        expandableListViewNextClasses = null;
        nextClassesArrayList = null;
        classListAdapterNext = null;
        expandableListViewTomorrowClasses = null;
        tomorrowClassesArrayList = null;
        classListAdapterTomorrow = null;
        progressBar = null;
        progressTextView = null;
        currentClassTitleTextView = null;
        currentClassLocationTeacherTextView = null;
        currentClassStartTimeTextView = null;
        currentClassEndTimeTextView = null;
        currentClassColorIndicator = null;

        super.onDestroyView();

    }

    private void updateUI() {

        if (DataStore.isCurrentClass) {

            StandardClass currentClass = DataStore.currentClass;

            if (currentClassTitleTextView == null)
                currentClassTitleTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_title);

            currentClassTitleTextView.setText(currentClass.getName());

            if (currentClassLocationTeacherTextView == null)
                currentClassLocationTeacherTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_location_teacher);

            currentClassLocationTeacherTextView.setText(currentClass.getTeacher() + " • " + currentClass.getLocation());

            if (currentClassStartTimeTextView == null)
                currentClassStartTimeTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_start_time);

            currentClassStartTimeTextView.setText(currentClass.getStartTimeString(true));

            if (currentClassEndTimeTextView == null)
                currentClassEndTimeTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_end_time);

            currentClassEndTimeTextView.setText(currentClass.getEndTimeString(true));

            if (currentClassColorIndicator == null)
                currentClassColorIndicator = (ImageView) getView().findViewById(R.id.overview_current_class_card_class_color_indicator);

            currentClassColorIndicator.setColorFilter(currentClass.getColor());

            updateProgressBar();

        }

        if (DataStore.isNextClasses) {

            nextClassesArrayList = DataStore.nextClassesArrayList;

            configureExpandableListViewNextClasses();

        } else {

            expandableListViewNextClasses = null;

        }

        if (DataStore.isTomorrowClasses && PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("tomorrow_classes", true)) {

            tomorrowClassesArrayList = DataStore.tomorrowClassesArrayList;

            configureExpandableListViewTomorrowClasses();

        } else {

            expandableListViewTomorrowClasses = null;

        }

        setMarginsVisibility();

    }

    private void updateProgressBar() {

        if (progressBar == null)
            progressBar = (ProgressBar) getView().findViewById(R.id.overview_current_class_card_progress_bar);

        if (progressTextView == null)
            progressTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_progress_percentage);

        progressBar.setIndeterminate(false);
        progressBar.setProgress(DataStore.progressBarProgress);

        progressTextView.setText(DataStore.progressBarText);

    }

    private void setMarginsVisibility() {

        if (DataStore.isCurrentClass) {

            CardView noClassesCard = (CardView) getView().findViewById(R.id.overview_no_classes_card);
            noClassesCard.setVisibility(View.GONE);

            CardView currentClassCard = (CardView) getView().findViewById(R.id.overview_current_class_card);
            currentClassCard.setVisibility(View.VISIBLE);

            if (DataStore.isNextClasses) {

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

                if (DataStore.isTomorrowClasses && PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("tomorrow_classes", true)) {

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

                if (DataStore.isTomorrowClasses && PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("tomorrow_classes", true)) {

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

            if (DataStore.isNextClasses) {

                CardView noClasses = (CardView) getView().findViewById(R.id.overview_no_classes_card);
                noClasses.setVisibility(View.GONE);

                CardView nextClassesCard = (CardView) getView().findViewById(R.id.overview_next_classes_card);
                nextClassesCard.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams layoutParamsNCC;
                layoutParamsNCC = (RelativeLayout.LayoutParams) nextClassesCard.getLayoutParams();
                layoutParamsNCC.setMargins(getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16));
                nextClassesCard.setLayoutParams(layoutParamsNCC);

                if (DataStore.isTomorrowClasses && PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("tomorrow_classes", true)) {

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

                if (DataStore.isTomorrowClasses && PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("tomorrow_classes", true)) {

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

                    TextView noClassesText = (TextView) getView().findViewById(R.id.overview_no_classes_card_text);

                    if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("tomorrow_classes", true)) {

                        noClassesText.setText("No further classes today or tomorrow.");

                    } else {

                        noClassesText.setText("No further classes today.");

                    }

                }

            }

        }

    }

    private void configureExpandableListViewNextClasses() {

        if (expandableListViewNextClasses == null) {

            expandableListViewNextClasses = (ExpandableListView) getView().findViewById(R.id.overview_next_classes_list);

            expandableListViewNextClasses.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                public boolean onGroupClick(ExpandableListView expandableListViewGroupListener, View view, int groupPosition, long id) {

                    expandableListViewNextClasses = expandableListViewGroupListener;

                    if (!expandableListViewNextClasses.isGroupExpanded(0))
                        expandableListViewNextClasses.expandGroup(0);
                    else
                        expandableListViewNextClasses.collapseGroup(0);

                    setListViewHeightBasedOnChildrenNext(true, true);

                    return true;

                }

            });

            expandableListViewNextClasses.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                    TextView classNameTextView = (TextView) view.findViewById(R.id.view_list_child_text);
                    startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", classNameTextView.getText().toString()));

                    expandableListViewNextClasses.collapseGroup(0);
                    setListViewHeightBasedOnChildrenNext(true, false);

                    if (expandableListViewTomorrowClasses != null) {

                        if (expandableListViewTomorrowClasses.isGroupExpanded(0)) {

                            expandableListViewTomorrowClasses.collapseGroup(0);
                            setListViewHeightBasedOnChildrenTomorrow(true, false);

                        }

                    }

                    return false;

                }

            });

        }

        if (classListAdapterNext == null) {

            classListAdapterNext = new ClassList(getContext(), nextClassesArrayList, "Next classes");
            expandableListViewNextClasses.setAdapter(classListAdapterNext);

        } else {

            classListAdapterNext.updateLinkedList((ArrayList<StandardClass>) nextClassesArrayList.clone());

        }

        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("expand_next_classes", true)) {

            expandableListViewNextClasses.postDelayed(new Runnable() {

                @Override
                public void run() {

                    expandableListViewNextClasses.expandGroup(0);
                    setListViewHeightBasedOnChildrenNext(true, true);

                }

            }, 100);

        } else {

            setListViewHeightBasedOnChildrenNext(false, false);

        }

    }

    private void configureExpandableListViewTomorrowClasses() {

        if (expandableListViewTomorrowClasses == null) {

            expandableListViewTomorrowClasses = (ExpandableListView) getView().findViewById(R.id.overview_tomorrow_classes_list);

            expandableListViewTomorrowClasses.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                public boolean onGroupClick(ExpandableListView expandableListViewGroupListener, View view, int groupPosition, long id) {

                    expandableListViewTomorrowClasses = expandableListViewGroupListener;

                    if (!expandableListViewTomorrowClasses.isGroupExpanded(0))
                        expandableListViewTomorrowClasses.expandGroup(0);
                    else
                        expandableListViewTomorrowClasses.collapseGroup(0);

                    setListViewHeightBasedOnChildrenTomorrow(true, true);

                    return true;

                }

            });

            expandableListViewTomorrowClasses.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                    TextView classNameTextView = (TextView) view.findViewById(R.id.view_list_child_text);
                    startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", classNameTextView.getText().toString()));

                    expandableListViewTomorrowClasses.collapseGroup(0);
                    setListViewHeightBasedOnChildrenTomorrow(true, false);

                    if (expandableListViewNextClasses != null) {

                        if (expandableListViewNextClasses.isGroupExpanded(0)) {

                            expandableListViewNextClasses.collapseGroup(0);
                            setListViewHeightBasedOnChildrenNext(true, false);

                        }

                    }

                    return false;

                }

            });

        }

        if (classListAdapterTomorrow == null) {

            classListAdapterTomorrow = new ClassList(getContext(), tomorrowClassesArrayList, "Tomorrow's classes");
            expandableListViewTomorrowClasses.setAdapter(classListAdapterTomorrow);

        } else {

            classListAdapterTomorrow.updateLinkedList((ArrayList<StandardClass>) tomorrowClassesArrayList.clone());

        }

        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("expand_tomorrow_classes", false)) {

            expandableListViewTomorrowClasses.postDelayed(new Runnable() {

                @Override
                public void run() {

                    expandableListViewTomorrowClasses.expandGroup(0);
                    setListViewHeightBasedOnChildrenTomorrow(true, true);

                }

            }, 100);

        } else {

            setListViewHeightBasedOnChildrenTomorrow(false, false);

        }

    }

    private void setListViewHeightBasedOnChildrenNext(boolean changeParams, boolean animate) {

        ListAdapter listAdapter = expandableListViewNextClasses.getAdapter();

        if (listAdapter == null)
            return;

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, expandableListViewNextClasses);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        final ViewGroup.LayoutParams listViewLayoutParams = expandableListViewNextClasses.getLayoutParams();

        if (changeParams) {

            TextView groupText = (TextView) expandableListViewNextClasses.findViewById(R.id.view_list_group_text);
            LinearLayout.LayoutParams groupTextLayoutParams = (LinearLayout.LayoutParams) groupText.getLayoutParams();

            if (expandableListViewNextClasses.isGroupExpanded(0)) {

                final RelativeLayout parentLayout = (RelativeLayout) expandableListViewNextClasses.getParent();
                final int listViewLayoutParamsHeight = totalHeight + getPixelFromDP(8) + (expandableListViewNextClasses.getDividerHeight() * (listAdapter.getCount() - 1));
                final FrameLayout.LayoutParams relativeLayoutParams = (FrameLayout.LayoutParams) parentLayout.getLayoutParams();

                if (animate) {

                    Animation expandAnimation = new Animation() {

                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {

                            relativeLayoutParams.height = (int) (getPixelFromDP(36) + interpolatedTime * (listViewLayoutParamsHeight - getPixelFromDP(36)));

                            parentLayout.requestLayout();

                        }
                    };

                    expandAnimation.setDuration(100);
                    parentLayout.startAnimation(expandAnimation);

                } else {

                    relativeLayoutParams.height = listViewLayoutParamsHeight;

                    parentLayout.requestLayout();

                }

                listViewLayoutParams.height = listViewLayoutParamsHeight;

                groupTextLayoutParams.setMargins(0, getPixelFromDP(8), 0, getPixelFromDP(16));
                groupText.setLayoutParams(groupTextLayoutParams);

            } else {

                final RelativeLayout parentLayout = (RelativeLayout) expandableListViewNextClasses.getParent();
                final int listViewLayoutParamsHeight = listViewLayoutParams.height;
                final FrameLayout.LayoutParams relativeLayoutParams = (FrameLayout.LayoutParams) parentLayout.getLayoutParams();

                if (animate) {

                    Animation collapseAnimation = new Animation() {

                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {

                            relativeLayoutParams.height = (int) (getPixelFromDP(36) + (1 - interpolatedTime) * (listViewLayoutParamsHeight - getPixelFromDP(36)));

                            parentLayout.requestLayout();

                        }
                    };

                    collapseAnimation.setDuration(100);
                    parentLayout.startAnimation(collapseAnimation);

                } else {

                    relativeLayoutParams.height = getPixelFromDP(36);

                    parentLayout.requestLayout();

                }

                listViewLayoutParams.height = totalHeight + (expandableListViewNextClasses.getDividerHeight() * (listAdapter.getCount() - 1));

                groupTextLayoutParams.setMargins(0, getPixelFromDP(8), 0, getPixelFromDP(8));
                groupText.setLayoutParams(groupTextLayoutParams);

            }

        } else {

            if (!expandableListViewNextClasses.isGroupExpanded(0))
                listViewLayoutParams.height = totalHeight + (expandableListViewNextClasses.getDividerHeight() * (listAdapter.getCount() - 1));
            else
                listViewLayoutParams.height = totalHeight + getPixelFromDP(8) + (expandableListViewNextClasses.getDividerHeight() * (listAdapter.getCount() - 1));

        }

        expandableListViewNextClasses.setLayoutParams(listViewLayoutParams);
        expandableListViewNextClasses.requestLayout();

    }

    private void setListViewHeightBasedOnChildrenTomorrow(boolean changeParams, boolean animate) {

        ListAdapter listAdapter = expandableListViewTomorrowClasses.getAdapter();

        if (listAdapter == null)
            return;

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, expandableListViewTomorrowClasses);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        final ViewGroup.LayoutParams listViewLayoutParams = expandableListViewTomorrowClasses.getLayoutParams();

        if (changeParams) {

            TextView groupText = (TextView) expandableListViewTomorrowClasses.findViewById(R.id.view_list_group_text);
            LinearLayout.LayoutParams groupTextLayoutParams = (LinearLayout.LayoutParams) groupText.getLayoutParams();

            if (expandableListViewTomorrowClasses.isGroupExpanded(0)) {

                final RelativeLayout parentLayout = (RelativeLayout) expandableListViewTomorrowClasses.getParent();
                final int listViewLayoutParamsHeight = totalHeight + getPixelFromDP(8) + (expandableListViewTomorrowClasses.getDividerHeight() * (listAdapter.getCount() - 1));
                final FrameLayout.LayoutParams relativeLayoutParams = (FrameLayout.LayoutParams) parentLayout.getLayoutParams();

                if (animate) {

                    Animation expandAnimation = new Animation() {

                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {

                            relativeLayoutParams.height = (int) (getPixelFromDP(36) + interpolatedTime * (listViewLayoutParamsHeight - getPixelFromDP(36)));

                            parentLayout.requestLayout();

                        }
                    };

                    expandAnimation.setDuration(100);
                    parentLayout.startAnimation(expandAnimation);

                } else {

                    relativeLayoutParams.height = listViewLayoutParamsHeight;

                    parentLayout.requestLayout();

                }

                listViewLayoutParams.height = listViewLayoutParamsHeight;

                groupTextLayoutParams.setMargins(0, getPixelFromDP(8), 0, getPixelFromDP(16));
                groupText.setLayoutParams(groupTextLayoutParams);

            } else {

                final RelativeLayout parentLayout = (RelativeLayout) expandableListViewTomorrowClasses.getParent();
                final int listViewLayoutParamsHeight = listViewLayoutParams.height;
                final FrameLayout.LayoutParams relativeLayoutParams = (FrameLayout.LayoutParams) parentLayout.getLayoutParams();

                if (animate) {

                    Animation collapseAnimation = new Animation() {

                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {

                            relativeLayoutParams.height = (int) (getPixelFromDP(36) + (1 - interpolatedTime) * (listViewLayoutParamsHeight - getPixelFromDP(36)));

                            parentLayout.requestLayout();

                        }
                    };

                    collapseAnimation.setDuration(100);
                    parentLayout.startAnimation(collapseAnimation);

                } else {

                    relativeLayoutParams.height = getPixelFromDP(36);

                    parentLayout.requestLayout();

                }

                listViewLayoutParams.height = totalHeight + (expandableListViewTomorrowClasses.getDividerHeight() * (listAdapter.getCount() - 1));

                groupTextLayoutParams.setMargins(0, getPixelFromDP(8), 0, getPixelFromDP(8));
                groupText.setLayoutParams(groupTextLayoutParams);

            }

        } else {

            if (!expandableListViewTomorrowClasses.isGroupExpanded(0))
                listViewLayoutParams.height = totalHeight + (expandableListViewTomorrowClasses.getDividerHeight() * (listAdapter.getCount() - 1));
            else
                listViewLayoutParams.height = totalHeight + getPixelFromDP(8) + (expandableListViewTomorrowClasses.getDividerHeight() * (listAdapter.getCount() - 1));

        }

        expandableListViewTomorrowClasses.setLayoutParams(listViewLayoutParams);
        expandableListViewTomorrowClasses.requestLayout();

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

}
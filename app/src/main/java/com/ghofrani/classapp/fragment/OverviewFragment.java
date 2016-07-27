package com.ghofrani.classapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
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
import com.ghofrani.classapp.adapter.ExpandableListAdapter;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OverviewFragment extends Fragment {

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
    private ExpandableListView expandableListViewNC;
    private HashMap<String, List<String>> nextClassesHM;
    private ExpandableListView expandableListViewTC;
    private HashMap<String, List<String>> tomorrowClassesHM;
    private boolean currentClass = false;
    private boolean nextClasses = false;
    private boolean tomorrowClasses = false;
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

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateProgressBarReceiver, new IntentFilter("updateProgressBar"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateUI, new IntentFilter("updateUI"));

        updateUI();

    }

    @Override
    public void onStop() {

        super.onStop();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateProgressBarReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateUI);

    }

    private void updateUI() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        currentClass = DataStore.getCurrentClass();

        if (DataStore.getNextClasses()) {

            nextClasses = DataStore.getNextClasses();
            nextClassesHM = DataStore.getNextClassesHM();

        }

        if (DataStore.getTomorrowClasses() && sharedPreferences.getBoolean("tomorrowClasses", true)) {

            tomorrowClasses = DataStore.getTomorrowClasses();
            tomorrowClassesHM = DataStore.getTomorrowClassesHM();

        }

        if (currentClass) {

            String[] currentClassInfo = DataStore.getCurrentClassInfo();

            TextView currentClassTitle = (TextView) getView().findViewById(R.id.current_class_title);
            currentClassTitle.setText(currentClassInfo[0]);

            TextView currentClassLocationTeacher = (TextView) getView().findViewById(R.id.current_class_location_teacher);
            String locationTeacher = new DatabaseHelper(getActivity()).getClassTeacher(currentClassInfo[0]) + ", " + currentClassInfo[1];
            currentClassLocationTeacher.setText(locationTeacher);

            TextView currentClassStartTime = (TextView) getView().findViewById(R.id.current_class_start_time);
            currentClassStartTime.setText(currentClassInfo[2]);

            TextView currentClassEndTime = (TextView) getView().findViewById(R.id.current_class_end_time);
            currentClassEndTime.setText(currentClassInfo[3]);

            updateProgressBarCache();

        }

        if (nextClasses) {

            configureExpandableListViewNC();
            expandableListViewNC.post(new Runnable() {

                @Override
                public void run() {

                    expandableListViewNC.setIndicatorBoundsRelative(expandableListViewNC.getRight() - getPixelFromDP(32), expandableListViewNC.getWidth());

                }

            });

        }

        if (tomorrowClasses) {

            configureExpandableListViewTC();
            expandableListViewTC.post(new Runnable() {

                @Override
                public void run() {

                    expandableListViewTC.setIndicatorBoundsRelative(expandableListViewTC.getRight() - getPixelFromDP(32), expandableListViewTC.getWidth());


                }

            });

        }

        setMarginsVisibility();

    }

    private void updateProgressBar() {

        final ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.current_class_progress);
        progressBar.setIndeterminate(false);

        TextView progressTextView = (TextView) getView().findViewById(R.id.current_class_progress_percentage);
        progressTextView.setText(DataStore.getProgressBarText());

        progressBar.setProgress(DataStore.getProgressBarProgress());

    }

    private void updateProgressBarCache() {

        final ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.current_class_progress);
        progressBar.setIndeterminate(false);

        TextView progressTextView = (TextView) getView().findViewById(R.id.current_class_progress_percentage);
        progressTextView.setText(DataStore.getProgressBarText());

        progressBar.setProgress(DataStore.getProgressBarProgress());

    }

    private void setMarginsVisibility() {

        if (currentClass) {

            CardView noClasses = (CardView) getView().findViewById(R.id.no_classes);
            noClasses.setVisibility(View.GONE);

            CardView currentClassCard = (CardView) getView().findViewById(R.id.current_class_card);
            currentClassCard.setVisibility(View.VISIBLE);

            if (nextClasses) {

                RelativeLayout.LayoutParams layoutParamsCCC;
                layoutParamsCCC = (RelativeLayout.LayoutParams) currentClassCard.getLayoutParams();
                layoutParamsCCC.setMargins(getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(4));
                currentClassCard.setLayoutParams(layoutParamsCCC);

                CardView nextClassesCard = (CardView) getView().findViewById(R.id.next_classes_card);
                nextClassesCard.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams layoutParamsNCC;
                layoutParamsNCC = (RelativeLayout.LayoutParams) nextClassesCard.getLayoutParams();
                layoutParamsNCC.setMargins(getPixelFromDP(16), getPixelFromDP(4), getPixelFromDP(16), getPixelFromDP(16));
                nextClassesCard.setLayoutParams(layoutParamsNCC);

                if (tomorrowClasses) {

                    layoutParamsNCC.setMargins(getPixelFromDP(16), getPixelFromDP(4), getPixelFromDP(16), getPixelFromDP(4));
                    nextClassesCard.setLayoutParams(layoutParamsNCC);

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams layoutParamsTCC;
                    layoutParamsTCC = (RelativeLayout.LayoutParams) tomorrowClassesCard.getLayoutParams();
                    layoutParamsTCC.setMargins(getPixelFromDP(16), getPixelFromDP(4), getPixelFromDP(16), getPixelFromDP(16));
                    tomorrowClassesCard.setLayoutParams(layoutParamsTCC);

                } else {

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.GONE);

                }

            } else {

                CardView nextClassesCard = (CardView) getView().findViewById(R.id.next_classes_card);
                nextClassesCard.setVisibility(View.GONE);

                if (tomorrowClasses) {

                    RelativeLayout.LayoutParams layoutParamsCCC;
                    layoutParamsCCC = (RelativeLayout.LayoutParams) currentClassCard.getLayoutParams();
                    layoutParamsCCC.setMargins(getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(4));
                    currentClassCard.setLayoutParams(layoutParamsCCC);

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams layoutParamsTCC;
                    layoutParamsTCC = (RelativeLayout.LayoutParams) tomorrowClassesCard.getLayoutParams();
                    layoutParamsTCC.setMargins(getPixelFromDP(16), getPixelFromDP(4), getPixelFromDP(16), getPixelFromDP(16));
                    tomorrowClassesCard.setLayoutParams(layoutParamsTCC);

                } else {

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.GONE);

                }

            }

        } else {

            CardView currentClassCard = (CardView) getView().findViewById(R.id.current_class_card);
            currentClassCard.setVisibility(View.GONE);

            if (nextClasses) {

                CardView noClasses = (CardView) getView().findViewById(R.id.no_classes);
                noClasses.setVisibility(View.GONE);

                CardView nextClassesCard = (CardView) getView().findViewById(R.id.next_classes_card);
                nextClassesCard.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams layoutParamsNCC;
                layoutParamsNCC = (RelativeLayout.LayoutParams) nextClassesCard.getLayoutParams();
                layoutParamsNCC.setMargins(getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16));
                nextClassesCard.setLayoutParams(layoutParamsNCC);

                if (tomorrowClasses) {

                    layoutParamsNCC.setMargins(getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(4));
                    nextClassesCard.setLayoutParams(layoutParamsNCC);

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams layoutParamsTCC;
                    layoutParamsTCC = (RelativeLayout.LayoutParams) tomorrowClassesCard.getLayoutParams();
                    layoutParamsTCC.setMargins(getPixelFromDP(16), getPixelFromDP(4), getPixelFromDP(16), getPixelFromDP(16));
                    tomorrowClassesCard.setLayoutParams(layoutParamsTCC);

                } else {

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.GONE);

                }

            } else {

                CardView nextClassesCard = (CardView) getView().findViewById(R.id.next_classes_card);
                nextClassesCard.setVisibility(View.GONE);

                if (tomorrowClasses) {

                    CardView noClasses = (CardView) getView().findViewById(R.id.no_classes);
                    noClasses.setVisibility(View.GONE);

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams layoutParamsTCC;
                    layoutParamsTCC = (RelativeLayout.LayoutParams) tomorrowClassesCard.getLayoutParams();
                    layoutParamsTCC.setMargins(getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16), getPixelFromDP(16));
                    tomorrowClassesCard.setLayoutParams(layoutParamsTCC);

                } else {

                    CardView tomorrowClassesCard = (CardView) getView().findViewById(R.id.tomorrow_classes_card);
                    tomorrowClassesCard.setVisibility(View.GONE);

                    CardView noClasses = (CardView) getView().findViewById(R.id.no_classes);
                    noClasses.setVisibility(View.VISIBLE);

                }

            }

        }

    }

    private void configureExpandableListViewNC() {

        expandableListViewNC = (ExpandableListView) getView().findViewById(R.id.upcoming_classes_list);

        List<String> nextClasses = new ArrayList<>(nextClassesHM.keySet());

        ExpandableListAdapter expandableListAdapter;
        expandableListAdapter = new ExpandableListAdapter(getActivity(), nextClassesHM, nextClasses);
        expandableListViewNC.setAdapter(expandableListAdapter);

        setListViewHeightBasedOnChildren(expandableListViewNC, false);

        expandableListViewNC.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            public boolean onGroupClick(ExpandableListView expandableListViewGL, View view, int groupPosition, long id) {

                expandableListViewNC = expandableListViewGL;

                if (!expandableListViewNC.isGroupExpanded(0))
                    expandableListViewNC.expandGroup(0);
                else
                    expandableListViewNC.collapseGroup(0);

                setListViewHeightBasedOnChildren(expandableListViewNC, true);

                return true;

            }

        });

    }

    private void configureExpandableListViewTC() {

        expandableListViewTC = (ExpandableListView) getView().findViewById(R.id.tomorrow_classes_list);

        List<String> tomorrowClasses = new ArrayList<>(tomorrowClassesHM.keySet());

        ExpandableListAdapter expandableListAdapter;
        expandableListAdapter = new ExpandableListAdapter(getActivity(), tomorrowClassesHM, tomorrowClasses);
        expandableListViewTC.setAdapter(expandableListAdapter);

        setListViewHeightBasedOnChildren(expandableListViewTC, false);

        expandableListViewTC.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            public boolean onGroupClick(ExpandableListView expandableListViewGL, View view, int groupPosition, long id) {

                expandableListViewTC = expandableListViewGL;

                if (!expandableListViewTC.isGroupExpanded(0))
                    expandableListViewTC.expandGroup(0);
                else
                    expandableListViewTC.collapseGroup(0);

                setListViewHeightBasedOnChildren(expandableListViewTC, true);

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

            TextView groupText = (TextView) listView.findViewById(R.id.list_group_text);
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
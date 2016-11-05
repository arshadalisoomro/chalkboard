package com.ghofrani.classapp.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import com.ghofrani.classapp.event.CollapseLists;
import com.ghofrani.classapp.event.UpdateClassesUI;
import com.ghofrani.classapp.event.UpdateProgressUI;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.module.DataSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class Overview extends Fragment {

    private final int EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE = 25;
    private final int EXPANDABLE_LIST_VIEW_HEIGHT = 36;

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
    private CardView currentClassCardView;
    private ExpandableListView expandableListViewTomorrowClasses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_overview, container, false);

    }

    @Subscribe
    public void OnEvent(UpdateProgressUI updateProgressUIEvent) {

        updateProgressBar();

    }

    @Subscribe
    public void onEvent(CollapseLists collapseListsEvent) {

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

    @Subscribe
    public void onEvent(UpdateClassesUI updateClassesUIEvent) {

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

    @Override
    public void onResume() {

        super.onResume();

        if (currentClassCardView == null)
            currentClassCardView = (CardView) getView().findViewById(R.id.overview_current_class_card);

        if (currentClassTitleTextView == null)
            currentClassTitleTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_title);

        if (currentClassLocationTeacherTextView == null)
            currentClassLocationTeacherTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_location_teacher);

        if (currentClassColorIndicator == null)
            currentClassColorIndicator = (ImageView) getView().findViewById(R.id.overview_current_class_card_class_color_indicator);

        if (currentClassStartTimeTextView == null)
            currentClassStartTimeTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_start_time);

        if (currentClassEndTimeTextView == null)
            currentClassEndTimeTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_end_time);

        if (progressBar == null)
            progressBar = (ProgressBar) getView().findViewById(R.id.overview_current_class_card_progress_bar);

        if (progressTextView == null)
            progressTextView = (TextView) getView().findViewById(R.id.overview_current_class_card_progress_percentage);

        updateUI();

        EventBus.getDefault().register(this);

    }

    @Override
    public void onPause() {

        EventBus.getDefault().unregister(this);

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

        super.onPause();

    }

    @Override
    public void onDestroyView() {

        expandableListViewNextClasses = null;
        classListAdapterNext = null;
        expandableListViewTomorrowClasses = null;
        classListAdapterTomorrow = null;
        progressBar = null;
        progressTextView = null;
        currentClassTitleTextView = null;
        currentClassLocationTeacherTextView = null;
        currentClassStartTimeTextView = null;
        currentClassEndTimeTextView = null;
        currentClassColorIndicator = null;
        currentClassCardView = null;

        super.onDestroyView();

    }

    private void updateUI() {

        int nullCount = 0;

        if (DataSingleton.getInstance().getCurrentClass() != null) {

            if (!currentClassCardView.hasOnClickListeners()) {

                currentClassCardView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        final TextView titleTextView = (TextView) view.findViewById(R.id.overview_current_class_card_title);

                        startActivity(new Intent(Overview.this.getContext(), ViewClass.class).putExtra("class", titleTextView.getText()));

                    }

                });

            }

            currentClassCardView.setVisibility(View.VISIBLE);

            StandardClass currentClass = DataSingleton.getInstance().getCurrentClass();

            currentClassTitleTextView.setText(currentClass.getName());

            if (currentClass.hasLocation()) {

                if (currentClass.hasTeacher())
                    currentClassLocationTeacherTextView.setText(currentClass.getLocation() + " • " + currentClass.getTeacher() + " • " + DataSingleton.getInstance().getMinutesLeftText());
                else
                    currentClassLocationTeacherTextView.setText(currentClass.getLocation() + " • " + DataSingleton.getInstance().getMinutesLeftText());

                currentClassColorIndicator.setTranslationY(getPixelFromDP(-1.5f));

            } else if (currentClass.hasTeacher()) {

                currentClassLocationTeacherTextView.setText(currentClass.getTeacher() + " • " + DataSingleton.getInstance().getMinutesLeftText());

                currentClassColorIndicator.setTranslationY(getPixelFromDP(-1.5f));

            } else {

                currentClassLocationTeacherTextView.setText(DataSingleton.getInstance().getMinutesLeftText());

                currentClassColorIndicator.setTranslationY(getPixelFromDP(0));

            }

            currentClassStartTimeTextView.setText(currentClass.getStartTimeString(true));
            currentClassEndTimeTextView.setText(currentClass.getEndTimeString(true));

            currentClassColorIndicator.setColorFilter(currentClass.getColor());

            progressBar.setProgressTintList(ColorStateList.valueOf(currentClass.getColor()));

            updateProgressBar();

        } else {

            getView().findViewById(R.id.overview_current_class_card).setVisibility(View.GONE);

            nullCount++;

        }

        if (DataSingleton.getInstance().getNextClass() != null) {

            configureExpandableListViewNextClasses();

            getView().findViewById(R.id.overview_next_classes_card).setVisibility(View.VISIBLE);

        } else {

            classListAdapterNext = null;
            expandableListViewNextClasses = null;

            getView().findViewById(R.id.overview_next_classes_card).setVisibility(View.GONE);

            nullCount++;

        }

        if (!DataSingleton.getInstance().getTomorrowClassesArrayList().isEmpty()) {

            configureExpandableListViewTomorrowClasses();

            getView().findViewById(R.id.overview_tomorrow_classes_card).setVisibility(View.VISIBLE);

        } else {

            classListAdapterTomorrow = null;
            expandableListViewTomorrowClasses = null;

            getView().findViewById(R.id.overview_tomorrow_classes_card).setVisibility(View.GONE);

            nullCount++;

        }

        if (nullCount == 3) {

            getView().findViewById(R.id.overview_no_classes_card).setVisibility(View.VISIBLE);

            if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("tomorrow_classes", true))
                ((TextView) getView().findViewById(R.id.overview_no_classes_card_text)).setText("No further classes today or tomorrow.");
            else
                ((TextView) getView().findViewById(R.id.overview_no_classes_card_text)).setText("No further classes today.");

        } else {

            getView().findViewById(R.id.overview_no_classes_card).setVisibility(View.GONE);

        }

    }

    private void updateProgressBar() {

        progressBar.setIndeterminate(false);
        progressBar.setProgress(DataSingleton.getInstance().getProgressBarProgress());

        progressTextView.setText(DataSingleton.getInstance().getProgressBarText());

    }

    private void configureExpandableListViewNextClasses() {

        if (expandableListViewNextClasses == null) {

            expandableListViewNextClasses = (ExpandableListView) getView().findViewById(R.id.overview_next_classes_list);

            expandableListViewNextClasses.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                public boolean onGroupClick(ExpandableListView expandableListViewGroupListener, View view, int groupPosition, long id) {

                    expandableListViewNextClasses = expandableListViewGroupListener;

                    if (!expandableListViewNextClasses.isGroupExpanded(0)) {

                        expandableListViewNextClasses.expandGroup(0);

                    } else {

                        DataSingleton.getInstance().setIsExpandableListViewCollapsed(true);

                        expandableListViewNextClasses.collapseGroup(0);

                    }

                    setListViewHeightBasedOnChildrenNext(true, true);

                    return true;

                }

            });

            expandableListViewNextClasses.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                    final TextView classNameTextView = (TextView) view.findViewById(R.id.view_list_child_text);
                    final TextView classNameTextViewCentered = (TextView) view.findViewById(R.id.view_list_child_text_centered);

                    if (DataSingleton.getInstance().getNextClassesArrayList().get(childPosition).hasLocation())
                        startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", classNameTextView.getText().toString()));
                    else if (DataSingleton.getInstance().getNextClassesArrayList().get(childPosition).hasTeacher())
                        startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", classNameTextView.getText().toString()));
                    else
                        startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", classNameTextViewCentered.getText().toString()));

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

            classListAdapterNext = new ClassList(getContext(), DataSingleton.getInstance().getNextClassesArrayList(), "Next classes");
            expandableListViewNextClasses.setAdapter(classListAdapterNext);

        } else {

            classListAdapterNext.updateLinkedList((ArrayList<StandardClass>) DataSingleton.getInstance().getNextClassesArrayList().clone());

        }

        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("expand_next_classes", true) && !DataSingleton.getInstance().isExpandableListViewCollapsed()) {

            expandableListViewNextClasses.postDelayed(new Runnable() {

                @Override
                public void run() {

                    if (expandableListViewNextClasses != null) {

                        expandableListViewNextClasses.expandGroup(0);
                        setListViewHeightBasedOnChildrenNext(true, true);

                    }

                }

            }, 200);

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

                    if (!expandableListViewTomorrowClasses.isGroupExpanded(0)) {

                        expandableListViewTomorrowClasses.expandGroup(0);

                    } else {

                        DataSingleton.getInstance().setIsExpandableListViewCollapsed(true);

                        expandableListViewTomorrowClasses.collapseGroup(0);

                    }

                    setListViewHeightBasedOnChildrenTomorrow(true, true);

                    return true;

                }

            });

            expandableListViewTomorrowClasses.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                    final TextView classNameTextView = (TextView) view.findViewById(R.id.view_list_child_text);
                    final TextView classNameTextViewCentered = (TextView) view.findViewById(R.id.view_list_child_text_centered);

                    if (DataSingleton.getInstance().getTomorrowClassesArrayList().get(childPosition).hasLocation())
                        startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", classNameTextView.getText().toString()));
                    else if (DataSingleton.getInstance().getTomorrowClassesArrayList().get(childPosition).hasTeacher())
                        startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", classNameTextView.getText().toString()));
                    else
                        startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", classNameTextViewCentered.getText().toString()));

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

            classListAdapterTomorrow = new ClassList(getContext(), DataSingleton.getInstance().getTomorrowClassesArrayList(), "Tomorrow's classes");
            expandableListViewTomorrowClasses.setAdapter(classListAdapterTomorrow);

        } else {

            classListAdapterTomorrow.updateLinkedList((ArrayList<StandardClass>) DataSingleton.getInstance().getTomorrowClassesArrayList().clone());

        }

        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("expand_tomorrow_classes", false) && !DataSingleton.getInstance().isExpandableListViewCollapsed()) {

            expandableListViewTomorrowClasses.postDelayed(new Runnable() {

                @Override
                public void run() {

                    if (expandableListViewTomorrowClasses != null) {

                        expandableListViewTomorrowClasses.expandGroup(0);

                        setListViewHeightBasedOnChildrenTomorrow(true, true);

                    }

                }

            }, 200);

        } else {

            setListViewHeightBasedOnChildrenTomorrow(false, false);

        }

    }

    private void setListViewHeightBasedOnChildrenNext(boolean changeParams, boolean animate) {

        final ListAdapter listAdapter = expandableListViewNextClasses.getAdapter();

        if (listAdapter == null)
            return;

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            final View listItem = listAdapter.getView(i, null, expandableListViewNextClasses);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        final ViewGroup.LayoutParams listViewLayoutParams = expandableListViewNextClasses.getLayoutParams();

        if (changeParams) {

            final TextView groupText = (TextView) expandableListViewNextClasses.findViewById(R.id.view_list_group_text);
            final LinearLayout.LayoutParams groupTextLayoutParams = (LinearLayout.LayoutParams) groupText.getLayoutParams();

            if (expandableListViewNextClasses.isGroupExpanded(0)) {

                final RelativeLayout parentLayout = (RelativeLayout) expandableListViewNextClasses.getParent();
                final int listViewLayoutParamsHeight = totalHeight + getPixelFromDP(8) + (expandableListViewNextClasses.getDividerHeight() * (listAdapter.getCount() - 1));
                final FrameLayout.LayoutParams relativeLayoutParams = (FrameLayout.LayoutParams) parentLayout.getLayoutParams();

                if (animate) {

                    Animation expandAnimation = new Animation() {

                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {

                            relativeLayoutParams.height = (int) (getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT) + interpolatedTime * (listViewLayoutParamsHeight - getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT)));

                            parentLayout.requestLayout();

                        }
                    };

                    expandAnimation.setDuration(EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * DataSingleton.getInstance().getNextClassesArrayList().size() > 100 ? EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * DataSingleton.getInstance().getNextClassesArrayList().size() : 100);
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

                            relativeLayoutParams.height = (int) (getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT) + (1 - interpolatedTime) * (listViewLayoutParamsHeight - getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT)));

                            parentLayout.requestLayout();

                        }
                    };

                    collapseAnimation.setDuration(EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * DataSingleton.getInstance().getNextClassesArrayList().size() > 100 ? EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * DataSingleton.getInstance().getNextClassesArrayList().size() : 100);
                    parentLayout.startAnimation(collapseAnimation);

                } else {

                    relativeLayoutParams.height = getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT);

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

        final ListAdapter listAdapter = expandableListViewTomorrowClasses.getAdapter();

        if (listAdapter == null)
            return;

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            final View listItem = listAdapter.getView(i, null, expandableListViewTomorrowClasses);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        final ViewGroup.LayoutParams listViewLayoutParams = expandableListViewTomorrowClasses.getLayoutParams();

        if (changeParams) {

            final TextView groupText = (TextView) expandableListViewTomorrowClasses.findViewById(R.id.view_list_group_text);
            final LinearLayout.LayoutParams groupTextLayoutParams = (LinearLayout.LayoutParams) groupText.getLayoutParams();

            if (expandableListViewTomorrowClasses.isGroupExpanded(0)) {

                final RelativeLayout parentLayout = (RelativeLayout) expandableListViewTomorrowClasses.getParent();
                final int listViewLayoutParamsHeight = totalHeight + getPixelFromDP(8) + (expandableListViewTomorrowClasses.getDividerHeight() * (listAdapter.getCount() - 1));
                final FrameLayout.LayoutParams relativeLayoutParams = (FrameLayout.LayoutParams) parentLayout.getLayoutParams();

                if (animate) {

                    Animation expandAnimation = new Animation() {

                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {

                            relativeLayoutParams.height = (int) (getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT) + interpolatedTime * (listViewLayoutParamsHeight - getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT)));

                            parentLayout.requestLayout();

                        }
                    };

                    expandAnimation.setDuration(EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * DataSingleton.getInstance().getTomorrowClassesArrayList().size() > 100 ? EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * DataSingleton.getInstance().getTomorrowClassesArrayList().size() : 100);
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

                            relativeLayoutParams.height = (int) (getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT) + (1 - interpolatedTime) * (listViewLayoutParamsHeight - getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT)));

                            parentLayout.requestLayout();

                        }
                    };

                    collapseAnimation.setDuration(EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * DataSingleton.getInstance().getTomorrowClassesArrayList().size() > 100 ? EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * DataSingleton.getInstance().getTomorrowClassesArrayList().size() : 100);
                    parentLayout.startAnimation(collapseAnimation);

                } else {

                    relativeLayoutParams.height = getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT);

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
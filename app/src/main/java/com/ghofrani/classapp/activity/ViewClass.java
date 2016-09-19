package com.ghofrani.classapp.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.ClassList;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.event.UpdateClassesUI;
import com.ghofrani.classapp.event.UpdateProgressUI;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.DatabaseHelper;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class ViewClass extends AppCompatActivity {

    private final int EXPANDABLE_LIST_VIEW_ANIMATION_DURATION = 100;
    private final int EXPANDABLE_LIST_VIEW_HEIGHT = 36;
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
    private ArrayList<StandardClass> nextClassesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.view_class_toolbar);
        toolbar.setTitle(getIntent().getStringExtra("class"));
        toolbar.setElevation(getPixelFromDP(4));
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Subscribe
    public void OnEvent(UpdateProgressUI updateProgressUIEvent) {

        updateProgressBar();

    }

    @Subscribe
    public void onEvent(UpdateClassesUI updateClassesUIEvent) {

        updateUI();

    }

    @Override
    protected void onResume() {

        super.onResume();

        updateUI();

        EventBus.getDefault().register(this);

    }

    @Override
    protected void onPause() {

        EventBus.getDefault().unregister(this);

        if (expandableListViewNextClasses != null) {

            if (expandableListViewNextClasses.isGroupExpanded(0)) {

                expandableListViewNextClasses.collapseGroup(0);

                setListViewHeightBasedOnChildrenNext(true, false);

            }

        }

        super.onPause();

    }

    private void updateUI() {

        int nullCount = 0;

        if (DataSingleton.getInstance().getCurrentClass() != null) {

            if (DataSingleton.getInstance().getCurrentClass().getName().equals(getIntent().getStringExtra("class"))) {

                if (currentClassCardView == null)
                    currentClassCardView = (CardView) findViewById(R.id.activity_view_classes_current_class_card);

                currentClassCardView.setVisibility(View.VISIBLE);

                StandardClass currentClass = DataSingleton.getInstance().getCurrentClass();

                if (currentClassTitleTextView == null)
                    currentClassTitleTextView = (TextView) findViewById(R.id.activity_view_classes_current_class_card_title);

                currentClassTitleTextView.setText(currentClass.getName());

                if (currentClassLocationTeacherTextView == null)
                    currentClassLocationTeacherTextView = (TextView) findViewById(R.id.activity_view_classes_current_class_card_teacher_location);

                if (currentClassColorIndicator == null)
                    currentClassColorIndicator = (ImageView) findViewById(R.id.activity_view_classes_current_class_card_class_color_indicator);

                if (currentClass.hasLocation()) {

                    if (currentClass.hasTeacher())
                        currentClassLocationTeacherTextView.setText(currentClass.getTeacher() + " • " + currentClass.getLocation() + " • " + DataSingleton.getInstance().getMinutesLeftText());
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

                if (currentClassStartTimeTextView == null)
                    currentClassStartTimeTextView = (TextView) findViewById(R.id.activity_view_classes_current_class_card_start_time);

                currentClassStartTimeTextView.setText(currentClass.getStartTimeString(true));

                if (currentClassEndTimeTextView == null)
                    currentClassEndTimeTextView = (TextView) findViewById(R.id.activity_view_classes_current_class_card_end_time);

                currentClassEndTimeTextView.setText(currentClass.getEndTimeString(true));

                currentClassColorIndicator.setColorFilter(currentClass.getColor());

                if (progressBar == null)
                    progressBar = (ProgressBar) findViewById(R.id.activity_view_classes_current_class_card_progress_bar);

                progressBar.setProgressTintList(ColorStateList.valueOf(currentClass.getColor()));

                updateProgressBar();

            } else {

                findViewById(R.id.activity_view_classes_current_class_card).setVisibility(View.GONE);

                nullCount++;

            }

        } else {

            findViewById(R.id.activity_view_classes_current_class_card).setVisibility(View.GONE);

            nullCount++;

        }

        if (nullCount == 2) {


        } else {


        }

    }

    private void updateProgressBar() {

        progressBar.setIndeterminate(false);
        progressBar.setProgress(DataSingleton.getInstance().getProgressBarProgress());

        if (progressTextView == null)
            progressTextView = (TextView) findViewById(R.id.activity_view_classes_current_class_card_progress_percentage);

        progressTextView.setText(DataSingleton.getInstance().getProgressBarText());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            super.onBackPressed();

            return true;

        } else if (menuItem.getItemId() == R.id.toolbar_delete_edit_delete) {

            final MaterialDialog.Builder materialDialogBuilder = new MaterialDialog.Builder(this);

            materialDialogBuilder.title("Delete class?");
            materialDialogBuilder.content("All timetable entries and homework will be deleted for this class.");
            materialDialogBuilder.positiveText("YES");
            materialDialogBuilder.positiveColorRes(R.color.black);
            materialDialogBuilder.negativeText("CANCEL");
            materialDialogBuilder.negativeColorRes(R.color.black);

            materialDialogBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                    materialDialog.dismiss();

                    DatabaseHelper databaseHelper = new DatabaseHelper(ViewClass.this);

                    try {

                        databaseHelper.deleteClass(getIntent().getStringExtra("class"));

                    } finally {

                        databaseHelper.close();

                    }

                    EventBus.getDefault().post(new Update(true, true, true, true));

                    ViewClass.super.onBackPressed();

                }

            });

            materialDialogBuilder.onNegative(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                    materialDialog.dismiss();

                }

            });

            materialDialogBuilder.show();

            return true;

        } else {

            return super.onOptionsItemSelected(menuItem);

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

                    expandAnimation.setDuration(EXPANDABLE_LIST_VIEW_ANIMATION_DURATION);
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

                    collapseAnimation.setDuration(EXPANDABLE_LIST_VIEW_ANIMATION_DURATION);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_delete_edit, menu);

        return true;

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

}
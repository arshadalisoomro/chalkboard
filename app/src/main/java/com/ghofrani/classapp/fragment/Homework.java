package com.ghofrani.classapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.HomeworkList;
import com.ghofrani.classapp.adapter.SimpleSectionedRecyclerView;
import com.ghofrani.classapp.modules.DataStore;

import java.util.ArrayList;
import java.util.List;

public class Homework extends Fragment {

    private RecyclerView recyclerView;
    private CardView noHomeworkCardView;
    private final BroadcastReceiver updateHomeworkUI = new BroadcastReceiver() {

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

        return inflater.inflate(R.layout.fragment_homework, container, false);

    }

    @Override
    public void onResume() {

        super.onResume();

        if (recyclerView == null)
            recyclerView = (RecyclerView) getView().findViewById(R.id.homework_recycler_view);

        if (noHomeworkCardView == null)
            noHomeworkCardView = (CardView) getView().findViewById(R.id.homework_no_homework_card);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(updateHomeworkUI, new IntentFilter("update_homework_UI"));

        updateUI();

    }

    @Override
    public void onStop() {

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(updateHomeworkUI);

        super.onStop();

    }

    private void updateUI() {

        ArrayList<com.ghofrani.classapp.model.Homework> homeworkArrayList = new ArrayList<>();

        homeworkArrayList.addAll(DataStore.todayHomeworkArrayList);
        homeworkArrayList.addAll(DataStore.tomorrowHomeworkArrayList);
        homeworkArrayList.addAll(DataStore.thisWeekHomeworkArrayList);
        homeworkArrayList.addAll(DataStore.nextWeekHomeworkArrayList);
        homeworkArrayList.addAll(DataStore.thisMonthHomeworkArrayList);
        homeworkArrayList.addAll(DataStore.beyondThisMonthHomeworkArrayList);
        homeworkArrayList.addAll(DataStore.pastHomeworkArrayList);

        if (!homeworkArrayList.isEmpty()) {

            noHomeworkCardView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);

            List<SimpleSectionedRecyclerView.Section> sections = new ArrayList<>();

            int nextSectionIndex = 0;

            if (!DataStore.todayHomeworkArrayList.isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due today"));
                nextSectionIndex += DataStore.todayHomeworkArrayList.size();

            }

            if (!DataStore.tomorrowHomeworkArrayList.isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due tomorrow"));
                nextSectionIndex += DataStore.tomorrowHomeworkArrayList.size();

            }

            if (!DataStore.thisWeekHomeworkArrayList.isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due this week"));
                nextSectionIndex += DataStore.thisWeekHomeworkArrayList.size();

            }

            if (!DataStore.nextWeekHomeworkArrayList.isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due next week"));
                nextSectionIndex += DataStore.nextWeekHomeworkArrayList.size();

            }

            if (!DataStore.thisMonthHomeworkArrayList.isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due this month"));
                nextSectionIndex += DataStore.thisMonthHomeworkArrayList.size();

            }

            if (!DataStore.beyondThisMonthHomeworkArrayList.isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due after this month"));
                nextSectionIndex += DataStore.beyondThisMonthHomeworkArrayList.size();

            }

            if (!DataStore.pastHomeworkArrayList.isEmpty())
                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due in the past"));

            SimpleSectionedRecyclerView.Section[] sectionArray = new SimpleSectionedRecyclerView.Section[sections.size()];
            SimpleSectionedRecyclerView sectionedAdapter = new SimpleSectionedRecyclerView(getContext(), R.layout.view_homework_list_section, R.id.view_homework_list_section_text_view, new HomeworkList(getContext(), homeworkArrayList));
            sectionedAdapter.setSections(sections.toArray(sectionArray));

            recyclerView.setAdapter(sectionedAdapter);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        } else {

            noHomeworkCardView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }

    }

    @Override
    public void onDestroyView() {

        recyclerView = null;
        noHomeworkCardView = null;

        super.onDestroyView();

    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {

        private final Drawable dividerDrawable;

        public SimpleDividerItemDecoration(Context context) {

            dividerDrawable = ContextCompat.getDrawable(context, R.drawable.line_divider);

        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parentView, RecyclerView.State recyclerViewState) {

            int left = parentView.getPaddingLeft();
            int right = parentView.getWidth() - parentView.getPaddingRight();

            int childCount = parentView.getChildCount();

            for (int i = 0; i < childCount; i++) {

                View childView = parentView.getChildAt(i);

                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();

                int top = childView.getBottom() + layoutParams.bottomMargin;
                int bottom = top + dividerDrawable.getIntrinsicHeight();

                dividerDrawable.setBounds(left, top, right, bottom);
                dividerDrawable.draw(c);

            }

        }

    }

}
package com.ghofrani.classapp.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.HomeworkList;
import com.ghofrani.classapp.adapter.SimpleSectionedRecyclerView;
import com.ghofrani.classapp.module.DataSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class Homework extends Fragment {

    private RecyclerView recyclerView;
    private CardView noHomeworkCardView;

    @Subscribe
    public void onEvent(com.ghofrani.classapp.event.UpdateHomeworkUI updateHomeworkUI) {

        updateUI();

    }

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

        EventBus.getDefault().register(this);

        updateUI();

    }

    @Override
    public void onPause() {

        EventBus.getDefault().unregister(this);

        super.onPause();

    }

    private void updateUI() {

        ArrayList<com.ghofrani.classapp.model.Homework> homeworkArrayList = new ArrayList<>();

        homeworkArrayList.addAll(DataSingleton.getInstance().getTodayHomeworkArrayList());
        homeworkArrayList.addAll(DataSingleton.getInstance().getTomorrowHomeworkArrayList());
        homeworkArrayList.addAll(DataSingleton.getInstance().getThisWeekHomeworkArrayList());
        homeworkArrayList.addAll(DataSingleton.getInstance().getNextWeekHomeworkArrayList());
        homeworkArrayList.addAll(DataSingleton.getInstance().getThisMonthHomeworkArrayList());
        homeworkArrayList.addAll(DataSingleton.getInstance().getBeyondThisMonthHomeworkArrayList());
        homeworkArrayList.addAll(DataSingleton.getInstance().getPastHomeworkArrayList());

        if (!homeworkArrayList.isEmpty()) {

            noHomeworkCardView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);

            List<SimpleSectionedRecyclerView.Section> sections = new ArrayList<>();

            int nextSectionIndex = 0;

            if (!DataSingleton.getInstance().getTodayHomeworkArrayList().isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due today"));
                nextSectionIndex += DataSingleton.getInstance().getTodayHomeworkArrayList().size();

            }

            if (!DataSingleton.getInstance().getTomorrowHomeworkArrayList().isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due tomorrow"));
                nextSectionIndex += DataSingleton.getInstance().getTomorrowHomeworkArrayList().size();

            }

            if (!DataSingleton.getInstance().getThisWeekHomeworkArrayList().isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due this week"));
                nextSectionIndex += DataSingleton.getInstance().getThisWeekHomeworkArrayList().size();

            }

            if (!DataSingleton.getInstance().getNextWeekHomeworkArrayList().isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due next week"));
                nextSectionIndex += DataSingleton.getInstance().getNextWeekHomeworkArrayList().size();

            }

            if (!DataSingleton.getInstance().getThisMonthHomeworkArrayList().isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due this month"));
                nextSectionIndex += DataSingleton.getInstance().getThisMonthHomeworkArrayList().size();

            }

            if (!DataSingleton.getInstance().getBeyondThisMonthHomeworkArrayList().isEmpty()) {

                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due after this month"));
                nextSectionIndex += DataSingleton.getInstance().getBeyondThisMonthHomeworkArrayList().size();

            }

            if (!DataSingleton.getInstance().getPastHomeworkArrayList().isEmpty())
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

                final View childView = parentView.getChildAt(i);

                final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();

                int top = childView.getBottom() + layoutParams.bottomMargin;
                int bottom = top + dividerDrawable.getIntrinsicHeight();

                dividerDrawable.setBounds(left, top, right, bottom);
                dividerDrawable.draw(c);

            }

        }

    }

}
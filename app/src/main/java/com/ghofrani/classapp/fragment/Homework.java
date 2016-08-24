package com.ghofrani.classapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        updateUI();

    }

    private void updateUI() {

        ArrayList<com.ghofrani.classapp.model.Homework> homeworkArrayList = new ArrayList<>();

        homeworkArrayList.addAll(DataStore.todayHomeworkArrayList);
        homeworkArrayList.addAll(DataStore.tomorrowHomeworkArrayList);
        homeworkArrayList.addAll(DataStore.thisWeekHomeworkArrayList);
        homeworkArrayList.addAll(DataStore.nextWeekHomeworkArrayList);
        homeworkArrayList.addAll(DataStore.thisMonthHomeworkArrayList);
        homeworkArrayList.addAll(DataStore.beyondThisMonthHomeworkArrayList);

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

            if (!DataStore.beyondThisMonthHomeworkArrayList.isEmpty())
                sections.add(new SimpleSectionedRecyclerView.Section(nextSectionIndex, "Due after this month"));

            SimpleSectionedRecyclerView.Section[] sectionArray = new SimpleSectionedRecyclerView.Section[sections.size()];
            SimpleSectionedRecyclerView sectionedAdapter = new SimpleSectionedRecyclerView(getContext(), R.layout.view_homework_list_section, R.id.view_homework_list_section_text_view, new HomeworkList(homeworkArrayList, getContext()));
            sectionedAdapter.setSections(sections.toArray(sectionArray));

            recyclerView.setAdapter(sectionedAdapter);

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

}
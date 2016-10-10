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
import com.ghofrani.classapp.adapter.AllClassesList;
import com.ghofrani.classapp.module.DataSingleton;

public class Classes extends Fragment {

    private RecyclerView recyclerView;
    private CardView noClassesCardView;
    private AllClassesList allClassesListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_classes, container, false);

    }

    @Override
    public void onResume() {

        super.onResume();

        if (recyclerView == null)
            recyclerView = (RecyclerView) getView().findViewById(R.id.classes_recycler_view);

        if (noClassesCardView == null)
            noClassesCardView = (CardView) getView().findViewById(R.id.classes_no_classes_card);

        updateUI();

    }

    @Override
    public void onDestroyView() {

        recyclerView = null;
        noClassesCardView = null;
        allClassesListAdapter = null;

        super.onDestroyView();

    }

    private void updateUI() {

        if (!DataSingleton.getInstance().getAllClassesArrayList().isEmpty()) {

            noClassesCardView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            allClassesListAdapter = new AllClassesList(getContext());
            recyclerView.setAdapter(allClassesListAdapter);

        } else {

            noClassesCardView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }

    }

}
package com.ghofrani.classapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.AllClassesList;
import com.ghofrani.classapp.modules.DataStore;

public class Classes extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_classes, container, false);

    }

    @Override
    public void onStart() {

        super.onStart();

        updateUI();

    }

    private void updateUI() {

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.classes_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        AllClassesList allClassesListAdapter = new AllClassesList(DataStore.getAllClassesLinkedList(), getActivity());
        recyclerView.setAdapter(allClassesListAdapter);

    }

}
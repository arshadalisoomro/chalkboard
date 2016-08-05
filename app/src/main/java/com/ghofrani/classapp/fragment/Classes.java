package com.ghofrani.classapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ViewClass;
import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.DatabaseHelper;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.LinkedList;

public class Classes extends Fragment {

    private View.OnClickListener cardOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            TextView classCardTitleTextView = (TextView) v.findViewById(R.id.view_class_card_title);

            startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", classCardTitleTextView.getText()));

        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_classes, container, false);

    }

    @Override
    public void onStart() {

        super.onStart();

        updateUI();

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        cardOnClickListener = null;

    }

    @SuppressWarnings("ResourceType")
    private void updateUI() {

        RelativeLayout classesClassListLayout = (RelativeLayout) getView().findViewById(R.id.classes_class_list_layout);
        classesClassListLayout.removeAllViews();

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinkedList<SlimClass> allClassesLinkedList = DataStore.getAllClassesLinkedList();

        RelativeLayout[] cardViewRelativeLayoutArray = new RelativeLayout[allClassesLinkedList.size()];

        for (int i = 1; i < (allClassesLinkedList.size() + 1); i++) {

            SlimClass slimClass = allClassesLinkedList.get(i - 1);

            RelativeLayout classCardLayout = (RelativeLayout) layoutInflater.inflate(R.layout.view_class_card, classesClassListLayout, false);
            classCardLayout.setId(i);

            RelativeLayout.LayoutParams classCardLayoutLayoutParams = (RelativeLayout.LayoutParams) classCardLayout.getLayoutParams();
            classCardLayoutLayoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            classCardLayoutLayoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;

            classCardLayout.setLayoutParams(classCardLayoutLayoutParams);

            TextView classCardTitleTextView = (TextView) classCardLayout.findViewById(R.id.view_class_card_title);
            classCardTitleTextView.setText(slimClass.getName());

            TextView classCardLocationTeacherTextView = (TextView) classCardLayout.findViewById(R.id.view_class_card_location_teacher);
            classCardLocationTeacherTextView.setText(slimClass.getTeacher() + ", " + slimClass.getLocation());

            CardView classCard = (CardView) classCardLayout.findViewById(R.id.view_class_card);
            classCard.setOnClickListener(cardOnClickListener);

            if (i == 1)
                classCardLayoutLayoutParams.topMargin = getPixelFromDP(12);

            if (i == cardViewRelativeLayoutArray.length)
                classCardLayoutLayoutParams.bottomMargin = getPixelFromDP(12);

            if (i > 1)
                classCardLayoutLayoutParams.addRule(RelativeLayout.BELOW, i - 1);

            classesClassListLayout.addView(classCardLayout);
            cardViewRelativeLayoutArray[i - 1] = classCardLayout;

        }

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

}
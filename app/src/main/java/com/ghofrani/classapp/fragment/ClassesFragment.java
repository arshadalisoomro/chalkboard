package com.ghofrani.classapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ViewClassActivity;
import com.ghofrani.classapp.modules.DatabaseHelper;
import com.github.ivbaranov.mli.MaterialLetterIcon;

public class ClassesFragment extends Fragment {

    View.OnClickListener cardOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            TextView classTitleTextView = (TextView) v.findViewById(R.id.class_title);

            startActivity(new Intent(getContext(), ViewClassActivity.class).putExtra("class", classTitleTextView.getText()));

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

    @SuppressWarnings("ResourceType")
    private void updateUI() {

        RelativeLayout classesLayout = (RelativeLayout) getView().findViewById(R.id.class_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DatabaseHelper db = new DatabaseHelper(getContext());

        Cursor result = db.getClasses();

        RelativeLayout[] cardViews = new RelativeLayout[result.getCount()];
        int index = 1;

        while (result.moveToNext()) {

            RelativeLayout classCard = (RelativeLayout) layoutInflater.inflate(R.layout.card_class, classesLayout, false);
            classCard.setId(index);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) classCard.getLayoutParams();
            layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;

            classCard.setLayoutParams(layoutParams);

            TextView classTitle = (TextView) classCard.findViewById(R.id.class_title);
            classTitle.setText(result.getString(1));

            TextView classLocationTeacher = (TextView) classCard.findViewById(R.id.class_location_teacher);
            classLocationTeacher.setText(result.getString(2) + ", " + result.getString(3));

            CardView card = (CardView) classCard.findViewById(R.id.card_view_class);
            card.setOnClickListener(cardOnClick);

            if ((index % 2) == 0) {

                MaterialLetterIcon homeworkIcon = (MaterialLetterIcon) classCard.findViewById(R.id.homework_icon);
                homeworkIcon.setShapeColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

            }

            if (index == 1)
                layoutParams.topMargin = getPixelFromDP(12);

            if (index == cardViews.length)
                layoutParams.bottomMargin = getPixelFromDP(12);

            if (index > 1)
                layoutParams.addRule(RelativeLayout.BELOW, index - 1);

            classesLayout.addView(classCard);
            cardViews[index - 1] = classCard;

            index++;

        }

        db.close();

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

}
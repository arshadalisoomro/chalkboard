package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ViewClass;
import com.ghofrani.classapp.module.DataSingleton;

public class AllClassesList extends RecyclerView.Adapter<AllClassesList.ClassViewHolder> {

    private final Context context;
    private final View.OnClickListener onClickListener;

    public AllClassesList(Context context) {

        this.context = context;

        onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final TextView titleTextView = (TextView) view.findViewById(R.id.view_class_card_title);
                final TextView titleTextViewCentered = (TextView) view.findViewById(R.id.view_class_card_title_centered);

                if (titleTextView.getVisibility() == View.GONE)
                    AllClassesList.this.context.startActivity(new Intent(AllClassesList.this.context, ViewClass.class).putExtra("class", titleTextViewCentered.getText().toString()));
                else
                    AllClassesList.this.context.startActivity(new Intent(AllClassesList.this.context, ViewClass.class).putExtra("class", titleTextView.getText().toString()));

            }

        };

    }

    @Override
    public int getItemCount() {

        return DataSingleton.getInstance().getAllClassesArrayList().size();

    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        return new ClassViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_class_card, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(ClassViewHolder classViewHolder, int position) {

        final FrameLayout.LayoutParams relativeParams = (FrameLayout.LayoutParams) classViewHolder.rootRelativeLayout.getLayoutParams();

        if (DataSingleton.getInstance().getAllClassesArrayList().get(position).hasLocation()) {

            relativeParams.setMargins(getPixelFromDP(16), getPixelFromDP(14), getPixelFromDP(16), getPixelFromDP(14));
            classViewHolder.rootRelativeLayout.setLayoutParams(relativeParams);

            classViewHolder.titleTextViewCentered.setVisibility(View.GONE);

            classViewHolder.relativeLayout.setVisibility(View.VISIBLE);

            classViewHolder.titleTextView.setVisibility(View.VISIBLE);
            classViewHolder.titleTextView.setText(DataSingleton.getInstance().getAllClassesArrayList().get(position).getName());

            classViewHolder.teacherLocationTextView.setVisibility(View.VISIBLE);

            if (DataSingleton.getInstance().getAllClassesArrayList().get(position).hasTeacher())
                classViewHolder.teacherLocationTextView.setText(DataSingleton.getInstance().getAllClassesArrayList().get(position).getTeacher() + " • " + DataSingleton.getInstance().getAllClassesArrayList().get(position).getLocation());
            else
                classViewHolder.teacherLocationTextView.setText(DataSingleton.getInstance().getAllClassesArrayList().get(position).getLocation());

            classViewHolder.colorIndicatorImageView.setTranslationY(getPixelFromDP(-1));

        } else if (DataSingleton.getInstance().getAllClassesArrayList().get(position).hasTeacher()) {

            relativeParams.setMargins(getPixelFromDP(16), getPixelFromDP(14), getPixelFromDP(16), getPixelFromDP(14));
            classViewHolder.rootRelativeLayout.setLayoutParams(relativeParams);

            classViewHolder.titleTextViewCentered.setVisibility(View.GONE);

            classViewHolder.relativeLayout.setVisibility(View.VISIBLE);

            classViewHolder.titleTextView.setVisibility(View.VISIBLE);
            classViewHolder.titleTextView.setText(DataSingleton.getInstance().getAllClassesArrayList().get(position).getName());

            classViewHolder.teacherLocationTextView.setVisibility(View.VISIBLE);
            classViewHolder.teacherLocationTextView.setText(DataSingleton.getInstance().getAllClassesArrayList().get(position).getTeacher());

            classViewHolder.colorIndicatorImageView.setTranslationY(getPixelFromDP(-1));

        } else {

            relativeParams.setMargins(getPixelFromDP(16), getPixelFromDP(10), getPixelFromDP(16), getPixelFromDP(10));
            classViewHolder.rootRelativeLayout.setLayoutParams(relativeParams);

            classViewHolder.relativeLayout.setVisibility(View.GONE);

            classViewHolder.titleTextView.setVisibility(View.GONE);
            classViewHolder.teacherLocationTextView.setVisibility(View.GONE);

            classViewHolder.titleTextViewCentered.setVisibility(View.VISIBLE);
            classViewHolder.titleTextViewCentered.setText(DataSingleton.getInstance().getAllClassesArrayList().get(position).getName());

            classViewHolder.colorIndicatorImageView.setTranslationY(0);

        }

        classViewHolder.colorIndicatorImageView.setColorFilter(DataSingleton.getInstance().getAllClassesArrayList().get(position).getColor());
        classViewHolder.cardView.setOnClickListener(onClickListener);

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {

        final CardView cardView;
        final RelativeLayout rootRelativeLayout;
        final RelativeLayout relativeLayout;
        final TextView titleTextView;
        final TextView titleTextViewCentered;
        final TextView teacherLocationTextView;
        final ImageView colorIndicatorImageView;

        ClassViewHolder(View itemView) {

            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.view_class_card);
            rootRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.view_class_card_root_relative_layout);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.view_class_card_relative_layout);
            titleTextView = (TextView) itemView.findViewById(R.id.view_class_card_title);
            titleTextViewCentered = (TextView) itemView.findViewById(R.id.view_class_card_title_centered);
            teacherLocationTextView = (TextView) itemView.findViewById(R.id.view_class_card_teacher_location);
            colorIndicatorImageView = (ImageView) itemView.findViewById(R.id.view_class_color_indicator);

        }

    }

}
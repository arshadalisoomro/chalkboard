package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.Homework;

import java.util.ArrayList;

public class HomeworkList extends RecyclerView.Adapter<HomeworkList.HomeworkViewHolder> {

    private final ArrayList<Homework> homeworkArrayList;
    private final Context context;
    private final View.OnClickListener onClickListener;

    public HomeworkList(ArrayList<Homework> homeworkArrayList, final Context contextInput) {

        this.homeworkArrayList = homeworkArrayList;
        this.context = contextInput;

        onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /*

                TextView titleTextView = (TextView) view.findViewById(R.id.view_class_card_title);

                context.startActivity(new Intent(context, ViewHomework.class).putExtra("homework", titleTextView.getText()));

                */

                Toast.makeText(context, "CLICKED!", Toast.LENGTH_SHORT);

            }

        };

    }

    @Override
    public int getItemCount() {

        return homeworkArrayList.size();

    }

    @Override
    public HomeworkViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        return new HomeworkViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_homework_list_item, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(HomeworkViewHolder homeworkViewHolder, int position) {

        homeworkViewHolder.relativeLayout.setOnClickListener(onClickListener);
        homeworkViewHolder.titleTextView.setText(homeworkArrayList.get(position).getName());
        homeworkViewHolder.subtitleTextView.setText(homeworkArrayList.get(position).getDateTime().toString());
        homeworkViewHolder.colorIndicatorImageView.setColorFilter(homeworkArrayList.get(position).getColor());

    }

    public static class HomeworkViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout relativeLayout;
        final TextView titleTextView;
        final TextView subtitleTextView;
        final ImageView colorIndicatorImageView;

        HomeworkViewHolder(View itemView) {

            super(itemView);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.view_homework_list_item_linear_layout);
            titleTextView = (TextView) itemView.findViewById(R.id.view_homework_list_item_name);
            subtitleTextView = (TextView) itemView.findViewById(R.id.view_homework_list_item_due);
            colorIndicatorImageView = (ImageView) itemView.findViewById(R.id.view_homework_list_item_color_indicator);

        }

    }

}
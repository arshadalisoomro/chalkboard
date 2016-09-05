package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.Homework;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.Utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class HomeworkList extends RecyclerView.Adapter<HomeworkList.HomeworkViewHolder> {

    private final ArrayList<Homework> homeworkArrayList;
    private final View.OnClickListener onClickListener;
    private final DateTimeFormatter dayOfWeekString;
    private final DateTimeFormatter time24Hour;
    private final DateTimeFormatter timeAMPM;
    private final DateTimeFormatter shortDate;
    private final boolean is24Hour;
    private final DateTime tomorrow;

    public HomeworkList(Context context, ArrayList<Homework> homeworkArrayList) {

        this.homeworkArrayList = homeworkArrayList;

        dayOfWeekString = DateTimeFormat.forPattern("EEEE");
        time24Hour = DateTimeFormat.forPattern("HH:mm");
        timeAMPM = DateTimeFormat.forPattern("h:mm a");
        shortDate = DateTimeFormat.forPattern("dd/MM/yy");
        is24Hour = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("24_hour_time", true);
        tomorrow = DateTime.now().plusDays(1);

        onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /*

                final TextView titleTextView = (TextView) view.findViewById(R.id.view_class_card_title);

                context.startActivity(new Intent(context, ViewHomework.class).putExtra("homework", titleTextView.getText()));

                */

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

        String subtitleTextViewText = "";

        if (homeworkArrayList.get(position).isAttach()) {

            ArrayList<StandardClass> classesList = Utils.getClassesArrayListOfDay(homeworkArrayList.get(position).getDateTime().getDayOfWeek());

            if (classesList != null) {

                int index = 0;
                boolean completed = false;

                while (index < classesList.size() && !completed) {

                    if (classesList.get(index).getName().equals(homeworkArrayList.get(position).getClassName())) {

                        if (classesList.get(index).getStartTime().equals(homeworkArrayList.get(position).getDateTime().toLocalTime())) {

                            if (!tomorrow.withTimeAtStartOfDay().isEqual(homeworkArrayList.get(position).getDateTime()))
                                subtitleTextViewText = dayOfWeekString.print(homeworkArrayList.get(position).getDateTime()) + " • " + classesList.get(index).getName() + " at " + (is24Hour ? time24Hour.print(homeworkArrayList.get(position).getDateTime()) : timeAMPM.print(homeworkArrayList.get(position).getDateTime()));
                            else
                                subtitleTextViewText = classesList.get(index).getName() + " at " + (is24Hour ? time24Hour.print(homeworkArrayList.get(position).getDateTime()) : timeAMPM.print(homeworkArrayList.get(position).getDateTime()));

                            completed = true;

                        }

                    }

                    index++;

                }

            }

        } else {

            if (homeworkArrayList.get(position).getDateTime().isAfter(DataStore.nextWeekEnd)) {

                subtitleTextViewText = homeworkArrayList.get(position).getClassName() + " • " + shortDate.print(homeworkArrayList.get(position).getDateTime()) + " • " + (is24Hour ? time24Hour.print(homeworkArrayList.get(position).getDateTime()) : timeAMPM.print(homeworkArrayList.get(position).getDateTime()));

            } else if (homeworkArrayList.get(position).getDateTime().withTimeAtStartOfDay().isEqual(tomorrow.withTimeAtStartOfDay())) {

                subtitleTextViewText = homeworkArrayList.get(position).getClassName() + " • " + (is24Hour ? time24Hour.print(homeworkArrayList.get(position).getDateTime()) : timeAMPM.print(homeworkArrayList.get(position).getDateTime()));

            } else {

                subtitleTextViewText = dayOfWeekString.print(homeworkArrayList.get(position).getDateTime()) + " • " + (is24Hour ? time24Hour.print(homeworkArrayList.get(position).getDateTime()) : timeAMPM.print(homeworkArrayList.get(position).getDateTime()));

            }

        }

        if (subtitleTextViewText.isEmpty()) {

            if (homeworkArrayList.get(position).getDateTime().isAfter(DataStore.nextWeekEnd)) {

                homeworkViewHolder.subtitleTextView.setText(homeworkArrayList.get(position).getClassName() + " • " + shortDate.print(homeworkArrayList.get(position).getDateTime()) + " • " + (is24Hour ? time24Hour.print(homeworkArrayList.get(position).getDateTime()) : timeAMPM.print(homeworkArrayList.get(position).getDateTime())));

            } else if (homeworkArrayList.get(position).getDateTime().withTimeAtStartOfDay().isEqual(tomorrow.withTimeAtStartOfDay())) {

                homeworkViewHolder.subtitleTextView.setText(homeworkArrayList.get(position).getClassName() + " • " + (is24Hour ? time24Hour.print(homeworkArrayList.get(position).getDateTime()) : timeAMPM.print(homeworkArrayList.get(position).getDateTime())));

            } else {

                homeworkViewHolder.subtitleTextView.setText(homeworkArrayList.get(position).getClassName() + " • " + dayOfWeekString.print(homeworkArrayList.get(position).getDateTime()) + " • " + (is24Hour ? time24Hour.print(homeworkArrayList.get(position).getDateTime()) : timeAMPM.print(homeworkArrayList.get(position).getDateTime())));

            }

        } else {

            homeworkViewHolder.subtitleTextView.setText(subtitleTextViewText);

        }

        homeworkViewHolder.colorIndicatorImageView.setColorFilter(homeworkArrayList.get(position).getColor());

        if (homeworkArrayList.get(position).isHighPriority())
            homeworkViewHolder.priorityIndicatorImageView.setVisibility(View.VISIBLE);
        else
            homeworkViewHolder.priorityIndicatorImageView.setVisibility(View.GONE);

    }

    public static class HomeworkViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout relativeLayout;
        final TextView titleTextView;
        final TextView subtitleTextView;
        final ImageView colorIndicatorImageView;
        final ImageView priorityIndicatorImageView;

        HomeworkViewHolder(View itemView) {

            super(itemView);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.view_homework_list_item_linear_layout);
            titleTextView = (TextView) itemView.findViewById(R.id.view_homework_list_item_name);
            subtitleTextView = (TextView) itemView.findViewById(R.id.view_homework_list_item_due);
            colorIndicatorImageView = (ImageView) itemView.findViewById(R.id.view_homework_list_item_color_indicator);
            priorityIndicatorImageView = (ImageView) itemView.findViewById(R.id.view_homework_list_item_priority_indicator);

        }

    }

}
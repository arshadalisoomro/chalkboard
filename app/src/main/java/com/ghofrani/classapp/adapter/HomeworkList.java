package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ViewHomework;
import com.ghofrani.classapp.model.Homework;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.Utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class HomeworkList extends RecyclerView.Adapter<HomeworkList.HomeworkViewHolder> {

    private final ArrayList<Homework> homeworkArrayList;
    private final DateTimeFormatter dayOfWeekString;
    private final DateTimeFormatter time24Hour;
    private final DateTimeFormatter timeAMPM;
    private final DateTimeFormatter shortDate;
    private final boolean is24Hour;
    private final View.OnClickListener onClickListener;
    private final DateTime tomorrow;

    public HomeworkList(final Context context, ArrayList<Homework> homeworkArrayList) {

        this.homeworkArrayList = homeworkArrayList;

        dayOfWeekString = DateTimeFormat.forPattern("EEEE");
        time24Hour = DateTimeFormat.forPattern("HH:mm");
        timeAMPM = DateTimeFormat.forPattern("h:mm a");
        shortDate = DateTimeFormat.forPattern("dd/MM/yy");
        is24Hour = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("24_hour_time", true);
        tomorrow = DateTime.now().plusDays(1).withTime(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour(), 0, 0);

        onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final TextView titleTextView = (TextView) view.findViewById(R.id.view_homework_list_item_name);
                final String[] titleTextViewArray = titleTextView.getText().toString().split(" • ");

                context.startActivity(new Intent(context, ViewHomework.class).putExtra("homework", titleTextViewArray[1]));

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

        final Homework homework = homeworkArrayList.get(position);

        homeworkViewHolder.relativeLayout.setOnClickListener(onClickListener);
        homeworkViewHolder.titleTextView.setText(homework.getClassName() + " • " + homework.getName());

        String subtitleTextViewText = "";

        if (homework.isAttach()) {

            ArrayList<StandardClass> classesList = Utils.getClassesArrayListOfDay(homework.getDateTime().getDayOfWeek() == 7 ? 1 : homework.getDateTime().getDayOfWeek() + 1);

            if (classesList != null) {

                int index = 0;
                boolean completed = false;

                while (index < classesList.size() && !completed) {

                    if (classesList.get(index).getName().equals(homework.getClassName())) {

                        if (classesList.get(index).getStartTime().equals(homework.getDateTime().toLocalTime())) {

                            if (tomorrow.withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay())) {

                                subtitleTextViewText = "Class at " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));

                            } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay())) {

                                if (homework.getDateTime().isBefore(tomorrow.minusDays(1)) || homework.getDateTime().isEqual(tomorrow.minusDays(1)))
                                    subtitleTextViewText = "Today • Class at " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));
                                else
                                    subtitleTextViewText = "Class at " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));

                            } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isAfter(homework.getDateTime())) {

                                if (tomorrow.minusDays(2).withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay()))
                                    subtitleTextViewText = "Yesterday • Class at " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));
                                else
                                    subtitleTextViewText = shortDate.print(homework.getDateTime()) + " • Class at " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));

                            } else {

                                subtitleTextViewText = dayOfWeekString.print(homework.getDateTime()) + " • Class at " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));

                            }

                            completed = true;

                        }

                    }

                    index++;

                }

            }

        } else {

            if (homework.getDateTime().isAfter(DataSingleton.getInstance().getNextWeekEnd())) {

                subtitleTextViewText = shortDate.print(homework.getDateTime()) + " • " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));

            } else if (tomorrow.withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay())) {

                subtitleTextViewText = is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime());

            } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay())) {

                if (homework.getDateTime().isBefore(tomorrow.minusDays(1)) || homework.getDateTime().isEqual(tomorrow.minusDays(1)))
                    subtitleTextViewText = "Today • " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));
                else
                    subtitleTextViewText = is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime());

            } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isAfter(homework.getDateTime())) {

                if (tomorrow.minusDays(2).withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay()))
                    subtitleTextViewText = "Yesterday • " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));
                else
                    subtitleTextViewText = shortDate.print(homework.getDateTime()) + " • " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));

            } else {

                subtitleTextViewText = dayOfWeekString.print(homework.getDateTime()) + " • " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));

            }

        }

        if (subtitleTextViewText.isEmpty()) {

            if (homework.getDateTime().isAfter(DataSingleton.getInstance().getNextWeekEnd())) {

                homeworkViewHolder.subtitleTextView.setText(shortDate.print(homework.getDateTime()) + " • " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime())));

            } else if (tomorrow.withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay())) {

                homeworkViewHolder.subtitleTextView.setText(is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));

            } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay())) {

                if (homework.getDateTime().isBefore(tomorrow.minusDays(1)) || homework.getDateTime().isEqual(tomorrow.minusDays(1)))
                    homeworkViewHolder.subtitleTextView.setText("Today • " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime())));
                else
                    homeworkViewHolder.subtitleTextView.setText(is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime()));

            } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isAfter(homework.getDateTime())) {

                if (tomorrow.minusDays(2).withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay()))
                    homeworkViewHolder.subtitleTextView.setText("Yesterday • " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime())));
                else
                    homeworkViewHolder.subtitleTextView.setText(shortDate.print(homework.getDateTime()) + " • " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime())));

            } else {

                homeworkViewHolder.subtitleTextView.setText(dayOfWeekString.print(homework.getDateTime()) + " • " + (is24Hour ? time24Hour.print(homework.getDateTime()) : timeAMPM.print(homework.getDateTime())));

            }

        } else {

            homeworkViewHolder.subtitleTextView.setText(subtitleTextViewText);

        }

        homeworkViewHolder.colorIndicatorImageView.setColorFilter(homework.getColor());

        if (homework.isHighPriority())
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
package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.Homework;
import com.ghofrani.classapp.model.HomeworkWithID;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.model.StringWithID;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.Utils;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemDrawableTypes;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemResults;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class HomeworkList extends RecyclerView.Adapter<AbstractSwipeableItemViewHolder> implements SwipeableItemAdapter<AbstractSwipeableItemViewHolder> {

    private final int VIEW_TYPE_HOMEWORK = 0;
    private final int VIEW_TYPE_SECTION = 1;

    private final DateTimeFormatter dayOfWeekString;
    private final DateTimeFormatter time24Hour;
    private final DateTimeFormatter timeAMPM;
    private final DateTimeFormatter shortDate;
    private final boolean is24Hour;
    private final DateTime tomorrow;
    private EventListener eventListener;

    public HomeworkList(final Context context) {

        setHasStableIds(true);

        dayOfWeekString = DateTimeFormat.forPattern("EEEE");
        time24Hour = DateTimeFormat.forPattern("HH:mm");
        timeAMPM = DateTimeFormat.forPattern("h:mm a");
        shortDate = DateTimeFormat.forPattern("dd/MM/yy");
        is24Hour = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("24_hour_time", true);
        tomorrow = DateTime.now().plusDays(1).withTime(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour(), 0, 0);

    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public long getItemId(int position) {

        if (DataSingleton.getInstance().getDataArrayList().get(position) instanceof StringWithID)
            return ((StringWithID) DataSingleton.getInstance().getDataArrayList().get(position)).getID();
        else
            return ((HomeworkWithID) DataSingleton.getInstance().getDataArrayList().get(position)).getID();

    }

    @Override
    public void onBindViewHolder(AbstractSwipeableItemViewHolder viewHolder, int position) {

        if (DataSingleton.getInstance().getDataArrayList().get(position) instanceof HomeworkWithID) {

            final Homework homework = ((HomeworkWithID) DataSingleton.getInstance().getDataArrayList().get(position)).getHomework();
            final ListItemViewHolder homeworkViewHolder = (ListItemViewHolder) viewHolder;

            homeworkViewHolder.titleTextView.setText(homework.getClassName() + " • " + homework.getName());

            String subtitleTextViewText = "";

            if (homework.isAttach()) {

                ArrayList<StandardClass> classesList = Utils.getClassesArrayListOfDay(homework.getDateTime().getDayOfWeek());

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


        } else {

            String text = ((StringWithID) DataSingleton.getInstance().getDataArrayList().get(position)).getString();

            final ListSectionViewHolder listSectionViewHolder = (ListSectionViewHolder) viewHolder;

            listSectionViewHolder.sectionTextView.setText(text);

        }

    }

    @Override
    public int getItemViewType(int position) {

        if (DataSingleton.getInstance().getDataArrayList().get(position) instanceof HomeworkWithID)
            return VIEW_TYPE_HOMEWORK;
        else
            return VIEW_TYPE_SECTION;

    }

    @Override
    public AbstractSwipeableItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HOMEWORK)
            return new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_homework_list_item, parent, false));
        else
            return new ListSectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_homework_list_section, parent, false));

    }

    @Override
    public int getItemCount() {
        return DataSingleton.getInstance().getDataArrayList().size();
    }

    @Override
    public SwipeResultAction onSwipeItem(AbstractSwipeableItemViewHolder listItemViewHolder, int position, @SwipeableItemResults int result) {

        if (result == Swipeable.RESULT_CANCELED)
            return new SwipeResultActionDefault();
        else
            return new HomeworkSwipeResultActionRemoveItem(this, position);

    }

    @Override
    public int onGetSwipeReactionType(AbstractSwipeableItemViewHolder listItemViewHolder, int position, int x, int y) {

        if (getItemViewType(position) == VIEW_TYPE_HOMEWORK)
            return Swipeable.REACTION_CAN_SWIPE_RIGHT;
        else
            return Swipeable.REACTION_CAN_NOT_SWIPE_ANY;

    }

    @Override
    public void onSetSwipeBackground(AbstractSwipeableItemViewHolder holder, int position, @SwipeableItemDrawableTypes int type) {

        if (getItemViewType(position) == VIEW_TYPE_HOMEWORK)
            holder.itemView.setBackgroundResource(R.drawable.background_swipe_right);

    }

    public interface EventListener {

        void onItemRemoved(int position);

    }

    private interface Swipeable extends SwipeableItemConstants {
    }

    static class HomeworkSwipeResultActionRemoveItem extends SwipeResultActionRemoveItem {

        private final int position;
        private HomeworkList adapter;

        public HomeworkSwipeResultActionRemoveItem(HomeworkList adapter, int position) {

            this.adapter = adapter;
            this.position = position;

        }

        @Override
        protected void onPerformAction() {

            DataSingleton.getInstance().getDataArrayList().remove(position);
            adapter.notifyItemRemoved(position);

            if (adapter.eventListener != null)
                adapter.eventListener.onItemRemoved(position);

        }

        @Override
        protected void onCleanUp() {

            super.onCleanUp();

            adapter = null;

        }

    }

    class ListItemViewHolder extends AbstractSwipeableItemViewHolder {

        final FrameLayout frameLayout;
        final TextView titleTextView;
        final TextView subtitleTextView;
        final ImageView colorIndicatorImageView;
        final ImageView priorityIndicatorImageView;

        ListItemViewHolder(View itemView) {

            super(itemView);

            frameLayout = (FrameLayout) itemView.findViewById(R.id.view_homework_list_item_frame_layout);
            titleTextView = (TextView) itemView.findViewById(R.id.view_homework_list_item_name);
            subtitleTextView = (TextView) itemView.findViewById(R.id.view_homework_list_item_due);
            colorIndicatorImageView = (ImageView) itemView.findViewById(R.id.view_homework_list_item_color_indicator);
            priorityIndicatorImageView = (ImageView) itemView.findViewById(R.id.view_homework_list_item_priority_indicator);

        }

        @Override
        public View getSwipeableContainerView() {
            return frameLayout;
        }

    }

    class ListSectionViewHolder extends AbstractSwipeableItemViewHolder {

        final FrameLayout frameLayout;
        final TextView sectionTextView;

        ListSectionViewHolder(View itemView) {

            super(itemView);

            frameLayout = (FrameLayout) itemView.findViewById(R.id.view_homework_list_section_frame_layout);
            sectionTextView = (TextView) itemView.findViewById(R.id.view_homework_list_section_text_view);

        }

        @Override
        public View getSwipeableContainerView() {
            return frameLayout;
        }

    }

}
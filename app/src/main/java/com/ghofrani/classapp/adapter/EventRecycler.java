package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.Event;
import com.ghofrani.classapp.model.EventWithID;
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

public class EventRecycler extends RecyclerView.Adapter<AbstractSwipeableItemViewHolder> implements SwipeableItemAdapter<AbstractSwipeableItemViewHolder> {

    private final int VIEW_TYPE_HOMEWORK = 0;
    private final int VIEW_TYPE_SECTION = 1;

    private final DateTimeFormatter dayOfWeekString;
    private final DateTimeFormatter time24Hour;
    private final DateTimeFormatter timeAMPM;
    private final DateTimeFormatter shortDate;
    private final boolean is24Hour;
    private final DateTime tomorrow;
    private EventListener eventListener;

    public EventRecycler(final Context context) {

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
            return ((EventWithID) DataSingleton.getInstance().getDataArrayList().get(position)).getID();

    }

    @Override
    public void onBindViewHolder(AbstractSwipeableItemViewHolder viewHolder, int position) {

        if (getItemViewType(position) == VIEW_TYPE_HOMEWORK) {

            final Event event = ((EventWithID) DataSingleton.getInstance().getDataArrayList().get(position)).getEvent();
            final ListEventViewHolder listEventViewHolder = (ListEventViewHolder) viewHolder;

            listEventViewHolder.titleTextView.setText(event.getClassName() + " • " + event.getName());

            String subtitleTextViewText = "";

            if (event.isAttach()) {

                ArrayList<StandardClass> classesList = Utils.getClassesArrayListOfDay(event.getDateTime().getDayOfWeek());

                if (classesList != null) {

                    int index = 0;
                    boolean completed = false;

                    while (index < classesList.size() && !completed) {

                        if (classesList.get(index).getName().equals(event.getClassName())) {

                            if (classesList.get(index).getStartTime().equals(event.getDateTime().toLocalTime())) {

                                if (tomorrow.withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                                    subtitleTextViewText = "Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                                    if (event.getDateTime().isBefore(tomorrow.minusDays(1)) || event.getDateTime().isEqual(tomorrow.minusDays(1)))
                                        subtitleTextViewText = "Today • Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));
                                    else
                                        subtitleTextViewText = "Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isAfter(event.getDateTime())) {

                                    if (tomorrow.minusDays(2).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay()))
                                        subtitleTextViewText = "Yesterday • Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));
                                    else
                                        subtitleTextViewText = shortDate.print(event.getDateTime()) + " • Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                                } else {

                                    subtitleTextViewText = dayOfWeekString.print(event.getDateTime()) + " • Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                                }

                                completed = true;

                            }

                        }

                        index++;

                    }

                }

            } else {

                if (event.getDateTime().isAfter(DataSingleton.getInstance().getNextWeekEnd())) {

                    subtitleTextViewText = shortDate.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                } else if (tomorrow.withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                    subtitleTextViewText = is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime());

                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                    if (event.getDateTime().isBefore(tomorrow.minusDays(1)) || event.getDateTime().isEqual(tomorrow.minusDays(1)))
                        subtitleTextViewText = "Today • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));
                    else
                        subtitleTextViewText = is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime());

                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isAfter(event.getDateTime())) {

                    if (tomorrow.minusDays(2).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay()))
                        subtitleTextViewText = "Yesterday • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));
                    else
                        subtitleTextViewText = shortDate.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                } else {

                    subtitleTextViewText = dayOfWeekString.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                }

            }

            if (subtitleTextViewText.isEmpty()) {

                if (event.getDateTime().isAfter(DataSingleton.getInstance().getNextWeekEnd())) {

                    listEventViewHolder.subtitleTextView.setText(shortDate.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime())));

                } else if (tomorrow.withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                    listEventViewHolder.subtitleTextView.setText(is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                    if (event.getDateTime().isBefore(tomorrow.minusDays(1)) || event.getDateTime().isEqual(tomorrow.minusDays(1)))
                        listEventViewHolder.subtitleTextView.setText("Today • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime())));
                    else
                        listEventViewHolder.subtitleTextView.setText(is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isAfter(event.getDateTime())) {

                    if (tomorrow.minusDays(2).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay()))
                        listEventViewHolder.subtitleTextView.setText("Yesterday • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime())));
                    else
                        listEventViewHolder.subtitleTextView.setText(shortDate.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime())));

                } else {

                    listEventViewHolder.subtitleTextView.setText(dayOfWeekString.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime())));

                }

            } else {

                listEventViewHolder.subtitleTextView.setText(subtitleTextViewText);

            }

            listEventViewHolder.colorIndicatorImageView.setColorFilter(event.getColor());


        } else {

            String text = ((StringWithID) DataSingleton.getInstance().getDataArrayList().get(position)).getString();

            final ListSectionViewHolder listSectionViewHolder = (ListSectionViewHolder) viewHolder;

            listSectionViewHolder.sectionTextView.setText(text);

        }

    }

    @Override
    public int getItemViewType(int position) {

        if (DataSingleton.getInstance().getDataArrayList().get(position) instanceof EventWithID)
            return VIEW_TYPE_HOMEWORK;
        else
            return VIEW_TYPE_SECTION;

    }

    @Override
    public AbstractSwipeableItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HOMEWORK)
            return new ListEventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_event_recycler_event, parent, false));
        else
            return new ListSectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_event_recycler_section, parent, false));

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
            return Swipeable.REACTION_CAN_SWIPE_LEFT;
        else
            return Swipeable.REACTION_CAN_NOT_SWIPE_ANY;

    }

    @Override
    public void onSetSwipeBackground(AbstractSwipeableItemViewHolder holder, int position, @SwipeableItemDrawableTypes int type) {

        if (getItemViewType(position) == VIEW_TYPE_HOMEWORK)
            holder.itemView.setBackgroundResource(R.drawable.background_swipe_left);

    }

    public interface EventListener {

        void onItemRemoved();

    }

    private interface Swipeable extends SwipeableItemConstants {
    }

    static class HomeworkSwipeResultActionRemoveItem extends SwipeResultActionRemoveItem {

        private final int position;
        private EventRecycler adapter;

        public HomeworkSwipeResultActionRemoveItem(EventRecycler adapter, int position) {

            this.adapter = adapter;
            this.position = position;

        }

        @Override
        protected void onPerformAction() {

            boolean triggerSectionRemoved = false;

            if (DataSingleton.getInstance().getDataArrayList().size() == 2) {

                triggerSectionRemoved = true;

            } else if (position == DataSingleton.getInstance().getDataArrayList().size() - 1) {

                if (adapter.getItemViewType(position - 1) == adapter.VIEW_TYPE_SECTION)
                    triggerSectionRemoved = true;

            } else {

                if (adapter.getItemViewType(position - 1) == adapter.VIEW_TYPE_SECTION)
                    if (adapter.getItemViewType(position + 1) == adapter.VIEW_TYPE_SECTION)
                        triggerSectionRemoved = true;

            }

            SparseArray<Object> newLastRemovedSparseArray = new SparseArray<>();
            newLastRemovedSparseArray.put(position, DataSingleton.getInstance().getDataArrayList().get(position));

            if (triggerSectionRemoved) {

                newLastRemovedSparseArray.put(position - 1, DataSingleton.getInstance().getDataArrayList().get(position - 1));

                DataSingleton.getInstance().getDataArrayList().remove(position);
                DataSingleton.getInstance().getDataArrayList().remove(position - 1);
                adapter.notifyItemRangeRemoved(position - 1, position);

            } else {

                DataSingleton.getInstance().getDataArrayList().remove(position);
                adapter.notifyItemRemoved(position);

            }

            DataSingleton.getInstance().setDataSparseArrayLastRemoved(newLastRemovedSparseArray);

            if (adapter.eventListener != null)
                adapter.eventListener.onItemRemoved();

        }

        @Override
        protected void onCleanUp() {

            adapter = null;

            super.onCleanUp();

        }

    }

    class ListEventViewHolder extends AbstractSwipeableItemViewHolder {

        final FrameLayout frameLayout;
        final TextView titleTextView;
        final TextView subtitleTextView;
        final ImageView colorIndicatorImageView;

        ListEventViewHolder(View itemView) {

            super(itemView);

            frameLayout = (FrameLayout) itemView.findViewById(R.id.view_event_recycler_event_frame_layout);
            titleTextView = (TextView) itemView.findViewById(R.id.view_event_recycler_event_name_text_view);
            subtitleTextView = (TextView) itemView.findViewById(R.id.view_event_recycler_event_due_text_view);
            colorIndicatorImageView = (ImageView) itemView.findViewById(R.id.view_event_recycler_event_color_indicator_image_view);

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

            frameLayout = (FrameLayout) itemView.findViewById(R.id.view_event_recycler_section_frame_layout);
            sectionTextView = (TextView) itemView.findViewById(R.id.view_event_recycler_section_title_text_view);

        }

        @Override
        public View getSwipeableContainerView() {
            return frameLayout;
        }

    }

}
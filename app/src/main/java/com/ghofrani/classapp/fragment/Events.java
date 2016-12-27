package com.ghofrani.classapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.EventRecycler;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.event.UpdateEventsUI;
import com.ghofrani.classapp.model.EventWithID;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.DatabaseHelper;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class Events extends Fragment {

    private RecyclerView recyclerView;
    private EventRecycler eventRecycler;
    private RecyclerViewSwipeManager recyclerViewSwipeManager;
    private RecyclerView.Adapter wrappedAdapter;
    private GeneralItemAnimator generalItemAnimator;
    private Snackbar snackbar;
    private LinearLayoutManager linearLayoutManager;
    private SparseArray<Object> lastItemRemoved;
    private RelativeLayout noEventsRelativeLayout;
    private Context context;

    @Subscribe
    public void onEvent(UpdateEventsUI updateEventsUI) {

        if (recyclerView.getVisibility() == View.VISIBLE)
            eventRecycler.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_events, container, false);

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        this.context = context;

    }

    @Override
    public void onResume() {

        super.onResume();

        if (recyclerView == null)
            recyclerView = (RecyclerView) getView().findViewById(R.id.fragment_events_recycler_view);

        if (noEventsRelativeLayout == null)
            noEventsRelativeLayout = (RelativeLayout) getView().findViewById(R.id.fragment_events_no_events_relative_layout);

        lastItemRemoved = new SparseArray<>();

        updateUI();

        EventBus.getDefault().register(this);

    }

    @Override
    public void onPause() {

        if (snackbar != null) {

            if (snackbar.isShown())
                snackbar.dismiss();
            else
                EventBus.getDefault().post(new Update(false, true, false, false));

        }

        EventBus.getDefault().unregister(this);
        DataSingleton.getInstance().setReactToBroadcastEvents(true);

        super.onPause();

    }

    @Override
    public void onDestroyView() {

        if (recyclerViewSwipeManager != null)
            recyclerViewSwipeManager.release();

        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(null);

        if (wrappedAdapter != null)
            WrapperAdapterUtils.releaseAll(wrappedAdapter);

        recyclerView = null;
        recyclerViewSwipeManager = null;
        wrappedAdapter = null;
        eventRecycler = null;
        generalItemAnimator = null;
        noEventsRelativeLayout = null;
        snackbar = null;
        linearLayoutManager = null;

        super.onDestroyView();

    }

    private void updateUI() {

        if (!DataSingleton.getInstance().getDataArrayList().isEmpty()) {

            noEventsRelativeLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerViewSwipeManager = new RecyclerViewSwipeManager();

            eventRecycler = new EventRecycler(getActivity(), recyclerView);
            eventRecycler.setEventListener(new EventRecycler.EventListener() {

                @Override
                public void onItemRemoved() {

                    showSnackbar();

                }

            });

            wrappedAdapter = recyclerViewSwipeManager.createWrappedAdapter(eventRecycler);

            generalItemAnimator = new SwipeDismissItemAnimator();
            generalItemAnimator.setSupportsChangeAnimations(false);

            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

            recyclerView.setAdapter(wrappedAdapter);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(generalItemAnimator);
            recyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getActivity(), R.drawable.line_divider), true));

            recyclerViewSwipeManager.attachRecyclerView(recyclerView);

        } else {

            recyclerView.setVisibility(View.GONE);
            noEventsRelativeLayout.setVisibility(View.VISIBLE);

        }

    }

    private void showSnackbar() {

        DataSingleton.getInstance().setReactToBroadcastEvents(false);

        deleteEvent();

        snackbar = Snackbar.make(getView().findViewById(R.id.fragment_events_container_relative_layout), "1 event done!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                undoLastItemRemoved();

            }

        });

        snackbar.setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {

                super.onDismissed(snackbar, event);

                if (event != DISMISS_EVENT_ACTION)
                    deleteEvent();

            }

        });

        snackbar.setActionTextColor(ContextCompat.getColor(getContext(), R.color.snackbar_action_color_done));
        snackbar.show();

        lastItemRemoved = DataSingleton.getInstance().getDataSparseArrayLastRemoved();

    }

    private void deleteEvent() {

        if (lastItemRemoved.size() != 0) {

            DatabaseHelper databaseHelper = new DatabaseHelper(context);

            try {

                if (lastItemRemoved.size() == 2)
                    databaseHelper.deleteEvent(((EventWithID) lastItemRemoved.valueAt(1)).getEvent());
                else
                    databaseHelper.deleteEvent(((EventWithID) lastItemRemoved.valueAt(0)).getEvent());

            } finally {

                databaseHelper.close();

            }

            if (!isVisible()) {

                context = null;

                DataSingleton.getInstance().setReactToBroadcastEvents(true);
                EventBus.getDefault().post(new Update(false, true, false, false));

            } else {

                if (DataSingleton.getInstance().getDataArrayList().isEmpty() && snackbar != null)
                    if (!snackbar.isShown())
                        updateUI();

            }

            lastItemRemoved = new SparseArray<>();

        } else {

            if (snackbar != null)
                if (!snackbar.isShown())
                    updateUI();

        }

    }

    private void undoLastItemRemoved() {

        if (DataSingleton.getInstance().getDataSparseArrayLastRemoved().size() == 2) {

            ArrayList<Object> dataArrayList = new ArrayList<>();

            dataArrayList.add(DataSingleton.getInstance().getDataSparseArrayLastRemoved().valueAt(0));
            dataArrayList.add(DataSingleton.getInstance().getDataSparseArrayLastRemoved().valueAt(1));

            DataSingleton.getInstance().getDataArrayList().addAll(DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0), dataArrayList);

            eventRecycler.notifyItemRangeInserted(DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0), DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0) + 1);

        } else {

            DataSingleton.getInstance().getDataArrayList().add(DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0), DataSingleton.getInstance().getDataSparseArrayLastRemoved().valueAt(0));

            eventRecycler.notifyItemInserted(DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0));

        }

        lastItemRemoved = new SparseArray<>();

    }

}
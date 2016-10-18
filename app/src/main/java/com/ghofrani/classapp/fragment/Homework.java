package com.ghofrani.classapp.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.HomeworkList;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.model.HomeworkWithID;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.DatabaseHelper;
import com.ghofrani.classapp.module.LinearLayoutManagerSmoothScroller;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class Homework extends Fragment {

    private RecyclerView recyclerView;
    private CardView noHomeworkCardView;
    private HomeworkList homeworkList;
    private RecyclerViewSwipeManager recyclerViewSwipeManager;
    private RecyclerView.Adapter wrappedAdapter;
    private GeneralItemAnimator generalItemAnimator;
    private Snackbar snackbar;
    private LinearLayoutManagerSmoothScroller linearLayoutManager;
    private SparseArray<Object> lastItemRemoved;

    @Subscribe
    public void onEvent(com.ghofrani.classapp.event.UpdateHomeworkUI updateHomeworkUI) {

        if (homeworkList != null)
            homeworkList.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_homework, container, false);

    }

    @Override
    public void onResume() {

        super.onResume();

        if (recyclerView == null)
            recyclerView = (RecyclerView) getView().findViewById(R.id.homework_recycler_view);

        if (noHomeworkCardView == null)
            noHomeworkCardView = (CardView) getView().findViewById(R.id.homework_no_homework_card);

        updateUI();

        EventBus.getDefault().register(this);

    }

    @Override
    public void onPause() {

        if (snackbar != null)
            if (snackbar.isShown())
                snackbar.dismiss();

        EventBus.getDefault().unregister(this);

        DataSingleton.getInstance().setReactToBroadcastHomework(true);
        EventBus.getDefault().post(new Update(false, false, false, true));

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
        homeworkList = null;
        generalItemAnimator = null;
        snackbar = null;
        linearLayoutManager = null;

        noHomeworkCardView = null;

        super.onDestroyView();

    }

    private void updateUI() {

        if (!DataSingleton.getInstance().getDataArrayList().isEmpty()) {

            noHomeworkCardView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerViewSwipeManager = new RecyclerViewSwipeManager();

            homeworkList = new HomeworkList(getContext());
            homeworkList.setEventListener(new HomeworkList.EventListener() {

                @Override
                public void onItemRemoved() {

                    //linearLayoutManager.smoothScrollToPosition(recyclerView, null, 0);

                    showSnackbar();

                }

            });

            wrappedAdapter = recyclerViewSwipeManager.createWrappedAdapter(homeworkList);

            generalItemAnimator = new SwipeDismissItemAnimator();
            generalItemAnimator.setSupportsChangeAnimations(false);

            linearLayoutManager = new LinearLayoutManagerSmoothScroller(getContext());

            recyclerView.setAdapter(wrappedAdapter);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(generalItemAnimator);
            recyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.line_divider), true));

            recyclerViewSwipeManager.attachRecyclerView(recyclerView);

        } else {

            noHomeworkCardView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }

    }

    private void showSnackbar() {

        DataSingleton.getInstance().setReactToBroadcastHomework(false);

        if (snackbar != null)
            if (snackbar.isShown())
                snackbar.dismiss();

        snackbar = Snackbar.make(getView().findViewById(R.id.homework_container), "1 homework done!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                snackbar.setCallback(null);

                undoLastItemRemoved();

            }

        });

        snackbar.setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {

                super.onDismissed(snackbar, event);

                deleteHomework();

            }

        });

        snackbar.setActionTextColor(ContextCompat.getColor(getContext(), R.color.snackbar_action_color_done));
        snackbar.show();

        lastItemRemoved = DataSingleton.getInstance().getDataSparseArrayLastRemoved();

    }

    private void deleteHomework() {

        if (lastItemRemoved.size() != 0) {

            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

            try {

                if (lastItemRemoved.size() == 2)
                    databaseHelper.deleteHomework(((HomeworkWithID) lastItemRemoved.get(lastItemRemoved.keyAt(1))).getHomework());
                else
                    databaseHelper.deleteHomework(((HomeworkWithID) lastItemRemoved.get(lastItemRemoved.keyAt(0))).getHomework());

            } finally {

                databaseHelper.close();

            }

        }

    }

    private void undoLastItemRemoved() {

        if (DataSingleton.getInstance().getDataSparseArrayLastRemoved().size() == 2) {

            ArrayList<Object> dataArrayList = new ArrayList<>();

            dataArrayList.add(DataSingleton.getInstance().getDataSparseArrayLastRemoved().get(DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0)));
            dataArrayList.add(DataSingleton.getInstance().getDataSparseArrayLastRemoved().get(DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(1)));

            DataSingleton.getInstance().getDataArrayList().addAll(DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0), dataArrayList);

            homeworkList.notifyItemRangeInserted(DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0), DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0) + 1);

        } else {

            DataSingleton.getInstance().getDataArrayList().add(DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0), DataSingleton.getInstance().getDataSparseArrayLastRemoved().get(DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0)));

            homeworkList.notifyItemInserted(DataSingleton.getInstance().getDataSparseArrayLastRemoved().keyAt(0));

        }

        DataSingleton.getInstance().setDataSparseArrayLastRemoved(new SparseArray<>());

    }

}
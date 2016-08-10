package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ViewClass;
import com.ghofrani.classapp.model.SlimClass;

import java.util.LinkedList;

public class AllClassesList extends RecyclerView.Adapter<AllClassesList.ClassViewHolder> {

    private final LinkedList<SlimClass> slimClassLinkedList;
    private final Context context;

    public AllClassesList(LinkedList<SlimClass> slimClassLinkedList, Context context) {

        this.slimClassLinkedList = slimClassLinkedList;
        this.context = context;

    }

    @Override
    public int getItemCount() {

        return slimClassLinkedList.size();

    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        return new ClassViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_class_card, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(ClassViewHolder classViewHolder, int position) {

        classViewHolder.titleTextView.setText(slimClassLinkedList.get(position).getName());
        classViewHolder.teacherLocationTextView.setText(slimClassLinkedList.get(position).getTeacher() + ", " + slimClassLinkedList.get(position).getLocation());

        classViewHolder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                TextView titleTextView = (TextView) view.findViewById(R.id.view_class_card_title);

                context.startActivity(new Intent(context, ViewClass.class).putExtra("class", titleTextView.getText()));

            }

        });

    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {

        final CardView cardView;
        final TextView titleTextView;
        final TextView teacherLocationTextView;

        ClassViewHolder(View itemView) {

            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.view_class_card);
            titleTextView = (TextView) itemView.findViewById(R.id.view_class_card_title);
            teacherLocationTextView = (TextView) itemView.findViewById(R.id.view_class_card_teacher_location);

        }

    }

}
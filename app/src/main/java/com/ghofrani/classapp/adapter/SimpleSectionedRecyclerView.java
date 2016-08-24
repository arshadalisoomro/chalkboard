package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;

public class SimpleSectionedRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SECTION_TYPE = 0;
    private final Context context;
    private boolean isValid = true;
    private int sectionResourceID;
    private int textResourceID;

    private RecyclerView.Adapter baseAdapter;
    private SparseArray<Section> sectionsSparseArray = new SparseArray<Section>();

    public SimpleSectionedRecyclerView(Context context, int sectionResourceID, int textResourceID, RecyclerView.Adapter baseAdapter) {

        this.sectionResourceID = sectionResourceID;
        this.textResourceID = textResourceID;
        this.baseAdapter = baseAdapter;
        this.context = context;

        this.baseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {

                isValid = SimpleSectionedRecyclerView.this.baseAdapter.getItemCount() > 0;
                notifyDataSetChanged();

            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {

                isValid = SimpleSectionedRecyclerView.this.baseAdapter.getItemCount() > 0;
                notifyItemRangeChanged(positionStart, itemCount);

            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {

                isValid = SimpleSectionedRecyclerView.this.baseAdapter.getItemCount() > 0;
                notifyItemRangeInserted(positionStart, itemCount);

            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {

                isValid = SimpleSectionedRecyclerView.this.baseAdapter.getItemCount() > 0;
                notifyItemRangeRemoved(positionStart, itemCount);

            }

        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {

        if (typeView == SECTION_TYPE) {

            final View view = LayoutInflater.from(context).inflate(sectionResourceID, parent, false);
            return new SectionViewHolder(view, textResourceID);

        } else {

            return baseAdapter.onCreateViewHolder(parent, typeView - 1);

        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder sectionViewHolder, int position) {

        if (isSectionHeaderPosition(position)) {

            ((SectionViewHolder) sectionViewHolder).title.setText(sectionsSparseArray.get(position).title);

        } else {

            baseAdapter.onBindViewHolder(sectionViewHolder, sectionedPositionToPosition(position));

        }

    }

    @Override
    public int getItemViewType(int position) {

        return isSectionHeaderPosition(position) ? SECTION_TYPE : baseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1;

    }

    public void setSections(Section[] sections) {

        sectionsSparseArray.clear();

        Arrays.sort(sections, new Comparator<Section>() {

            @Override
            public int compare(Section o, Section o1) {

                return (o.firstPosition == o1.firstPosition) ? 0 : ((o.firstPosition < o1.firstPosition) ? -1 : 1);

            }

        });

        int offset = 0;

        for (Section section : sections) {

            section.sectionedPosition = section.firstPosition + offset;
            sectionsSparseArray.append(section.sectionedPosition, section);

            ++offset;

        }

        notifyDataSetChanged();

    }

    public int positionToSectionedPosition(int position) {

        int offset = 0;

        for (int i = 0; i < sectionsSparseArray.size(); i++) {

            if (sectionsSparseArray.valueAt(i).firstPosition > position)
                break;

            ++offset;

        }

        return position + offset;

    }

    public int sectionedPositionToPosition(int sectionedPosition) {

        if (isSectionHeaderPosition(sectionedPosition))
            return RecyclerView.NO_POSITION;

        int offset = 0;

        for (int i = 0; i < sectionsSparseArray.size(); i++) {

            if (sectionsSparseArray.valueAt(i).sectionedPosition > sectionedPosition)
                break;

            --offset;

        }

        return sectionedPosition + offset;

    }

    public boolean isSectionHeaderPosition(int position) {

        return sectionsSparseArray.get(position) != null;

    }

    @Override
    public long getItemId(int position) {

        return isSectionHeaderPosition(position) ? Integer.MAX_VALUE - sectionsSparseArray.indexOfKey(position) : baseAdapter.getItemId(sectionedPositionToPosition(position));

    }

    @Override
    public int getItemCount() {

        return (isValid ? baseAdapter.getItemCount() + sectionsSparseArray.size() : 0);

    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public SectionViewHolder(View view, int textResourceID) {

            super(view);
            title = (TextView) view.findViewById(textResourceID);

        }

    }

    public static class Section {

        int firstPosition;
        int sectionedPosition;
        CharSequence title;

        public Section(int firstPosition, CharSequence title) {

            this.firstPosition = firstPosition;
            this.title = title;

        }

        public CharSequence getTitle() {

            return title;

        }

    }

}
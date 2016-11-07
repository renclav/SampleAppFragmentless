package com.touchnote.renclav.touchnotesampleapp.clues.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.touchnote.renclav.touchnotesampleapp.R;
import com.touchnote.renclav.touchnotesampleapp.clues.CluesActivity;
import com.touchnote.renclav.touchnotesampleapp.clues.presenter.CluesPresenter;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renclav on 2016/11/04.
 */

public class CluesRecyclerView extends RecyclerView {

    private CluesRecyclerViewAdapter adapter;

    public CluesRecyclerView(Context context) {
        super(context);
    }

    public CluesRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CluesRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(linearLayoutManager);
        adapter = new CluesRecyclerViewAdapter(new ArrayList<Clue>());
        setAdapter(adapter);
    }

    public boolean isGridLayout()
    {
        return adapter.isGridLayout();
    }

    public void setClues(List<Clue> clues) {
        if (adapter != null) {
            adapter.setClues(clues);
        }
    }

    public void toggleLayout() {
        if (adapter.isGridLayout()) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            setLayoutManager(linearLayoutManager);
            adapter.setGridLayout(false);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            setLayoutManager(gridLayoutManager);
            adapter.setGridLayout(true);
        }
    }

    private class CluesRecyclerViewAdapter
            extends RecyclerView.Adapter<CluesRecyclerViewAdapter.ViewHolder> {

        private final List<Clue> clues;

        private boolean isGridLayout;

        private int VIEW_TYPE_LIST = 0;
        private int VIEW_TYPE_GRID = 1;

        public CluesRecyclerViewAdapter(List<Clue> clues) {
            this.clues = clues;
        }

        public void setClues(List<Clue> clues) {
            this.clues.clear();
            this.clues.addAll(clues);
            notifyDataSetChanged();
        }

        public boolean isGridLayout() {
            return isGridLayout;
        }

        public void setGridLayout(boolean gridLayout) {
            isGridLayout = gridLayout;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(
                            viewType == VIEW_TYPE_GRID
                                    ? R.layout.cluse_grid_content
                                    : R.layout.clue_list_content
                            , parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.bind(clues.get(position));
        }

        @Override
        public int getItemCount() {
            return clues.size();
        }

        @Override
        public int getItemViewType(int position) {
            return isGridLayout ? VIEW_TYPE_GRID : VIEW_TYPE_LIST;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View view;
            public final TextView titleView;
            public final TextView descriptionView;
            public final ImageView imageView;
            public Clue clue;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                this.titleView = (TextView) view.findViewById(R.id.title);
                this.descriptionView = (TextView) view.findViewById(R.id.description);
                this.imageView = (ImageView) view.findViewById(R.id.image);
            }

            public void bind(final Clue clue) {
                this.clue = clue;
                this.titleView.setText(clue.getTitle());
                if(this.descriptionView != null){
                    this.descriptionView.setText(clue.getDescription());
                }
                if (TextUtils.isEmpty(clue.getImage())) {
                    Glide.clear(this.imageView);
                    this.imageView.setImageDrawable(null);
                } else {
                    Glide.with(this.view.getContext())
                            .load(clue.getImage())
                            .placeholder(R.drawable.ic_timelapse_black_24dp)
                            .error(R.drawable.ic_block_black_24dp)
                            .into(this.imageView);
                }
                this.view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CluesActivity activity = (CluesActivity) getContext();
                        CluesPresenter cluesPresenter = activity.getCluesPresenter();
                        cluesPresenter.openClueDetails(clue.getId());

                    }
                });
            }

            @Override
            public String toString() {
                return super.toString() + " '" + descriptionView.getText() + "'";
            }
        }
    }


    //Layout Editor fix, see
    //http://stackoverflow.com/questions/40161934/exception-raised-during-rendering-unable-to-locate-mode-0
    @Override
    protected void onAttachedToWindow() {
        if (!isInEditMode()) {
            super.onAttachedToWindow();
        }
    }
}

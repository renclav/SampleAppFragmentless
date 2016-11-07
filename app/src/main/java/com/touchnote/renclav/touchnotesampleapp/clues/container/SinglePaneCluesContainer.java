package com.touchnote.renclav.touchnotesampleapp.clues.container;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.touchnote.renclav.touchnotesampleapp.R;
import com.touchnote.renclav.touchnotesampleapp.clues.CluesContract;
import com.touchnote.renclav.touchnotesampleapp.clues.MenuStates;
import com.touchnote.renclav.touchnotesampleapp.clues.views.ClueDetailView;
import com.touchnote.renclav.touchnotesampleapp.clues.views.CluesRecyclerView;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.BaseSchedulerProvider;

import java.util.List;

/**
 * Created by Renclav on 2016/11/03.
 */

public class SinglePaneCluesContainer extends FrameLayout implements CluesContainer {

    private CluesRecyclerView cluesRecyclerView;
    private ClueDetailView detailView;
    private CluesContract.Presenter presenter;

    private ContentLoadingProgressBar progressBar;

    private BaseSchedulerProvider schedulerProvider;

    public SinglePaneCluesContainer(Context context) {
        super(context);
    }

    public SinglePaneCluesContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SinglePaneCluesContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SinglePaneCluesContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        cluesRecyclerView = (CluesRecyclerView) findViewById(R.id.clue_list);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_view);
    }

    @Override
    public boolean onBackPressed() {
        if (!cluesRecyclerViewAttached()) {
            detailView.setCluesContainer(null);
            removeView(detailView);
            addView(cluesRecyclerView);
            presenter.updateActivityMenuState(cluesRecyclerView.isGridLayout() ? MenuStates.GRID : MenuStates.LIST);
            presenter.setDetailViewIsVisibleWithClue(false, null);
            return true;
        }
        return false;
    }

    private boolean cluesRecyclerViewAttached() {
        return cluesRecyclerView.getParent() != null;
    }

    private boolean cluesDetailViewAttached() {
        return detailView != null && detailView.getParent() != null;
    }

    private void setupDetailViewifNull() {
        if (detailView == null) {
            detailView = (ClueDetailView) View.inflate(getContext(), R.layout.clue_detail, this).findViewById(R.id.clue_detail);
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            progressBar.show();
        } else {
            progressBar.hide();
        }
    }

    @Override
    public void showClues(List<Clue> clues) {
        cluesRecyclerView.setClues(clues);
    }

    @Override
    public void showClueDetailsUi(Clue clue) {
        if (cluesRecyclerViewAttached()) {
            removeView(cluesRecyclerView);
        }
        setupDetailViewifNull();
        if (!cluesDetailViewAttached()) {
            addView(detailView);
        }
        detailView.setClueWithSchedulerProvider(clue, schedulerProvider);
        detailView.setCluesContainer(this);
        presenter.setDetailViewIsVisibleWithClue(true, clue);
        presenter.updateActivityMenuState(MenuStates.HIDDEN);
    }

    @Override
    public void showLoadingCluesError() {

        final TextView textView = (TextView) findViewById(R.id.errortextView);
        textView.setText("Sorry, please tap to try again");
        textView.setVisibility(VISIBLE);
        textView.setClickable(true);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loadClues(true);
                textView.setOnClickListener(null);
                textView.setVisibility(GONE);
            }
        });
    }

    @Override
    public void showNoClues() {

    }

    @Override
    public void toggleListLayout() {
        cluesRecyclerView.toggleLayout();
        if (cluesRecyclerViewAttached()) {
            presenter.updateActivityMenuState(cluesRecyclerView.isGridLayout() ? MenuStates.GRID : MenuStates.LIST);
        } else {
            presenter.updateActivityMenuState(MenuStates.HIDDEN);
        }
    }

    @Override
    public void setPresenter(CluesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setSchedulerProvider(BaseSchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public void updateSelectedClue(Clue clue) {
        presenter.setDetailViewIsVisibleWithClue(true, clue);
    }
}
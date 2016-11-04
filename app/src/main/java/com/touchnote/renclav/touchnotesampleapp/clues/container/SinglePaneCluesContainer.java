package com.touchnote.renclav.touchnotesampleapp.clues.container;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.touchnote.renclav.touchnotesampleapp.R;
import com.touchnote.renclav.touchnotesampleapp.clues.CluesContract;
import com.touchnote.renclav.touchnotesampleapp.clues.MenuStates;
import com.touchnote.renclav.touchnotesampleapp.clues.views.CluesRecyclerView;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;

import java.util.List;

/**
 * Created by Renclav on 2016/11/03.
 */

public class SinglePaneCluesContainer extends FrameLayout implements CluesContainer {

    private CluesRecyclerView cluesRecyclerView;
    private CluesContract.Presenter presenter;

    private ContentLoadingProgressBar progressBar;

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

    public boolean onBackPressed() {
        if (!cluesRecyclerViewAttached()) {
            removeViewAt(0);
            addView(cluesRecyclerView);
            return true;
        }
        return false;
    }

    private boolean cluesRecyclerViewAttached() {
        return cluesRecyclerView.getParent() != null;
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
    public void showClueDetailsUi(String clueId) {
        if (cluesRecyclerViewAttached()) {
            removeViewAt(0);
            View.inflate(getContext(), R.layout.clue_detail, this);
        }
        //MyDetailView detailView = (MyDetailView) getChildAt(0);
        //detailView.setItem(item);
    }

    @Override
    public void showLoadingCluesError() {
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
}
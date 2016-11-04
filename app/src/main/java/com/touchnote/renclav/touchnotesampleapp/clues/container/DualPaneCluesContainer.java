package com.touchnote.renclav.touchnotesampleapp.clues.container;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.touchnote.renclav.touchnotesampleapp.R;
import com.touchnote.renclav.touchnotesampleapp.clues.CluesContract;
import com.touchnote.renclav.touchnotesampleapp.clues.MenuStates;
import com.touchnote.renclav.touchnotesampleapp.clues.views.CluesRecyclerView;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;

import java.util.List;

/**
 * Created by Renclav on 2016/11/03.
 */

public class DualPaneCluesContainer extends LinearLayout implements CluesContainer {

    private View detailView;
    private CluesRecyclerView cluesRecyclerView;
    private CluesContract.Presenter presenter;

    private ContentLoadingProgressBar progressBar;

    public DualPaneCluesContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        detailView = findViewById(R.id.clue_detail);
        cluesRecyclerView = (CluesRecyclerView) findViewById(R.id.clue_list);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_view);
    }

    public boolean onBackPressed() {
        return false;
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
        presenter.updateActivityMenuState(cluesRecyclerView.isGridLayout() ? MenuStates.GRID : MenuStates.LIST);
    }

    @Override
    public void setPresenter(CluesContract.Presenter presenter) {
        this.presenter = presenter;
    }
}

package com.touchnote.renclav.touchnotesampleapp.clue.container;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.touchnote.renclav.touchnotesampleapp.R;
import com.touchnote.renclav.touchnotesampleapp.clue.contract.CluesContract;
import com.touchnote.renclav.touchnotesampleapp.clue.MenuStates;
import com.touchnote.renclav.touchnotesampleapp.clue.view.ClueDetailView;
import com.touchnote.renclav.touchnotesampleapp.clue.view.CluesRecyclerView;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.BaseSchedulerProvider;

import java.util.List;

/**
 * Created by Renclav on 2016/11/03.
 * Master detail container
 */
public class DualPaneCluesContainer extends LinearLayout implements CluesContainer {

    private ClueDetailView detailView;
    private CluesRecyclerView cluesRecyclerView;
    private CluesContract.Presenter presenter;

    private ContentLoadingProgressBar progressBar;

    private BaseSchedulerProvider schedulerProvider;

    public DualPaneCluesContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        detailView = (ClueDetailView) findViewById(R.id.clue_detail);
        detailView.showNoSelection();
        cluesRecyclerView = (CluesRecyclerView) findViewById(R.id.clue_list);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_view);
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

        detailView.setClueWithSchedulerProvider(clue, schedulerProvider);
        presenter.setDetailViewIsVisibleWithClue(true, clue);

    }

    @Override
    public void showLoadingCluesError() {
        final TextView textView = (TextView) findViewById(R.id.errortextView);
        textView.setText("Sorry, please try again");
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
        presenter.updateActivityMenuState(cluesRecyclerView.isGridLayout() ? MenuStates.GRID : MenuStates.LIST);
    }

    @Override
    public void setPresenter(CluesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public boolean onBackPressed() {
        return false;
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

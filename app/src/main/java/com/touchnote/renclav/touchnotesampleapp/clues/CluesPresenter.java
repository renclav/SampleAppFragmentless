package com.touchnote.renclav.touchnotesampleapp.clues;

import android.support.annotation.NonNull;

import com.touchnote.renclav.touchnotesampleapp.clues.container.CluesContainer;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;
import com.touchnote.renclav.touchnotesampleapp.data.source.CluesRepository;
import com.touchnote.renclav.touchnotesampleapp.util.EspressoIdlingResource;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.BaseSchedulerProvider;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Renclav on 2016/11/04.
 */

public class CluesPresenter implements CluesContract.Presenter {


    @NonNull
    private final CluesRepository cluesRepository;

    @NonNull
    private final CluesContract.View cluesView;

    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    private final WeakReference<ClueListActivity> clueListActivityWeakReference;

    private boolean mFirstLoad = true;

    @NonNull
    private CompositeSubscription subscriptions;
    private Subscription loadClueSubscription;

    public CluesPresenter(@NonNull CluesRepository cluesRepository,
                          @NonNull CluesContainer cluesView,
                          @NonNull BaseSchedulerProvider schedulerProvider, ClueListActivity activity) {

        this.cluesRepository = cluesRepository;
        this.cluesView = cluesView;
        this.schedulerProvider = schedulerProvider;
        subscriptions = new CompositeSubscription();
        cluesView.setPresenter(this);
        this.clueListActivityWeakReference = new WeakReference<>(activity);
    }


    @Override
    public void loadClues(boolean forceUpdate) {
        loadClues(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    //Ignoring forceupdate for now
    private void loadClues(final boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            cluesView.setLoadingIndicator(true);
        }

        if (forceUpdate) {
            cluesRepository.refreshClues();
        }

        EspressoIdlingResource.increment();

        subscriptions.clear();
        subscriptions.add(
                cluesRepository
                        .getClues()
                        .subscribeOn(schedulerProvider.computation())
                        .observeOn(schedulerProvider.ui())
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                    EspressoIdlingResource.decrement(); // Set app as idle.
                                }
                            }
                        })
                        .subscribe(new Observer<List<Clue>>() {
                            @Override
                            public void onCompleted() {
                                cluesView.setLoadingIndicator(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                cluesView.showLoadingCluesError();
                            }

                            @Override
                            public void onNext(List<Clue> clues) {
                                processClues(clues);
                            }
                        })
        );
    }

    private void processClues(@NonNull List<Clue> clues) {
        if (clues.isEmpty()) {
            cluesView.showNoClues();
        } else {
            // Show the list of tasks
            cluesView.showClues(clues);
        }
    }

    @Override
    public void openClueDetails(@NonNull Clue requestedClue) {

        EspressoIdlingResource.increment();
        subscriptions.add(
                cluesRepository
                        .getClue(requestedClue.getId())
                        .subscribeOn(schedulerProvider.computation())
                        .observeOn(schedulerProvider.ui())
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                    EspressoIdlingResource.decrement(); // Set app as idle.
                                }
                            }
                        })
                        .subscribe(new Observer<Clue>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Clue clue) {
                                cluesView.showClueDetailsUi(clue);
                            }
                        }));
    }

    @Override
    public void toggleListLayout() {
        cluesView.toggleListLayout();
    }

    @Override
    public void updateActivityMenuState(@MenuStates.MenuState int state) {
        ClueListActivity clueListActivity = clueListActivityWeakReference.get();
        if (clueListActivity != null) {
            clueListActivity.setMenuState(state);
        }
    }

    @Override
    public void subscribe() {
        loadClues(false);
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }
}

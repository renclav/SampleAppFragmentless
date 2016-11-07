package com.touchnote.renclav.touchnotesampleapp.clues.presenter;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.touchnote.renclav.touchnotesampleapp.clues.MenuStates;
import com.touchnote.renclav.touchnotesampleapp.clues.container.CluesContainer;
import com.touchnote.renclav.touchnotesampleapp.clues.contract.CluesActivityContract;
import com.touchnote.renclav.touchnotesampleapp.clues.contract.CluesContract;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;
import com.touchnote.renclav.touchnotesampleapp.data.source.CluesRepository;
import com.touchnote.renclav.touchnotesampleapp.util.EspressoIdlingResource;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.BaseSchedulerProvider;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observer;
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

    private final WeakReference<CluesActivityContract> cluesActivityInterfaceWeakReference;

    private boolean mFirstLoad = true;

    private Clue selectedClue;

    private Clue temporySavedClue;

    @NonNull
    private CompositeSubscription subscriptions;

    public CluesPresenter(@NonNull CluesRepository cluesRepository,
                          @NonNull CluesContainer cluesView,
                          @NonNull BaseSchedulerProvider schedulerProvider, CluesActivityContract cluesActivityInterfacerface) {

        this.cluesRepository = cluesRepository;
        this.cluesView = cluesView;
        this.schedulerProvider = schedulerProvider;
        subscriptions = new CompositeSubscription();
        cluesView.setPresenter(this);
        cluesView.setSchedulerProvider(schedulerProvider);
        this.cluesActivityInterfaceWeakReference = new WeakReference<>(cluesActivityInterfacerface);
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
                                cluesView.setLoadingIndicator(false);
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
        if(temporySavedClue != null)
        {
            cluesView.showClueDetailsUi(temporySavedClue);
            temporySavedClue = null;
        }
    }

    @Override
    public void openClueDetails(@NonNull String requestedClueId) {

        EspressoIdlingResource.increment();
        subscriptions.add(
                cluesRepository
                        .getClue(requestedClueId)
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
        CluesActivityContract cluesActivity = cluesActivityInterfaceWeakReference.get();
        if (cluesActivity != null) {
            cluesActivity.setMenuState(state);
        }
    }

    @Override
    public void setDetailViewIsVisibleWithClue(boolean visible, @Nullable Clue clue) {
        if(visible)
        {
            selectedClue = clue;
        } else {
            selectedClue = null;
        }
    }

    @Override
    public Parcelable getSaveStateParcelable() {
        return selectedClue;
    }

    @Override
    public void restoreSaveStateParcelable(Parcelable stateParcelable) {
        if(stateParcelable != null)
        {
            temporySavedClue = (Clue) stateParcelable;
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

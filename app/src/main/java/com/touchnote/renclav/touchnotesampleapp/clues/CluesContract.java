package com.touchnote.renclav.touchnotesampleapp.clues;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.touchnote.renclav.touchnotesampleapp.BasePresenter;
import com.touchnote.renclav.touchnotesampleapp.BaseView;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;

import java.util.List;

/**
 * Created by Renclav on 2016/11/04.
 */

public interface CluesContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showClues(List<Clue> clues);

        void showClueDetailsUi(Clue clue);

        void showLoadingCluesError();

        void showNoClues();

        void toggleListLayout();

    }

    interface Presenter extends BasePresenter {

        void loadClues(boolean forceUpdate);

        void openClueDetails(@NonNull String requestedClueId);

        void toggleListLayout();

        void updateActivityMenuState(@MenuStates.MenuState int state);

        void setDetailViewIsVisibleWithClue(boolean visible, @Nullable Clue clue);

        Parcelable getSaveStateParcelable();

        void restoreSaveStateParcelable(Parcelable stateParcelable);
    }
}

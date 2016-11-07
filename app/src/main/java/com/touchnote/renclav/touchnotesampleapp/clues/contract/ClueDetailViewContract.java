package com.touchnote.renclav.touchnotesampleapp.clues.contract;

import com.touchnote.renclav.touchnotesampleapp.clues.container.CluesContainer;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.BaseSchedulerProvider;

/**
 * Created by Renclav on 2016/11/07.
 */

public interface ClueDetailViewContract {

    void setCluesContainer(CluesContainer cluesContainer);
    void setClueWithSchedulerProvider(Clue clue, BaseSchedulerProvider schedulerProvider);
}

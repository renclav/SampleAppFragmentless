package com.touchnote.renclav.touchnotesampleapp.clues.container;

import com.touchnote.renclav.touchnotesampleapp.clues.contract.CluesContract;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.BaseSchedulerProvider;

/**
 * Created by Renclav on 2016/11/03.
 */

public interface CluesContainer extends CluesContract.View{

    boolean onBackPressed();
    void setSchedulerProvider(BaseSchedulerProvider schedulerProvider);
    void updateSelectedClue(Clue clue);
}

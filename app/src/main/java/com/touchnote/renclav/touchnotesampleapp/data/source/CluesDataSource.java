package com.touchnote.renclav.touchnotesampleapp.data.source;

import android.support.annotation.NonNull;

import com.touchnote.renclav.touchnotesampleapp.data.Clue;

import java.util.List;

import rx.Observable;

/**
 * Created by Renclav on 2016/11/03.
 */

public interface CluesDataSource {

    Observable<List<Clue>> getClues();

    Observable<Clue> getClue(@NonNull String clueId);

    void saveClues(List<Clue> clues);

    void refreshClues();
}

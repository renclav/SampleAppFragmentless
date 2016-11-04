package com.touchnote.renclav.touchnotesampleapp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import com.touchnote.renclav.touchnotesampleapp.data.Clue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Renclav on 2016/11/03.
 */

public class CluesRepository implements CluesDataSource {

    @Nullable
    private static CluesRepository INSTANCE = null;

    @NonNull
    private final CluesDataSource cluesRemoteDataSource;

    @NonNull
    private final CluesDataSource cluesLocalDataSource;


    @VisibleForTesting
    @Nullable
    List<Clue> mCachedClues;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    @VisibleForTesting
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private CluesRepository(@NonNull CluesDataSource cluesRemoteDataSource,
                            @NonNull CluesDataSource cluesLocalDataSource) {

        if (cluesRemoteDataSource == null) {
            throw new NullPointerException();
        }
        if (cluesLocalDataSource == null) {
            throw new NullPointerException();
        }
        this.cluesRemoteDataSource = cluesRemoteDataSource;
        this.cluesLocalDataSource = cluesLocalDataSource;
    }

    public static CluesRepository getInstance(@NonNull CluesDataSource cluesRemoteDataSource,
                                              @NonNull CluesDataSource cluesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CluesRepository(cluesRemoteDataSource, cluesLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(CluesDataSource, CluesDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public Observable<List<Clue>> getClues() {
        // Respond immediately with cache if available and not dirty
        if (mCachedClues != null && !mCacheIsDirty) {
            return Observable.from(mCachedClues).toList();
        } else if (mCachedClues == null) {
            mCachedClues = new ArrayList<>();
        }

        Observable<List<Clue>> remoteClues = getAndSaveRemoteClues();

        if (mCacheIsDirty) {
            return remoteClues;
        } else {
            // Query the local storage if available. If not, query the network.
            Observable<List<Clue>> localClues = getAndCacheLocalClues();
            return Observable.concat(localClues, remoteClues)
                    .filter(new Func1<List<Clue>, Boolean>() {
                        @Override
                        public Boolean call(List<Clue> tasks) {
                            return !tasks.isEmpty();
                        }
                    }).first();
        }
    }

    @Override
    public Observable<Clue> getClue(@NonNull final String clueId) {

        final Clue cachedClue = getClueWithId(clueId);

        // Respond immediately with cache if available
        if (cachedClue != null) {
            return Observable.just(cachedClue);
        }

        // Load from server/persisted if needed.

        // Do in memory cache update to keep the app UI up to date
        if (mCachedClues == null) {
            mCachedClues = new ArrayList<>();
        }

        // Is the task in the local data source? If not, query the network.
        Observable<Clue> localTask = getClueWithIdFromLocalRepository(clueId);
        Observable<Clue> remoteTask = cluesRemoteDataSource
                .getClue(clueId);

        return Observable.concat(localTask, remoteTask).first()
                .map(new Func1<Clue, Clue>() {
                    @Override
                    public Clue call(Clue clue) {
                        if (clue == null) {
                            throw new NoSuchElementException("No clue found with clueId " + clueId);
                        }
                        return clue;
                    }
                });

    }

    @Override
    public void saveClues(List<Clue> clues) {
        cluesRemoteDataSource.saveClues(clues);
        cluesLocalDataSource.saveClues(clues);

        resetCachedClues(clues);
    }

    @Override
    public void refreshClues() {
        mCacheIsDirty = true;
    }

    private Observable<List<Clue>> getAndSaveRemoteClues() {
        return cluesRemoteDataSource
                .getClues()
                .doOnNext(new Action1<List<Clue>>() {
                    @Override
                    public void call(List<Clue> clues) {
                        cluesLocalDataSource.saveClues(clues);
                        resetCachedClues(clues);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        mCacheIsDirty = false;
                    }
                });
    }

    @NonNull
    Observable<Clue> getClueWithIdFromLocalRepository(@NonNull final String clueId) {
        return cluesLocalDataSource
                .getClue(clueId)
                .first();
    }

    private Observable<List<Clue>> getAndCacheLocalClues() {
        return cluesLocalDataSource.getClues()
                .doOnNext(new Action1<List<Clue>>() {
                    @Override
                    public void call(List<Clue> clues) {
                        resetCachedClues(clues);
                    }
                });
    }

    @Nullable
    private Clue getClueWithId(@NonNull String id) {
        if (mCachedClues == null || mCachedClues.isEmpty()) {
            return null;
        } else {
            for (Clue clue : mCachedClues) {
                if(TextUtils.equals(clue.getId(), id))
                {
                    return clue;
                }
            }
            return null;
        }
    }

    private void resetCachedClues(List<Clue> clues)
    {
        if (mCachedClues == null) {
            mCachedClues = new ArrayList<>();
        }
        mCachedClues.clear();
        mCachedClues.addAll(clues);
    }
}

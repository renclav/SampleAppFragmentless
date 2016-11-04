package com.touchnote.renclav.touchnotesampleapp.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;
import com.touchnote.renclav.touchnotesampleapp.data.source.CluesDataSource;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Renclav on 2016/11/04.
 */

public class CluesLocalDataSource implements CluesDataSource {

    private static final String SHAREDPREFERENCES_KEY = CluesLocalDataSource.class.getSimpleName();

    @Nullable
    private static CluesLocalDataSource INSTANCE;
    private final SharedPreferences sharedPreferences;
    private final Moshi moshi;
    private final JsonAdapter<List<Clue>> jsonAdapter;


    private CluesLocalDataSource(@NonNull Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        moshi = new Moshi.Builder().build();
        ParameterizedType parameterizedType = Types.newParameterizedType(List.class, Clue.class);
        jsonAdapter = moshi.adapter(parameterizedType);
    }

    public static CluesLocalDataSource getInstance(
            @NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CluesLocalDataSource(context);
        }
        return INSTANCE;
    }
    @Override
    public Observable<List<Clue>> getClues() {
        return Observable.fromCallable(new Callable<List<Clue>>() {
            @Override
            public List<Clue> call() throws Exception {
                String string = sharedPreferences.getString(SHAREDPREFERENCES_KEY, "");
                if(TextUtils.isEmpty(string))
                {
                    return new ArrayList<>();
                }
                return jsonAdapter.fromJson(string);
            }
        });
    }

    @Override
    public Observable<Clue> getClue(@NonNull final String clueId) {
        return getClues().flatMap(new Func1<List<Clue>, Observable<Clue>>() {
            @Override
            public Observable<Clue> call(List<Clue> clues) {
                return Observable.from(clues);
            }
        }).firstOrDefault(null, new Func1<Clue, Boolean>() {
            @Override
            public Boolean call(Clue clue) {
                return TextUtils.equals(clueId, clue.getId());
            }
        });
    }

    @Override
    public void saveClues(List<Clue> clues) {
        sharedPreferences.edit().putString(SHAREDPREFERENCES_KEY, jsonAdapter.toJson(clues)).commit();
    }

    @Override
    public void refreshClues() {
        sharedPreferences.edit().remove(SHAREDPREFERENCES_KEY).commit();
    }
}

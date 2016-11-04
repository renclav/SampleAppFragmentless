package com.touchnote.renclav.touchnotesampleapp.data.source.remote;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;
import com.touchnote.renclav.touchnotesampleapp.data.source.CluesDataSource;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.BaseSchedulerProvider;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Renclav on 2016/11/04.
 */

public class CluesRemoteDataSource implements CluesDataSource {

    private final static String TEST_DATA_URL = "http://www.mocky.io/v2/57ee2ca8260000f80e1110fa";

    private static CluesRemoteDataSource INSTANCE;

    private final OkHttpClient client;
    private final Moshi moshi;
    private final JsonAdapter<List<Clue>> jsonAdapter;

    public static CluesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CluesRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private CluesRemoteDataSource() {
        client = new OkHttpClient();
        moshi = new Moshi.Builder().build();
        ParameterizedType parameterizedType = Types.newParameterizedType(List.class, Clue.class);
        jsonAdapter = moshi.adapter(parameterizedType);
    }


    @Override
    public Observable<List<Clue>> getClues() {

        return Observable.fromCallable(new Callable<List<Clue>>() {
            @Override
            public List<Clue> call() throws Exception {
                Request request = new Request.Builder()
                        .url(TEST_DATA_URL)
                        .build();
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()){
                    throw new IOException("Unexpected code " + response);
                }

                List<Clue> clues = jsonAdapter.fromJson(response.body().source());
                return clues;
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
        //no-op
    }

    @Override
    public void refreshClues() {
        //
    }
}

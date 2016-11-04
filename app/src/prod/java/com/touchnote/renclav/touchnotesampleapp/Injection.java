package com.touchnote.renclav.touchnotesampleapp;

/**
 * Created by Renclav on 2016/11/03.
 */

import android.content.Context;
import android.support.annotation.NonNull;

import com.touchnote.renclav.touchnotesampleapp.data.source.CluesRepository;
import com.touchnote.renclav.touchnotesampleapp.data.source.local.CluesLocalDataSource;
import com.touchnote.renclav.touchnotesampleapp.data.source.remote.CluesRemoteDataSource;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.BaseSchedulerProvider;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.SchedulerProvider;

/**
 * Enables injection of production implementations for
 * {@link CluesRepository} at compile time.
 */
public class Injection {

    public static CluesRepository provideCluesRepository(@NonNull Context context) {
        return CluesRepository.getInstance(CluesRemoteDataSource.getInstance(),
                CluesLocalDataSource.getInstance(context));
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}

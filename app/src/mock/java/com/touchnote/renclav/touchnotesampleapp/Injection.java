package com.touchnote.renclav.touchnotesampleapp;

/**
 * Created by Renclav on 2016/11/03.
 */

import android.content.Context;
import android.support.annotation.NonNull;

import com.touchnote.renclav.touchnotesampleapp.data.source.CluesDataSource;
import com.touchnote.renclav.touchnotesampleapp.data.source.CluesRepository;
import com.touchnote.renclav.touchnotesampleapp.data.source.local.CluesLocalDataSource;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.BaseSchedulerProvider;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.ImmediateSchedulerProvider;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.SchedulerProvider;

/**
 * Enables injection of mock implementations for
 * {@link CluesDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static CluesRepository provideCluesRepository(@NonNull Context context) {
        return CluesRepository.getInstance(FakeCluesRemoteDataSource.getInstance(),
                CluesLocalDataSource.getInstance(context));
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return ImmediateSchedulerProvider.getInstance();
    }
}


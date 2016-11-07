package com.touchnote.renclav.touchnotesampleapp.util.schedulers;

/**
 * Created by Renclav on 2016/11/03.
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Implementation of the {@link BaseSchedulerProvider} making all {@link Scheduler}s immediate.
 */
public class ImmediateSchedulerProvider implements BaseSchedulerProvider {

    @Nullable
    private static ImmediateSchedulerProvider INSTANCE;

    // Prevent direct instantiation.
    private ImmediateSchedulerProvider() {
    }

    public static synchronized ImmediateSchedulerProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImmediateSchedulerProvider();
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.immediate();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.immediate();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return Schedulers.immediate();
    }
}

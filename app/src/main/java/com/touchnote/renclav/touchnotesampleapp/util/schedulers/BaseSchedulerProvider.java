package com.touchnote.renclav.touchnotesampleapp.util.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Created by Renclav on 2016/11/03.
 */

public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}

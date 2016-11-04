package com.touchnote.renclav.touchnotesampleapp;

/**
 * Created by Renclav on 2016/11/03.
 */

import android.content.Context;
import android.support.annotation.NonNull;

import com.touchnote.renclav.touchnotesampleapp.util.schedulers.BaseSchedulerProvider;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.SchedulerProvider;

/**
 * Enables injection of mock implementations for
 * {@link TasksDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        return TasksRepository.getInstance(FakeTasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(context, provideSchedulerProvider()));
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}


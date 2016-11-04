package com.touchnote.renclav.touchnotesampleapp.clues;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Renclav on 2016/11/04.
 */

public final class MenuStates {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HIDDEN, LIST, GRID})
    public @interface MenuState {}

    public static final int HIDDEN = 0;
    public static final int LIST = 1;
    public static final int GRID = 2;
}

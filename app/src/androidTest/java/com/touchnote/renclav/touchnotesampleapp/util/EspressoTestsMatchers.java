package com.touchnote.renclav.touchnotesampleapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.DrawableRes;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Created by Renclav on 2016/11/08.
 */

public class EspressoTestsMatchers {

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }

    public static Matcher<View> withActionIconDrawable(@DrawableRes final int resourceId) {
        return new BoundedMatcher<View, ActionMenuItemView>(ActionMenuItemView.class) {
            @Override
            public void describeTo(final Description description) {
                description.appendText("has image drawable resource " + resourceId);
            }

            @Override
            public boolean matchesSafely(final ActionMenuItemView actionMenuItemView) {
                return sameBitmap(actionMenuItemView.getContext(), actionMenuItemView.getItemData().getIcon(), resourceId);
            }
        };
    }

    private static boolean sameBitmap(Context context, Drawable drawable, int resourceId) {
        Drawable otherDrawable = context.getResources().getDrawable(resourceId);


        if (drawable == null || otherDrawable == null) {
            return false;
        }
        if (drawable instanceof StateListDrawable && otherDrawable instanceof StateListDrawable) {
            drawable = drawable.getCurrent();
            otherDrawable = otherDrawable.getCurrent();
        }
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap otherBitmap = ((BitmapDrawable) otherDrawable).getBitmap();
            return bitmap.sameAs(otherBitmap);
        }
        if(drawable instanceof VectorDrawable)
        {
            //TODO: implement vector comparison
            Drawable.ConstantState stateA = drawable.getConstantState();
            Drawable.ConstantState stateB = otherDrawable.getConstantState();

            //boolean b = getBitmap(drawable, 0 , 0).sameAs(getBitmap(otherDrawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));
            //boolean d = stateA != null && stateB != null && stateA.equals(stateB);
            //int dd =0;
            return false;
        }
        return false;
    }

    public static Bitmap getBitmap(Drawable drawable, int assumedWidth, int assumedHeight) {
        Bitmap result;
        if (drawable instanceof BitmapDrawable) {
            result = ((BitmapDrawable) drawable).getBitmap();
        } else {

            int width;
            int height;
            if(assumedWidth > 0)
            {
                width = assumedWidth;
            } else {
                width = drawable.getIntrinsicWidth();
            }
            if(assumedHeight > 0)
            {
                height = assumedHeight;
            } else {
                height = drawable.getIntrinsicHeight();
            }
            // Some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1;
            }
            if (height <= 0) {
                height = 1;
            }

            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return result;
    }
}

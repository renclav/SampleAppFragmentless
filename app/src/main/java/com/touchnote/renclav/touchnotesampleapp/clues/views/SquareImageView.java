package com.touchnote.renclav.touchnotesampleapp.clues.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Renclav on 2016/11/05.
 */

public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int minDimension = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(minDimension, minDimension);
    }
}

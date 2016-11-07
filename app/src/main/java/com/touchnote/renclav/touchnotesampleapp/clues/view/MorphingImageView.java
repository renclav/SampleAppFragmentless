package com.touchnote.renclav.touchnotesampleapp.clues.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.IntRange;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Renclav on 2016/11/05.
 */

/**
 * ImageView with ability to morph into a cirle
 * Expects 1:1 ratio
 */
public class MorphingImageView extends ImageView {

    private Path path;
    private Paint paint;
    private RectF rect;

    private int morphStep;
    private int range;

    private int width;

    public MorphingImageView(Context context) {
        super(context);
        init();
    }

    public MorphingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MorphingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public MorphingImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * Initialize View with defaults
     */
    private void init() {
        width = 0;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        path = new Path();
        rect = new RectF();
        range = 10;
        morphStep = 0;
    }

    /**
     * Current step in-between 1 and {@link MorphingImageView#range}
     * @return
     */
    @SuppressWarnings("unused")
    @Keep
    public int getMorphStep() {
        return morphStep;
    }

    /**
     * Used programmatically and by ObjectAnimators
     * @param morphStep
     */
    @SuppressWarnings("unused")
    @Keep
    public void setMorphStep(int morphStep) {
        this.morphStep = morphStep;
        setPathMask();
        invalidate();
    }

    /**
     * Sets range of slider used to morph this view to a circle
     * Anything greater than a 100 should not be used as this will
     * create unnecessary load on the view
     * @param range between 1 and 100
     */
    public void setRange(@IntRange(from = 1, to = 100) int range) {
        this.range = range;
        this.morphStep = 0;
    }

    private void setPathMask()
    {
        float cornerRadius = width / 2f * morphStep / range;
        rect.set(0, 0, width, width);
        path.reset();
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW);
        path.close();
    }

    /**
     * Removes current drawable and resets mask path
     */
    public void revertView()
    {
        morphStep = 0;
        setPathMask();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //Note: assume 1:1 ratio, should throw exception here if not
        width = w;
        setPathMask();
    }

    /**
     * TODO: Investigate using hardware layer here, 'may' be beneficial
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

}

package com.touchnote.renclav.touchnotesampleapp.clues.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jakewharton.rxbinding.widget.RxSeekBar;
import com.touchnote.renclav.touchnotesampleapp.R;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Renclav on 2016/11/05.
 */

public class ClueDetailView extends LinearLayout {

    private ImageView imageView;
    private SeekBar seekBar;
    private CompositeSubscription subscription;

    public ClueDetailView(Context context) {
        super(context);
        init();
    }

    public ClueDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClueDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ClueDetailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        subscription = new CompositeSubscription();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView = (ImageView) findViewById(R.id.imageView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setEnabled(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        subscription.clear();
    }

    public void setClue(Clue clue) {
        Glide.with(getContext())
                .load(clue.getImage())
                .placeholder(R.drawable.ic_timelapse_black_24dp)
                .error(R.drawable.ic_block_black_24dp)
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //Image loaded
                        setUpSeekbar();
                        return false;
                    }
                })
                .into(this.imageView);
    }

    private void setUpSeekbar() {
        seekBar.setEnabled(true);
        subscription.add(
                RxSeekBar.userChanges(seekBar)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {

                            }
                        }));
    }
}

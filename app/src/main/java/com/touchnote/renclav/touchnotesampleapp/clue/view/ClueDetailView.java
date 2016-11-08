package com.touchnote.renclav.touchnotesampleapp.clue.view;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.jakewharton.rxbinding.widget.RxSeekBar;
import com.touchnote.renclav.touchnotesampleapp.R;
import com.touchnote.renclav.touchnotesampleapp.clue.contract.ClueDetailViewContract;
import com.touchnote.renclav.touchnotesampleapp.clue.container.CluesContainer;
import com.touchnote.renclav.touchnotesampleapp.custom.glide.PaletteBitmap;
import com.touchnote.renclav.touchnotesampleapp.custom.glide.PaletteBitmapTranscoder;
import com.touchnote.renclav.touchnotesampleapp.data.Clue;
import com.touchnote.renclav.touchnotesampleapp.util.schedulers.BaseSchedulerProvider;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Renclav on 2016/11/05.
 */

public class ClueDetailView extends LinearLayout implements ClueDetailViewContract {

    private TimeInterpolator interpolator;

    private MorphingImageView imageView;
    private SeekBar seekBar;
    private FrameLayout imageViewFrame;

    private BaseSchedulerProvider schedulerProvider;
    private CompositeSubscription subscription;

    private ContentLoadingProgressBar progressBar;

    private Clue currentClue;

    private CluesContainer cluesContainer;

    private
    @ColorInt
    int defaultbackroundColour;

    private ObjectAnimator morphAnimation;

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
        interpolator = new LinearInterpolator();
        defaultbackroundColour = ContextCompat.getColor(ClueDetailView.this.getContext(), R.color.primary);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView = (MorphingImageView) findViewById(R.id.imageView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        imageViewFrame = (FrameLayout) findViewById(R.id.imageViewBorderFrame);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_view);
        progressBar.hide();
        seekBar.setEnabled(false);
        imageView.setRange(seekBar.getMax());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        imageView.setImageDrawable(null);
        imageViewFrame.setBackgroundColor(Color.TRANSPARENT);
        subscription.clear();
        progressBar.hide();
        if (cluesContainer != null) {
            cluesContainer.updateSelectedClue(null);
        }
        super.onDetachedFromWindow();
    }


    @Override
    public void setClueWithSchedulerProvider(Clue clue, BaseSchedulerProvider schedulerProvider) {

        resetWithClue(clue);
        setSchedulerProvider(schedulerProvider);
        loadImage();
    }

    private void resetWithClue(Clue clue)
    {
        //Nice to have, resetting morph step if clicking same list item in dual pane mode
        if(currentClue != null && TextUtils.equals(currentClue.getId(), clue.getId()))
        {
            clue.setMorphStep(0);
        }
        currentClue = clue;
        imageViewFrame.setBackgroundColor(Color.TRANSPARENT);
        imageView.revertView();
        seekBar.setVisibility(VISIBLE);
        seekBar.setProgress(clue.getMorphStep());
        seekBar.setEnabled(false);
        seekBar.setPressed(false);
        progressBar.show(); //TODO: prob best to re-inflate this everytime,or roll my own, since the internal timer is shared
    }

    private void loadImage()
    {
        //TODO: Need to force Glide to use schedulerProvider above, rely on Glide's testing for now
        Glide.with(getContext())
                .load(currentClue.getImage())
                .asBitmap()
                .transcode(new PaletteBitmapTranscoder(getContext()), PaletteBitmap.class)
                .error(R.drawable.ic_block_black_24dp)
                .dontAnimate()
                .listener(new RequestListener<String, PaletteBitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<PaletteBitmap> target, boolean isFirstResource) {
                        progressBar.hide();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(PaletteBitmap resource, String model, Target<PaletteBitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //Image loaded
                        progressBar.hide();
                        setUpSeekbar();
                        return false;
                    }
                })
                .into(new ImageViewTarget<PaletteBitmap>(this.imageView) {
                    @Override
                    protected void setResource(PaletteBitmap resource) {
                        super.view.setImageBitmap(resource.bitmap);
                        int colour = resource.palette.getVibrantColor(defaultbackroundColour);
                        imageViewFrame.setBackgroundColor(colour);
                    }
                });
    }

    private void setUpSeekbar() {
        seekBar.setEnabled(true);
        subscription.add(
                RxSeekBar.userChanges(seekBar)
//                        /.debounce(200, TimeUnit.MILLISECONDS, schedulerProvider.computation()) //TODO: play around some more, not sure I like this, feels unnatural
                        .distinctUntilChanged()
                        .observeOn(schedulerProvider.ui())
                        .subscribeOn(schedulerProvider.ui())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                setMaskWithStep(integer.intValue());
                            }
                        }));
    }

    private void setMaskWithStep(int step) {

        currentClue.setMorphStep(step);
        if (cluesContainer != null) {
            cluesContainer.updateSelectedClue(currentClue);
        }

        if (morphAnimation != null && morphAnimation.isStarted()) {
            morphAnimation.end();
        }

        morphAnimation = ObjectAnimator.ofInt(imageView, "morphStep", step);
        morphAnimation.setRepeatCount(0);
        morphAnimation.setDuration(200);
        morphAnimation.setInterpolator(interpolator);
        morphAnimation.start();
    }

    public void showNoSelection() {
        seekBar.setVisibility(GONE);
        imageView.setImageResource(R.drawable.coffee_cup);
    }

    @VisibleForTesting
    public void setSchedulerProvider(BaseSchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public void setCluesContainer(CluesContainer cluesContainer) {
        this.cluesContainer = cluesContainer;
        if(this.cluesContainer == null)
        {
            currentClue = null;
        }
    }
}

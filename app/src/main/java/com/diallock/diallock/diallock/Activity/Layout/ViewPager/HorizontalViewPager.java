package com.diallock.diallock.diallock.Activity.Layout.ViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Fragment.CircleDial_press;
import com.diallock.diallock.diallock.Activity.Fragment.LockScreenFragment;

/**
 * Created by park on 2016-09-29.
 */
public class HorizontalViewPager extends ViewPager {
    private GestureDetector xScrollDetector;
    private final String LOG_NAME = "HorizontalViewPager";

    public HorizontalViewPager(Context context) {
        super(context);
    }

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        xScrollDetector = new GestureDetector(getContext(), new XScrollDetector());
        setPageTransformer(false, new HorizontalViewPager.HorizontalPageTransformer());
    }

    class XScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            CommonJava.Loging.i(LOG_NAME, "onDown(MotionEvent e)  MotionEvent : " + e);/*
            if (LockScreenFragment.mSwitchValue == 2) {
                CircleDial_press.getInstance(LockScreenFragment.newInstance()).isSmallDialInner();
            }*/
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceX) > Math.abs(distanceY);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (xScrollDetector.onTouchEvent(ev)) {
            super.onInterceptTouchEvent(ev);
            return true;
        }

        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public int getCurrentItem() {

        CommonJava.Loging.i(LOG_NAME, "getCurrentItem : " + super.getCurrentItem());

        return super.getCurrentItem();
    }

    private class HorizontalPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

}

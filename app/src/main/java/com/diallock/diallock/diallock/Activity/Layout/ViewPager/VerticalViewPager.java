package com.diallock.diallock.diallock.Activity.Layout.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.diallock.diallock.diallock.Activity.Activity.LockScreenActivity;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Fragment.CircleDial_press;
import com.diallock.diallock.diallock.Activity.Fragment.LockScreenFragment;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;

/**
 * Created by park on 2016-09-29.
 */
public class VerticalViewPager extends ViewPager {

    private final String LOG_NAME = "VerticalViewPager";
    private Context mContext;

    public VerticalViewPager(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        setPageTransformer(true, new VerticalPageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
        setOnClick();
    }

    private void setOnClick() {
        CommonJava.Loging.i(LOG_NAME, "mContext : " + mContext);
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {

            CommonJava.Loging.i(LOG_NAME, "view : " + view);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*if (LockScreenFragment.mSwitchValue == 2) {
                        CircleDial_press.getInstance(LockScreenFragment.newInstance()).isCheckPassword();
                    }*/
                }
            });

            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 1) {
                view.setAlpha(1);

                view.setTranslationX(view.getWidth() * -position);

                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);
            } else {
                view.setAlpha(0);
            }
        }
    }

    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev);
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        CommonJava.Loging.i(LOG_NAME, "onTouchEvent MotionEvent ev : " + ev);
        return super.onTouchEvent(swapXY(ev));
    }

}
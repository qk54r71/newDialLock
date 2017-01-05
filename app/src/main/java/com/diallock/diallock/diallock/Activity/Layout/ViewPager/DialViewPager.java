package com.diallock.diallock.diallock.Activity.Layout.ViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;

/**
 * Created by park on 2016-09-29.
 */
public class DialViewPager extends ViewPager {
    private GestureDetector xScrollDetector;
    private final String LOG_NAME = "DialViewPager";

    public DialViewPager(Context context) {
        super(context);
    }

    public DialViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        xScrollDetector = new GestureDetector(getContext(), new XScrollDetector());
    }

    class XScrollDetector extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //return Math.abs(distanceX) > Math.abs(distanceY);
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*if (xScrollDetector.onTouchEvent(ev)) {
            super.onInterceptTouchEvent(ev);
            return true;
        }
*/
        //return super.onInterceptTouchEvent(ev);
        return false;
    }


    @Override
    public int getCurrentItem() {

        CommonJava.Loging.i(LOG_NAME, "getCurrentItem : " + super.getCurrentItem());

        return super.getCurrentItem();
    }


}

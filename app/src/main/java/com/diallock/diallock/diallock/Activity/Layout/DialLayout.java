package com.diallock.diallock.diallock.Activity.Layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.ogaclejapan.arclayout.ArcLayout;

/**
 * Created by park on 2016-12-21.
 */

public class DialLayout extends ArcLayout implements View.OnTouchListener {

    private static final String LOG_NAME = "DialLayout";


    public DialLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        CommonJava.Loging.i(LOG_NAME, "DialLayout construct");
        CommonJava.Loging.i(LOG_NAME, "DialLayout context : " + context);
        CommonJava.Loging.i(LOG_NAME, "DialLayout attrs : " + attrs.getAttributeCount());

        for (int attrIndex = 0; attrIndex < attrs.getAttributeCount(); attrIndex++) {
            CommonJava.Loging.i(LOG_NAME, "DialLayout attrs " + attrIndex + " : " + attrs.getAttributeValue(attrIndex));
            CommonJava.Loging.i(LOG_NAME, "DialLayout attrs " + attrIndex + " : " + attrs.getAttributeName(attrIndex));
        }


        CommonJava.Loging.i(LOG_NAME, "DialLayout attrs arc_radius : " + attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "arc_radius"));
        CommonJava.Loging.i(LOG_NAME, "DialLayout attrs arc_axisRadius : " + attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "arc_axisRadius"));

    }

    public DialLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        CommonJava.Loging.i(LOG_NAME, "DialLayout construct");
        CommonJava.Loging.i(LOG_NAME, "DialLayout context : " + context);
        CommonJava.Loging.i(LOG_NAME, "DialLayout attrs : " + attrs.getAttributeCount());
        CommonJava.Loging.i(LOG_NAME, "DialLayout defStyleAttr : " + defStyleAttr);
        CommonJava.Loging.i(LOG_NAME, "DialLayout defStyleRes : " + defStyleRes);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        CommonJava.Loging.i(LOG_NAME, "onTouch event :: " + event);

        return false;
    }
}

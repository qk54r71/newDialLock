package com.diallock.diallock.diallock.Activity.Layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;

/**
 * Created by park on 2016-12-26.
 */

public class CustomLinearLayout extends LinearLayout {

    private static final String LOG_NAME = "CustomLinearLayout";

    public CustomLinearLayout(Context context) {
        super(context);
        CommonJava.Loging.i(LOG_NAME, "CustomLinearLayout(Context context)");
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        CommonJava.Loging.i(LOG_NAME, "CustomLinearLayout(Context context, AttributeSet attrs)");
    }
}

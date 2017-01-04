package com.diallock.diallock.diallock.Activity.ParkSDK.Util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by park on 2017-01-04.
 */
public class Conversion {

    public static Integer dp_to_px(float dp, Context context) {
        Integer resultPx = null;
        resultPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return resultPx;
    }

    public static Integer dp_to_px(int dp, Context context) {
        Integer resultPx = null;
        resultPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return resultPx;
    }

}

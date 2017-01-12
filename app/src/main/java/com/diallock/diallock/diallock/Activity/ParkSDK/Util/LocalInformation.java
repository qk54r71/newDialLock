package com.diallock.diallock.diallock.Activity.ParkSDK.Util;

import android.content.Context;

/**
 * Created by park on 2017-01-12.
 */

public class LocalInformation {
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}

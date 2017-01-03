package com.diallock.diallock.diallock.Activity.ParkSDK.Debug;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * Created by park on 2017-01-03.
 */

public class Loging {
    public static Boolean logingCheck = true;

    public static void i(String className, String strContent) {
        if (logingCheck) {
            Log.i(className, strContent);
        }
    }

    public static void i(Context context, String strContent) {
        if (logingCheck) {
            Log.i(((Activity) context).getLocalClassName(), strContent);
        }
    }

    public static void d(String className, String strContent) {
        if (logingCheck) {
            Log.d(className, strContent);
        }
    }

    public static void d(Context context, String strContent) {
        if (logingCheck) {
            Log.d(((Activity) context).getLocalClassName(), strContent);
        }
    }

    public static void e(String className, String strContent) {
        if (logingCheck) {
            Log.e(className, strContent);
        }
    }

    public static void e(Context context, String strContent) {
        if (logingCheck) {
            Log.e(((Activity) context).getLocalClassName(), strContent);
        }
    }

    public static void w(String className, String strContent) {
        if (logingCheck) {
            Log.w(className, strContent);
        }
    }

    public static void w(Context context, String strContent) {
        if (logingCheck) {
            Log.w(((Activity) context).getLocalClassName(), strContent);
        }
    }
}

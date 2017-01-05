package com.diallock.diallock.diallock.Activity.Common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Activity.LockScreenViewActivity;
import com.diallock.diallock.diallock.Activity.Adapter.WidgetPagerAdapter;
import com.diallock.diallock.diallock.Activity.Fragment.EventInfo;
import com.diallock.diallock.diallock.Activity.Fragment.LockScreenFragment;
import com.diallock.diallock.diallock.Activity.Fragment.TourismInfo;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.Activity.Layout.ViewPager.HorizontalViewPager;
import com.diallock.diallock.diallock.Activity.taskAction.NoLockStatusListenerException;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by park on 2016-08-26.
 */
public class LockScreenManager {

    public static LockScreenManager mLockScreenManager;
    public static Activity mActivity;
    public static View mLockView;
    private static FragmentManager mFragmentManager;
    private WindowManager.LayoutParams layoutParams;
    private boolean mIsLock;
    private WeakReference<WindowManager> mWindowManagerRef;
    private CircleLayout circleLayout;
    private Button btn_cancle;
    private LockStatusListener lockStatusListener;

    private final static String LOG_NAME = "LockScreenManager";
    private boolean backFlag;

    public static synchronized LockScreenManager getInstance(Activity activity) {
        CommonJava.Loging.i(LOG_NAME, "getInstance");
        if (mLockScreenManager == null) {
            mLockScreenManager = new LockScreenManager(activity);
        }

        return mLockScreenManager;
    }

    public LockScreenManager(Activity activity) {
        this.mActivity = activity;

        initLock();
    }

    public void updateActivity(Activity ac) {
        this.mActivity = ac;
    }

    private void initLock() {
        mIsLock = false;
        mWindowManagerRef = new WeakReference<WindowManager>(mActivity.getWindowManager());
        layoutParams = new WindowManager.LayoutParams();
        //layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        String strSwitch = mActivity.getIntent().getStringExtra("strSwitch");
        if (!strSwitch.equals("SettingActivity")) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR; // 이 기능임
        }
        //layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR; // 이 기능임

        CommonJava.Loging.i(LOG_NAME, "initLock layoutParams.height : " + layoutParams.height);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        CommonJava.Loging.i(LOG_NAME, "initLock point.y : " + point.y);
        int disHeight = point.y;
        layoutParams.height = point.y - 100 * (disHeight / 2560);
        layoutParams.verticalMargin = 200 * (disHeight / 2560);
        layoutParams.flags = 1280;

    }

    public void setLockScreen(View v) {
        mLockView = v;
        setLockBg();

        mLockView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                CommonJava.Loging.i(LOG_NAME, "onTouch : " + event);
                LockScreenFragment.newInstance().setOnTouchEvent(event);
                return false;
            }
        });

        init();

    }

    private void setFindView() {
    }

    private void init() {
        backFlag = false;
    }


    private void setLockBg() {
        Uri uri = Uri.parse("");
        String path = uri.getPath();
        Bitmap bitmap = null;
        Point point = new Point();
        ScreenHelper.getScreenPixel(mActivity, point);
        bitmap = ImageHelper.scaleImage(path, point.x, point.y);
    }

    private WindowManager getWindowManager() {
        WindowManager windowManager = mWindowManagerRef.get();
        if (windowManager == null) {
            windowManager = mActivity.getWindowManager();
            mWindowManagerRef = new WeakReference<WindowManager>(windowManager);
        }
        return windowManager;
    }

    public synchronized void Lock() throws NoLockStatusListenerException {
        CommonJava.Loging.i(LOG_NAME, "Lock()");


        if (lockStatusListener == null) {
            throw new NoLockStatusListenerException();
        }

        if (mLockView != null && !mIsLock) {
            if (mIsLock) {
                getWindowManager().updateViewLayout(mLockView, layoutParams);
                CommonJava.Loging.i(LOG_NAME, "updateViewLayout()");
            } else {
                getWindowManager().addView(mLockView, layoutParams);
                CommonJava.Loging.i(LOG_NAME, "addView()");
            }

            mIsLock = true;
            lockStatusListener.onLocked();

        }

        setFindView();

    }

    public synchronized void unLock() {
        Log.i(LOG_NAME, "unLock()");
        Log.i(LOG_NAME, "getWindowManager() : " + getWindowManager());
        Log.i(LOG_NAME, "mIsLock : " + mIsLock);
        if (getWindowManager() != null && mIsLock) {
            Log.i(LOG_NAME, "unLock() removeView");
/*
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            getWindowManager().removeView(mLockView);
            getWindowManager().addView(mLockView, layoutParams);*/
            getWindowManager().removeView(mLockView);
            mIsLock = false;
            mActivity.finish();
        }
    }

    public void setLockStatusListener(LockStatusListener listener) {
        this.lockStatusListener = listener;
    }

    public interface LockStatusListener {
        void onLocked();

        void onUnlock();
    }
}

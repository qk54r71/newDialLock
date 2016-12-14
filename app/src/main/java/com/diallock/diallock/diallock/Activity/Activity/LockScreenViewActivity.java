package com.diallock.diallock.diallock.Activity.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.LockScreenManager;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.Activity.taskAction.NoLockStatusListenerException;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Timer;

/**
 * 이메일 보내기
 * 출처 : {Link :http://blog.naver.com/PostView.nhn?blogId=junhwen&logNo=130151732452 }
 */
public class LockScreenViewActivity extends BaseActivity implements LockScreenManager.LockStatusListener {

    private CircleLayout circleLayout;
    private Boolean backFlag;


    /**
     * 비밀번호 찾기 버튼
     */
    private Button btn_find_pass;
    private Timer mTimer;


    private RelativeLayout info_dial;

    private static LockScreenManager mLockScreenManager;

    public static Activity mLockScreenViewActivity;
    private static Context mContext;

    private static FragmentManager mFragmentManager;
    private View mLockScreenManagerView;
    private final static String LOG_NAME = "LockScreenViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {/*
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        super.onCreate(savedInstanceState);

        Fresco.initialize(LockScreenViewActivity.this);
        CommonJava.Loging.i(LOG_NAME, "onCreate()");


        setupView(R.layout.activity_lock_screen_view);
        mLockScreenManager = LockScreenManager.getInstance(LockScreenViewActivity.this); // 한개의 액티비티만 생성 되게 함 싱글톤 방식

        mLockScreenManager.setLockStatusListener(this);
        mLockScreenManagerView = LayoutInflater.from(LockScreenViewActivity.this).inflate(R.layout.activity_lock_screen, null);
        mLockScreenManager.setLockScreen(mLockScreenManagerView);
        mLockScreenManager.updateActivity(LockScreenViewActivity.this);

       /* HomeKeyLocker homeKeyLoader = new HomeKeyLocker();
        homeKeyLoader.lock(this);*/
        setFindView();
        init();
        setOnClick();

    }


    private void setFindView() {
        info_dial = (RelativeLayout) mLockScreenManagerView.findViewById(R.id.info_dial);
    }

    private void init() {
        mLockScreenViewActivity = this;
        mContext = this;
    }

    private void setOnClick() {

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.info_slideView_cancle:
                    info_dial.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    public void onFinish() {
        CommonJava.Loging.i(LOG_NAME, "onFinish()");
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CommonJava.Loging.i(LOG_NAME, "onStart()");

        try {
            mLockScreenManager.Lock();
        } catch (NoLockStatusListenerException e) {
            e.printStackTrace();
        }
    }

    @Override

    protected void onDestroy() {
        mLockScreenManager.unLock();
        mLockScreenManager.updateActivity(LockScreenViewActivity.this);
        //mLockScreenManager.timeCancle();
        super.onDestroy();

    }


    @Override

    protected void onPause() {

        //mLockScreenManager.timeCancle();

        super.onPause();


    }


    @Override

    protected void onResume() {

        super.onResume();
        //mLockScreenManager.timeStart();
        CommonJava.Loging.i(LOG_NAME, "onResume");


        mFragmentManager = getSupportFragmentManager();
        CommonJava.Loging.i(LOG_NAME, "mFragmentManager " + mFragmentManager.getFragments());
    }


    @Override
    public void onLocked() {

    }

    @Override
    public void onUnlock() {
        // mLockScreenManager.timeCancle();
        mLockScreenManager.unLock();
    }

    public void setInfoDialVisibility(int visibility) {
        info_dial.setVisibility(visibility);
    }

}

package com.diallock.diallock.diallock.Activity.Activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Fragment.LockScreenFragment;
import com.diallock.diallock.diallock.Activity.Layout.DialLayout;
import com.diallock.diallock.diallock.Activity.Layout.DialLayout.TouchBtnIndexInteractionListener;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * 이메일 보내기
 * 출처 : {Link :http://blog.naver.com/PostView.nhn?blogId=junhwen&logNo=130151732452 }
 */
public class LockScreenActivity extends AppCompatActivity implements TouchBtnIndexInteractionListener {

    private Boolean mBackFlag;
    private LockScreenFragment mLockScreenFragment;

    private SensorManager mSensorManager;
    private Sensor mLight;

    private final String LOG_NAME = "LockScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(LockScreenActivity.this);
        setContentView(R.layout.activity_lock_screen);
        CommonJava.Loging.i(LOG_NAME, "onCreate()");

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setFindView();
        init();
    }


    private void setFindView() {

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof LockScreenFragment) {
                mLockScreenFragment = (LockScreenFragment) fragment;
            }
        }

    }

    private void init() {
        mBackFlag = false;

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            CommonJava.Loging.i(LOG_NAME, "fragment : " + fragment);
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mLockScreenFragment.setOnTouchEvent(event);

        return super.onTouchEvent(event);
    }

    /**
     * 뒤로가기 버튼 클릭 시 종료
     */
    @Override
    public void onBackPressed() {

        CommonJava.Loging.i(LOG_NAME, "onBackPressed()");


        if (mBackFlag) {

            CommonJava.Loging.i(LOG_NAME, "onBackPressed() : 종료");

            finish();
        } else {
            mBackFlag = true;

            Toast.makeText(LockScreenActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            Handler han = new Handler();
            han.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mBackFlag = false;
                }
            }, 2000);
        }

    }

    @Override

    protected void onDestroy() {

        super.onDestroy();

    }


    @Override

    protected void onPause() {

        super.onPause();

    }


    @Override

    protected void onResume() {

        super.onResume();

    }

    public void isToast(String strMsg) {
        Toast.makeText(LockScreenActivity.this, strMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void touchBtnIndex(int btnIndex) {
        CommonJava.Loging.i(LOG_NAME, "touchBtnIndex : " + btnIndex);
    }
}

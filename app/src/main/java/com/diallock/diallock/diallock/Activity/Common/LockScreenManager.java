package com.diallock.diallock.diallock.Activity.Common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Activity.LockScreenViewActivity;
import com.diallock.diallock.diallock.Activity.Adapter.ListViewAdapter;
import com.diallock.diallock.diallock.Activity.Adapter.WidgetPagerAdapter;
import com.diallock.diallock.diallock.Activity.Fragment.CircleDial;
import com.diallock.diallock.diallock.Activity.Fragment.EventInfo;
import com.diallock.diallock.diallock.Activity.Fragment.EventInfoItem;
import com.diallock.diallock.diallock.Activity.Fragment.SlideImage;
import com.diallock.diallock.diallock.Activity.Fragment.TourismInfo;
import com.diallock.diallock.diallock.Activity.Fragment.TourismInfoItem;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.Activity.Layout.ViewPager.HorizontalViewPager;
import com.diallock.diallock.diallock.Activity.taskAction.NoLockStatusListenerException;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
    private Date nowDate;

    private Boolean backFlag;
    private TextView txt_lock_day;
    private TextView txt_lock_time;
    private TextView txt_lock_title;
    private SimpleDraweeView lock_screen_pre;
    private SimpleDraweeView lock_screen_nex;
    private SimpleDraweeView lock_screen_pre_double;
    private SimpleDraweeView lock_screen_nex_double;
    private Date mNowDate;
    private Calendar mCalendar;

    /**
     * 비밀번호 찾기 버튼
     */
    private SimpleDraweeView btn_find_pass;
    private Timer mTimer;

    /**
     * dial control button
     */
    private SimpleDraweeView btn_dial_pattern;
    private ArrayList<Integer> mImageBtn;
    private ArrayList<Integer> mImageBtnRandom;
    private ArrayList<Integer> mImageBtnDefault;
    private ArrayList<String> mStrTitle;
    public static Integer mSwitchValue;

    /**
     * dail control button
     */
    private SimpleDraweeView btn_call;
    private SimpleDraweeView btn_app;

    /**
     * random button
     */
    public static Boolean smSwitchRandom;

    private LinearLayout info_view;
    private RelativeLayout info_dial;
    private RelativeLayout info_slideView;
    private HorizontalViewPager info_slideView_viewPager_horizontal;
    private SimpleDraweeView info_slideView_cancle;

    private WidgetPagerAdapter mWidgetPagerAdapter;
    private HorizontalViewPager mHorizontalViewPager;

    private ArrayList<Fragment> mInfoFragmentArrayList;
    private EventInfo mEventInfo;
    private TourismInfo mTourismInfo;

    private TextView txtImgSlideProgress;

    private final static String LOG_NAME = "LockScreenManager";

    public static synchronized LockScreenManager getInstance(Activity activity) {
        CommonJava.Loging.i(LOG_NAME, "getInstance");
        if (mLockScreenManager == null) {
            mLockScreenManager = new LockScreenManager(activity);
        }

        return mLockScreenManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;

        setFragment();
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


        nowDate = CommonJava.getNowDate();
    }

    public void setLockScreen(View v) {
        mLockView = v;
        setLockBg();
        mLockView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                CommonJava.Loging.i(LOG_NAME, "onTouch : " + event);
                float xLocation = event.getX(0);
                float yLocation = event.getY(0);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((CircleLayout) mLockView.findViewById(R.id.circle_screen)).screenTouchLocationStart(xLocation, yLocation);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        ((CircleLayout) mLockView.findViewById(R.id.circle_screen)).screenTouchLocationDrag(xLocation, yLocation);
                        break;
                    case MotionEvent.ACTION_UP:

                        ((CircleLayout) mLockView.findViewById(R.id.circle_screen)).screenTouchLocationEnd(xLocation, yLocation);
                        break;
                }
/*
                CircleDial.newInstance().setOnTouchCircleDial(event);*/

                return false;
            }
        });


        /*setFindView();
        init();
        setOnClick();*/
        //setListView();

    }

    private void setFindView() {
        circleLayout = (CircleLayout) mLockView.findViewById(R.id.circle_screen);
        btn_find_pass = (SimpleDraweeView) mLockView.findViewById(R.id.btn_find_pass);
        /*txt_lock_day = (TextView) mLockView.findViewById(R.id.txt_lock_day);
        lock_screen_pre = (SimpleDraweeView) mLockView.findViewById(R.id.lock_screen_pre);
        lock_screen_pre_double = (SimpleDraweeView) mLockView.findViewById(R.id.lock_screen_pre_double);
        lock_screen_nex = (SimpleDraweeView) mLockView.findViewById(R.id.lock_screen_nex);
        lock_screen_nex_double = (SimpleDraweeView) mLockView.findViewById(R.id.lock_screen_nex_double);*/
        txt_lock_title = (TextView) mLockView.findViewById(R.id.txt_lock_title);

        btn_dial_pattern = (SimpleDraweeView) mLockView.findViewById(R.id.btn_dial_pattern);

        //mHorizontalViewPager = (HorizontalViewPager) mLockView.findViewById(R.id.info_frag_horizontal);

        info_view = (LinearLayout) mLockView.findViewById(R.id.info_view);
        info_dial = (RelativeLayout) mLockView.findViewById(R.id.info_dial);
        info_slideView = (RelativeLayout) mLockView.findViewById(R.id.info_slideView);
        info_slideView_viewPager_horizontal = (HorizontalViewPager) mLockView.findViewById(R.id.info_slideView_viewPager_horizontal);
        info_slideView_cancle = (SimpleDraweeView) mLockView.findViewById(R.id.info_slideView_cancle);

        btn_call = (SimpleDraweeView) mLockView.findViewById(R.id.btn_call);
        btn_app = (SimpleDraweeView) mLockView.findViewById(R.id.btn_app);

        txtImgSlideProgress = (TextView) mLockView.findViewById(R.id.txtImgSlideProgress);
    }

    private void init() {

        backFlag = false;

        /*mNowDate = CommonJava.getNowDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        String strDate = dateFormat.format(mNowDate);

        CommonJava.Loging.i(LOG_NAME, "strDate : " + strDate);

        String strTxtLockDay =
                CommonJava.getYear(mNowDate) + "년 " + CommonJava.getMonth(mNowDate) + "월 " + CommonJava.getDay(mNowDate) + "일 " + CommonJava.getDayOfWeek(mNowDate);

        txt_lock_day.setText(strTxtLockDay);*/

        //MainTimerTask timerTask = new MainTimerTask();

       /* mTimer = new Timer();

        mTimer.schedule(timerTask, 500, 1000);*/

        //mCalendar = Calendar.getInstance();

        //txt_lock_title.setText("다이얼락");

        mImageBtnDefault = new ArrayList<>();
        mImageBtnDefault.add(R.drawable.btn_num_1);
        mImageBtnDefault.add(R.drawable.btn_num_2);
        mImageBtnDefault.add(R.drawable.btn_num_3);

        mImageBtnRandom = new ArrayList<>();
        mImageBtnRandom.add(R.drawable.btn_num_1_random);
        mImageBtnRandom.add(R.drawable.btn_num_2_random);
        mImageBtnRandom.add(R.drawable.btn_num_3_random);

        mImageBtn = mImageBtnRandom;

        mStrTitle = new ArrayList<>();
        mStrTitle.add("행사");
        mStrTitle.add("관광");
        txt_lock_title.setText(mStrTitle.get(0));
        mSwitchValue = 0;

        btn_dial_pattern.getHierarchy().setPlaceholderImage(mImageBtn.get(mSwitchValue));

        circleLayout.isInitDial();

        smSwitchRandom = true;

       /* mInfoFragmentArrayList = new ArrayList<>();
        mEventInfo = new EventInfo().newInstance(mNowDate);
        mTourismInfo = new TourismInfo().newInstance(mNowDate);

        //TODO:나중에 회원 설정 정보에 따라서 추가 해야됨
        mInfoFragmentArrayList.add(mEventInfo);
        mInfoFragmentArrayList.add(mTourismInfo);

        mStrTitle = new ArrayList<>();

        for (Fragment fragment : mInfoFragmentArrayList) {
            if (fragment instanceof EventInfo) {
                mStrTitle.add("행사");
            } else if (fragment instanceof TourismInfo) {
                mStrTitle.add("관광");
            }
        }*/

        //setFragment();

        info_slideView.setVisibility(View.GONE);

    }

    private void setFragment() {
        mWidgetPagerAdapter = new WidgetPagerAdapter(mFragmentManager, mInfoFragmentArrayList);

        CommonJava.Loging.i(LOG_NAME, "mHorizontalViewPager : " + mHorizontalViewPager);
        CommonJava.Loging.i(LOG_NAME, "mHorizontalViewPager : " + mHorizontalViewPager.getId());
        CommonJava.Loging.i(LOG_NAME, "mInfoFragmentArrayList : " + mInfoFragmentArrayList.toString());

        mHorizontalViewPager.setAdapter(mWidgetPagerAdapter);
        mHorizontalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                CommonJava.Loging.i(LOG_NAME, "onPageScrolled position : " + position);
                CommonJava.Loging.i(LOG_NAME, "onPageScrolled positionOffset : " + positionOffset);
                CommonJava.Loging.i(LOG_NAME, "onPageScrolled positionOffsetPixels : " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                CommonJava.Loging.i(LOG_NAME, "onPageSelected position : " + position);
                String strTitle = mStrTitle.get(position);
                //txt_lock_title.setText(strTitle);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //CommonJava.Loging.i(LOG_NAME, "onPageScrollStateChanged state :" + state);

            }
        });

    }

    private void setFragmentChangeDay(Date nowDate) {

        String strNowDay =
                CommonJava.getYear(nowDate) + "." + CommonJava.getMonth(nowDate) + "." + CommonJava.getDay(nowDate);
        CommonJava.Loging.i(LOG_NAME, "setFragmentChangeDay strNowDay : " + strNowDay);

        mEventInfo.dayChangeInstance(strNowDay);
        mTourismInfo.dayChangeInstance(strNowDay);

        mEventInfo.onResume();
        mTourismInfo.onResume();

        mHorizontalViewPager.invalidate();
    }

   /* @Override
    public void onFragmentInteraction(final ArrayList<String> strEventSlideImage) {
        CommonJava.Loging.i(LOG_NAME, "strEventSlideImage : " + strEventSlideImage.toString());

        info_view.setVisibility(View.GONE);
        info_dial.setVisibility(View.GONE);
        info_slideView.setVisibility(View.VISIBLE);

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        for (String strSildeImageURL : strEventSlideImage) {
            fragmentArrayList.add(new SlideImage().newInstance(strSildeImageURL));
        }

        WidgetPagerAdapter eventSildeWidgetPagerAdapter = new WidgetPagerAdapter(mFragmentManager, fragmentArrayList);
        info_slideView_viewPager_horizontal.setAdapter(eventSildeWidgetPagerAdapter);

        String strImgSlideProgress = "1 / " + strEventSlideImage.size();
        txtImgSlideProgress.setText(strImgSlideProgress);

        info_slideView_viewPager_horizontal.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String strImgSlideProgress = (position + 1) + " / " + strEventSlideImage.size();
                txtImgSlideProgress.setText(strImgSlideProgress);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onTourismInfoItemInteraction(final ArrayList<String> strTourSlideImage) {

        CommonJava.Loging.i(LOG_NAME, "strTourSlideImage : " + strTourSlideImage.toString());

        info_view.setVisibility(View.GONE);
        info_dial.setVisibility(View.GONE);
        info_slideView.setVisibility(View.VISIBLE);

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        for (String strTourSildeImageURL : strTourSlideImage) {
            fragmentArrayList.add(new SlideImage().newInstance(strTourSildeImageURL));
        }

        WidgetPagerAdapter tourSildeWidgetPagerAdapter = new WidgetPagerAdapter(mFragmentManager, fragmentArrayList);
        info_slideView_viewPager_horizontal.setAdapter(tourSildeWidgetPagerAdapter);

        String strImgSlideProgress = "1 / " + strTourSlideImage.size();
        txtImgSlideProgress.setText(strImgSlideProgress);

        info_slideView_viewPager_horizontal.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                String strImgSlideProgress = (position + 1) + " / " + strTourSlideImage.size();
                txtImgSlideProgress.setText(strImgSlideProgress);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }*/


    private void setOnClick() {
        btn_find_pass.setOnClickListener(onClickListener);

        /*lock_screen_pre.setOnClickListener(onClickListener);
        lock_screen_pre_double.setOnClickListener(onClickListener);
        lock_screen_nex.setOnClickListener(onClickListener);
        lock_screen_nex_double.setOnClickListener(onClickListener);*/

        btn_dial_pattern.setOnLongClickListener(onLongClickListener);
        btn_dial_pattern.setOnClickListener(onClickListener);

        info_slideView_cancle.setOnClickListener(onClickListener);

        btn_call.setOnClickListener(onClickListener);
        btn_app.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_find_pass:
                    CommonJava.Loging.i(LOG_NAME, "onClick()");
                    startEmailSend();

                    Toast.makeText(mActivity, "플레이스토어에 등록된 gmail 로 비밀번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();

                    break;
              /*  case R.id.lock_screen_pre:

                    mCalendar.add(Calendar.DAY_OF_MONTH, -1);
                    mNowDate = mCalendar.getTime();

                    setTextDay(mNowDate);

                    setFragmentChangeDay(mNowDate);
                    break;

                case R.id.lock_screen_pre_double:
                    mCalendar.add(Calendar.MONTH, -1);
                    mNowDate = mCalendar.getTime();

                    setTextDay(mNowDate);

                    setFragmentChangeDay(mNowDate);
                    break;

                case R.id.lock_screen_nex:

                    mCalendar.add(Calendar.DAY_OF_MONTH, +1);
                    mNowDate = mCalendar.getTime();

                    setTextDay(mNowDate);


                    setFragmentChangeDay(mNowDate);
                    break;

                case R.id.lock_screen_nex_double:

                    mCalendar.add(Calendar.MONTH, +1);
                    mNowDate = mCalendar.getTime();

                    setTextDay(mNowDate);

                    setFragmentChangeDay(mNowDate);
                    break;*/

                case R.id.btn_dial_pattern:
                    changeDialPattern();
                    break;
                case R.id.info_slideView_cancle:
                    info_view.setVisibility(View.VISIBLE);
                    info_dial.setVisibility(View.VISIBLE);
                    info_slideView.setVisibility(View.GONE);
                    break;

                case R.id.btn_call:
                    break;
                case R.id.btn_app:
                    break;
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            btn_dial_pattern.setOnClickListener(null);
            switch (view.getId()) {
                case R.id.btn_dial_pattern:
                    if (smSwitchRandom) {
                        //TODO: 랜덤 적용안된 이미지
                        mImageBtn = mImageBtnDefault;
                        btn_dial_pattern.getHierarchy().setPlaceholderImage(mImageBtn.get(mSwitchValue));
                        smSwitchRandom = false;
                    } else {
                        //TODO: 랜덤 적용된 이미지
                        mImageBtn = mImageBtnRandom;
                        btn_dial_pattern.getHierarchy().setPlaceholderImage(mImageBtn.get(mSwitchValue));
                        smSwitchRandom = true;
                    }
                    break;
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn_dial_pattern.setOnClickListener(onClickListener);
                }
            }, 500);

            return false;
        }
    };

    private void changeDialPattern() {

        switch (mSwitchValue) {
            case 0:
                mSwitchValue = 1;
                break;
            case 1:
                mSwitchValue = 2;
                break;
            case 2:
                mSwitchValue = 0;
                break;
        }

        //btn_dial_pattern.setBackgroundResource(mImageBtn.get(mSwitchValue));
        btn_dial_pattern.getHierarchy().setPlaceholderImage(mImageBtn.get(mSwitchValue));
        circleLayout.isInitDial();

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

    /**
     * 등록된 이메일로 비밀번호 보내는 함수
     */
    private void startEmailSend() {

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {

                    GMailSender mail = new GMailSender("geogotae@gmail.com", "molppang202501");  //보내는 사람 메일 주소와 암호
                    String email = CommonJava.loadSharedPreferences(mActivity, "email");
                    String password = CommonJava.loadSharedPreferences(mActivity, "password");
                    //순서대로, 제목 - 본문 - 보내는 사람 메일 - 받는 사람 메일

                    mail.sendMail(
                            "다이얼락 비밀번호입니다.",
                            password,
                            "geogotae@gmail.com",
                            email
                    );


                } catch (Exception e) {
                }

                return null;
            }
        };
        asyncTask.execute();
    }

    /**
     * 이메일 발송시 토스트 메시지 3초 동안 보여줌
     */
    public void startTxtToast(String strMsg) {
        ((TextView) mLockView.findViewById(R.id.txt_toast)).setText(strMsg);
        ((TextView) mLockView.findViewById(R.id.txt_toast)).setVisibility(View.VISIBLE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    ((TextView) mLockView.findViewById(R.id.txt_toast)).setVisibility(View.INVISIBLE);
                } catch (NullPointerException e) {
                    //화면이 정상적으로 죽으면  에러 발생
                }
            }
        }, 2000);
    }

    public void setLockStatusListener(LockStatusListener listener) {
        this.lockStatusListener = listener;
    }

    public interface LockStatusListener {
        void onLocked();

        void onUnlock();
    }

    private void setListView() {
        //mNowDate = mCalendar.getTime();

        setTextDay(mNowDate);
    }


    private void setTextDay(Date nowDate) {
        String strTxtLockDay =
                CommonJava.getYear(nowDate) + ". " + CommonJava.getMonth(nowDate) + ". " + CommonJava.getDay(nowDate) + ". " + CommonJava.getDayOfWeek(nowDate);

        txt_lock_day.setText(strTxtLockDay);
    }
}
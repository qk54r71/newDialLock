package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Activity.LockScreenActivity;
import com.diallock.diallock.diallock.Activity.Activity.LockScreenViewActivity;
import com.diallock.diallock.diallock.Activity.Adapter.DialPagerAdapter;
import com.diallock.diallock.diallock.Activity.Adapter.WidgetPagerAdapter;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.GMailSender;
import com.diallock.diallock.diallock.Activity.Common.LockScreenManager;
import com.diallock.diallock.diallock.Activity.Layout.ViewPager.DialViewPager;
import com.diallock.diallock.diallock.Activity.Layout.ViewPager.HorizontalViewPager;
import com.diallock.diallock.diallock.Activity.ParkSDK.Debug.Loging;
import com.diallock.diallock.diallock.Activity.taskAction.ScreenService;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LockScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LockScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LockScreenFragment extends Fragment {

    private View mView;
    private Boolean backFlag;
    private static Context mContext;
    private static LockScreenFragment mLockScreenFragment;

    private TextView txt_lock_day;
    private TextView txt_lock_title;
    private SimpleDraweeView lock_screen_pre;
    private SimpleDraweeView lock_screen_nex;
    private SimpleDraweeView lock_screen_pre_double;
    private SimpleDraweeView lock_screen_nex_double;
    private Date mNowDate;
    private Calendar mCalendar;

    private HorizontalViewPager mCategoryViewPager;

    private ArrayList<Fragment> mInfoFragmentArrayList;
    private EventInfo mEventInfo;
    private TourismInfo mTourismInfo;

    private LinearLayout info_view;
    private RelativeLayout info_dial;
    private RelativeLayout info_slideView;
    private HorizontalViewPager info_slideView_viewPager_horizontal;
    private SimpleDraweeView info_slideView_cancle;
    private TextView txtImgSlideProgress;

    /**
     * 비밀번호 찾기 버튼
     */
    private SimpleDraweeView btn_find_pass;

    /**
     * dial view control button
     */
    private SimpleDraweeView btn_dial_pattern;
    private ArrayList<Integer> mImageBtn;
    private ArrayList<Integer> mImageBtnRandom;
    private ArrayList<Integer> mImageBtnDefault;
    private ArrayList<String> mStrTitle;
    public static Integer mSwitchValue;
    private final int DIAL_SLIDE = 0;
    private final int DIAL_ZIGZAG = 1;
    private final int DIAL_PRESS = 2;

    /**
     * dail second control button
     */
    private SimpleDraweeView btn_call;
    private SimpleDraweeView btn_app;

    /**
     * random button
     */
    public static Boolean smSwitchRandom;


    /**
     * widget control
     */
    private HorizontalViewPager mWidget_view;
    private WidgetPagerAdapter mWidgetPagerAdapter;

    /**
     * dial fragment control
     */
    private LinearLayout mDial_view;
    private DialPagerAdapter mDialPagerAdpater;

    private CircleDial_slide mCircleDial_slide;
    private CircleDial_zigzag mCircleDial_zigzag;
    private CircleDial_press mCircleDial_press;

    /**
     * toast msg controll
     */
    private static TextView mTxt_toast;

    private final String LOG_NAME = "LockScreenFragment";

    private OnFragmentInteractionListener mListener;

    public LockScreenFragment() {
        // Required empty public constructor
        if (mContext == null) {
            mContext = getContext();
        }
    }

    public static synchronized LockScreenFragment newInstance() {
        if (mLockScreenFragment == null) {
            mLockScreenFragment = new LockScreenFragment();
        }
        return mLockScreenFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonJava.Loging.i(LOG_NAME, "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CommonJava.Loging.i(LOG_NAME, "onCreateView()");
        mView = inflater.inflate(R.layout.fragment_lock_screen, container, false);

        findViewById();
        init();
        setOnClick();

        return mView;
    }

    private void findViewById() {

        btn_find_pass = (SimpleDraweeView) mView.findViewById(R.id.btn_find_pass);
        btn_dial_pattern = (SimpleDraweeView) mView.findViewById(R.id.btn_dial_pattern);

        txt_lock_day = (TextView) mView.findViewById(R.id.txt_lock_day);
        txt_lock_title = (TextView) mView.findViewById(R.id.txt_lock_title);
        lock_screen_pre = (SimpleDraweeView) mView.findViewById(R.id.lock_screen_pre);
        lock_screen_pre_double = (SimpleDraweeView) mView.findViewById(R.id.lock_screen_pre_double);
        lock_screen_nex = (SimpleDraweeView) mView.findViewById(R.id.lock_screen_nex);
        lock_screen_nex_double = (SimpleDraweeView) mView.findViewById(R.id.lock_screen_nex_double);
        mCategoryViewPager = (HorizontalViewPager) mView.findViewById(R.id.info_frag_horizontal);

        info_view = (LinearLayout) mView.findViewById(R.id.info_view);
        info_dial = (RelativeLayout) mView.findViewById(R.id.info_dial);
        info_slideView = (RelativeLayout) mView.findViewById(R.id.info_slideView);
        info_slideView_viewPager_horizontal = (HorizontalViewPager) mView.findViewById(R.id.info_slideView_viewPager_horizontal);
        info_slideView_cancle = (SimpleDraweeView) mView.findViewById(R.id.info_slideView_cancle);

        btn_call = (SimpleDraweeView) mView.findViewById(R.id.btn_call);
        btn_app = (SimpleDraweeView) mView.findViewById(R.id.btn_app);

        txtImgSlideProgress = (TextView) mView.findViewById(R.id.txtImgSlideProgress);

        mDial_view = (LinearLayout) mView.findViewById(R.id.dial_view);
        mWidget_view = (HorizontalViewPager) mView.findViewById(R.id.widget_view);

        mCircleDial_slide = CircleDial_slide.getInstance(mLockScreenFragment);
        mCircleDial_zigzag = CircleDial_zigzag.getInstance(mLockScreenFragment);
        mCircleDial_press = CircleDial_press.getInstance(mLockScreenFragment);

        mTxt_toast = (TextView) mView.findViewById(R.id.txt_toast);
    }

    private void init() {
        CommonJava.Loging.i(LOG_NAME, "init");

        backFlag = false;

        mNowDate = CommonJava.getNowDate();
        mCalendar = Calendar.getInstance();

        setTextDay(mNowDate);

        smSwitchRandom = CommonJava.loadSharedPreferences_Boolean(getContext(), "smSwitchRandom");

        mImageBtnDefault = new ArrayList<>();
        mImageBtnDefault.add(R.drawable.dial_slide);
        mImageBtnDefault.add(R.drawable.dial_zigzag);
        mImageBtnDefault.add(R.drawable.dial_press);

        mImageBtnRandom = new ArrayList<>();
        mImageBtnRandom.add(R.drawable.dial_slide_random);
        mImageBtnRandom.add(R.drawable.dial_zigzag_random);
        mImageBtnRandom.add(R.drawable.dial_press_random);

        if (smSwitchRandom) {
            mImageBtn = mImageBtnRandom;
        } else {
            mImageBtn = mImageBtnDefault;
        }

        mSwitchValue = CommonJava.loadSharedPreferences_Integer(getContext(), "mSwitchValue");

        btn_dial_pattern.getHierarchy().setPlaceholderImage(mImageBtn.get(mSwitchValue));

        //circleLayout.isInitDial();

        mStrTitle = new ArrayList<>();

        mInfoFragmentArrayList = new ArrayList<>();
        mEventInfo = new EventInfo().newInstance(mNowDate);
        mTourismInfo = new TourismInfo().newInstance(mNowDate);

        //TODO:나중에 회원 설정 정보에 따라서 추가 해야됨
        mInfoFragmentArrayList.add(mEventInfo);
        mInfoFragmentArrayList.add(mTourismInfo);

        for (Fragment fragment : mInfoFragmentArrayList) {
            if (fragment instanceof EventInfo) {
                mStrTitle.add("행사");
            } else if (fragment instanceof TourismInfo) {
                mStrTitle.add("관광");
            }
        }

        txt_lock_title.setText(mStrTitle.get(0));


        info_slideView.setVisibility(View.GONE);

        setFragment();

    }

    private void setFragment() {
        setCategoryFragment();
        setDialFragment();
        setWidgetFragment();
    }

    private void setDialFragment() {

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(mCircleDial_slide);
        fragmentArrayList.add(mCircleDial_zigzag);
        fragmentArrayList.add(mCircleDial_press);

        /*mDialPagerAdpater = new DialPagerAdapter(getChildFragmentManager(), fragmentArrayList);
        mDial_view.setAdapter(mDialPagerAdpater);*/
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.dial_view, fragmentArrayList.get(mSwitchValue)).commit();

    }

    private void setWidgetFragment() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new WidgetTimeBaseFragment());
        fragmentArrayList.add(new ImageFragment());

        mWidgetPagerAdapter = new WidgetPagerAdapter(getChildFragmentManager(), fragmentArrayList);
        mWidget_view.setAdapter(mWidgetPagerAdapter);
    }

    private void setCategoryFragment() {
        WidgetPagerAdapter categoryPagerAdapter = new WidgetPagerAdapter(getChildFragmentManager(), mInfoFragmentArrayList);

        mCategoryViewPager.setAdapter(categoryPagerAdapter);
        mCategoryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*
                CommonJava.Loging.i(LOG_NAME, "onPageScrolled position : " + position);
                CommonJava.Loging.i(LOG_NAME, "onPageScrolled positionOffset : " + positionOffset);
                CommonJava.Loging.i(LOG_NAME, "onPageScrolled positionOffsetPixels : " + positionOffsetPixels);*/
            }

            @Override
            public void onPageSelected(int position) {
                CommonJava.Loging.i(LOG_NAME, "onPageSelected position : " + position);
                String strTitle = mStrTitle.get(position);
                txt_lock_title.setText(strTitle);
                if (getActivity() instanceof LockScreenActivity) {
                    ((LockScreenActivity) getActivity()).setTitle(strTitle);
                } else if (getActivity() instanceof LockScreenViewActivity) {

                }
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

        mCategoryViewPager.invalidate();
    }

    private void setOnClick() {

        lock_screen_pre.setOnClickListener(onClickListener);
        lock_screen_pre.setOnLongClickListener(onLongClickListener);
        //lock_screen_pre_double.setOnClickListener(onClickListener);

        lock_screen_nex.setOnClickListener(onClickListener);
        lock_screen_nex.setOnLongClickListener(onLongClickListener);
        //lock_screen_nex_double.setOnClickListener(onClickListener);

        btn_find_pass.setOnLongClickListener(onLongClickListener);
        btn_dial_pattern.setOnLongClickListener(onLongClickListener);
        btn_dial_pattern.setOnClickListener(onClickListener);
        info_slideView_cancle.setOnClickListener(onClickListener);
        btn_call.setOnClickListener(onClickListener);
        btn_app.setOnClickListener(onClickListener);

        mWidget_view.setOnClickListener(onClickListener);

        info_slideView_cancle.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CommonJava.Loging.i(LOG_NAME, "onClickListener view.getId() : " + view.getId());
            switch (view.getId()) {
                case R.id.lock_screen_pre:

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
                    break;

                case R.id.btn_dial_pattern:
                    changeDialPattern();
                    break;

                case R.id.btn_call:
                    break;
                case R.id.btn_app:
                    break;

                case R.id.info_slideView_cancle:
                    info_view.setVisibility(View.VISIBLE);
                    info_slideView.setVisibility(View.GONE);
                    info_dial.setVisibility(View.VISIBLE);

                    /*if (getActivity() instanceof LockScreenViewActivity) {
                        ((LockScreenViewActivity) getActivity()).setInfoDialVisibility(View.VISIBLE);
                    }*/
                    break;
                case R.id.widget_view:
                    Loging.i(LOG_NAME, "widget_view touch");
                    break;
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            CommonJava.Loging.i(LOG_NAME, "onLongClickListener view.getId() : " + view.getId());
            switch (view.getId()) {
                case R.id.btn_dial_pattern:
                    btn_dial_pattern.setOnClickListener(null);
                    CommonJava.Loging.i(LOG_NAME, "onLongClickListener btn_dial_pattern");
                    if (smSwitchRandom) {
                        toastMsgShow("기본 으로 적용되었습니다.");
                        //TODO: 랜덤 적용안된 이미지
                        mImageBtn = mImageBtnDefault;
                        btn_dial_pattern.getHierarchy().setPlaceholderImage(mImageBtn.get(mSwitchValue));
                        smSwitchRandom = false;
                    } else {
                        toastMsgShow("랜덤 으로 적용되었습니다.");
                        //TODO: 랜덤 적용된 이미지
                        mImageBtn = mImageBtnRandom;
                        btn_dial_pattern.getHierarchy().setPlaceholderImage(mImageBtn.get(mSwitchValue));
                        smSwitchRandom = true;
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_dial_pattern.setOnClickListener(onClickListener);
                        }
                    }, 500);

                    break;

                case R.id.btn_find_pass:
                    CommonJava.Loging.i(LOG_NAME, "onClick()");
                    startEmailSend();

                    toastMsgShow("플레이스토어에 등록된 gmail 로 비밀번호가 전송되었습니다.");
                    break;

                case R.id.lock_screen_pre:

                    mCalendar.add(Calendar.MONTH, -1);
                    mNowDate = mCalendar.getTime();

                    setTextDay(mNowDate);

                    setFragmentChangeDay(mNowDate);

                    lock_screen_pre.setSelected(true);
                    lock_screen_pre.setOnClickListener(null);
                    comebackLongBtnEanble(R.id.lock_screen_pre);
                    break;

                case R.id.lock_screen_nex:

                    mCalendar.add(Calendar.MONTH, +1);
                    mNowDate = mCalendar.getTime();

                    setTextDay(mNowDate);

                    setFragmentChangeDay(mNowDate);

                    lock_screen_nex.setSelected(true);
                    lock_screen_nex.setOnClickListener(null);
                    comebackLongBtnEanble(R.id.lock_screen_nex);
                    break;

            }
            return false;
        }
    };

    /**
     * click 되어 바뀐 이미지를 원상 복귀
     *
     * @param btnId change 된 btn 의 index 값
     */
    private void comebackBtnImage(final Integer btnId) {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (btnId) {
                    case R.id.lock_screen_pre:

                        lock_screen_pre.setBackgroundResource(R.drawable.previous);
                        break;
                    case R.id.lock_screen_nex:

                        lock_screen_nex.setBackgroundResource(R.drawable.next);
                        break;
                }

            }
        }.sendEmptyMessageDelayed(0, 200);
    }

    /**
     * @param btnId
     */
    private void comebackLongBtnEanble(final Integer btnId) {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (btnId) {
                    case R.id.lock_screen_pre:

                        lock_screen_pre.setSelected(false);
                        lock_screen_pre.setOnClickListener(onClickListener);
                        break;
                    case R.id.lock_screen_nex:

                        lock_screen_nex.setSelected(false);
                        lock_screen_nex.setOnClickListener(onClickListener);
                        break;
                }

            }
        }.sendEmptyMessageDelayed(0, 500);
    }

    private void changeDialPattern() {

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (mSwitchValue) {
            case DIAL_SLIDE:
                mSwitchValue = DIAL_ZIGZAG;
                fragmentTransaction.replace(R.id.dial_view, mCircleDial_zigzag);
                break;
            case DIAL_ZIGZAG:
                mSwitchValue = DIAL_PRESS;
                fragmentTransaction.replace(R.id.dial_view, mCircleDial_press);
                break;
            case DIAL_PRESS:
                mSwitchValue = DIAL_SLIDE;
                fragmentTransaction.replace(R.id.dial_view, mCircleDial_slide);
                break;
        }
        fragmentTransaction.commit();

        //btn_dial_pattern.setBackgroundResource(mImageBtn.get(mSwitchValue));
        btn_dial_pattern.getHierarchy().setPlaceholderImage(mImageBtn.get(mSwitchValue));

    }

    /**
     * 등록된 이메일로 비밀번호 보내는 함수
     */
    private void startEmailSend() {

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {

                    GMailSender mail = new GMailSender("geogotae@gmail.com", "diallock202501");  //보내는 사람 메일 주소와 암호
                    String email = CommonJava.loadSharedPreferences(getContext(), "email");
                    String password = CommonJava.loadSharedPreferences(getContext(), "password");
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

    private void setTextDay(Date nowDate) {
        String strTxtLockDay =
                CommonJava.getYear(nowDate) + ". " + CommonJava.getMonth(nowDate) + ". " + CommonJava.getDay(nowDate) + ". " + CommonJava.getDayOfWeek(nowDate);

        txt_lock_day.setText(strTxtLockDay);
    }

    public void setImageSildeView(final ArrayList<String> strSlideImage) {
        CommonJava.Loging.i(LOG_NAME, "setImageSildeView : " + strSlideImage);
        info_view.setVisibility(View.GONE);
        //info_dial.setVisibility(View.GONE);
        if (getActivity() instanceof LockScreenViewActivity) {
            ((LockScreenViewActivity) getActivity()).setInfoDialVisibility(View.GONE);
        }
        info_slideView.setVisibility(View.VISIBLE);

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        for (String strSildeImageURL : strSlideImage) {
            fragmentArrayList.add(new SlideImage().newInstance(strSildeImageURL));
            CommonJava.Loging.i(LOG_NAME, "strSildeImageURL : " + strSildeImageURL);
        }

        WidgetPagerAdapter tourSildeWidgetPagerAdapter = new WidgetPagerAdapter(getChildFragmentManager(), fragmentArrayList);
        info_slideView_viewPager_horizontal.setAdapter(tourSildeWidgetPagerAdapter);

        String strImgSlideProgress = "1 / " + strSlideImage.size();
        txtImgSlideProgress.setText(strImgSlideProgress);

        info_slideView_viewPager_horizontal.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                String strImgSlideProgress = (position + 1) + " / " + strSlideImage.size();
                txtImgSlideProgress.setText(strImgSlideProgress);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 현재 Activity 에 따라서 Toast 방식을 달리 보여줌
     *
     * @param strMgs 보여줄 메세지
     */
    public void toastMsgShow(String strMgs) {
        if (getContext() instanceof LockScreenActivity) {

            Toast.makeText(getContext(), strMgs, Toast.LENGTH_SHORT).show();

        } else if (getContext() instanceof LockScreenViewActivity || getContext() == null) {
            CommonJava.Loging.i(LOG_NAME, "mContext : " + mContext);
            mTxt_toast.setText(strMgs);
            mTxt_toast.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTxt_toast.setVisibility(View.GONE);
                    mTxt_toast.setText(null);
                }
            }, 2000);
        }
    }

    /**
     * 현재 Activity에 따라서 화면을 finish 함
     */
    public void unLockDial() {
        CommonJava.Loging.i(LOG_NAME, "unLockDial()");
        CommonJava.Loging.i(LOG_NAME, "getContext() : " + getContext());

        if (getContext() instanceof LockScreenActivity) {

            Intent intentStartService = new Intent(getContext(), ScreenService.class);
            getContext().startService(intentStartService);
            CommonJava.saveSharedPreferences(getContext(), "lockCheck", "true");
            ((LockScreenActivity) getContext()).finish();

        } else if (getContext() instanceof LockScreenViewActivity || getContext() == null) {
            CommonJava.Loging.i(LOG_NAME, "unLockDial()");
            LockScreenManager.getInstance(getActivity()).unLock();
            ScreenService.mPhoneProgressLock = false;

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setOnTouchEvent(MotionEvent onTouchEvent) {

        switch (mSwitchValue) {
            case DIAL_SLIDE:
                if (mCircleDial_slide == null) {
                    mCircleDial_slide = CircleDial_slide.getInstance(mLockScreenFragment);
                }
                mCircleDial_slide.setOnTouchEvent(onTouchEvent);
                break;
            case DIAL_ZIGZAG:
                if (mCircleDial_zigzag == null) {
                    mCircleDial_zigzag = CircleDial_zigzag.getInstance(mLockScreenFragment);
                }
                mCircleDial_zigzag.setOnTouchEvent(onTouchEvent);

                break;
            case DIAL_PRESS:
                if (mCircleDial_press == null) {
                    mCircleDial_press = CircleDial_press.getInstance(mLockScreenFragment);
                }
                mCircleDial_press.setOnTouchEvent(onTouchEvent);
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonJava.Loging.i(LOG_NAME, "onDestroy()");
        CommonJava.saveSharedPreferences_Integer(getContext(), "mSwitchValue", mSwitchValue);
        CommonJava.saveSharedPreferences_Boolean(getContext(), "smSwitchRandom", smSwitchRandom);
    }
}

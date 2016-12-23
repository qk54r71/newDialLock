package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.diallock.diallock.diallock.Activity.Adapter.WidgetPagerAdapter;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.GMailSender;
import com.diallock.diallock.diallock.Activity.Layout.*;
import com.diallock.diallock.diallock.Activity.Layout.ViewPager.HorizontalViewPager;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.view.SimpleDraweeView;

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
    private CircleDial circleDial;
    private Boolean backFlag;

    private TextView txt_lock_day;
    private TextView txt_lock_title;
    private SimpleDraweeView lock_screen_pre;
    private SimpleDraweeView lock_screen_nex;
    private SimpleDraweeView lock_screen_pre_double;
    private SimpleDraweeView lock_screen_nex_double;
    private Date mNowDate;
    private Calendar mCalendar;

    private WidgetPagerAdapter mWidgetPagerAdapter;
    private HorizontalViewPager mHorizontalViewPager;

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

    private final String LOG_NAME = "LockScreenFragment";

    private OnFragmentInteractionListener mListener;

    public LockScreenFragment() {
        // Required empty public constructor
    }

    public static LockScreenFragment newInstance() {
        LockScreenFragment fragment = new LockScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_lock_screen, container, false);

        findViewById();
        init();
        setOnClick();

        return mView;
    }

    private void findViewById() {

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            CommonJava.Loging.i(LOG_NAME, "fragment : " + fragment);
            if (fragment instanceof CircleDial) {
                circleDial = (CircleDial) fragment;
            }
        }

        btn_find_pass = (SimpleDraweeView) mView.findViewById(R.id.btn_find_pass);
        btn_dial_pattern = (SimpleDraweeView) mView.findViewById(R.id.btn_dial_pattern);

        txt_lock_day = (TextView) mView.findViewById(R.id.txt_lock_day);
        txt_lock_title = (TextView) mView.findViewById(R.id.txt_lock_title);
        lock_screen_pre = (SimpleDraweeView) mView.findViewById(R.id.lock_screen_pre);
        lock_screen_pre_double = (SimpleDraweeView) mView.findViewById(R.id.lock_screen_pre_double);
        lock_screen_nex = (SimpleDraweeView) mView.findViewById(R.id.lock_screen_nex);
        lock_screen_nex_double = (SimpleDraweeView) mView.findViewById(R.id.lock_screen_nex_double);
        mHorizontalViewPager = (HorizontalViewPager) mView.findViewById(R.id.info_frag_horizontal);

        info_view = (LinearLayout) mView.findViewById(R.id.info_view);
        info_dial = (RelativeLayout) mView.findViewById(R.id.info_dial);
        info_slideView = (RelativeLayout) mView.findViewById(R.id.info_slideView);
        info_slideView_viewPager_horizontal = (HorizontalViewPager) mView.findViewById(R.id.info_slideView_viewPager_horizontal);
        info_slideView_cancle = (SimpleDraweeView) mView.findViewById(R.id.info_slideView_cancle);

        btn_call = (SimpleDraweeView) mView.findViewById(R.id.btn_call);
        btn_app = (SimpleDraweeView) mView.findViewById(R.id.btn_app);

        txtImgSlideProgress = (TextView) mView.findViewById(R.id.txtImgSlideProgress);
    }

    private void init() {
        CommonJava.Loging.i(LOG_NAME, "init");

        backFlag = false;

        mNowDate = CommonJava.getNowDate();
        mCalendar = Calendar.getInstance();

        setTextDay(mNowDate);

        mImageBtnDefault = new ArrayList<>();
        mImageBtnDefault.add(R.drawable.btn_num_1);
        mImageBtnDefault.add(R.drawable.btn_num_2);
        mImageBtnDefault.add(R.drawable.btn_num_3);

        mImageBtnRandom = new ArrayList<>();
        mImageBtnRandom.add(R.drawable.btn_num_1_random);
        mImageBtnRandom.add(R.drawable.btn_num_2_random);
        mImageBtnRandom.add(R.drawable.btn_num_3_random);

        mImageBtn = mImageBtnRandom;

        mSwitchValue = 0;

        btn_dial_pattern.getHierarchy().setPlaceholderImage(mImageBtn.get(mSwitchValue));

        //circleLayout.isInitDial();

        smSwitchRandom = true;

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

        setFragment();

        info_slideView.setVisibility(View.GONE);
    }

    private void setFragment() {
        mWidgetPagerAdapter = new WidgetPagerAdapter(getChildFragmentManager(), mInfoFragmentArrayList);

        mHorizontalViewPager.setAdapter(mWidgetPagerAdapter);
        mHorizontalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        mHorizontalViewPager.invalidate();
    }

    private void setOnClick() {

        lock_screen_pre.setOnClickListener(onClickListener);
        lock_screen_pre_double.setOnClickListener(onClickListener);
        lock_screen_nex.setOnClickListener(onClickListener);
        lock_screen_nex_double.setOnClickListener(onClickListener);

        btn_find_pass.setOnClickListener(onClickListener);
        btn_dial_pattern.setOnLongClickListener(onLongClickListener);
        btn_dial_pattern.setOnClickListener(onClickListener);
        info_slideView_cancle.setOnClickListener(onClickListener);
        btn_call.setOnClickListener(onClickListener);
        btn_app.setOnClickListener(onClickListener);

        info_slideView_cancle.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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
                case R.id.btn_find_pass:
                    CommonJava.Loging.i(LOG_NAME, "onClick()");
                    startEmailSend();

                    Toast.makeText(getContext(), "플레이스토어에 등록된 gmail 로 비밀번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();

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
                        isToast("기본 으로 적용되었습니다.");
                        //TODO: 랜덤 적용안된 이미지
                        mImageBtn = mImageBtnDefault;
                        btn_dial_pattern.getHierarchy().setPlaceholderImage(mImageBtn.get(mSwitchValue));
                        smSwitchRandom = false;
                    } else {
                        isToast("랜덤 으로 적용되었습니다.");
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
            }
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

    public void isToast(String strMsg) {
        Toast.makeText(getContext(), strMsg, Toast.LENGTH_SHORT).show();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setOnTouchEvent(MotionEvent onTouchEvent) {
        circleDial.setOnTouchEvent(onTouchEvent);
    }

}

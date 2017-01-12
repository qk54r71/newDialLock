package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Data.ChildBtnInfo;
import com.diallock.diallock.diallock.Activity.ParkSDK.Data.DialCircleInfo_Image;
import com.diallock.diallock.diallock.Activity.ParkSDK.Data.DialCircleInfo_Location;
import com.diallock.diallock.diallock.Activity.ParkSDK.Debug.Loging;
import com.diallock.diallock.diallock.Activity.ParkSDK.Util.Circle;
import com.diallock.diallock.diallock.Activity.ParkSDK.Util.LocalInformation;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collections;

import static com.diallock.diallock.diallock.Activity.Common.LockScreenManager.mActivity;

public class CircleDial_press extends Fragment {

    static View mView;

    private static LockScreenFragment mLockScreenFragment;
    private static CircleDial_press mCircleDial_press;

    private com.diallock.diallock.diallock.Activity.Layout.DialLayout mDialLayout_press;
    private SimpleDraweeView btn_index_00;
    private SimpleDraweeView btn_index_01;
    private SimpleDraweeView btn_index_02;
    private SimpleDraweeView btn_index_03;
    private SimpleDraweeView btn_index_04;
    private SimpleDraweeView btn_index_05;
    private SimpleDraweeView btn_index_06;
    private SimpleDraweeView btn_index_07;
    private SimpleDraweeView btn_index_08;
    private SimpleDraweeView btn_index_09;
    private SimpleDraweeView btn_index_10;
    private SimpleDraweeView btn_index_11;

    /**
     * Dial Image 의 정보
     */
    private ArrayList<ChildBtnInfo> mChildBtnInfos;
    private ArrayList<DialCircleInfo_Image> mChildBtnInfo_Images;
    private ArrayList<DialCircleInfo_Location> mChildBtnInfo_Location;
    private ArrayList<Integer> mBtnIndexId;
    private DialCircleInfo_Location mBigDialImage_Location;
    private DialCircleInfo_Location mSmallDialImage_Location;

    /**
     * 현재 다이얼 진행 값
     */
    private static final Boolean DIAL_TOUCH_TRUE = true;
    private static final Boolean DIAL_TOUCH_FALSE = false;

    /**
     * touch 되는 index 값을 기록하는 List
     */
    private ArrayList<Integer> mRegisterIndex;

    /**
     * touch 시에 실제 입력되는 값을 기록하는 List
     */
    private ArrayList<Integer> mRegisterPressIndex;

    /**
     * dial touch 시작을 기록하는 boolean
     */
    private Boolean mDialTouch;

    /**
     * touch 의 정방향 true, 역방향 false 을 기록하는 변수
     */
    private Boolean mDirection;

    /**
     * 다이얼 안의 위젯이 터치됬는지 체크
     */
    private Boolean mSmallDialTouch;

    /**
     * 현재 입력되는 패스워드 값
     */
    private String mInputPassword;

    private MotionEvent mMotionEvent;

    private static final String LOG_NAME = "CircleDial_press";

    public CircleDial_press() {
        // Required empty public constructor
    }

    public static synchronized CircleDial_press getInstance(Fragment fragment) {
        CommonJava.Loging.i(LOG_NAME, " getInstance(Context context)");
        if (mCircleDial_press == null) {
            mLockScreenFragment = (LockScreenFragment) fragment;
            mCircleDial_press = new CircleDial_press();
        }

        return mCircleDial_press;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonJava.Loging.i(LOG_NAME, "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonJava.Loging.i(LOG_NAME, "onCreateView()");
        mView = inflater.inflate(R.layout.fragment_circle_dial_press, container, false);

        setFindViewById();
        init();
        setEvent();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        CommonJava.Loging.i(LOG_NAME, "onAttach()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        CommonJava.Loging.i(LOG_NAME, "onDetach()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
    }

    private void setFindViewById() {
        mDialLayout_press = (com.diallock.diallock.diallock.Activity.Layout.DialLayout) mView.findViewById(R.id.dialLayout_press);
        btn_index_00 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_00);
        btn_index_01 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_01);
        btn_index_02 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_02);
        btn_index_03 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_03);
        btn_index_04 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_04);
        btn_index_05 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_05);
        btn_index_06 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_06);
        btn_index_07 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_07);
        btn_index_08 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_08);
        btn_index_09 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_09);
        btn_index_10 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_10);
        btn_index_11 = (SimpleDraweeView) mView.findViewById(R.id.btn_index_11);

    }

    private void init() {

        mBtnIndexId = new ArrayList<>();
        mBtnIndexId.add(R.id.btn_index_00);
        mBtnIndexId.add(R.id.btn_index_01);
        mBtnIndexId.add(R.id.btn_index_02);
        mBtnIndexId.add(R.id.btn_index_03);
        mBtnIndexId.add(R.id.btn_index_04);
        mBtnIndexId.add(R.id.btn_index_05);
        mBtnIndexId.add(R.id.btn_index_06);
        mBtnIndexId.add(R.id.btn_index_07);
        mBtnIndexId.add(R.id.btn_index_08);
        mBtnIndexId.add(R.id.btn_index_09);
        mBtnIndexId.add(R.id.btn_index_10);
        mBtnIndexId.add(R.id.btn_index_11);

        ArrayList<Integer> btn_images = new ArrayList();
        btn_images.add(R.drawable.num_0);
        btn_images.add(R.drawable.num_1);
        btn_images.add(R.drawable.num_2);
        btn_images.add(R.drawable.num_3);
        btn_images.add(R.drawable.num_4);
        btn_images.add(R.drawable.num_5);
        btn_images.add(R.drawable.num_6);
        btn_images.add(R.drawable.num_7);
        btn_images.add(R.drawable.num_8);
        btn_images.add(R.drawable.num_9);

        ArrayList<Integer> btn_images_click = new ArrayList();
        btn_images_click.add(R.drawable.num_0_click);
        btn_images_click.add(R.drawable.num_1_click);
        btn_images_click.add(R.drawable.num_2_click);
        btn_images_click.add(R.drawable.num_3_click);
        btn_images_click.add(R.drawable.num_4_click);
        btn_images_click.add(R.drawable.num_5_click);
        btn_images_click.add(R.drawable.num_6_click);
        btn_images_click.add(R.drawable.num_7_click);
        btn_images_click.add(R.drawable.num_8_click);
        btn_images_click.add(R.drawable.num_9_click);

        ArrayList<Integer> btn_selector_images = new ArrayList();
        btn_selector_images.add(R.drawable.selector_btn_num_0);
        btn_selector_images.add(R.drawable.selector_btn_num_1);
        btn_selector_images.add(R.drawable.selector_btn_num_2);
        btn_selector_images.add(R.drawable.selector_btn_num_3);
        btn_selector_images.add(R.drawable.selector_btn_num_4);
        btn_selector_images.add(R.drawable.selector_btn_num_5);
        btn_selector_images.add(R.drawable.selector_btn_num_6);
        btn_selector_images.add(R.drawable.selector_btn_num_7);
        btn_selector_images.add(R.drawable.selector_btn_num_8);
        btn_selector_images.add(R.drawable.selector_btn_num_9);

        mChildBtnInfo_Images = new ArrayList<>();

        for (int btn_index = 0; btn_index < 12; btn_index++) {

            DialCircleInfo_Image dialCircleInfo_image = new DialCircleInfo_Image();
            if (btn_index < 10) {
                dialCircleInfo_image.setDialCircleImage(btn_images.get(btn_index));
                dialCircleInfo_image.setDialCircleImage_click(btn_images_click.get(btn_index));
                dialCircleInfo_image.setDialCircleValue(String.valueOf(btn_index));
                dialCircleInfo_image.setDialCircleImage_selector(btn_selector_images.get(btn_index));
            } else if (btn_index == 10) {
                Integer btn_index_10 = randomIndex(null);
                dialCircleInfo_image.setDialCircleImage(btn_images.get(btn_index_10));
                dialCircleInfo_image.setDialCircleImage_click(btn_images_click.get(btn_index_10));
                dialCircleInfo_image.setDialCircleValue(String.valueOf(btn_index_10));
                dialCircleInfo_image.setDialCircleImage_selector(btn_selector_images.get(btn_index_10));
            } else if (btn_index == 11) {
                Integer btn_index_11 = randomIndex(Integer.valueOf(mChildBtnInfo_Images.get(10).getDialCircleValue()));
                dialCircleInfo_image.setDialCircleImage(btn_images.get(btn_index_11));
                dialCircleInfo_image.setDialCircleImage_click(btn_images_click.get(btn_index_11));
                dialCircleInfo_image.setDialCircleValue(String.valueOf(btn_index_11));
                dialCircleInfo_image.setDialCircleImage_selector(btn_selector_images.get(btn_index_11));
            }

            mChildBtnInfo_Images.add(dialCircleInfo_image);
            //CommonJava.Loging.i(LOG_NAME, "dialCircleInfo_image " + btn_index + " value: " + dialCircleInfo_image.getDialCircleValue());
        }
        mChildBtnInfo_Images = setShuffle(mChildBtnInfo_Images);

        btn_index_00.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(0).getDialCircleImage_selector());
        btn_index_01.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(1).getDialCircleImage_selector());
        btn_index_02.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(2).getDialCircleImage_selector());
        btn_index_03.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(3).getDialCircleImage_selector());
        btn_index_04.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(4).getDialCircleImage_selector());
        btn_index_05.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(5).getDialCircleImage_selector());
        btn_index_06.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(6).getDialCircleImage_selector());
        btn_index_07.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(7).getDialCircleImage_selector());
        btn_index_08.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(8).getDialCircleImage_selector());
        btn_index_09.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(9).getDialCircleImage_selector());
        btn_index_10.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(10).getDialCircleImage_selector());
        btn_index_11.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(11).getDialCircleImage_selector());

        mBigDialImage_Location = new DialCircleInfo_Location();
        mSmallDialImage_Location = new DialCircleInfo_Location();

        mRegisterIndex = new ArrayList<>();
        mRegisterPressIndex = new ArrayList<>();

        mDialTouch = false;

        mDirection = null;
    }

    private void setBtnImage() {

        mChildBtnInfo_Images = setShuffle(mChildBtnInfo_Images);

        btn_index_00.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(0).getDialCircleImage_selector());
        btn_index_01.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(1).getDialCircleImage_selector());
        btn_index_02.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(2).getDialCircleImage_selector());
        btn_index_03.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(3).getDialCircleImage_selector());
        btn_index_04.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(4).getDialCircleImage_selector());
        btn_index_05.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(5).getDialCircleImage_selector());
        btn_index_06.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(6).getDialCircleImage_selector());
        btn_index_07.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(7).getDialCircleImage_selector());
        btn_index_08.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(8).getDialCircleImage_selector());
        btn_index_09.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(9).getDialCircleImage_selector());
        btn_index_10.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(10).getDialCircleImage_selector());
        btn_index_11.getHierarchy().setPlaceholderImage(mChildBtnInfo_Images.get(11).getDialCircleImage_selector());

    }

    /**
     * 주어진 List 를 섞음
     *
     * @param shuffleList 섞을 List
     * @return
     */
    private ArrayList setShuffle(ArrayList shuffleList) {

        Collections.shuffle(shuffleList);

        return shuffleList;
    }

    /**
     * 금지 된 번호를 제외한 0~9까지의 랜덤번호를 리턴함
     *
     * @param embargoIndex 금지 된 번호
     * @return
     */
    private int randomIndex(Integer embargoIndex) {

        int randomIndex = (int) Math.floor(Math.random() * 10);

        while (embargoIndex != null && embargoIndex == randomIndex) {
            randomIndex = (int) Math.floor(Math.random() * 10);
        }

        return randomIndex;
    }

    private void setEvent() {
        btn_index_00.setOnClickListener(onClickListener);
        btn_index_01.setOnClickListener(onClickListener);
        btn_index_02.setOnClickListener(onClickListener);
        btn_index_03.setOnClickListener(onClickListener);
        btn_index_04.setOnClickListener(onClickListener);
        btn_index_05.setOnClickListener(onClickListener);
        btn_index_06.setOnClickListener(onClickListener);
        btn_index_07.setOnClickListener(onClickListener);
        btn_index_08.setOnClickListener(onClickListener);
        btn_index_09.setOnClickListener(onClickListener);
        btn_index_10.setOnClickListener(onClickListener);
        btn_index_11.setOnClickListener(onClickListener);

        mDialLayout_press.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Integer touchedIndex = null;

            switch (v.getId()) {
                case R.id.btn_index_00:
                    touchedIndex = 0;
                    break;
                case R.id.btn_index_01:
                    touchedIndex = 1;
                    break;
                case R.id.btn_index_02:
                    touchedIndex = 2;
                    break;
                case R.id.btn_index_03:
                    touchedIndex = 3;
                    break;
                case R.id.btn_index_04:
                    touchedIndex = 4;
                    break;
                case R.id.btn_index_05:
                    touchedIndex = 5;
                    break;
                case R.id.btn_index_06:
                    touchedIndex = 6;
                    break;
                case R.id.btn_index_07:
                    touchedIndex = 7;
                    break;
                case R.id.btn_index_08:
                    touchedIndex = 8;
                    break;
                case R.id.btn_index_09:
                    touchedIndex = 9;
                    break;
                case R.id.btn_index_10:
                    touchedIndex = 10;
                    break;
                case R.id.btn_index_11:
                    touchedIndex = 11;
                    break;
            }
            if (mDialTouch == null) {
                mDialTouch = true;
            }

            btnImageChange(touchedIndex);
            inputPassword(touchedIndex);
            isVibrator();

            if (LockScreenFragment.smSwitchRandom) {
                setBtnImage();
            }

        }
    };

    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            CommonJava.Loging.i(LOG_NAME, "mDialLayout_press.getHeight() : " + mDialLayout_press.getHeight());
            CommonJava.Loging.i(LOG_NAME, "mDialLayout_press.getWidth() : " + mDialLayout_press.getWidth());
            CommonJava.Loging.i(LOG_NAME, "mView.getHeight() : " + mView.getHeight());
            CommonJava.Loging.i(LOG_NAME, "mView.getWidth() : " + mView.getWidth());
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            CommonJava.Loging.i(LOG_NAME, "point.x : " + point.x);
            CommonJava.Loging.i(LOG_NAME, "point.y : " + point.y);

            int[] location = new int[2];

            btn_index_11.getLocationOnScreen(location);
            int btn_index_11_x = location[0];
            int btn_index_11_y = location[1];
            int diff_x = point.x / 2 - btn_index_11_x;

            btn_index_02.getLocationOnScreen(location);
            int btn_index_02_x = location[0];
            int btn_index_02_y = location[1];
            int diff_y = point.y / 4 * 3 - btn_index_02_y;

            int dialRadiusValue = point.y / 4 * 3 - btn_index_11_y - diff_y;

            CommonJava.Loging.i(LOG_NAME, "diff_x : " + diff_x + " diff_y : " + diff_y + " dialRadiusValue : " + dialRadiusValue);

            // ------------------------ setting big dial location start ----------------------------
            int bigDial_X = point.x / 2;
            mBigDialImage_Location.set_xPosition(bigDial_X);
            CommonJava.Loging.i(LOG_NAME, "bigDial_X : " + bigDial_X);

            int bigDial_Y = point.y / 4 * 3;
            mBigDialImage_Location.set_yPosition(bigDial_Y);
            CommonJava.Loging.i(LOG_NAME, "bigDial_Y : " + bigDial_Y);


            int btn_index_11_getHeight_Big = btn_index_11.getHeight() / 2;
            CommonJava.Loging.i(LOG_NAME, "btn_index_11_getHeight_Big : " + btn_index_11_getHeight_Big);

            int bigRadius = (int) (dialRadiusValue + (btn_index_11_getHeight_Big * 1.5)); // Dial 중심 값에서 Button 중심값 까지의 거리 값  + (Button 반지름 값*1.5)

            mBigDialImage_Location.setDialCircleRadius(bigRadius);
            CommonJava.Loging.i(LOG_NAME, "bigRadius : " + bigRadius);
            // ------------------------ setting big dial location end ------------------------------

            // ------------------------ setting small dial location start --------------------------
            int smallDial_X = point.x / 2;
            mSmallDialImage_Location.set_xPosition(smallDial_X);
            CommonJava.Loging.i(LOG_NAME, "smallDIal_X : " + smallDial_X);

            int smallDial_Y = point.y / 4 * 3;
            mSmallDialImage_Location.set_yPosition(smallDial_Y);
            CommonJava.Loging.i(LOG_NAME, "smallDial_Y : " + smallDial_Y);

            int btn_index_11_getHeight_Small = btn_index_11.getHeight() / 2;

            int smallRadius = (int) (dialRadiusValue - (btn_index_11_getHeight_Small * 1.5)); // Dial 중심 값에서 Button 중심값 까지의 거리 값  - (Button 반지름 값*1.5)

            mSmallDialImage_Location.setDialCircleRadius(smallRadius);
            CommonJava.Loging.i(LOG_NAME, "smallRadius : " + smallRadius);
            // ------------------------ setting small dial location end ----------------------------

            // ------------------------ setting child button location start-------------------------

            int displayHeight = point.y / 2;
            CommonJava.Loging.i(LOG_NAME, "displayHeight : " + displayHeight);
            mChildBtnInfo_Location = new ArrayList<>();

            for (int btn_index = 0; btn_index < mBtnIndexId.size(); btn_index++) {
                SimpleDraweeView childBtn = (SimpleDraweeView) mDialLayout_press.getChildAt(btn_index);

                //CommonJava.Loging.i(LOG_NAME, "(int) childBtn.getX() : " + (int) childBtn.getX() + " diff_x : " + diff_x + "  childBtn.getWidth() / 2 : " + childBtn.getWidth() / 2);
                //CommonJava.Loging.i(LOG_NAME, " Conversion.dp_to_px(childBtn.getY(), getContext()) : " + Conversion.dp_to_px(childBtn.getY(), getContext()) + " childBtn.getHeight() / 2 : " + Conversion.dp_to_px(childBtn.getHeight() / 2, getContext()));
                int[] childLocation = new int[2];
                childBtn.getLocationOnScreen(childLocation);
                CommonJava.Loging.i(LOG_NAME, "location[0] : " + childLocation[0] + " location[1] : " + childLocation[1]);
                int childBtn_X = childLocation[0] + diff_x;
                //int childBtn_Y = (int) childBtn.getY() + childBtn.getHeight() / 2 + displayHeight;
                int childBtn_Y = childLocation[1] + diff_y;
                DialCircleInfo_Location dialCircleInfo_location = new DialCircleInfo_Location();
                dialCircleInfo_location.set_xPosition(childBtn_X);
                dialCircleInfo_location.set_yPosition(childBtn_Y);
                dialCircleInfo_location.setIndex(btn_index);

                CommonJava.Loging.i(LOG_NAME, "btn_index : " + btn_index + " childBtn_X : " + childBtn_X + " childBtn_Y : " + childBtn_Y);
                mChildBtnInfo_Location.add(dialCircleInfo_location);
            }
            // ------------------------ setting child button location end --------------------------

            mDialLayout_press.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    };

    /**
     * 현재 입력된 password 값을 저장하는 함수
     *
     * @param inputIndexNumber 현재 눌린 index 값
     */
    private void inputPassword(int inputIndexNumber) {
        String inputPasswordNumber = mChildBtnInfo_Images.get(inputIndexNumber).getDialCircleValue();

        if (mInputPassword == null) {
            mInputPassword = String.valueOf(inputPasswordNumber);
        } else {
            mInputPassword += String.valueOf(inputPasswordNumber);
        }

        CommonJava.Loging.i(LOG_NAME, "mInputPassword : " + mInputPassword);
    }

    /**
     * 입력된 password 값이 실제 password 와 맞는지 체크
     */
    public void isCheckPassword() {

        Boolean isCheckBl = null;
        if (mInputPassword != null) {
            isCheckBl = Circle._IsImaginaryCheck(getContext(), mInputPassword);
        } else {
            CommonJava.Loging.e(LOG_NAME, "strPassword is null");
        }

        if (isCheckBl != null && isCheckBl) {
            CommonJava.Loging.i(LOG_NAME, "password is true");
            unLock();
            isToast("맞는 비밀번호 입니다.");
        } else {
            CommonJava.Loging.i(LOG_NAME, "password is false strPassword : " + mInputPassword);
            isToast("잘못된 비밀번호 입니다.");
            initDialTouch();
        }
    }

    /**
     * 작은 원 안에 있는지 체크
     */
    private void isSmallDialInner() {
        Loging.i(LOG_NAME, "isSmallDialInner()");
       /* float touch_x = mMotionEvent.getX();
        float touch_y = mMotionEvent.getY();

        float smallCenterX = mSmallDialImage_Location.get_xPosition();
        float smallCenterY = mSmallDialImage_Location.get_yPosition();
        int smallRadius = mSmallDialImage_Location.getDialCircleRadius();
        Boolean smallDialInnerCheck = Circle._IsInnerCircleCheck(smallCenterX, smallCenterY, smallRadius, touch_x, touch_y);
        Loging.i(LOG_NAME, "smallDialInnerCheck : " + smallDialInnerCheck);*/
    }

    /**
     * 잠금화면을 풀어줌
     */
    private void unLock() {
        CommonJava.Loging.i(LOG_NAME, "unLock()");
        if (mLockScreenFragment == null) {
            mLockScreenFragment = LockScreenFragment.newInstance();
        }
        mLockScreenFragment.unLockDial();
    }

    /**
     * 메세지를 띄워주는 함수
     *
     * @param strMsg 띄워줄 메세지
     */
    private void isToast(String strMsg) {
        if (mLockScreenFragment == null) {
            mLockScreenFragment = LockScreenFragment.newInstance();
        }
        mLockScreenFragment.toastMsgShow(strMsg);
    }

    /**
     * 현재 진행에 따른 dial circle 의 bg 설정
     *
     * @param isDialTouck 현재 진행
     */
    private void setDialCircleBg(Boolean isDialTouck) {
        if (isDialTouck == null) {
            mDialLayout_press.setBackgroundResource(R.drawable.dial_image_press_off);
        } else if (isDialTouck == DIAL_TOUCH_TRUE) {
            mDialLayout_press.setBackgroundResource(R.drawable.dial_image_press_on);
        } else if (isDialTouck == DIAL_TOUCH_FALSE) {
            mDialLayout_press.setBackgroundResource(R.drawable.dial_image_press_off);
        }
    }


    /**
     * 현재 touch 되는 dial 의 모든 값을 초기화
     */
    private void initDialTouch() {
        setDialCircleBg(null);
        mDialTouch = false;
        mDirection = null;
        mInputPassword = null;
        mRegisterIndex.clear();
        mRegisterPressIndex.clear();

        if (LockScreenFragment.smSwitchRandom) {
            setBtnImage();
        }
    }

    /**
     * touch된 인덱스 값의 button 의 이미지를 change
     *
     * @param btnIndex touch 된 btn 의 index 값
     */
    private void btnImageChange(Integer btnIndex) {
        Integer changeImage = mChildBtnInfo_Images.get(btnIndex).getDialCircleImage_click();
        SimpleDraweeView changeBtn = (SimpleDraweeView) mDialLayout_press.getChildAt(btnIndex);
        changeBtn.setSelected(true);
        mDialLayout_press.setBackgroundResource(R.drawable.dial_image_press_on);

        //changeBtn.getHierarchy().setPlaceholderImage(changeImage);

        comebackBtnImage(btnIndex);

    }

    /**
     * touch 되어 바뀐 이미지를 원상 복귀
     *
     * @param changedIndex change 된 btn 의 index 값
     */
    private void comebackBtnImage(final Integer changedIndex) {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Integer changeImage = mChildBtnInfo_Images.get(changedIndex).getDialCircleImage();
                SimpleDraweeView changeBtn = (SimpleDraweeView) mDialLayout_press.getChildAt(changedIndex);
                //changeBtn.getHierarchy().setPlaceholderImage(changeImage);
                changeBtn.setSelected(false);
                mDialLayout_press.setBackgroundResource(R.drawable.dial_image_press_off);
            }
        }.sendEmptyMessageDelayed(0, 200);
    }

    /**
     * touch 되는 좌표값이 dial circle 을 벗어날 경우 error 표시
     *
     * @param repeatNumber 반복될 횟수
     */
    private void errorBtnImage(Integer repeatNumber) {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 4:
                        mDialLayout_press.setBackgroundResource(R.drawable.dial_image_press_on);
                        isVibrator();
                        isToast("다이얼 안에서 눌러 주세요.");
                        break;
                    case 3:
                        mDialLayout_press.setBackgroundResource(R.drawable.dial_image_press_off);
                        break;
                    case 2:
                        mDialLayout_press.setBackgroundResource(R.drawable.dial_image_press_on);
                        isVibrator();
                        break;
                    case 1:
                        mDialLayout_press.setBackgroundResource(R.drawable.dial_image_press_off);
                        break;
                }
                errorBtnImage(--msg.what);
            }
        }.sendEmptyMessageDelayed(repeatNumber, 200);
    }

    /**
     * 현재 touch 되는 Event check
     *
     * @param onTouchEvent
     */
    public void setOnTouchEvent(MotionEvent onTouchEvent) {
        //CommonJava.Loging.i(LOG_NAME, "setOnTouchEvent(MotionEvent onTouchEvent) MotionEvent : " + onTouchEvent);

       /* String strSwitch = getActivity().getIntent().getStringExtra("strSwitch");
        if (!strSwitch.equals("SettingActivity")) {
            int intStatusBarHeight = LocalInformation.getStatusBarHeight(getContext());
            touch_y = touch_y + intStatusBarHeight;
        }*/

    }

    /**
     * 진동 함수
     */
    private void isVibrator() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }
}

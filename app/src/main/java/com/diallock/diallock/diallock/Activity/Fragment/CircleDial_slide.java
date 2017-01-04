package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Data.ChildBtnInfo;
import com.diallock.diallock.diallock.Activity.Layout.*;
import com.diallock.diallock.diallock.Activity.Layout.DialLayout;
import com.diallock.diallock.diallock.Activity.ParkSDK.Data.DialCircleInfo_Image;
import com.diallock.diallock.diallock.Activity.ParkSDK.Data.DialCircleInfo_Location;
import com.diallock.diallock.diallock.Activity.ParkSDK.Util.Circle;
import com.diallock.diallock.diallock.Activity.ParkSDK.Util.Conversion;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.Collections;

public class CircleDial_slide extends Fragment {

    private VelocityTracker mVelocityTracker = null;

    static View mView;

    private com.diallock.diallock.diallock.Activity.Layout.DialLayout mArcLayout_slide;
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
    private Boolean mDrection;


    private final String LOG_NAME = "CircleDial_slide";

    public CircleDial_slide() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }

        CommonJava.Loging.i(LOG_NAME, "onCreate()");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        CommonJava.Loging.i(LOG_NAME, "onCreateView()");
        mView = inflater.inflate(R.layout.fragment_circle_dial_slide, container, false);

        setFindViewById();
        init();
        setEvent();
        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        CommonJava.Loging.i(LOG_NAME, "onAttach()");
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonJava.Loging.i(LOG_NAME, "onResume()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        mArcLayout_slide = (DialLayout) mView.findViewById(R.id.arcLayout_slide);
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


        mChildBtnInfo_Images = new ArrayList<>();

        for (int btn_index = 0; btn_index < 12; btn_index++) {

            DialCircleInfo_Image dialCircleInfo_image = new DialCircleInfo_Image();
            if (btn_index < 10) {
                dialCircleInfo_image.setDialCircleImage(btn_images.get(btn_index));
                dialCircleInfo_image.setDialCircleImage_click(btn_images_click.get(btn_index));
                dialCircleInfo_image.setDialCircleValue(String.valueOf(btn_index));
            } else if (btn_index == 10) {
                Integer btn_index_10 = randomIndex(null);
                dialCircleInfo_image.setDialCircleImage(btn_images.get(btn_index_10));
                dialCircleInfo_image.setDialCircleImage_click(btn_images_click.get(btn_index_10));
                dialCircleInfo_image.setDialCircleValue(String.valueOf(btn_index_10));
            } else if (btn_index == 11) {
                Integer btn_index_11 = randomIndex(Integer.valueOf(mChildBtnInfo_Images.get(10).getDialCircleValue()));
                dialCircleInfo_image.setDialCircleImage(btn_images.get(btn_index_11));
                dialCircleInfo_image.setDialCircleImage_click(btn_images_click.get(btn_index_11));
                dialCircleInfo_image.setDialCircleValue(String.valueOf(btn_index_11));
            }

            mChildBtnInfo_Images.add(dialCircleInfo_image);
            //CommonJava.Loging.i(LOG_NAME, "dialCircleInfo_image " + btn_index + " value: " + dialCircleInfo_image.getDialCircleValue());
        }

        setBtnImage();

        mBigDialImage_Location = new DialCircleInfo_Location();
        mSmallDialImage_Location = new DialCircleInfo_Location();

        mRegisterIndex = new ArrayList<>();
        mRegisterPressIndex = new ArrayList<>();

        mDialTouch = false;

        mDrection = null;


    }


    private void setBtnImage() {

        mChildBtnInfo_Images = setShuffle(mChildBtnInfo_Images);

        for (int btn_Index = 0; btn_Index < mChildBtnInfo_Images.size(); btn_Index++) {
            int btnId = mBtnIndexId.get(btn_Index);
            SimpleDraweeView childBtn = (SimpleDraweeView) mView.findViewById(btnId);
            int imageId = mChildBtnInfo_Images.get(btn_Index).getDialCircleImage();
            childBtn.getHierarchy().setPlaceholderImage(imageId);
            CommonJava.Loging.i(LOG_NAME, "dialCircleInfo_image index : " + btn_Index + " value: " + mChildBtnInfo_Images.get(btn_Index).getDialCircleValue());
        }

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
        mArcLayout_slide.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener); // 자식 화면의 크기를 구해옴

    }

    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            CommonJava.Loging.i(LOG_NAME, "mArcLayout_slide.getHeight() : " + mArcLayout_slide.getHeight());
            CommonJava.Loging.i(LOG_NAME, "mArcLayout_slide.getWidth() : " + mArcLayout_slide.getWidth());
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

            int dialRadiusValue = point.y / 4 * 3 - btn_index_11_y-diff_y;

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
                SimpleDraweeView childBtn = (SimpleDraweeView) mArcLayout_slide.getChildAt(btn_index);

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

            mArcLayout_slide.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    };


    /**
     * mVelocityTracker 를 사용해서 처음 위치로부터 어느 위치로 이동하는지 추적
     *
     * @param onTouchEvent
     */
    public void setOnTouchEvent(MotionEvent onTouchEvent) {
        CommonJava.Loging.i(LOG_NAME, "setOnTouchEvent(MotionEvent onTouchEvent) MotionEvent : " + onTouchEvent);
        dialTouchIndexCheckEvent(onTouchEvent);
    }

    /**
     * dial 안의 어느 버튼 에 터치됬는지 체크 하는 event
     *
     * @param onTouchEvent
     */
    private void dialTouchIndexCheckEvent(MotionEvent onTouchEvent) {
        float touch_x = onTouchEvent.getX();
        float touch_y = onTouchEvent.getY();

        float bigCenterX = mBigDialImage_Location.get_xPosition();
        float bigCenterY = mBigDialImage_Location.get_yPosition();
        int bigRadius = mBigDialImage_Location.getDialCircleRadius();
        Boolean bigDialInnerCheck = Circle._IsInnerCircleCheck(bigCenterX, bigCenterY, bigRadius, touch_x, touch_y);

        float smallCenterX = mSmallDialImage_Location.get_xPosition();
        float smallCenterY = mSmallDialImage_Location.get_yPosition();
        int smallRadius = mSmallDialImage_Location.getDialCircleRadius();
        Boolean smallDailInnerCheck = Circle._IsInnerCircleCheck(smallCenterX, smallCenterY, smallRadius, touch_x, touch_y);

        if (bigDialInnerCheck && !smallDailInnerCheck) { // 큰 Dial Circle 과 작은 Dail Circle 사이 에 있을 때
            Integer touchedIndex = Circle._TouchedIndex(mChildBtnInfo_Location, touch_x, touch_y);

            switch (onTouchEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    btnImageChange(touchedIndex);

                    if (mRegisterIndex.size() != 0) {
                        mRegisterIndex.clear();
                    }

                    mRegisterIndex.add(touchedIndex);
                    mRegisterPressIndex.add(touchedIndex);
                    CommonJava.Loging.i(LOG_NAME, "setOnTouchEvent(MotionEvent onTouchEvent) MotionEvent.ACTION_DOWN mRegisterIndex.add(touchedIndex) touchedIndex : " + touchedIndex);
                    mDialTouch = true;
                    break;
                case MotionEvent.ACTION_MOVE:

                    if (mRegisterIndex.size() != 0 && mRegisterIndex.get(mRegisterIndex.size() - 1) != touchedIndex) {
                        mRegisterIndex.add(touchedIndex);
                        CommonJava.Loging.i(LOG_NAME, "setOnTouchEvent(MotionEvent onTouchEvent) MotionEvent.ACTION_MOVE mRegisterIndex.add(touchedIndex) touchedIndex : " + touchedIndex);

                        if (mDrection == null) {
                            mDrection = checkDirection();
                            setDialCircleBg(mDrection);
                        } else if (mDrection != checkDirection()) {
                            mDrection = checkDirection();
                            setDialCircleBg(mDrection);
                            int preIndex = mRegisterIndex.get(mRegisterIndex.size() - 2);
                            mRegisterPressIndex.add(preIndex);
                            btnImageChange(preIndex);
                        }

                    }

                    break;
                case MotionEvent.ACTION_UP:

                    btnImageChange(touchedIndex);

                    mRegisterPressIndex.add(touchedIndex);
                    //TODO: 비밀번호 값 체크

                    initDialTouch();
                    break;
            }

        } else {
            if (mDialTouch) {
                initDialTouch();
                errorBtnImage(4);
            }
        }
    }

    /**
     * 현재 진행 방향에 따른 dial circle 의 bg 설정
     *
     * @param direction 현재 진행 방향
     */
    private void setDialCircleBg(Boolean direction) {
        if (direction == null) {
            mArcLayout_slide.setBackgroundResource(R.drawable.dial_image_slide);
        } else if (direction) {
            mArcLayout_slide.setBackgroundResource(R.drawable.dial_image_slide_right);
        } else if (!direction) {
            mArcLayout_slide.setBackgroundResource(R.drawable.dial_image_slide_left);
        }
    }

    /**
     * 현재 touch 되는 dial 의 모든 값을 초기화
     */
    private void initDialTouch() {
        setDialCircleBg(null);
        mDialTouch = false;
        mDrection = null;
        mRegisterIndex.clear();
    }

    /**
     * 현재 touch 되는 방향이 정방향인지, 역방향인지 체크하는 함수
     *
     * @return
     */
    private Boolean checkDirection() {
        Boolean resultBl = null;

        Integer preIndex = mRegisterIndex.get(mRegisterIndex.size() - 2);
        Integer currentIndex = mRegisterIndex.get(mRegisterIndex.size() - 1);
        if (preIndex + 1 == currentIndex) {
            resultBl = true;
        } else if (preIndex == 11 && currentIndex == 0) {
            resultBl = true;
        } else if (preIndex - 1 == currentIndex) {
            resultBl = false;
        } else if (preIndex == 0 && currentIndex == 11) {
            resultBl = false;
        }

        return resultBl;
    }

    /**
     * touch된 인덱스 값의 button 의 이미지를 change
     *
     * @param btnIndex touch 된 btn 의 index 값
     */
    private void btnImageChange(Integer btnIndex) {
        Integer changeImage = mChildBtnInfo_Images.get(btnIndex).getDialCircleImage_click();
        SimpleDraweeView changeBtn = (SimpleDraweeView) mArcLayout_slide.getChildAt(btnIndex);
        changeBtn.getHierarchy().setPlaceholderImage(changeImage);

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
                SimpleDraweeView changeBtn = (SimpleDraweeView) mArcLayout_slide.getChildAt(changedIndex);
                changeBtn.getHierarchy().setPlaceholderImage(changeImage);
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
                        mArcLayout_slide.setBackgroundResource(R.drawable.dial_image_slide_error);
                        isVibrator();
                        break;
                    case 3:
                        mArcLayout_slide.setBackgroundResource(R.drawable.dial_image_slide);
                        break;
                    case 2:
                        mArcLayout_slide.setBackgroundResource(R.drawable.dial_image_slide_error);
                        isVibrator();
                        break;
                    case 1:
                        mArcLayout_slide.setBackgroundResource(R.drawable.dial_image_slide);
                        break;
                }
                errorBtnImage(--msg.what);
            }
        }.sendEmptyMessageDelayed(repeatNumber, 200);
    }

    /**
     * 진동 함수
     */
    private void isVibrator() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    private void trackerEvent(MotionEvent motionEvent) {
        int index = motionEvent.getActionIndex();
        int action = motionEvent.getActionMasked();
        int pointerId = motionEvent.getPointerId(index);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }
                // Add a user's movement to the tracker.
                mVelocityTracker.addMovement(motionEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(motionEvent);
                // When you want to determine the velocity, call
                // computeCurrentVelocity(). Then call getXVelocity()
                // and getYVelocity() to retrieve the velocity for each pointer ID.
                mVelocityTracker.computeCurrentVelocity(1000);
                // Log velocity of pixels per second
                // Best practice to use VelocityTrackerCompat where possible.
                CommonJava.Loging.d(LOG_NAME, "X velocity: " +
                        VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                                pointerId));
                CommonJava.Loging.d(LOG_NAME, "Y velocity: " +
                        VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                                pointerId));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.clear();
                break;
        }
    }

}

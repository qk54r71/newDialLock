package com.diallock.diallock.diallock.Activity.Layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Activity.LockScreenActivity;
import com.diallock.diallock.diallock.Activity.Activity.LockScreenViewActivity;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.LockScreenManager;
import com.diallock.diallock.diallock.Activity.Fragment.LockScreenFragment;
import com.diallock.diallock.diallock.Activity.taskAction.ScreenService;
import com.diallock.diallock.diallock.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.Lock;

/**
 * Created by park on 2016-08-09.
 */
public class CircleLayout extends View {

    private static Context mContext;

    private final static int TOTAL_DEGREE = 360;
    private final static int START_DEGREE = -90;

    private Paint mPaint;
    private RectF mOvalRect = null;

    private int mItemCount = 12;
    private int mSweepAngle;

    private int mInnerRadius;
    private int mOuterRadius;

    private ArrayList<Bitmap> mIcon = new ArrayList<Bitmap>();
    private ArrayList<Bitmap> mIconClick = new ArrayList<Bitmap>();

    private int[] mArcIconIdRandom = {
            R.drawable.num_0, R.drawable.num_1, R.drawable.num_2,
            R.drawable.num_3, R.drawable.num_4, R.drawable.num_5,
            R.drawable.num_6, R.drawable.num_7, R.drawable.num_8,
            R.drawable.num_9};
    private int[] mArcIconClickIdRandom = {
            R.drawable.num_0_click, R.drawable.num_1_click, R.drawable.num_2_click,
            R.drawable.num_3_click, R.drawable.num_4_click, R.drawable.num_5_click,
            R.drawable.num_6_click, R.drawable.num_7_click, R.drawable.num_8_click,
            R.drawable.num_9_click};
    private int mBgColors = Color.rgb(55, 96, 146);
    private ArrayList<Bitmap> mResizeIcon = new ArrayList<Bitmap>();
    private ArrayList<Bitmap> mResizeIconClick = new ArrayList<Bitmap>();

    private ArrayList<BitmapImage> mBitmapImages = new ArrayList<BitmapImage>();

    BitmapImage mBigArcLocation = new BitmapImage();
    BitmapImage mSmallArcLocation = new BitmapImage();

    /**
     * 다이얼락의 눌린 정보를 기록 하는 클래스
     */
    public static DialImageInfo mDialImageInfo = new DialImageInfo();

    /**
     * 비어있다는 뜻의 상수
     */
    private final int NUM_NULL = 99;

    /**
     * 다이얼 락 잠금 해제 스타트를 구분하기 위한 변수
     */
    private Boolean mStartDial;

    /**
     * 현재 드래그 하고 있는 숫자의 인덱스
     */
    private int mDragIndex = NUM_NULL;

    private Bitmap mCenterBitmapImg;

    /**
     * 현재 입력되는 패스워드를 기억하는 변수
     */
    public static String mPassword;

    /**
     * 비밀번호 입력 틀렸을 시 사용되는 변수
     */
    private Handler handlerError;
    private Boolean errorDrowBl;
    private LockScreenManager mLockScreenManager;
    private final static String LOG_NAME = "CircleLayout";

    private static CircleLayout psCircleLayout;


    public CircleLayout(Context context) {
        this(context, null);
    }

    public CircleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        CommonJava.Loging.i(LOG_NAME, "Context : " + context);
        CommonJava.Loging.i(LOG_NAME, "AttributeSet : " + attrs);
        CommonJava.Loging.i(LOG_NAME, "defStyleAttr : " + defStyleAttr);

        mContext = context;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSweepAngle = TOTAL_DEGREE / mItemCount;

        init();

    }

    private void init() {

        bitmapImageListShuffle();

        String centerStrImgUrl = CommonJava.loadSharedPreferences(mContext, "imgUrl");

        if (centerStrImgUrl != null && !centerStrImgUrl.isEmpty()) {
            Uri centerImgUrl = Uri.parse(centerStrImgUrl);
            CommonJava.Loging.i(LOG_NAME, "centerStrImgUrl : " + centerStrImgUrl);
            try {
                mCenterBitmapImg = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), centerImgUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            CommonJava.Loging.e(LOG_NAME, "centerStrImgUrl Null ");
            //TODO: 기본 이미지 로고 넣어야 함
            mCenterBitmapImg = BitmapFactory.decodeResource(getResources(), R.drawable.dialcenter);
        }

        mPassword = new String();
        errorDrowBl = false;


        //((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        psCircleLayout = this;
    }

    /**
     * 비트맵 이미지 리사이징 하기
     * 비트맵 이미지 원으로 만들기
     *
     * @param bitmap
     * @return
     */
    private Bitmap getCircleBitmap(Bitmap bitmap, int innerRadius) {
        Bitmap reSize = Bitmap.createScaledBitmap(bitmap, 2 * innerRadius, 2 * innerRadius, true);
        Bitmap output = Bitmap.createBitmap(reSize.getWidth(), reSize.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, reSize.getWidth(), reSize.getHeight());

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        int size = (reSize.getWidth() / 2);

        canvas.drawCircle(size, size, size, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(reSize, rect, rect, paint);

        return output;

    }


    /**
     * 이미지 리사이즈
     *
     * @param bitmaps : 기존 이미지 리스트
     * @return
     */

    private ArrayList<Bitmap> resizeBitmap(ArrayList<Bitmap> bitmaps) {

        ArrayList<Bitmap> resizeBitmap = new ArrayList<Bitmap>();

        CommonJava.Loging.i(LOG_NAME, "resizeBitmap 원래 아이콘 가로 getWidth : " + bitmaps.get(0).getWidth());
        CommonJava.Loging.i(LOG_NAME, "resizeBitmap 원래 아이콘 세로 getHeight : " + bitmaps.get(0).getHeight());

        WindowManager windowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        Integer parentLinearWidth = point.x;
        Integer parentLinearHeight = point.y;

        int innerRadius = parentLinearHeight / 2 - (parentLinearWidth * 270 / 1440);
        int outerRadius = parentLinearHeight / 2 - (parentLinearWidth * 30 / 1440);

        CommonJava.Loging.i(LOG_NAME, "resizeBitmap innerRadius : " + innerRadius);
        CommonJava.Loging.i(LOG_NAME, "resizeBitmap outerRadius : " + outerRadius);

        int bitmapSize = 48 * parentLinearWidth / 1440;

        CommonJava.Loging.i(LOG_NAME, "resizeBitmap bitmapSize : " + bitmapSize);

        for (Bitmap bitmap : bitmaps) {

            int bitmapWidth = (int) (bitmap.getWidth() * 0.75);
            int bitmapHeight = (int) (bitmap.getHeight() * 0.75);

            resizeBitmap.add(Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, true));
        }

        return resizeBitmap;
    }

    /**
     * 화면에 다이얼 모양을 그려줌
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();

        WindowManager windowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        Integer parentLinearWidth = point.x;
        Integer parentLinearHeight = point.y;

        CommonJava.Loging.i(LOG_NAME, "Display parentLinearWidth : " + parentLinearWidth);
        CommonJava.Loging.i(LOG_NAME, "Display parentLinearHeight : " + parentLinearHeight);

        if (mInnerRadius == 0) {
            mInnerRadius = height / 2 - (width * 270 / 1440);
        }
        if (mOuterRadius == 0) {
            mOuterRadius = height / 2 - (width * 30 / 1440);
        }

        CommonJava.Loging.i(LOG_NAME, "현재 화면 가로 getWidth : " + getWidth());
        CommonJava.Loging.i(LOG_NAME, "현재 화면 세로 getHeight: " + getHeight());
        CommonJava.Loging.i(LOG_NAME, "Display mInnerRadius : " + mInnerRadius);
        CommonJava.Loging.i(LOG_NAME, "Display mOuterRadius : " + mOuterRadius);


        if (mOvalRect == null) {
            mOvalRect = new RectF(width / 2 - mOuterRadius, height / 2 - mOuterRadius, width / 2 + mOuterRadius, height / 2 + mOuterRadius);
        }

        mPaint.setColor(mBgColors);
        mPaint.setStyle(Paint.Style.FILL);

        Path clip = new Path();

        clip.addCircle(width / 2, height / 2, mOuterRadius, Path.Direction.CCW);
        clip.addCircle(width / 2, height / 2, mInnerRadius, Path.Direction.CW);

        canvas.clipPath(clip);

        canvas.drawArc(mOvalRect, 0, 360, true, mPaint);

        for (int i = 0; i < mItemCount; i++) {
            int startAngle = START_DEGREE + i * mSweepAngle;

            int centerX = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(startAngle + mSweepAngle / 2)));
            int centerY = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(startAngle + mSweepAngle / 2)));

            /**
             * 현재 눌려진 위치 찾아서 클릭 이미지로 변경
             */
            Bitmap bitmapClickImage = null;
            /*if (mDialImageInfo.getCurrentClickBitmapImageIndex() == i) {
                bitmapClickImage = mBitmapImages.get(i).getNumClick();
            } else if (mDialImageInfo.getPreClickBitmapImageIndex() == i) {
                bitmapClickImage = mBitmapImages.get(i).getNumClick();
            } else if (mDialImageInfo.getPrePreClickBitmapImageIndex() == i) {
                bitmapClickImage = mBitmapImages.get(i).getNumClick();
            } else {
                bitmapClickImage = mBitmapImages.get(i).getNum();
            }*/
            CommonJava.Loging.i(LOG_NAME, "bitmapClickImage i :" + i);
            if (mDialImageInfo.getPressBitmapImageIndex() != null && mDialImageInfo.getPressBitmapImageIndex().size() != 0 /*&& LockScreenFragment.mSwitchValue != 0*/) {
                CommonJava.Loging.i(LOG_NAME, "bitmapClickImage  mDialImageInfo.getPressBitmapImageIndex() :" + mDialImageInfo.getPressBitmapImageIndex());
                for (Integer bitmapIndex : mDialImageInfo.getPressBitmapImageIndex()) {
                    if (bitmapIndex == i) {
                        CommonJava.Loging.i(LOG_NAME, "bitmapClickImage  mBitmapImages.get(i).getNumClick()");
                        bitmapClickImage = mBitmapImages.get(i).getNumClick();
                        break;
                    } else {
                        CommonJava.Loging.i(LOG_NAME, "bitmapClickImage  mBitmapImages.get(i).getNum()");
                        bitmapClickImage = mBitmapImages.get(i).getNum();
                    }
                }
            } else {
                CommonJava.Loging.i(LOG_NAME, "bitmapClickImage  mBitmapImages.get(i).getNum()");
                bitmapClickImage = mBitmapImages.get(i).getNum();
            }
            //bitmapClickImage = mBitmapImages.get(i).getNum();
            CommonJava.Loging.i(LOG_NAME, "bitmapClickImage :" + bitmapClickImage);
            CommonJava.Loging.i(LOG_NAME, "bitmapClickImage width:" + (width / 2 + centerX - bitmapClickImage.getWidth() / 2));
            CommonJava.Loging.i(LOG_NAME, "bitmapClickImage height:" + (height / 2 + centerY - bitmapClickImage.getHeight() / 2));
            canvas.drawBitmap(bitmapClickImage, width / 2 + centerX - bitmapClickImage.getWidth() / 2, height / 2 + centerY - bitmapClickImage.getHeight() / 2, null);

            float bitmapImgX = width / 2 + centerX;
            //float bitmapImgY = height / 2 + centerY + (parentLinearHeight /2);
            float bitmapImgY = height / 2 + centerY + (int) (parentLinearHeight / 2);

            CommonJava.Loging.i(LOG_NAME, "bitmapClickImage bitmapImgX:" + bitmapImgX);
            CommonJava.Loging.i(LOG_NAME, "bitmapClickImage centerX:" + centerX);
            CommonJava.Loging.i(LOG_NAME, "bitmapClickImage bitmapImgY:" + bitmapImgY);
            CommonJava.Loging.i(LOG_NAME, "bitmapClickImage centerY+ parentLinearHeight / 2:" + centerY + (parentLinearHeight / 3));

            int bitmapRadius = bitmapClickImage.getWidth() / 2;


            mBitmapImages.get(i).setxLocation(bitmapImgX);
            mBitmapImages.get(i).setyLocation(bitmapImgY);
            /**
             * 터치되는 버튼 영역 크기 조절
             */
            mBitmapImages.get(i).setImgRadius((int) (bitmapRadius * 1.25 * width / 1440));
            CommonJava.Loging.i(LOG_NAME, "(int) (bitmapRadius * 1.25 * width / 1440) :" + (int) (bitmapRadius * 1.25 * width / 1440));
        }

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        //canvas.drawCircle(width / 2, height / 2, mInnerRadius, mPaint);

        /**
         * 가운에 화면에 이미지 넣기
         */
        String centerStrImgUrl = CommonJava.loadSharedPreferences(mContext, "imgUrl");
        Bitmap centerImg = null;
        if (centerStrImgUrl != null && !centerStrImgUrl.isEmpty()) {

            centerImg = getCircleBitmap(mCenterBitmapImg, mInnerRadius);
        } else {
            centerImg = getCircleBitmap(mCenterBitmapImg, mInnerRadius / 2);

        }
        //canvas.drawBitmap(centerImg, width / 2 - centerImg.getWidth() / 2, height / 2 - centerImg.getHeight() / 2, null);

        float bitmapImgX = width / 2;
        float bitmapImgY = height / 2 + (int) (parentLinearHeight / 2);
        int bitmapRadius = (int) (mOuterRadius * 1.2);

        mBigArcLocation.setxLocation(bitmapImgX);
        mBigArcLocation.setyLocation(bitmapImgY);
        mBigArcLocation.setImgRadius(bitmapRadius);

        bitmapRadius = mInnerRadius;

        mSmallArcLocation.setxLocation(bitmapImgX);
        mSmallArcLocation.setyLocation(bitmapImgY);
        mSmallArcLocation.setImgRadius(bitmapRadius);

        super.onDraw(canvas);
    }

    /**
     * 최초 버튼 눌린 좌표값
     *
     * @param xLocation : x 좌표
     * @param yLocation : y 좌표
     */
    public void screenTouchLocationStart(float xLocation, float yLocation) {

        if (mDialImageInfo == null) {
            mDialImageInfo = new DialImageInfo();
        }

        switch (LockScreenFragment.mSwitchValue) {
            case 0:
                Boolean isDialInner = isDialInner(xLocation, yLocation);
                mStartDial = false;
                mDialImageInfo.initDialImageInfo();

                if (isDialInner) {

                    //int isImageInnerIndex = isImageInner(xLocation, yLocation);
                    int isImageInnerIndex = isDistance(xLocation, yLocation);

                    if (isImageInnerIndex != NUM_NULL) {

                        CommonJava.Loging.i(LOG_NAME, "screenTouchLocationStart Event start isImageInnerIndex : " + isImageInnerIndex);
                        mStartDial = true;
                        //mDialImageInfo.setCurrentClickBitmapImageIndex(isImageInnerIndex);
                        mDialImageInfo.setPressBitmapImageIndex(isImageInnerIndex);
                        mDialImageInfo.setPressBitmapImageIndexNumber(isImageInnerIndex);

                        mPassword = isImageInnerValue(xLocation, yLocation);
                        CommonJava.Loging.i(LOG_NAME, "screenTouchLocationStart isImageInnerValue : " + mPassword);
                        if (LockScreenFragment.smSwitchRandom) {
                            bitmapImageListShuffle();
                        }
                        CommonJava.Loging.i(LOG_NAME, "LockScreenFragment.smSwitchRandom : " + LockScreenFragment.smSwitchRandom);
                        invalidate();
                        isVibrator();
                        comebackBitmapImage(isImageInnerIndex);

                    }
                }

                break;
            case 1:
                Boolean isDialInnerZIG = isDialInner(xLocation, yLocation);
                mStartDial = false;
                mDialImageInfo.initDialImageInfo();

                if (isDialInnerZIG) {

                    int isImageInnerIndex = isImageInner(xLocation, yLocation);
                    //int isImageInnerIndex = isDistance(xLocation, yLocation);

                    if (isImageInnerIndex != NUM_NULL) {

                        CommonJava.Loging.i(LOG_NAME, "screenTouchLocationStart Dial ZIG isImageInnerIndex : " + isImageInnerIndex);
                        mStartDial = true;
                        mDragIndex = isImageInnerIndex;
                        CommonJava.Loging.i(LOG_NAME, "screenTouchLocationStart mDragIndex : " + mDragIndex);
                        mDialImageInfo.setPressBitmapImageIndex(isImageInnerIndex);

                        mPassword = isImageInnerValue(xLocation, yLocation);
                        CommonJava.Loging.i(LOG_NAME, "screenTouchLocationStart  Dial ZIG isImageInnerValue : " + mPassword);

                        if (LockScreenFragment.smSwitchRandom) {
                            bitmapImageListShuffle();
                        }
                        CommonJava.Loging.i(LOG_NAME, "LockScreenFragment.smSwitchRandom : " + LockScreenFragment.smSwitchRandom);
                        invalidate();
                        isVibrator();
                        comebackBitmapImage(isImageInnerIndex);

                    }
                }

                break;
            case 2:

                int isImageInnerIndex = isImageInner(xLocation, yLocation);
                //int isImageInnerIndex = isDistance(xLocation, yLocation);

                if (isImageInnerIndex != NUM_NULL) {

                    CommonJava.Loging.i(LOG_NAME, "screenTouchLocationStart Event start isImageInnerIndex : " + isImageInnerIndex);

                    String btnValue = isImageInnerValue(xLocation, yLocation);

                    if (mPassword == null) {
                        mPassword = isImageInnerValue(xLocation, yLocation);
                    } else {
                        mPassword += isImageInnerValue(xLocation, yLocation);
                    }

                    if (mDialImageInfo.getPressBitmapImageIndex() == null) {
                        mDialImageInfo.initDialImageInfo();
                    }
                    mDialImageInfo.setPressBitmapImageIndex(isImageInnerIndex);

                    if (LockScreenFragment.smSwitchRandom) {
                        bitmapImageListShuffle();
                    }
                    CommonJava.Loging.i(LOG_NAME, "LockScreenFragment.smSwitchRandom : " + LockScreenFragment.smSwitchRandom);
                    invalidate();
                    isVibrator();
                    comebackBitmapImage(isImageInnerIndex);

                }

                break;
        }


    }

    /**
     * 진행중 좌표 눌린값
     *
     * @param xLocation : x 좌표
     * @param yLocation : y 좌표
     */
    public void screenTouchLocationDrag(float xLocation, float yLocation) {

        CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag xLocation : " + xLocation);
        CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag yLocation : " + yLocation);

        switch (LockScreenFragment.mSwitchValue) {
            case 0:
                if (mStartDial) { // 다이얼 안에서 터치됬는지 체크
                    Boolean isDialInner = isDialInner(xLocation, yLocation);

                    if (isDialInner) {
                        int isImageInnerIndex = isImageInner(xLocation, yLocation);
                        //int isImageInnerIndex = isDistance(xLocation, yLocation);

                        if (isImageInnerIndex != NUM_NULL && mDragIndex != isImageInnerIndex) {

                            CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag Event start isImageInnerIndex : " + isImageInnerIndex);

                            //mDialImageInfo.setCurrentClickBitmapImageIndex(isImageInnerIndex);
                            mDialImageInfo.setPressBitmapImageIndexNumber(isImageInnerIndex);

                            mStartDial = true;
                            mDragIndex = isImageInnerIndex;
                            //bitmapImageListShuffle();

                            Boolean vactorBl = dragIndexVactor();
                            CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag  dragIndexVactor : " + vactorBl);
                            if (!vactorBl) {

                                mDialImageInfo.setPressBitmapImageIndex(isImageInnerIndex);
                                invalidate();

                                int preIndex = mDialImageInfo.getPressBitmapImageIndexNumber().size() - 2;
                                CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag  preIndex : " + preIndex);

                                if (mPassword == null) {
                                    //mPassword = mBitmapImages.get(mDialImageInfo.getPreClickBitmapImageIndex()).getBitmapValue();
                                    mPassword = mBitmapImages.get(mDialImageInfo.getPressBitmapImageIndexNumber().get(preIndex)).getBitmapValue();
                                    CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag isImageInnerValue : " + mPassword);
                                } else {
                                    //mPassword += mBitmapImages.get(mDialImageInfo.getPreClickBitmapImageIndex()).getBitmapValue();
                                    mPassword += mBitmapImages.get(mDialImageInfo.getPressBitmapImageIndexNumber().get(preIndex)).getBitmapValue();
                                    CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag isImageInnerValue : " + mPassword);
                                }
                                //mDialImageInfo.initDialImageInfo();

                                if (LockScreenFragment.smSwitchRandom) {
                                    bitmapImageListShuffle();
                                }
                                CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag  LockScreenFragment.smSwitchRandom : " + LockScreenFragment.smSwitchRandom);
                                invalidate();
                                isVibrator();
                                comebackBitmapImage(isImageInnerIndex);
                            }


                        }
                    } else {
                        if (errorDrowBl == false) {
                            errorDrowBl = true;
                            errorDrow();
                        }
                    }
                }

                break;
            case 1:

                if (mStartDial) { // 다이얼 안에서 터치됬는지 체크
                    Boolean isBigDialInner = isInnerLocation(
                            mBigArcLocation.getxLocation(), mBigArcLocation.getyLocation(),
                            xLocation, yLocation,
                            mBigArcLocation.getImgRadius()
                    );

                    if (isBigDialInner) {

                        Boolean isSmallDialInner = isInnerLocation(
                                mSmallArcLocation.getxLocation(), mSmallArcLocation.getyLocation(),
                                xLocation, yLocation,
                                mSmallArcLocation.getImgRadius()
                        );

                        CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag  Dial ZIG isSmallDialInner : " + isSmallDialInner);

                        if (isSmallDialInner) {
                            mDragIndex = NUM_NULL;
                        }

                        int isImageInnerIndex = isImageInner(xLocation, yLocation);
                        //int isImageInnerIndex = isDistance(xLocation, yLocation);

                        if (isImageInnerIndex != NUM_NULL && mDragIndex != isImageInnerIndex) {

                            CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag  Dial ZIG isImageInnerIndex : " + isImageInnerIndex);

                            //mDialImageInfo.setCurrentClickBitmapImageIndex(isImageInnerIndex);
                            mDialImageInfo.setPressBitmapImageIndex(isImageInnerIndex);

                            mStartDial = true;
                            mDragIndex = isImageInnerIndex;
                            CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag mDragIndex : " + mDragIndex);
                            //bitmapImageListShuffle();
                            invalidate();

                            CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag Dial ZIG  mDragIndex : " + mDragIndex);
                            //mPassword = mBitmapImages.get(mDialImageInfo.getPreClickBitmapImageIndex()).getBitmapValue();
                            mPassword += mBitmapImages.get(mDragIndex).getBitmapValue();
                            CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag  Dial ZIG isImageInnerValue : " + mBitmapImages.get(mDragIndex).getBitmapValue());
                            CommonJava.Loging.i(LOG_NAME, "screenTouchLocationDrag  Dial ZIG mPassword : " + mPassword);

                            //mDialImageInfo.initDialImageInfo();

                            if (LockScreenFragment.smSwitchRandom) {
                                bitmapImageListShuffle();
                            }
                            CommonJava.Loging.i(LOG_NAME, "LockScreenFragment.smSwitchRandom : " + LockScreenFragment.smSwitchRandom);
                            invalidate();
                            isVibrator();
                            comebackBitmapImage(isImageInnerIndex);


                        }
                    }
                }

                break;
            case 2:
                break;
        }


    }

    /**
     * 마지막 눌린 좌표값
     *
     * @param xLocation : x 좌표
     * @param yLocation : y 좌표
     */
    public void screenTouchLocationEnd(float xLocation, float yLocation) {

        CommonJava.Loging.i(LOG_NAME, "xLocation : " + xLocation);
        CommonJava.Loging.i(LOG_NAME, "yLocation : " + yLocation);

        switch (LockScreenFragment.mSwitchValue) {
            case 0:
                if (mStartDial) {

                    mPassword += isImageInnerValue(xLocation, yLocation);
                    CommonJava.Loging.i(LOG_NAME, "screenTouchLocationEnd isImageInnerValue : " + mPassword);

                    mStartDial = false;
                    mDialImageInfo.initDialImageInfo();
                    if (LockScreenFragment.smSwitchRandom) {
                        bitmapImageListShuffle();
                    }
                    CommonJava.Loging.i(LOG_NAME, "LockScreenFragment.smSwitchRandom : " + LockScreenFragment.smSwitchRandom);
                    invalidate();
                    isVibrator();

                    String loadPassword = CommonJava.loadSharedPreferences(mContext, "password");

                    CommonJava.Loging.i(LOG_NAME, "screenTouchLocationEnd loadPassword : " + loadPassword);

                    if (isImaginaryCheck(mPassword)) {

                        CommonJava.Loging.i(LOG_NAME, "맞는 비밀번호 입니다.");
                        if (mContext instanceof LockScreenViewActivity) {
                            LockScreenManager.getInstance((Activity) mContext).startTxtToast("맞는 비밀번호 입니다.");
                        } else if (mContext instanceof LockScreenActivity) {
                            ((LockScreenActivity) mContext).isToast("맞는 비밀번호 입니다.");
                        }


                /*Intent intentSetting = new Intent(mContext, SettingActivity.class);
                mContext.startActivity(intentSetting);*/

                        String strSwitch = ((Activity) mContext).getIntent().getStringExtra("strSwitch");
                        if (strSwitch != null && strSwitch.equals("SettingActivity")) {

                            CommonJava.saveSharedPreferences(mContext, "lockCheck", "true");

                            CommonJava.Loging.i(LOG_NAME, "screenTouchLocationEnd ScreenService start");
                            Intent intentStartService = new Intent(mContext, ScreenService.class);
                            mContext.startService(intentStartService);

                            ((Activity) mContext).finish();
                        } else if (strSwitch != null && strSwitch.equals("ScreenReceiver")) {
                            ((LockScreenViewActivity) mContext).onUnlock();
                            ScreenService.mPhoneProgressLock = false;
                        }
                    } else {
                        if (errorDrowBl == false) {
                            errorDrowBl = true;
                            errorDrow();
                        }

                        if (mContext instanceof LockScreenViewActivity) {
                            LockScreenManager.getInstance((Activity) mContext).startTxtToast("잘못된 비밀번호 입니다.");
                        } else if (mContext instanceof LockScreenActivity) {
                            ((LockScreenActivity) mContext).isToast("잘못된 비밀번호 입니다.");
                        }

                        CommonJava.Loging.i(LOG_NAME, "잘못된 비밀번호 입니다.");
                    }

                    mPassword = null;
                }
                break;
            case 1:

                if (mStartDial) {

                    //mPassword += isImageInnerValue(xLocation, yLocation);
                    CommonJava.Loging.i(LOG_NAME, "screenTouchLocationEnd  Dial ZIG isImageInnerValue : " + mPassword);

                    mStartDial = false;
                    mDialImageInfo.initDialImageInfo();

                    if (LockScreenFragment.smSwitchRandom) {
                        bitmapImageListShuffle();
                    }
                    CommonJava.Loging.i(LOG_NAME, "LockScreenFragment.smSwitchRandom : " + LockScreenFragment.smSwitchRandom);
                    invalidate();
                    isVibrator();

                    String loadPassword = CommonJava.loadSharedPreferences(mContext, "password");

                    CommonJava.Loging.i(LOG_NAME, "screenTouchLocationEnd  Dial ZIG loadPassword : " + loadPassword);

                    if (isImaginaryCheck(mPassword)) {

                        CommonJava.Loging.i(LOG_NAME, " Dial ZIG 맞는 비밀번호 입니다.");

                /*Intent intentSetting = new Intent(mContext, SettingActivity.class);
                mContext.startActivity(intentSetting);*/

                        String strSwitch = ((Activity) mContext).getIntent().getStringExtra("strSwitch");
                        if (strSwitch != null && strSwitch.equals("SettingActivity")) {

                            CommonJava.saveSharedPreferences(mContext, "lockCheck", "true");

                            CommonJava.Loging.i(LOG_NAME, "screenTouchLocationEnd  Dial ZIG ScreenService start");
                            Intent intentStartService = new Intent(mContext, ScreenService.class);
                            mContext.startService(intentStartService);

                            if (mContext instanceof LockScreenViewActivity) {
                                LockScreenManager.getInstance((Activity) mContext).startTxtToast("맞는 비밀번호 입니다.");
                            } else if (mContext instanceof LockScreenActivity) {
                                ((LockScreenActivity) mContext).isToast("맞는 비밀번호 입니다.");
                            }

                            ((Activity) mContext).finish();
                        } else if (strSwitch != null && strSwitch.equals("ScreenReceiver")) {
                            ((LockScreenViewActivity) mContext).onUnlock();
                            ScreenService.mPhoneProgressLock = false;
                        }
                    } else {
                        if (errorDrowBl == false) {
                            errorDrowBl = true;
                            errorDrow();
                        }
                        CommonJava.Loging.i(LOG_NAME, " Dial ZIG " +
                                "틀린 비밀번호 입니다.");

                        if (mContext instanceof LockScreenViewActivity) {
                            LockScreenManager.getInstance((Activity) mContext).startTxtToast("잘못된 비밀번호 입니다.");
                        } else if (mContext instanceof LockScreenActivity) {
                            ((LockScreenActivity) mContext).isToast("잘못된 비밀번호 입니다.");
                        }

                    }

                    mPassword = null;
                }

                break;
            case 2:
                break;
        }

    }

    /**
     * 현재 화면에 적용될 이미지 정보를 갖는 클래스
     */
    private class BitmapImage {
        private Bitmap num;
        private Bitmap numClick;
        private String bitmapValue;
        private float xLocation;
        private float yLocation;
        private int imgRadius;
        private String bitmapID;

        public Bitmap getNum() {
            return num;
        }

        public void setNum(Bitmap num) {
            this.num = num;
        }

        public Bitmap getNumClick() {
            return numClick;
        }

        public void setNumClick(Bitmap numClick) {
            this.numClick = numClick;
        }

        public String getBitmapID() {
            return bitmapID;
        }

        public void setBitmapID(String bitmapID) {
            this.bitmapID = bitmapID;
        }

        public String getBitmapValue() {
            return bitmapValue;
        }

        public void setBitmapValue(String bitmapValue) {
            this.bitmapValue = bitmapValue;
        }

        public float getyLocation() {
            return yLocation;
        }

        public void setyLocation(float yLocation) {
            this.yLocation = yLocation;
        }

        public float getxLocation() {
            return xLocation;
        }

        public void setxLocation(float xLocation) {
            this.xLocation = xLocation;
        }

        public int getImgRadius() {
            return imgRadius;
        }

        public void setImgRadius(int imgRadius) {
            this.imgRadius = imgRadius;
        }
    }

    /**
     * 현재 눌려진 이미지의 정보를 갖음
     * 총 3개의 눌려진 정보를 갖음 (변경)
     * 2016-09-19 수정
     * 총 12개 까지의 눌려진 정보를 갖음
     */
    private static class DialImageInfo {/*
        private int currentClickBitmapImageIndex = NUM_NULL;
        private int preClickBitmapImageIndex = NUM_NULL;
        private int prePreClickBitmapImageIndex = NUM_NULL;*/
        private static ArrayList<Integer> pressBitmapImageIndex = null;
        /**
         * 진행 중이던 방향이 맞는지 체크하기 위한 리스트
         */
        private static ArrayList<Integer> pressBitmapImageIndexNumber = null;

        public static void initDialImageInfo() {/*
            this.currentClickBitmapImageIndex = NUM_NULL;
            this.preClickBitmapImageIndex = NUM_NULL;
            this.prePreClickBitmapImageIndex = NUM_NULL;*/
            pressBitmapImageIndex = new ArrayList<>();
            pressBitmapImageIndexNumber = new ArrayList<>();
        }

       /* public int getCurrentClickBitmapImageIndex() {

            return currentClickBitmapImageIndex;
        }

        public void setCurrentClickBitmapImageIndex(int currentClickBitmapImageIndex) {

            if (this.currentClickBitmapImageIndex != NUM_NULL) {

                if (this.preClickBitmapImageIndex != NUM_NULL) {
                    this.prePreClickBitmapImageIndex = this.preClickBitmapImageIndex;
                }

                this.preClickBitmapImageIndex = this.currentClickBitmapImageIndex;
            }

            this.currentClickBitmapImageIndex = currentClickBitmapImageIndex;

        }

        public int getPreClickBitmapImageIndex() {
            return preClickBitmapImageIndex;
        }

        public void setPreClickBitmapImageIndex(int preClickBitmapImageIndex) {
            this.preClickBitmapImageIndex = preClickBitmapImageIndex;
        }

        public int getPrePreClickBitmapImageIndex() {
            return prePreClickBitmapImageIndex;
        }

        public void setPrePreClickBitmapImageIndex(int prePreClickBitmapImageIndex) {
            this.prePreClickBitmapImageIndex = prePreClickBitmapImageIndex;
        }*/

        public ArrayList<Integer> getPressBitmapImageIndex() {
            return pressBitmapImageIndex;
        }

        public ArrayList<Integer> getPressBitmapImageIndexNumber() {
            return pressBitmapImageIndexNumber;
        }

        public void setPressBitmapImageIndex(Integer pressBitmapImageIndexNumber) {
            this.pressBitmapImageIndex.add(pressBitmapImageIndexNumber);
        }

        public void setPressBitmapImageIndexNumber(Integer pressBitmapImageIndexNumber) {
            this.pressBitmapImageIndexNumber.add(pressBitmapImageIndexNumber);
        }

        public void removePressBitmapImageIndex(Integer pressBitmapImageIndexNumber) {
            pressBitmapImageIndex.remove(pressBitmapImageIndexNumber);
        }
    }


    /**
     * 현재 터치된 좌표값이 이미지(원)안에 눌린건지 판별하는 함수
     *
     * @param xLocation : 이미지의 중앙 x 값
     * @param yLocation : 이미지의 중앙 y 값
     * @param xTouch    : 터치된 x 값
     * @param yTouch    : 터치된 y 값
     * @param radius    : 이미지 원의 반지름
     * @return
     */
    private Boolean isInnerLocation(float xLocation, float yLocation, float xTouch, float yTouch, int radius) {

        if (Math.pow((xLocation - xTouch), 2) + Math.pow((yLocation - yTouch), 2) < Math.pow(radius, 2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 현재 터치된 화면 값이 어느 버튼과 가까운지 체크
     *
     * @param xTouch
     * @param yTouch
     * @return 현재 제일 가까운 버튼의 인덱스
     */
    private int isDistance(float xTouch, float yTouch) {

        int currentIndex = NUM_NULL;
        double currentDistance = 0;

        for (int btnIndex = 0; btnIndex < mBitmapImages.size(); btnIndex++) {

            float xLocation = mBitmapImages.get(btnIndex).getxLocation();
            float yLocation = mBitmapImages.get(btnIndex).getyLocation();

            double distance = Math.pow(Math.abs(xLocation - xTouch), 2) + Math.pow(Math.abs(yLocation - yTouch), 2);
            CommonJava.Loging.i(LOG_NAME, "isDistance distance : " + distance);
            CommonJava.Loging.i(LOG_NAME, "isDistance currentDistance : " + currentDistance);
            if (currentDistance == 0) {
                currentDistance = distance;
            } else if (distance < currentDistance) {
                currentDistance = distance;
                currentIndex = btnIndex;
            }
        }
        CommonJava.Loging.i(LOG_NAME, "isDistance currentIndex : " + currentIndex);
        return currentIndex;
    }

    /**
     * 다이얼 모양 안에 터치됬는지 확인
     *
     * @param xTouch : 터치된 x값
     * @param yTouch : 터치된 y값
     * @return
     */
    private Boolean isDialInner(float xTouch, float yTouch) {

        CommonJava.Loging.i(LOG_NAME, "isDialInner xTouch :" + xTouch);
        CommonJava.Loging.i(LOG_NAME, "isDialInner yTouch :" + yTouch);

        Boolean isBigDialInner = isInnerLocation(
                mBigArcLocation.getxLocation(), mBigArcLocation.getyLocation(),
                xTouch, yTouch,
                mBigArcLocation.getImgRadius()
        );

        CommonJava.Loging.i(LOG_NAME, "isDialInner isBigDialInner :" + isBigDialInner);

        if (isBigDialInner) {
            return !isInnerLocation(
                    mSmallArcLocation.getxLocation(), mSmallArcLocation.getyLocation(),
                    xTouch, yTouch,
                    mSmallArcLocation.getImgRadius()
            );
        } else {
            return false;
        }

    }

    /**
     * 이미지 버튼 안에 터치됬는지 확인
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private int isImageInner(float xTouch, float yTouch) {

        for (int i = 0; i < mBitmapImages.size(); i++) {
            Boolean isClick = isInnerLocation(
                    mBitmapImages.get(i).getxLocation(), mBitmapImages.get(i).getyLocation(),
                    xTouch, yTouch,
                    mBitmapImages.get(i).getImgRadius()
            );
            if (isClick) {
                return i;
            }
        }
        return NUM_NULL;
    }

    /**
     * 터치된 이미지의 값 리턴
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private String isImageInnerValue(float xTouch, float yTouch) {
        /*for (int i = 0; i < mBitmapImages.size(); i++) {
            Boolean isClick = isInnerLocation(
                    mBitmapImages.get(i).getxLocation(), mBitmapImages.get(i).getyLocation(),
                    xTouch, yTouch,
                    mBitmapImages.get(i).getImgRadius()
            );
            if (isClick) {
                return mBitmapImages.get(i).getBitmapValue();
            }
        }*/

        int btnIndex = isDistance(xTouch, yTouch);

        CommonJava.Loging.i(LOG_NAME, "btnIndex : " + btnIndex);
        CommonJava.Loging.i(LOG_NAME, "mBitmapImages.get(btnIndex).getBitmapValue() : " + mBitmapImages.get(btnIndex).getBitmapValue());

        return mBitmapImages.get(btnIndex).getBitmapValue();
    }


    /**
     * 최초 이미지 배열 설정
     */
    private void bitmapImageListInit() {
        mIcon.clear();
        mIconClick.clear();
        mBitmapImages.clear();

        int randomFirstIndex = (int) (Math.random() * 9);
        int randomSecondIndex = 0;

        do {
            randomSecondIndex = (int) (Math.random() * 9);
        } while (randomFirstIndex == randomSecondIndex);

        int randomImgFirst = mArcIconIdRandom[randomFirstIndex];
        int randomImgSecond = mArcIconIdRandom[randomSecondIndex];
        int randomImgClickFirst = mArcIconClickIdRandom[randomFirstIndex];
        int randomImgClickSecond = mArcIconClickIdRandom[randomSecondIndex];

        if (mIcon.size() == 0) {
            for (int arcIconId : mArcIconIdRandom) {
                mIcon.add(BitmapFactory.decodeResource(getResources(), arcIconId));
            }

            for (int arcIconId : mArcIconClickIdRandom) {
                mIconClick.add(BitmapFactory.decodeResource(getResources(), arcIconId));
            }

            mIcon.add(BitmapFactory.decodeResource(getResources(), randomImgFirst));
            mIcon.add(BitmapFactory.decodeResource(getResources(), randomImgSecond));
            mIconClick.add(BitmapFactory.decodeResource(getResources(), randomImgClickFirst));
            mIconClick.add(BitmapFactory.decodeResource(getResources(), randomImgClickSecond));

            mResizeIcon = resizeBitmap(mIcon);
            mResizeIconClick = resizeBitmap(mIconClick);

        }

        for (int i = 0; i < mResizeIcon.size(); i++) {

            BitmapImage bitmapImage = new BitmapImage();
            bitmapImage.setNum(mResizeIcon.get(i));
            bitmapImage.setNumClick(mResizeIconClick.get(i));
            bitmapImage.setBitmapID("" + i);

            if (i < 10) {
                bitmapImage.setBitmapValue("" + i);
            } else if (i == 10) {
                bitmapImage.setBitmapValue("" + randomFirstIndex);
            } else if (i == 11) {
                bitmapImage.setBitmapValue("" + randomSecondIndex);
            }

            mBitmapImages.add(bitmapImage);
        }
    }

    /**
     * 이미지 배열 섞기
     */
    private void bitmapImageListShuffle() {
        bitmapImageListInit();
        Collections.shuffle(mBitmapImages);

        for (BitmapImage bitmapImage : mBitmapImages) {
            CommonJava.Loging.i(LOG_NAME, "bitmapImageListInit : " + bitmapImage.getBitmapValue());
        }

    }

    /**
     * 터치 진행방향이 진행하던 방향이면
     * true
     * 반대방향이면
     * false
     *
     * @return
     */
    private Boolean dragIndexVactor() {

        int indexSize = mDialImageInfo.getPressBitmapImageIndexNumber().size() - 1;
        if (indexSize != -1) {
            int currentIndex = mDialImageInfo.getPressBitmapImageIndexNumber().get(indexSize);
            CommonJava.Loging.i(LOG_NAME, "dragIndexVactor() indexSize : " + indexSize);
            CommonJava.Loging.i(LOG_NAME, "dragIndexVactor() currentIndex : " + currentIndex);
            int preIndex = 99;
            int prePreIndex = 99;
            if (indexSize != 0) {
                preIndex = mDialImageInfo.getPressBitmapImageIndexNumber().get(indexSize - 1);
                if (indexSize != 1) {
                    prePreIndex = mDialImageInfo.getPressBitmapImageIndexNumber().get(indexSize - 2);
                }
            }

            if (preIndex == 99 || prePreIndex == 99) {
                return true;
            }

            Boolean vactorBl = (currentIndex == (preIndex + 1) % 12) && (preIndex == (prePreIndex + 1) % 12) || (prePreIndex == (preIndex + 1) % 12) && (preIndex == (currentIndex + 1) % 12);

            return vactorBl;
        } else {
            return true;
        }
    }

    /**
     * 비밀번호 입력시 틀렸을 경우 점멸
     */
    private void errorDrow() {

        final ArrayList<BitmapImage> bitmaps = mBitmapImages;
        final ArrayList<BitmapImage> bitmapsClick = mBitmapImages;
        final int[] msgSwitch = {0};

        handlerError = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                CommonJava.Loging.i(LOG_NAME, "handlerError msgSwitch[0] : " + msgSwitch[0]);
                switch (msgSwitch[0]) {
                    case 0:
                    case 2:
                        for (int i = 0; i < bitmaps.size(); i++) {
                            mBitmapImages.get(i).setNum(mResizeIconClick.get(Integer.parseInt(mBitmapImages.get(i).getBitmapValue())));
                            mBitmapImages.get(i).setNumClick(mResizeIconClick.get(Integer.parseInt(mBitmapImages.get(i).getBitmapValue())));
                        }
                        invalidate();
                        msgSwitch[0]++;
                        handlerError.sendEmptyMessageDelayed(0, 200);
                        isVibrator();
                        break;
                    case 1:

                        for (int i = 0; i < bitmaps.size(); i++) {
                            mBitmapImages.get(i).setNum(mResizeIcon.get(Integer.parseInt(mBitmapImages.get(i).getBitmapValue())));
                            mBitmapImages.get(i).setNumClick(mResizeIcon.get(Integer.parseInt(mBitmapImages.get(i).getBitmapValue())));
                        }
                        msgSwitch[0]++;
                        invalidate();
                        handlerError.sendEmptyMessageDelayed(0, 200);

                        if (mContext instanceof LockScreenViewActivity) {

                            mLockScreenManager = LockScreenManager.getInstance((Activity) mContext);
                            mLockScreenManager.startTxtToast("잘못된 비밀번호 입니다.");
                        } else if (mContext instanceof LockScreenActivity) {
                            ((LockScreenActivity) mContext).isToast("잘못된 비밀번호 입니다.");
                        }

                        break;
                    case 3:

                        for (int i = 0; i < bitmaps.size(); i++) {
                            mBitmapImages.get(i).setNum(mResizeIcon.get(Integer.parseInt(mBitmapImages.get(i).getBitmapValue())));
                            mBitmapImages.get(i).setNumClick(mResizeIconClick.get(Integer.parseInt(mBitmapImages.get(i).getBitmapValue())));
                        }
                        msgSwitch[0] = 0;
                        invalidate();

                        mStartDial = false;
                        mDragIndex = NUM_NULL;
                        mDialImageInfo.initDialImageInfo();
                        errorDrowBl = false;
                        break;
                }


            }
        };

        handlerError.sendEmptyMessageDelayed(0, 200);

    }

    /**
     * 허수 체크 함수
     *
     * @return
     */
    private static Boolean isImaginaryCheck(String strPassword) {

        String loadPassword = CommonJava.loadSharedPreferences(mContext, "password");
        String strPass = strPassword;
        int minimumPass = 2;
        int maximumPass = loadPassword.length() * 2;

        if (strPassword.length() >= minimumPass && strPassword.length() <= maximumPass) {

            if (strPassword.contains(loadPassword)) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    private void isVibrator() {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    /**
     * 외부 클래스에서 컨트롤
     */
    public static void isInitDial() {
        mPassword = null;
        mDialImageInfo.initDialImageInfo();
        psCircleLayout.invalidate();
    }

    public static void isPasswordCheck() {

        if (mPassword != null && isImaginaryCheck(mPassword)) {

                /*Intent intentSetting = new Intent(mContext, SettingActivity.class);
                mContext.startActivity(intentSetting);*/

            String strSwitch = ((Activity) mContext).getIntent().getStringExtra("strSwitch");
            if (strSwitch != null && strSwitch.equals("SettingActivity")) {
                Intent intentStartService = new Intent(mContext, ScreenService.class);
                mContext.startService(intentStartService);
                isInitDial();
                CommonJava.saveSharedPreferences(mContext, "lockCheck", "true");

                if (mContext instanceof LockScreenViewActivity) {
                    LockScreenManager.getInstance((Activity) mContext).startTxtToast("맞는 비밀번호 입니다.");
                } else if (mContext instanceof LockScreenActivity) {
                    ((LockScreenActivity) mContext).isToast("잘못된 비밀번호 입니다.");
                }

                ((Activity) mContext).finish();
            } else if (strSwitch != null && strSwitch.equals("ScreenReceiver")) {
                ((LockScreenViewActivity) mContext).onUnlock();
                ScreenService.mPhoneProgressLock = false;
            }
        } else {
            if (mContext instanceof LockScreenViewActivity) {
                LockScreenManager.getInstance((Activity) mContext).startTxtToast("잘못된 비밀번호 입니다.");
            } else if (mContext instanceof LockScreenActivity) {
                ((LockScreenActivity) mContext).isToast("잘못된 비밀번호 입니다.");
            }

            isInitDial();
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
        }

        mPassword = null;
    }

    private void comebackBitmapImage(final Integer pressBitmapImageIndex) {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mDialImageInfo.removePressBitmapImageIndex(pressBitmapImageIndex);
                invalidate();
            }
        }.sendEmptyMessageDelayed(0, 200);
    }

}
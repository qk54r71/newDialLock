package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.Activity.ParkSDK.Debug.Loging;
import com.diallock.diallock.diallock.R;

import java.io.IOException;

public class ImageFragment extends Fragment {

    private View mView;
    private LinearLayout image_layout;
    private ImageView sildeImage;
    private CircleLayout circle_screen;

    private Context mContext;

    private Bitmap mCenterBitmapImg;

    private static final String LOG_NAME = "ImageFramgnet";

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String param1, String param2) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        CommonJava.Loging.i(LOG_NAME, "onCreateView");

        mView = inflater.inflate(R.layout.fragment_image, container, false);

        findViewById();
        init();
        setEvent();
        return mView;
    }

    private void findViewById() {
        image_layout = (LinearLayout) mView.findViewById(R.id.fm_time_widget_image_container);
        sildeImage = (ImageView) mView.findViewById(R.id.slideImage);
        //circle_screen = (CircleLayout) mView.findViewById(R.id.circle_screen);
    }

    private void init() {
        mContext = getContext();

        String centerStrImgUrl = CommonJava.loadSharedPreferences(mContext, "imgUrl");
        int radius = 0;
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

        radius = mCenterBitmapImg.getHeight() >= mCenterBitmapImg.getWidth() ? mCenterBitmapImg.getHeight() / 2 : mCenterBitmapImg.getWidth() / 2;

        mCenterBitmapImg = getCircleBitmap(mCenterBitmapImg, radius);

        sildeImage.setImageBitmap(mCenterBitmapImg);
        mCenterBitmapImg = null;

        CommonJava.Loging.i(LOG_NAME, "circle_screen : " + circle_screen);
    }

    /**
     * 비트맵 이미지 리사이징 하기
     * 비트맵 이미지 원으로 만들기
     *
     * @param bitmap
     * @return
     */
    private Bitmap getCircleBitmap(Bitmap bitmap, int innerRadius) {
        Bitmap reSize = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.8), (int) (bitmap.getHeight() * 0.8), true);
        Bitmap output = Bitmap.createBitmap(reSize.getWidth(), reSize.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, reSize.getWidth(), reSize.getHeight());

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        int sizeX = (reSize.getWidth() / 2);
        int sizeY = (reSize.getHeight() / 2);

        canvas.drawCircle(sizeX, sizeY, innerRadius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(reSize, rect, rect, paint);
        reSize = null;

        return output;

    }

    private void setEvent() {
        image_layout.setOnClickListener(onClickListener);
        sildeImage.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Loging.i(LOG_NAME, "v.getId() : " + v.getId());
            if (LockScreenFragment.mSwitchValue == 2) {
                CircleDial_press.getInstance(LockScreenFragment.newInstance()).isCheckPassword();
            }
            switch (v.getId()) {
                case R.id.fm_time_widget_image_container:
                    Loging.i(LOG_NAME, "fm_time_widget_image_container onClick");
                    break;
                case R.id.sildeImageView:
                    Loging.i(LOG_NAME, "sildeImageView onClick");
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}

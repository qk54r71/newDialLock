package com.diallock.diallock.diallock.Activity.Activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Data.ChildBtnInfo;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private SimpleDraweeView btn_index_00;
    private ArrayList<ChildBtnInfo> mChildBtnInfos;

    private static final String LOG_NAME = "MainActivty";

    private com.diallock.diallock.diallock.Activity.Layout.DialLayout dialLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(MainActivity.this);
        setContentView(R.layout.activity_main);
        CommonJava.Loging.i(LOG_NAME, "onCreate");

        setFindViewById();
        init();
        setOnClick();
    }

    private void setFindViewById() {
        dialLayout = (com.diallock.diallock.diallock.Activity.Layout.DialLayout) findViewById(R.id.dialLayout);
        btn_index_00 = (SimpleDraweeView) findViewById(R.id.btn_index_00);
    }

    private void init() {

        Drawable drawable = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(R.drawable.dial_image_slide, null);
        } else {
            drawable = getResources().getDrawable(R.drawable.dial_image_slide);

        }


    }


    private void setOnClick() {
        btn_index_00.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CommonJava.Loging.i(LOG_NAME, "event : " + event);
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //CommonJava.Loging.i("LockScreenActivity", "onTouchEvent : " + event);

        float xLocation = event.getX(0);
        float yLocation = event.getY(0);

        CommonJava.Loging.i(LOG_NAME, "onTouchEvent : " + event);

        /*switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                circleLayout.screenTouchLocationStart(xLocation, yLocation);
                break;
            case MotionEvent.ACTION_MOVE:

                circleLayout.screenTouchLocationDrag(xLocation, yLocation);
                break;
            case MotionEvent.ACTION_UP:

                circleLayout.screenTouchLocationEnd(xLocation, yLocation);
                break;
        }*/

        return super.onTouchEvent(event);
    }

    public void setArrayList(ArrayList<ChildBtnInfo> childBtnInfos) {
        mChildBtnInfos = childBtnInfos;

        for (ChildBtnInfo childBtnInfo : childBtnInfos) {
            CommonJava.Loging.i(LOG_NAME, "getChildName : " + childBtnInfo.getChildName() + " getxPosition : " + childBtnInfo.getxPosition() + " getyPosition : " + childBtnInfo.getyPosition() + " childWidth : " + childBtnInfo.getChildWidth());
        }

        CommonJava.Loging.i(LOG_NAME, "dialLayout.getX() : " + dialLayout.getX() + " getY() : " + dialLayout.getY() + " Right : " + dialLayout.getRight() + " Left : " + dialLayout.getLeft() + " Top :" + dialLayout.getTop() + " Bottom : " + dialLayout.getBottom());

        //dialLayout.setTouch();

        /*dialLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CommonJava.Loging.i(LOG_NAME, "onTouch View : " + view);
                CommonJava.Loging.i(LOG_NAME, "onTouch MotionEvent : " + motionEvent);
                return false;
            }
        });*/

    }
}

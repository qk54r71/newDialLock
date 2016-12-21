package com.diallock.diallock.diallock.Activity.Activity;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Fragment.DialLayout;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends Activity {

    private SimpleDraweeView btn_index_00;

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
            drawable = getResources().getDrawable(R.drawable.dial_image, null);
        } else {
            drawable = getResources().getDrawable(R.drawable.dial_image);

        }


        //dialLayout.setBackgroundResource(drawable);
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
}

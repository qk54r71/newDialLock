package com.diallock.diallock.diallock.Activity.Layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.diallock.diallock.diallock.Activity.Activity.MainActivity;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Data.ChildBtnInfo;
import com.diallock.diallock.diallock.R;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by park on 2016-12-21.
 */

public class DialLayout extends ArcLayout {

    private ArrayList<ChildBtnInfo> childBtnInfos;
    private static final String LOG_NAME = "DialLayout";

    private Context mContext;
    private static DialLayout dialLayout;

    public DialLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mContext == null) {
            mContext = context;
            dialLayout = this;
        }

        CommonJava.Loging.i(LOG_NAME, "DialLayout construct");
        CommonJava.Loging.i(LOG_NAME, "DialLayout context : " + context);
        CommonJava.Loging.i(LOG_NAME, "DialLayout attrs : " + attrs.getAttributeCount());

        for (int attrIndex = 0; attrIndex < attrs.getAttributeCount(); attrIndex++) {
            CommonJava.Loging.i(LOG_NAME, "DialLayout attrs " + attrIndex + " : " + attrs.getAttributeValue(attrIndex));
            CommonJava.Loging.i(LOG_NAME, "DialLayout attrs " + attrIndex + " : " + attrs.getAttributeName(attrIndex));
        }


        CommonJava.Loging.i(LOG_NAME, "DialLayout attrs arc_radius : " + attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "arc_radius"));
        CommonJava.Loging.i(LOG_NAME, "DialLayout attrs arc_axisRadius : " + attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "arc_axisRadius"));

        init();

    }

    public static synchronized DialLayout getInstance() {
        CommonJava.Loging.i(LOG_NAME, "getInstance");
        return dialLayout;
    }


    public DialLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        CommonJava.Loging.i(LOG_NAME, "DialLayout construct");
        CommonJava.Loging.i(LOG_NAME, "DialLayout context : " + context);
        CommonJava.Loging.i(LOG_NAME, "DialLayout attrs : " + attrs.getAttributeCount());
        CommonJava.Loging.i(LOG_NAME, "DialLayout defStyleAttr : " + defStyleAttr);
        CommonJava.Loging.i(LOG_NAME, "DialLayout defStyleRes : " + defStyleRes);

    }



    @Override
    protected void childLayoutBy(View child, int childX, int childY) {


        CommonJava.Loging.i(LOG_NAME, "childLayoutBy child.getId() : " + child.getId());
        CommonJava.Loging.i(LOG_NAME, "childLayoutBy Right : " + child.getRight() + " Left : " + child.getLeft() + " Top :" + child.getTop() + " Bottom : " + child.getBottom());
        CommonJava.Loging.i(LOG_NAME, "childLayoutBy getResources().getResourceEntryName(child.getId()) : " + getResources().getResourceEntryName(child.getId()));
        CommonJava.Loging.i(LOG_NAME, "childLayoutBy x : " + childX);
        CommonJava.Loging.i(LOG_NAME, "childLayoutBy y : " + childY);

        if (child.getRight() != 0) {

            ChildBtnInfo childBtnInfo = new ChildBtnInfo();
            childBtnInfo.setxPosition(childX);
            childBtnInfo.setyPosition(childY);
            childBtnInfo.setChildName(getResources().getResourceEntryName(child.getId()));

            int childWidth = Math.abs(child.getRight() - child.getLeft());
            childBtnInfo.setChildWidth(childWidth);

            childBtnInfos.add(childBtnInfo);
        }

        if (childBtnInfos.size() == 12) {
            ((MainActivity) mContext).setArrayList(childBtnInfos);
        }

        super.childLayoutBy(child, childX, childY);
    }


    private void init() {
        childBtnInfos = new ArrayList<>();
    }

    public void setTouch(){
        dialLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CommonJava.Loging.i(LOG_NAME,"onTouch View : "+view);
                CommonJava.Loging.i(LOG_NAME,"onTouch MotionEvent : "+motionEvent);
                return false;
            }
        });

        dialLayout.setOnGenericMotionListener(new OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View view, MotionEvent motionEvent) {
                CommonJava.Loging.i(LOG_NAME,"onGenericMotion View : "+view);
                CommonJava.Loging.i(LOG_NAME,"onGenericMotion MotionEvent : "+motionEvent);
                return false;
            }
        });

    }

}

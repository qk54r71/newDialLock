package com.diallock.diallock.diallock.Activity.Layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Data.ChildBtnInfo;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;

/**
 * Created by park on 2016-12-21.
 */

public class DialLayout extends ArcLayout {

    private ArrayList<ChildBtnInfo> childBtnInfos;

    private TouchBtnIndexInteractionListener mListener;

    private static final String LOG_NAME = "DialLayout";

    private Context mContext;
    private static DialLayout dialLayout;

    public DialLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mContext == null) {
            mContext = context;
            dialLayout = this;

           /* if (context instanceof TouchBtnIndexInteractionListener) {
                mListener = (TouchBtnIndexInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement TouchBtnIndexInteractionListener");
            }*/
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

        /*if (child.getRight() != 0) {

            ChildBtnInfo childBtnInfo = new ChildBtnInfo();
            childBtnInfo.setxPosition(childX);
            childBtnInfo.setyPosition(childY);
            childBtnInfo.setChildName(getResources().getResourceEntryName(child.getId()));

            int childWidth = Math.abs(child.getRight() - child.getLeft());
            childBtnInfo.setChildWidth(childWidth);

            childBtnInfos.add(childBtnInfo);
        }*/

        /*if (childBtnInfos.size() == 12) {
            ((MainActivity) mContext).setArrayList(childBtnInfos);
        }*/

        super.childLayoutBy(child, childX, childY);
    }


    private void init() {
        childBtnInfos = new ArrayList<>();
    }

    public interface TouchBtnIndexInteractionListener {
        void touchBtnIndex(int btnIndex);
    }

    public void touchBtnIndex(int btnIndex) {
        if (mListener != null) {
            mListener.touchBtnIndex(btnIndex);
        }
    }

    public void setOnTouchEvent(MotionEvent onTouchEvent) {
        this.touchBtnIndex(44);
    }

}

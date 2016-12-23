package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.VelocityTrackerCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import com.diallock.diallock.diallock.Activity.Adapter.WidgetPagerAdapter;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Layout.ViewPager.CustomViewPager;
import com.diallock.diallock.diallock.Activity.Layout.ViewPager.HorizontalViewPager;
import com.diallock.diallock.diallock.R;

import java.util.ArrayList;

public class CircleDial extends Fragment {

    private OnFragmentInteractionListener mListener;
    private VelocityTracker mVelocityTracker = null;

    static View mView;

    private HorizontalViewPager mWidget_view;
    private WidgetPagerAdapter mWidgetPagerAdapter;
    private final String LOG_NAME = "CircleDial";

    public CircleDial() {
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
        mView = inflater.inflate(R.layout.fragment_circle_dial, container, false);

        setFindViewById();
        init();
        setOnTouch();
        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
        mWidget_view = (HorizontalViewPager) mView.findViewById(R.id.widget_view);
    }

    private void init() {

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new WidgetTimeBaseFragment());
        fragmentArrayList.add(new ImageFragment());

        mWidgetPagerAdapter = new WidgetPagerAdapter(getChildFragmentManager(), fragmentArrayList);
        mWidget_view.setAdapter(mWidgetPagerAdapter);

    }

    private void setOnTouch() {

    }

    /**
     * mVelocityTracker 를 사용해서 처음 위치로부터 어느 위치로 이동하는지 추적
     *
     * @param onTouchEvent
     */
    public void setOnTouchEvent(MotionEvent onTouchEvent) {
        int index = onTouchEvent.getActionIndex();
        int action = onTouchEvent.getActionMasked();
        int pointerId = onTouchEvent.getPointerId(index);

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
                mVelocityTracker.addMovement(onTouchEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(onTouchEvent);
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

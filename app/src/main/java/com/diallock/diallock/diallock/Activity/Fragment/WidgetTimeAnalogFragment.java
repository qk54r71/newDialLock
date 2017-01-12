package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.ParkSDK.Debug.Loging;
import com.diallock.diallock.diallock.R;


public class WidgetTimeAnalogFragment extends Fragment {

    private AnalogClock mAnalogClock;

    private View mView;
    private static final String LOG_NAME = "WidgetTimeAnalogFragment";

    public WidgetTimeAnalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WidgetTimeAnalogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WidgetTimeAnalogFragment newInstance(String param1, String param2) {
        WidgetTimeAnalogFragment fragment = new WidgetTimeAnalogFragment();
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

        CommonJava.Loging.i(LOG_NAME, "onCreateView()");
        mView = inflater.inflate(R.layout.fragment_time_widget_analog, container, false);
        setFindByViewId();
        init();
        setEvent();
        return mView;

    }

    private void setFindByViewId() {
        mAnalogClock = (AnalogClock) mView.findViewById(R.id.analogClock);
    }

    private void init() {

    }

    private void setEvent() {
        mView.setOnClickListener(onClickListener);
        mAnalogClock.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Loging.i(LOG_NAME, "v.getId() : " + v.getId());
            if (LockScreenFragment.mSwitchValue == 2) {
                CircleDial_press.getInstance(LockScreenFragment.newInstance()).isCheckPassword();
            }
            switch (v.getId()) {
                case R.id.fm_time_widget_analog_container:
                    Loging.i(LOG_NAME, "fm_time_widget_analog_container onClick");
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

package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.ParkSDK.Debug.Loging;
import com.diallock.diallock.diallock.R;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WidgetTimeDigitalFragment extends Fragment {


    private View mView;
    private LinearLayout fm_time_widget_digital_container;
    private TextView txt_time;
    private TextView txt_am_pm;
    private Timer mTimer;
    private final String LOG_NAME = "WidgetTimeDigitalFragment";

    public WidgetTimeDigitalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WidgetTimeDigitalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WidgetTimeDigitalFragment newInstance(String param1, String param2) {
        WidgetTimeDigitalFragment fragment = new WidgetTimeDigitalFragment();
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
        mView = inflater.inflate(R.layout.fragment_widget_time_digital, container, false);

        setFindById();
        init();
        setEvent();

        return mView;
    }

    private void setFindById() {
        fm_time_widget_digital_container = (LinearLayout) mView.findViewById(R.id.fm_time_widget_digital_container);
        txt_time = (TextView) mView.findViewById(R.id.txt_time);
        txt_am_pm = (TextView) mView.findViewById(R.id.txt_am_pm);
    }

    private void init() {

        MainTimerTask timerTask = new MainTimerTask();

        mTimer = new Timer();

        mTimer.schedule(timerTask, 500, 1000);
    }

    private Handler mHandler = new Handler();

    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {

            Date nowDate = new Date();

            String strTxtAmPm =
                    CommonJava.getAmPm(nowDate);
            String strTxtTime = CommonJava.getHour(nowDate) + "시 " + CommonJava.getMinute(nowDate) + "분";
            txt_am_pm.setText(strTxtAmPm);
            txt_time.setText(strTxtTime);
            //CommonJava.Loging.i(LOG_NAME, "strTxtAmPm : " + strTxtAmPm);
            //CommonJava.Loging.i(LOG_NAME, "strTxtTime : " + strTxtTime);
        }

    };


    class MainTimerTask extends TimerTask {

        public void run() {

            mHandler.post(mUpdateTimeTask);

        }
    }

    private void setEvent() {
        fm_time_widget_digital_container.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Loging.i(LOG_NAME, "v.getId() : " + v.getId());
            if (LockScreenFragment.mSwitchValue == 2) {
                CircleDial_press.getInstance(LockScreenFragment.newInstance()).isCheckPassword();
            }
            switch (v.getId()) {
                case R.id.fm_time_widget_digital_container:
                    Loging.i(LOG_NAME, "fm_time_widget_digital_container onClick");
                    break;
                case R.id.txt_am_pm:
                case R.id.txt_time:
                    Loging.i(LOG_NAME, "txt_am_pm txt_time onClick");
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

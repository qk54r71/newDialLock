package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Fragment.Data.EventInfoData;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventInfoItem.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventInfoItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInfoItem extends Fragment {

    private OnFragmentInteractionListener mListener;

    private EventInfoData mEventInfoData;
    private View mView;
    private TextView eventCategory;
    private TextView eventSido;
    private TextView eventGungu;
    private TextView eventTitle;
    private TextView eventDate;
    private TextView eventTime;
    private TextView eventLocal;
    private SimpleDraweeView eventProgress_bg;
    private TextView eventProgress_text;
    private SimpleDraweeView eventRemainDay_bg;
    private TextView eventRemainDay_text;
    private SimpleDraweeView eventImgView;

    private final String LOG_NAME = "EventInfoItem";

    private ArrayList<String> mStrEventSlideImage;

    public EventInfoItem() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EventInfoItem newInstance(EventInfoData eventInfoData) {
        EventInfoItem fragment = new EventInfoItem();
        Bundle args = new Bundle();
        args.putParcelable("eventInfoData", eventInfoData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEventInfoData = getArguments().getParcelable("eventInfoData");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_event_info_item, container, false);

        findViewById();
        init();
        setOnClick();

        return mView;
    }

    private void findViewById() {
        eventCategory = (TextView) mView.findViewById(R.id.eventCategory);
        eventSido = (TextView) mView.findViewById(R.id.eventSido);
        eventGungu = (TextView) mView.findViewById(R.id.eventGungu);
        eventTitle = (TextView) mView.findViewById(R.id.eventTitle);
        eventDate = (TextView) mView.findViewById(R.id.eventDate);
        eventTime = (TextView) mView.findViewById(R.id.eventTime);
        eventLocal = (TextView) mView.findViewById(R.id.eventLocal);
        eventProgress_bg = (SimpleDraweeView) mView.findViewById(R.id.eventProgress_bg);
        eventProgress_text = (TextView) mView.findViewById(R.id.eventProgress_text);
        eventRemainDay_bg = (SimpleDraweeView) mView.findViewById(R.id.eventRemainDay_bg);
        eventRemainDay_text = (TextView) mView.findViewById(R.id.eventRemainDay_text);
        eventImgView = (SimpleDraweeView) mView.findViewById(R.id.view_evnet_imageView);
    }

    private void init() {
        try {
            String strEventCategory = mEventInfoData.getEventCategory();
            CommonJava.Loging.i(LOG_NAME, "strEventCategory : " + strEventCategory);
            String strEventSido = mEventInfoData.getEventSido();
            CommonJava.Loging.i(LOG_NAME, "strEventSido : " + strEventSido);
            String strEventGungu = mEventInfoData.getEventGungu();
            CommonJava.Loging.i(LOG_NAME, "strEventGungu : " + strEventGungu);
            String strEventTitle = mEventInfoData.getEventTitle();
            CommonJava.Loging.i(LOG_NAME, "strEventTitle : " + strEventTitle);
            String strEventDate = mEventInfoData.getEventDate();
            CommonJava.Loging.i(LOG_NAME, "strEventDate : " + strEventDate);
            String strEventTime = mEventInfoData.getEventTime();
            CommonJava.Loging.i(LOG_NAME, "strEventTime : " + strEventTime);
            String strEventLocal = mEventInfoData.getEventLocal();
            CommonJava.Loging.i(LOG_NAME, "strEventLocal : " + strEventLocal);
            String strEventProgress = mEventInfoData.getEventProgress();
            CommonJava.Loging.i(LOG_NAME, "strEventProgress : " + strEventProgress);
            String strEventImgURL = mEventInfoData.getEventImgURL();
            CommonJava.Loging.i(LOG_NAME, "strEventImgURL : " + strEventImgURL);
            String strEventRemainDay = mEventInfoData.getEventRemainDay();
            CommonJava.Loging.i(LOG_NAME, "strEventRemainDay : " + strEventRemainDay);

            eventCategory.setText(strEventCategory);
            eventSido.setText(strEventSido);
            eventGungu.setText(strEventGungu);
            eventTitle.setText(strEventTitle);
            eventDate.setText(strEventDate);
            eventTime.setText(strEventTime);
            eventLocal.setText(strEventLocal);
            eventProgress_text.setText(strEventProgress);

            switch (strEventProgress) {
                case "끝":
                    eventProgress_bg.setBackgroundResource(R.drawable.btn_lock_screen_end);
                    break;
                case "예정":
                    eventProgress_bg.setBackgroundResource(R.drawable.btn_lock_screen_end);
                    break;
                case "당일":
                    eventProgress_bg.setBackgroundResource(R.drawable.btn_lock_screen_today);
                    break;
                case "시작":
                    eventProgress_bg.setBackgroundResource(R.drawable.btn_lock_screen_start);
                    break;
                case "종료":
                    eventProgress_bg.setBackgroundResource(R.drawable.btn_lock_screen_end);
                    break;
                case "진행":
                    eventProgress_bg.setBackgroundResource(R.drawable.btn_lock_screen_pro);
                    break;
            }

            eventImgView.setImageURI(strEventImgURL);
            if (strEventRemainDay.equals("0")) {

                if (mEventInfoData.getEventSlideImage() != null && mEventInfoData.getCode().equals("1000")) {
                    eventRemainDay_text.setText("" + mEventInfoData.getEventSlideImage().size());
                }

            } else {
                eventRemainDay_text.setText("D-" + strEventRemainDay);
            }


        } catch (NullPointerException e) {
            CommonJava.Loging.e(LOG_NAME, "ERROR : " + e.toString());
        }

        if (mEventInfoData.getEventSlideImage() != null && mEventInfoData.getCode().equals("1000")) {
            mStrEventSlideImage = mEventInfoData.getEventSlideImage();
            CommonJava.Loging.i(LOG_NAME, "strEventSlideImage : " + mStrEventSlideImage);
            eventRemainDay_text.setVisibility(View.VISIBLE);
            eventRemainDay_bg.setVisibility(View.VISIBLE);
        } else if (mEventInfoData.getCode().equals("3001")) {
            eventRemainDay_text.setVisibility(View.GONE);
            eventRemainDay_bg.setVisibility(View.GONE);
        } else if(mEventInfoData.getCode().equals("1001")){
            eventRemainDay_text.setVisibility(View.GONE);
            eventRemainDay_bg.setVisibility(View.GONE);
        }

    }

    private void setOnClick() {
        eventRemainDay_bg.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.eventRemainDay_bg:
                    onButtonPressed(mStrEventSlideImage);
                    for(Fragment fragment : getActivity().getSupportFragmentManager().getFragments()){
                        if(fragment instanceof LockScreenFragment){
                            ((LockScreenFragment)fragment).setImageSildeView(mStrEventSlideImage);
                        }
                    }
                    break;
            }
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(ArrayList<String> strEventSlideImage) {
       /* if (mListener != null) {
            mListener.onFragmentInteraction(strEventSlideImage);
        }*/
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
        void onFragmentInteraction(ArrayList<String> strEventSlideImage);
    }
}

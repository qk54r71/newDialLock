package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Fragment.Data.TourismInfoData;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class TourismInfoItem extends Fragment {

    private SimpleDraweeView mView_tour_imageView;
    private SimpleDraweeView mTourSlideBtn_bg;
    private TextView mTourSlideBtn_text;

    private View mView;

    private TourismInfoData mTourismInfoData;
    private ArrayList<String> mStrTourSlideImage;

    private LockScreenFragment mLockScreenFragment;

    private static final String LOG_NAME = "TourismInfoItem";

    private OnTourismInfoItemInteractionListener mListener;

    public TourismInfoItem() {
        // Required empty public constructor
    }

    public static TourismInfoItem newInstance(TourismInfoData tourismInfoData) {
        TourismInfoItem fragment = new TourismInfoItem();
        Bundle args = new Bundle();
        args.putParcelable("tourismInfoData", tourismInfoData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTourismInfoData = getArguments().getParcelable("tourismInfoData");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_tourism_info_item, container, false);

        findViewById();
        init();
        setOnClick();

        return mView;
    }

    private void findViewById() {
        mView_tour_imageView = (SimpleDraweeView) mView.findViewById(R.id.view_tour_imageView);
        mTourSlideBtn_bg = (SimpleDraweeView) mView.findViewById(R.id.tourSlideBtn_bg);
        mTourSlideBtn_text = (TextView) mView.findViewById(R.id.tourSlideBtn_text);
    }

    private void init() {
        try {
            String strTourImgURL = mTourismInfoData.getTourImgURL();
            CommonJava.Loging.i(LOG_NAME, "strTourImgURL : " + strTourImgURL);
            mView_tour_imageView.setImageURI(strTourImgURL);

            if (mTourismInfoData.getTourSlideImage() != null) {
                mStrTourSlideImage = mTourismInfoData.getTourSlideImage();
                CommonJava.Loging.i(LOG_NAME, "mStrTourSlideImage : " + mStrTourSlideImage);

                mTourSlideBtn_text.setText("" + mTourismInfoData.getTourSlideImage().size());
            }
        } catch (NullPointerException e) {
            CommonJava.Loging.e(LOG_NAME, "Error : " + e.toString());
        }

    }

    private void setOnClick() {
        mTourSlideBtn_bg.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tourSlideBtn_bg:
                    //onButtonPressed(mStrTourSlideImage);
                    for(Fragment fragment :  getActivity().getSupportFragmentManager().getFragments()){
                        CommonJava.Loging.i(LOG_NAME,"fragment : "+fragment);
                        if(fragment instanceof LockScreenFragment){
                            ((LockScreenFragment)fragment).setImageSildeView(mStrTourSlideImage);
                        }
                    }


                    break;
            }
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(ArrayList<String> strTourSlideImage) {
        /*if (mListener != null) {
            mListener.onTourismInfoItemInteraction(strTourSlideImage);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnTourismInfoItemInteractionListener) {
            mListener = (OnTourismInfoItemInteractionListener) context;
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
    public interface OnTourismInfoItemInteractionListener {
        // TODO: Update argument type and name
        void onTourismInfoItemInteraction(ArrayList<String> strTourSlideImage);
    }
}

package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.diallock.diallock.diallock.Activity.Adapter.WidgetPagerAdapter;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.VolleyNetwork;
import com.diallock.diallock.diallock.Activity.Fragment.Data.EventInfoData;
import com.diallock.diallock.diallock.Activity.Layout.ViewPager.VerticalViewPager;
import com.diallock.diallock.diallock.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.Authenticator;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInfo extends Fragment {

    private static final String LOG_NAME = "EventInfo";
    private OnFragmentInteractionListener mListener;

    private View mView;
    private VerticalViewPager mVerticalViewPager;
    private PagerAdapter mWidgetPagerAdapter;

    private ArrayList<EventInfoData> mEventInfoDataArrayList;

    private static String mStrNowDay;

    public EventInfo() {
        // Required empty public constructor
    }

    public static EventInfo newInstance(Date nowDate) {


        String strNowDay =
                CommonJava.getYear(nowDate) + "." + CommonJava.getMonth(nowDate) + "." + CommonJava.getDay(nowDate);
        CommonJava.Loging.i(LOG_NAME, "newInstance strNowDay : " + strNowDay);
        EventInfo fragment = new EventInfo();
       /* Bundle args = new Bundle();
        args.putString("strNowDay", strNowDay);

        fragment.setArguments(args);*/

        mStrNowDay = strNowDay;

        return fragment;
    }

    public void dayChangeInstance(String strNowDay) {

        CommonJava.Loging.i(LOG_NAME, "dayChangeInstance strNowDay : " + strNowDay);

        mStrNowDay = strNowDay;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);/*
        if (getArguments() != null) {
            mEventInfoDataArrayList = getArguments().getParcelableArrayList("eventInfoDataArrayList");
        }*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_event_info, container, false);

        findViewById();
        init();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void findViewById() {
        mVerticalViewPager = (VerticalViewPager) mView.findViewById(R.id.event_frag_vertical);
    }

    private void init() {
        networkConnect();
        //setFragment();
    }

    private void setFragment(ArrayList<EventInfoData> mEventInfoDataArrayList) {


        if (mEventInfoDataArrayList != null) {

            Collections.shuffle(mEventInfoDataArrayList);

            ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

            for (EventInfoData eventInfoData : mEventInfoDataArrayList) {
                fragmentArrayList.add(new EventInfoItem().newInstance(eventInfoData));
            }

            mWidgetPagerAdapter = new WidgetPagerAdapter(getChildFragmentManager(), fragmentArrayList);
            mVerticalViewPager.setAdapter(mWidgetPagerAdapter);

        } else if (mEventInfoDataArrayList == null) {
            CommonJava.Loging.e(LOG_NAME, "mEventInfoDataArrayList is Null");
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);/*
        if (context instanceof OnFragmentInteractionListener) {
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

    private void networkConnect() {
        CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest networkConnect");

        String strCode = "1000";
        String strMemberIdx = CommonJava.loadSharedPreferences(getContext(), "memberIdx");
        String strRequestDay = mStrNowDay;
        String strIndexStartNum = "0";


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", strCode);
            jsonObject.put("member_idx", strMemberIdx);
            jsonObject.put("requestDay", strRequestDay);
            jsonObject.put("indexStartNum", strIndexStartNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonJava.Loging.i(LOG_NAME, "jsonObject : " + jsonObject);

        final JSONArray jsonElements = new JSONArray();
        jsonElements.put(jsonObject);

        CommonJava.Loging.i(LOG_NAME, "jsonElements : " + jsonElements);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://molppangmy.cafe24.com/APPAPI/eventInfoList", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CommonJava.Loging.i(LOG_NAME, "JsonObjectRequest onResponse :" + response);
                String strJsonArray = null;
                try {
                    strJsonArray = response.getJSONArray("eventList").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Type listType = new TypeToken<ArrayList<EventInfoData>>() {
                }.getType();

                CommonJava.Loging.i(LOG_NAME, "JsonObjectRequest strJsonArray :" + strJsonArray);
                mEventInfoDataArrayList = new Gson().fromJson(strJsonArray, listType);

                CommonJava.Loging.i(LOG_NAME, "JsonObjectRequest mEventInfoDataArrayList :" + mEventInfoDataArrayList.toString());
                CommonJava.Loging.i(LOG_NAME, "JsonObjectRequest mEventInfoDataArrayList getCode :" + mEventInfoDataArrayList.get(0).getCode());

                setFragment(mEventInfoDataArrayList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonJava.Loging.e(LOG_NAME, "JsonObjectRequest onErrorResponse :" + error);
            }
        });

        VolleyNetwork.CustomVolleyRequestQueue.getInstance(getContext()).getRequestQueue().add(jsonObjectRequest);
/*//TODO: 하드코딩 작업용 start
        String strJsonArray = null;

        Type listType = new TypeToken<ArrayList<EventInfoData>>() {
        }.getType();

        CommonJava.Loging.i(LOG_NAME, "하드코딩 JsonObjectRequest strJsonArray :" + strJsonArray);
        strJsonArray = "[{\"eventDate\":\"eventDate\",\"eventSido\":\"eventSido\",\"eventSlideImage\":[\"arrayList1\",\"arrayList2\",\"arrayList3\"],\"eventGungu\":\"eventGungu\",\"eventTitle\":\"eventTitle\",\"eventRemainDay\":\"eventRemainDay\",\"eventTime\":\"eventTime\",\"code\":\"1000\",\"eventLocal\":\"eventLocal\",\"eventProgress\":\"eventProgress\",\"eventCategory\":\"eventCategory\",\"eventImgURL\":\"eventImgURL\"}]";
        mEventInfoDataArrayList = new Gson().fromJson(strJsonArray, listType);

        CommonJava.Loging.i(LOG_NAME, "하드코딩 JsonObjectRequest mEventInfoDataArrayList :" + mEventInfoDataArrayList.toString());
        CommonJava.Loging.i(LOG_NAME, "하드코딩 JsonObjectRequest mEventInfoDataArrayList getCode :" + mEventInfoDataArrayList.get(0).getCode());

        setFragment(mEventInfoDataArrayList);
//TODO: 하드코딩 작업용 end*/
    }


}

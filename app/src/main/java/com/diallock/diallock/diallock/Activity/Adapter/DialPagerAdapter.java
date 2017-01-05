package com.diallock.diallock.diallock.Activity.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by park on 2016-09-28.
 */
public class DialPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mFragmentArrayList;
    private final static String LOG_NAME = "DialPagerAdapter";

    public DialPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {
        super(fm);
        mFragmentArrayList = fragmentArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentArrayList.size();
    }

}

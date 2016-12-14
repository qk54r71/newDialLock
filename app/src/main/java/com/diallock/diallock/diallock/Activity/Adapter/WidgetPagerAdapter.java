package com.diallock.diallock.diallock.Activity.Adapter;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;

import java.util.ArrayList;

/**
 * Created by park on 2016-09-28.
 */
public class WidgetPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mFragmentArrayList;
    private final static String LOG_NAME = "WidgetPagerAdapter";

    public WidgetPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {
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

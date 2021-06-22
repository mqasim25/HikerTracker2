package com.example.hikertracker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.example.hikertracker.Fragments.AboutFragment;
import com.example.hikertracker.Fragments.HomeFragment;
import com.example.hikertracker.Fragments.StatusFragment;

public class ViewPagerTabAdapter extends FragmentPagerAdapter {

    //private static final String TAG = "ViewPagerTabAdapter";
    /*private List<Fragment> mFragments;
    private List<String> mTitles;

    public ViewPagerTabAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles)
    {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return  mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }


    /*@Override
    public void passData(String data) {

        Log.d(TAG, "data received to view pager... sending to tab 2");

        StatusFragment statusFragment = new StatusFragment();
        statusFragment.passData(data);

    }*/


    public ViewPagerTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new HomeFragment();
        } else if (position == 1) {
            fragment = new StatusFragment();
        } else if (position == 2) {
            fragment = new AboutFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Home";
        } else if (position == 1) {
            title = "Status";
        } else if (position == 2) {
            title = "About";
        }

        return title;
    }

}

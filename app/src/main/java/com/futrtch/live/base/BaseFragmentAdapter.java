package com.futrtch.live.base;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class BaseFragmentAdapter extends FragmentStatePagerAdapter {

    protected List<Fragment> mFragmentList;
    protected String[] mTitles;

    public BaseFragmentAdapter(FragmentManager fm) {
        this(fm, null, null);
    }

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] mTitles) {
        super(fm);
        this.mFragmentList = fragmentList;
        this.mTitles = mTitles;
    }


    @NotNull
    @Override
    public Fragment getItem (int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount () {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }

    public boolean isEmpty() {
        return mFragmentList == null;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }



}

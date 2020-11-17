package com.futrtch.live.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public abstract class BaseViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Integer> mIndex;
    private final String[] mTitles;

    public abstract Fragment getFragment(int position);

    public BaseViewPagerAdapter (FragmentManager fm, List<Integer> fragments, String[] mTitles) {
        super(fm);
        mIndex = fragments;
        this.mTitles = mTitles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    @Override
    public int getCount() {
        return mIndex == null ? 0 : mIndex.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle (int position) {
        return mTitles[position];
    }

}

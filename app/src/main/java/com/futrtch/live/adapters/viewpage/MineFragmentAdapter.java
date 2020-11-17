package com.futrtch.live.adapters.viewpage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.futrtch.live.base.BaseViewPagerAdapter;
import com.futrtch.live.factorys.MineFragmentFactory;

import java.util.List;

public class MineFragmentAdapter extends BaseViewPagerAdapter {

    public MineFragmentAdapter(FragmentManager fm, List<Integer> fragments, String[] mTitles) {
        super(fm, fragments, mTitles);
    }

    @Override
    public Fragment getFragment(int position) {
        return MineFragmentFactory.getFragment(position);
    }
}

package com.futrtch.live.factorys;

import androidx.fragment.app.Fragment;

import com.futrtch.live.activitys.fragments.mine.MineListFragment;

public class MineFragmentFactory {
    public static Fragment getFragment(int index){
        switch (index){
            case 0: // 作品
                return MineListFragment.getInstance(index);
            case 1: // 动态
                return MineListFragment.getInstance(index);
            case 2: // 喜欢
                return MineListFragment.getInstance(index);
        }
        return new Fragment();
    }
}

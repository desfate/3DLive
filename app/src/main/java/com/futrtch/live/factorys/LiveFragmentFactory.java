package com.futrtch.live.factorys;

import androidx.fragment.app.Fragment;

import com.futrtch.live.activitys.fragments.main.LiveCareFragment;
import com.futrtch.live.activitys.fragments.main.LiveListFragment;
import com.futrtch.live.activitys.fragments.main.LiveReplayFragment;
import com.futrtch.live.mvvm.vm.LiveReplayViewModel;

public class LiveFragmentFactory {
    public static Fragment getFragment(int index){
        switch (index){
            case 0: // 推荐
                return LiveListFragment.getInstance(index);
            case 1: // 关注
                return LiveCareFragment.getInstance(index);
            case 2: // 回放
                return LiveReplayFragment.getInstance(index);
        }
        return new Fragment();
    }
}

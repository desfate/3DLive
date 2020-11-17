package com.futrtch.live.factorys;

import androidx.fragment.app.Fragment;

import com.futrtch.live.activitys.fragments.friend.FindFriendFragment;
import com.futrtch.live.activitys.fragments.friend.FriendListFragment;

public class FriendFragmentFactory {
    public static Fragment getFragment(int index){
        switch (index){
            case 0: // 朋友列表
                return FriendListFragment.getInstance(index);
            case 1: // 发现朋友
                return FindFriendFragment.getInstance(index);
        }
        return new Fragment();
    }
}

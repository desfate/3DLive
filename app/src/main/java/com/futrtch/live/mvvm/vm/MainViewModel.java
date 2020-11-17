package com.futrtch.live.mvvm.vm;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.activitys.BaseIMLVBActivity;
import com.futrtch.live.activitys.fragments.FriendFragment;
import com.futrtch.live.activitys.fragments.LiveFragment;
import com.futrtch.live.activitys.fragments.MessageFragment;
import com.futrtch.live.activitys.fragments.MineFragment;
import com.futrtch.live.tencent.liveroom.MLVBLiveRoom;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    public MLVBLiveRoom mLiveRoom;//                                                            直播间 M L V B

    List<Fragment> mFragments = new ArrayList<>();

    MainViewModel(){

    }

    public void prepare(){
        mFragments.add(new LiveFragment());
        mFragments.add(new FriendFragment());
        mFragments.add(new MessageFragment());
        mFragments.add(new MineFragment());
    }

    public void setExitListener(BaseIMLVBActivity activity){
        mLiveRoom = MLVBLiveRoom.sharedInstance(activity.getApplicationContext());//            初始化 M L V B 组件
        mLiveRoom.setListener(activity);
    }

    public List<Fragment> getFragments(){
        return mFragments;
    }

    public void release(){
        mLiveRoom.setListener(null);
    }

}

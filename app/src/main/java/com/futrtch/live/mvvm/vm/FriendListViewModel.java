package com.futrtch.live.mvvm.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.beans.FriendBean;

import java.util.ArrayList;
import java.util.List;

public class FriendListViewModel extends ViewModel {

    public final static int FRIEND_LIST = 10086;
    public final static int FIND_FRIEND = 10010;

    MutableLiveData<Boolean> isFocus = new MutableLiveData<>();
    MutableLiveData<List<FriendBean>> mData = new MutableLiveData<>();

    private List<FriendBean> mFriendList;

    private int state = FRIEND_LIST;  // 1: 朋友列表 2： 发现朋友

    public MutableLiveData<Boolean> getIsFocus() {
        return isFocus;
    }

    public void prepare(){
        List<FriendBean> beans = new ArrayList<>();
        for(int i = 0 ; i < 50; i ++) {
            beans.add(new FriendBean("用户"+i, "", false, false));
        }
        if(mFriendList == null) mFriendList = new ArrayList<>();
        mFriendList.addAll(beans);
    }

    public void setFriendState(int state){
        this.state = state;
    }

    public int getFriendState(){
        return state;
    }


    public List<FriendBean> getFriendList(){
        if(mFriendList == null) mFriendList = new ArrayList<>();
        return mFriendList;
    }





    public MutableLiveData<List<FriendBean>> getmData() {
        return mData;
    }
}

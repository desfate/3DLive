package com.futrtch.live.mvvm.repository;

import com.futrtch.live.beans.FriendBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐列表数据集
 */
public class FriendListRepository {


    /**
     * 通过网络推荐获取好友
     */
    public List<FriendBean> onRecommendRequest(){
        List<FriendBean> beans = new ArrayList<>();
        for(int i = 0 ; i < 50; i ++) {
            beans.add(new FriendBean("用户"+i, "", false, false, true));
        }
        return beans;
    }

    /**
     * 通过通讯录获取好友
     */
    public List<FriendBean> onRecommendByAddressBook(){
        List<FriendBean> beans = new ArrayList<>();
        for(int i = 0 ; i < 50; i ++) {
            beans.add(new FriendBean("用户"+i, "", false, false, false));
        }
        return beans;
    }




}

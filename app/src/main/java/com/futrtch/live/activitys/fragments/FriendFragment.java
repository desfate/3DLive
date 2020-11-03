package com.futrtch.live.activitys.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.futrtch.live.R;
import com.futrtch.live.base.BaseFragmentAdapter;
import com.futrtch.live.databinding.FragmentFriendBinding;
import com.futrtch.live.mvvm.MVVMFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 朋友页面
 */
public class FriendFragment extends MVVMFragment{

    public static final int START_LIVE_PLAY = 100;

    FragmentFriendBinding mDataBinding;
    List<Fragment> mFragments = new ArrayList<>();
    String[] mTitles = new String[]{
            "朋友列表", "发现朋友"
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {

    }

    @Override
    public void init() {
        for (int i = 0; i < mTitles.length; i++) {
            MVVMFragment listFragment;
            if(i == 0) {
                listFragment = new FriendListFragment();
            }else{
                listFragment = new FindFriendFragment();
            }
            mFragments.add(listFragment);
        }
        BaseFragmentAdapter adapter =
                new BaseFragmentAdapter(getChildFragmentManager(), mFragments, mTitles);
        mDataBinding.viewpager.setAdapter(adapter);
        mDataBinding.tabs.setupWithViewPager(mDataBinding.viewpager);
    }

    public void bindUi(){

    }

    public void subscribeUi(){

    }

    @Override
    public void initRequest() {

    }

}

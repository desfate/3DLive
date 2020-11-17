package com.futrtch.live.activitys.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.adapters.viewpage.FriendFragmentAdapter;
import com.futrtch.live.databinding.FragmentFriendBinding;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.ViewPagerViewModel;
import com.futrtch.live.mvvm.vm.ViewPagerViewModelFactory;

/**
 * 朋友页面  主页 -> 朋友
 */
public class FriendFragment extends MVVMFragment{

    ViewPagerViewModel mViewModel;
    FriendFragmentAdapter mAdapter;
    FragmentFriendBinding mDataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new ViewPagerViewModelFactory(getActivity().getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(ViewPagerViewModel.class);
    }

    @Override
    public void init() {
        mAdapter = new FriendFragmentAdapter(getChildFragmentManager(), mViewModel.getIndexTwo(), mViewModel.getFriendTitles());
        mDataBinding.viewpager.setAdapter(mAdapter);
        mDataBinding.tabs.setupWithViewPager(mDataBinding.viewpager);
    }

    public void bindUi(){

    }

    public void subscribeUi(){

    }

    @Override
    public void initRequest() {

    }

    @Override
    public void releaseBinding() {
        releaseBindingList(mDataBinding);
    }

}

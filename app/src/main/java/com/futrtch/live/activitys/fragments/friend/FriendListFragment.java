package com.futrtch.live.activitys.fragments.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.futrtch.live.R;
import com.futrtch.live.activitys.UserActivity;
import com.futrtch.live.adapters.FriendListAdapter;
import com.futrtch.live.databinding.FragmentFriendListBinding;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.FriendListViewModel;
import com.futrtch.live.mvvm.vm.FriendListViewModelFactory;
import com.futrtch.live.mvvm.vm.UserViewModel;
import com.futrtch.live.utils.InputKeybroadUtils;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.Objects;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 朋友列表   主页 -> 朋友 -> 朋友列表
 */
public class FriendListFragment extends MVVMFragment{

    FragmentFriendListBinding mDataBinding;
    FriendListViewModel mViewModel;
    FriendListAdapter mAdapter;
    LinearLayoutManager manager;

    public static FriendListFragment getInstance(int index){
        FriendListFragment fragment = new FriendListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_list, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new FriendListViewModelFactory(Objects.requireNonNull(getActivity()).getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(FriendListViewModel.class);
        mViewModel.setFriendState(FriendListViewModel.FRIEND_LIST);
    }

    @Override
    public void init() {
        mDataBinding.scanImg.setVisibility(View.INVISIBLE);
        manager = new LinearLayoutManager(getActivity());
        mDataBinding.recyclerList.setLayoutManager(manager);
        mAdapter = new FriendListAdapter(R.layout.layout_friend_item, getActivity(),mViewModel.getFriendList());
        mDataBinding.recyclerList.setAdapter(mAdapter);
        mViewModel.prepare();
    }

    public void bindUi(){
        RxView.clicks(mDataBinding.searchInput)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {

                });

        RxView.focusChanges(mDataBinding.searchInput)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(aBoolean -> {
                    mViewModel.getIsFocus().postValue(aBoolean);
                });

        RxView.clicks(mDataBinding.scanImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    if(mViewModel.getIsFocus().getValue()){ // 有焦点情况下是删除
                        mDataBinding.searchInput.setText("");
                        mDataBinding.searchInput.clearFocus();
                        InputKeybroadUtils.hideInput(Objects.requireNonNull(getActivity()));
                    }else{

                    }
                });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            intent.putExtra(UserViewModel.USER_ID, Objects.requireNonNull(mViewModel.getFriendList()).get(position).getUserName());
            startActivity(intent);
        });
    }

    public void subscribeUi(){
        mViewModel.getIsFocus().observe(this, aBoolean -> {
            if(aBoolean) {
                mDataBinding.seachTv.setVisibility(View.VISIBLE);
                mDataBinding.scanImg.setImageResource(R.mipmap.input_close_icon);
                mDataBinding.scanImg.setVisibility(View.VISIBLE);
            } else {
                mDataBinding.seachTv.setVisibility(View.GONE);
                mDataBinding.scanImg.setImageResource(R.mipmap.scan_icon);
                mDataBinding.scanImg.setVisibility(View.INVISIBLE);
            }
        });
        mViewModel.getmData().observe(this, friendBeans -> {
            manager.scrollToPositionWithOffset(0, 0);
        });
    }


    @Override
    public void initRequest() {

    }

    @Override
    public void releaseBinding() {
        releaseBindingList(mDataBinding);
    }


}
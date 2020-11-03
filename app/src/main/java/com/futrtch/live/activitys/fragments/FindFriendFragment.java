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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.futrtch.live.R;
import com.futrtch.live.adapters.FriendListAdapter;
import com.futrtch.live.databinding.FragmentFriendListBinding;
import com.futrtch.live.databinding.LayoutFriendHeadBinding;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.FriendListViewModel;
import com.futrtch.live.mvvm.vm.FriendListViewModelFactory;
import com.futrtch.live.utils.InputKeybroadUtils;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.Objects;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class FindFriendFragment extends MVVMFragment {

    FragmentFriendListBinding mDataBinding;
    Bundle savedInstanceState;
    FriendListViewModel mViewModel;
    FriendListAdapter mAdapter;
    LinearLayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_list, container, false);
        this.savedInstanceState = savedInstanceState;
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new FriendListViewModelFactory(Objects.requireNonNull(getActivity()).getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(FriendListViewModel.class);
    }

    @Override
    public void init() {
        manager = new LinearLayoutManager(getActivity());
        mDataBinding.recyclerList.setLayoutManager(manager);
        mAdapter = new FriendListAdapter(R.layout.layout_friend_item, getActivity(),mViewModel.getFriendList());
        mDataBinding.recyclerList.setAdapter(mAdapter);
        mAdapter.setHeaderView(getHeaderView(v -> {

        }));
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
    }

    public void subscribeUi(){
        mViewModel.getIsFocus().observe(this, aBoolean -> {
            if(aBoolean) {
                mDataBinding.seachTv.setVisibility(View.VISIBLE);
                mDataBinding.scanImg.setImageResource(R.mipmap.input_close_icon);
            } else {
                mDataBinding.seachTv.setVisibility(View.GONE);
                mDataBinding.scanImg.setImageResource(R.mipmap.scan_icon);
            }
        });
        mViewModel.getmData().observe(this, friendBeans -> {

            manager.scrollToPositionWithOffset(0, 0);
        });
    }

    private View getHeaderView(View.OnClickListener listener) {
        LayoutFriendHeadBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_friend_head, mDataBinding.recyclerList, false);
        binding.userInfoRly.setOnClickListener(listener);
        binding.recommandTv.setOnClickListener(listener);
        return binding.getRoot();
    }

    @Override
    public void initRequest() {

    }


}

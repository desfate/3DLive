package com.futrtch.live.activitys.fragments;

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

import com.futrtch.live.R;
import com.futrtch.live.activitys.SettingActivity;
import com.futrtch.live.adapters.viewpage.MineFragmentAdapter;
import com.futrtch.live.databinding.FragmentOtherBinding;
import com.futrtch.live.databinding.LayoutDialogBottomBinding;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.UserViewModel;
import com.futrtch.live.mvvm.vm.UserViewModelFactory;
import com.futrtch.live.views.BottomDialog;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.Objects;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 他人头像  点击他人头像
 */
public class UserFragment extends MVVMFragment {

    FragmentOtherBinding mDataBinding;
    LayoutDialogBottomBinding mBottomBinding;

    UserViewModel mViewModel;
    BottomDialog mDialog;
    MineFragmentAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_other, container, false);
        mBottomBinding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_bottom, null, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new UserViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        mViewModel.getIntent(getActivity().getIntent());
        mViewModel.prepare(getActivity(), mDataBinding);
    }

    @Override
    public void init() {
        mAdapter = new MineFragmentAdapter(getChildFragmentManager(), mViewModel.getmIndex(), mViewModel.getmTitles());
        mDataBinding.viewpager.setAdapter(mAdapter);
        mDataBinding.tabs.setupWithViewPager(mDataBinding.viewpager);
        mDialog = new BottomDialog(Objects.requireNonNull(getContext()), mBottomBinding);
    }

    @Override
    public void bindUi() {
        RxView.clicks(mDataBinding.more)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), SettingActivity.class)));

        RxView.clicks(mDataBinding.moreBtn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), SettingActivity.class)));

        RxView.clicks(mDataBinding.backBtn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> Objects.requireNonNull(getActivity()).finish());

        RxView.clicks(mDataBinding.moreBtn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDialog.showDialog());

        RxView.clicks(mDataBinding.more)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDialog.showDialog());

        RxView.clicks(mBottomBinding.cancelTv)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDialog.cancel());

        mDataBinding.toolbar.setNavigationOnClickListener(v -> {
            if(getActivity() != null) getActivity().finish();
        });
    }

    @Override
    public void subscribeUi() {
        // 设置头部标题颜色
        mViewModel.getAvgColor().observe(this, integer -> mDataBinding.toolbar.setBackgroundColor(integer));
        // 根据节目变化  改变头部透明度
        mViewModel.getTitleColor().observe(this, integer -> mDataBinding.toolbar.setAlpha(integer));
        // 用户名变化 （自己和他人主页）
        mViewModel.getUserName().observe(this, string -> {
            mDataBinding.title.setText(string);
            mDataBinding.userNameTv.setText(string);
        });
    }

    @Override
    public void initRequest() {

    }

    @Override
    public void releaseBinding() {
        releaseBindingList(mDataBinding, mBottomBinding);
    }
}

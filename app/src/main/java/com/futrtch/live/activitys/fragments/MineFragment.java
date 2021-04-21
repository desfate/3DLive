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
import com.futrtch.live.activitys.EditActivity;
import com.futrtch.live.activitys.SettingActivity;
import com.futrtch.live.adapters.viewpage.MineFragmentAdapter;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.AccountInfoBean;
import com.futrtch.live.databinding.FragmentUserBinding;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.MineViewModel;
import com.futrtch.live.mvvm.vm.MineViewModelFactory;
import com.jakewharton.rxbinding4.view.RxView;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.Objects;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 我的页面  主页 -> 我的
 */
public class MineFragment extends MVVMFragment {

    FragmentUserBinding mDataBinding;
    MineViewModel mViewModel;
    MineFragmentAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new MineViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(MineViewModel.class);
        mViewModel.prepare(getActivity(), mDataBinding);
    }

    @Override
    public void init() {
        mAdapter = new MineFragmentAdapter(getChildFragmentManager(), mViewModel.getmIndex(), mViewModel.getmTitles());
        mDataBinding.viewpager.setAdapter(mAdapter);
        mDataBinding.tabs.setupWithViewPager(mDataBinding.viewpager);
    }

    @Override
    public void bindUi() {
        RxView.clicks(mDataBinding.edtUserInfoBtn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), EditActivity.class)));

        RxView.clicks(mDataBinding.more)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), SettingActivity.class)));

        RxView.clicks(mDataBinding.moreBtn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), SettingActivity.class)));

        RxView.clicks(mDataBinding.share)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mViewModel.share());

        RxView.clicks(mDataBinding.shareBtn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mViewModel.share());
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
        // 收到账户信息请求返回
        // 请求账户信息返回
        LiveEventBus.get(RequestTags.ACCOUNT_INFO_REQ, BaseResponBean.class)
                .observe(this, baseRespondBean -> {
                    mViewModel.getUserName().postValue(((AccountInfoBean)baseRespondBean.getData()).getNickname());
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

package com.futrtch.live.activitys.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.activitys.EditActivity;
import com.futrtch.live.activitys.SettingActivity;
import com.futrtch.live.activitys.fragments.mine.MineListFragment;
import com.futrtch.live.base.BaseFragmentAdapter;
import com.futrtch.live.databinding.FragmentMineBinding;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.MineViewModel;
import com.futrtch.live.mvvm.vm.MineViewModelFactory;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 我的页面  主页 -> 我的
 */
public class MineFragment extends MVVMFragment {

    FragmentMineBinding mDataBinding;
    MineViewModel mViewModel;
    List<Fragment> mFragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false);
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
        for (int i = 0; i < mViewModel.getmTitles().length; i++) {
            MineListFragment listFragment = new MineListFragment();
            mFragments.add(listFragment);
        }
        BaseFragmentAdapter adapter =
                new BaseFragmentAdapter(getChildFragmentManager(), mFragments, mViewModel.getmTitles());
        mDataBinding.viewpager.setAdapter(adapter);
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
}

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
import com.futrtch.live.activitys.fragments.main.LiveCareFragment;
import com.futrtch.live.activitys.fragments.main.LiveListFragment;
import com.futrtch.live.activitys.fragments.main.LiveReplayFragment;
import com.futrtch.live.base.BaseFragmentAdapter;
import com.futrtch.live.databinding.FragmentLiveBinding;
import com.futrtch.live.mvvm.MVVMFragment;

import java.util.ArrayList;
import java.util.List;

public class LiveFragment extends MVVMFragment {

    FragmentLiveBinding mDataBinding;
    List<Fragment> mFragments = new ArrayList<>();
    String[] mTitles = new String[]{
            "推荐",  "关注", "回放"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {

    }

    @Override
    public void init() {

        mFragments.add(new LiveListFragment());
        mFragments.add(new LiveCareFragment());
        mFragments.add(new LiveReplayFragment());

        BaseFragmentAdapter adapter =
                new BaseFragmentAdapter(getFragmentManager(), mFragments, mTitles);
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

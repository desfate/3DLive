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
import com.futrtch.live.activitys.LiveRecordActivity;
import com.futrtch.live.adapters.BannerImageAdapter;
import com.futrtch.live.adapters.LiveListAdapter;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.BannerBean;
import com.futrtch.live.databinding.FragmentLiveListBinding;
import com.futrtch.live.databinding.LayoutBannerBinding;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.LiveListViewModel;
import com.futrtch.live.mvvm.vm.LiveListViewModelFactory;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 直播列表页面
 */
public class LiveListFragment extends MVVMFragment{

    public static final int START_LIVE_PLAY = 100;

    FragmentLiveListBinding mDataBinding;
    LiveListAdapter mAdapter;
    LiveListViewModel mViewModel;
    BannerImageAdapter mBannerAdpater;
    Bundle savedInstanceState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_list, container, false);
        this.savedInstanceState = savedInstanceState;
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new LiveListViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(LiveListViewModel.class);
    }

    @Override
    public void init() {
        mAdapter = new LiveListAdapter(R.layout.listview_video_item, getActivity(), mViewModel.getListData());
        List<BannerBean> mBannerList = new ArrayList<>();
        mBannerList.add(new BannerBean(R.mipmap.banner1, ""));
        mBannerList.add(new BannerBean(R.mipmap.banner2, ""));
        mBannerAdpater = new BannerImageAdapter(mBannerList);
        mAdapter.setHeaderView(getHeaderView()); // 有头部轮播图
        mDataBinding.grid.setAdapter(mAdapter);

        mDataBinding.swipeLayout.setOnRefreshListener(() -> {
            mViewModel.onRefresh();
        });
    }

    public void bindUi(){
        mDataBinding.fab.setOnClickListener(v -> mDataBinding.fab.setExpanded(true));

        mDataBinding.scrim.setOnClickListener(v -> mDataBinding.fab.setExpanded(false));

        mDataBinding.startLive3Btn.setOnClickListener(v -> {
            if (TCUtils.checkRecordPermission(getActivity())) {
                getActivity().startActivity(new Intent(getActivity(), LiveRecordActivity.class));
            }
        });
    }

    public void subscribeUi(){
        // 收到列表数据返回
        LiveEventBus.get(RequestTags.LIVEROOMLIST_REQ, BaseResponBean.class)
                .observe(this, baseResponBean -> {
                    mDataBinding.swipeLayout.setRefreshing(false);
                    mViewModel.getListData().clear();
                    mViewModel.getListData().addAll((List<TCVideoInfo>)baseResponBean.getData());
                });
    }

    @Override
    public void initRequest() {
        mViewModel.onRefresh();
    }

    public View getHeaderView(){
        LayoutBannerBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_banner, mDataBinding.grid, false);
        binding.banner.setAdapter(mBannerAdpater);
        return binding.getRoot();
    }

}

package com.futrtch.live.activitys.fragments.main;

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
import androidx.recyclerview.widget.GridLayoutManager;

import com.futrtch.live.R;
import com.futrtch.live.activitys.LiveRecordActivity;
import com.futrtch.live.adapters.LiveListAdapter;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.databinding.FragmentLiveListBinding;
import com.futrtch.live.databinding.LayoutEmptyListBinding;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.LiveListViewModel;
import com.futrtch.live.mvvm.vm.LiveListViewModelFactory;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.futrtch.live.utils.decoration.GridSectionAverageGapItemDecoration;
import com.futrtch.live.views.ViewsBuilder;
import com.jakewharton.rxbinding4.view.RxView;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.List;
import java.util.Objects;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 直播列表页面  主页 -> 推荐 -> 直播列表
 */
public class LiveCareFragment extends MVVMFragment {

    public final static int START_LIVE_PLAY = 10090;

    LiveListAdapter mAdapter;
    LiveListViewModel mViewModel;

    FragmentLiveListBinding mDataBinding;
    LayoutEmptyListBinding mEmptyBinding; // 页面为空

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_list, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new LiveListViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(LiveListViewModel.class);
    }

    @Override
    public void init() {
        mEmptyBinding = (LayoutEmptyListBinding) new ViewsBuilder()
                .setParent(mDataBinding.grid)
                .setInflater(getLayoutInflater())
                .setLayoutId(R.layout.layout_empty_list)
                .setAttachToParent(false)
                .build()
                .getDataBinding();

        mAdapter = new LiveListAdapter(R.layout.listview_video_item, getActivity(), mViewModel.getListData());
        mAdapter.setEmptyView(mEmptyBinding.getRoot());
        mDataBinding.grid.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mDataBinding.grid.addItemDecoration(new GridSectionAverageGapItemDecoration(10, 10, 20, 15));
        mDataBinding.grid.setAdapter(mAdapter);
    }

    public void bindUi() {
        RxView.clicks(mDataBinding.fab)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDataBinding.fab.setExpanded(true));
        RxView.clicks(mDataBinding.scrim)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDataBinding.fab.setExpanded(false));
        RxView.clicks(mEmptyBinding.emptyImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    mDataBinding.swipeLayout.setRefreshing(true);
                    initRequest();
                });
        RxView.clicks(mDataBinding.startLive3Btn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    if (TCUtils.checkRecordPermission(getActivity())) {
                        Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), LiveRecordActivity.class));
                    }
                });

        mDataBinding.swipeLayout.setOnRefreshListener(() -> mViewModel.onRefresh());
    }

    public void subscribeUi() {
        // 收到列表数据返回
        LiveEventBus.get(RequestTags.LIVEROOMLIST_REQ, BaseResponBean.class)
                .observe(this, baseResponBean -> {
                    mDataBinding.swipeLayout.setRefreshing(false);
                    mViewModel.getListData().clear();
                    mViewModel.getListData().addAll((List<TCVideoInfo>) baseResponBean.getData());
                });
    }

    @Override
    public void initRequest() {
        mViewModel.onRefresh();
    }

}

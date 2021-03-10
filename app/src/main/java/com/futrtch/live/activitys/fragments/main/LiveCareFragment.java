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
import com.futrtch.live.databinding.FragmentLiveCareBinding;
import com.futrtch.live.databinding.FragmentLiveListBinding;
import com.futrtch.live.databinding.LayoutEmptyListBinding;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.LiveCareViewModel;
import com.futrtch.live.mvvm.vm.LiveCareViewModelFactory;
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
    LiveCareViewModel mViewModel;

    FragmentLiveCareBinding mDataBinding;
    LayoutEmptyListBinding mEmptyBinding; // 页面为空

    public static LiveCareFragment getInstance(int index){
        LiveCareFragment fragment = new LiveCareFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_care, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new LiveCareViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(LiveCareViewModel.class);
    }

    @Override
    public void init() {
        mAdapter = new LiveListAdapter(R.layout.listview_video_item, getActivity(), mViewModel.getListData());
        mDataBinding.grid.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mDataBinding.grid.addItemDecoration(new GridSectionAverageGapItemDecoration(10, 10, 20, 15));
        mDataBinding.grid.setAdapter(mAdapter);

        mEmptyBinding = (LayoutEmptyListBinding) new ViewsBuilder()
                .setParent(mDataBinding.grid)
                .setInflater(getLayoutInflater())
                .setLayoutId(R.layout.layout_empty_list)
                .setAttachToParent(false)
                .build()
                .getDataBinding();

        mAdapter.setEmptyView(mEmptyBinding.getRoot());
    }

    public void bindUi() {
        RxView.clicks(mEmptyBinding.emptyRly)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    mDataBinding.swipeLayout.setRefreshing(true);
                    initRequest();
                });
        mDataBinding.swipeLayout.setOnRefreshListener(() -> mViewModel.onRefresh());
    }

    public void subscribeUi() {
        // 收到列表数据返回
        LiveEventBus.get(RequestTags.LIVECARELIST_REQ, BaseResponBean.class)
                .observe(this, baseResponBean -> {
                    mDataBinding.swipeLayout.setRefreshing(false);
                    mViewModel.getListData().clear();
                    if(baseResponBean != null && baseResponBean.getData() != null) {
                        mViewModel.getListData().addAll((List<TCVideoInfo>) baseResponBean.getData());
                    }
                    mAdapter.notifyDataSetChanged();
                });
    }

    @Override
    public void initRequest() {
        mViewModel.onRefresh();
    }

    @Override
    public void releaseBinding() {
        releaseBindingList(mDataBinding, mEmptyBinding);
    }

}

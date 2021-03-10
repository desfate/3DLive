package com.futrtch.live.activitys.fragments.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.futrtch.live.R;
import com.futrtch.live.activitys.LivePlayActivity;
import com.futrtch.live.activitys.LiveRecordActivity;
import com.futrtch.live.adapters.BannerImageAdapter;
import com.futrtch.live.adapters.LiveListAdapter;
import com.futrtch.live.anims.CustomAnimDown;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.configs.Constants;
import com.futrtch.live.databinding.FragmentLiveListBinding;
import com.futrtch.live.databinding.LayoutBannerBinding;
import com.futrtch.live.databinding.LayoutEmptyListBinding;
import com.futrtch.live.databinding.ListviewVideoItemBinding;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.LiveListViewModel;
import com.futrtch.live.mvvm.vm.LiveListViewModelFactory;
import com.futrtch.live.mvvm.vm.LiveReplayViewModel;
import com.futrtch.live.tencent.common.utils.TCConstants;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.live.TCVideoInfo;
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
public class LiveListFragment extends MVVMFragment {

    public final static int START_LIVE_PLAY = 10090;

    LiveListAdapter mAdapter;
    BannerImageAdapter mBannerAdpater;
    LiveListViewModel mViewModel;

    FragmentLiveListBinding mDataBinding;
    LayoutBannerBinding mBannerBinding;  // banner 图
    LayoutEmptyListBinding mEmptyBinding; // 页面为空

    public static LiveListFragment getInstance(int index){
        LiveListFragment fragment = new LiveListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

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
        mBannerBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.layout_banner, mDataBinding.grid, false);
        mBannerBinding = (LayoutBannerBinding) new ViewsBuilder()
                .setParent(mDataBinding.grid)
                .setInflater(getLayoutInflater())
                .setLayoutId(R.layout.layout_banner)
                .setAttachToParent(false)
                .build()
                .getDataBinding();
        mEmptyBinding = (LayoutEmptyListBinding) new ViewsBuilder()
                .setParent(mDataBinding.grid)
                .setInflater(getLayoutInflater())
                .setLayoutId(R.layout.layout_empty_list)
                .setAttachToParent(false)
                .build()
                .getDataBinding();
        mBannerAdpater = new BannerImageAdapter(mViewModel.getmBannerBean());
        mBannerAdpater.setDatas(mViewModel.getmBannerBean());
        mBannerBinding.banner.setAdapter(mBannerAdpater);
        mAdapter = new LiveListAdapter(R.layout.listview_video_item, getActivity(), mViewModel.getListData());
        mAdapter.setHeaderView(mBannerBinding.getRoot());
        mAdapter.setEmptyView(mEmptyBinding.getRoot());
        mAdapter.setAdapterAnimation(new CustomAnimDown());
        mDataBinding.grid.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mDataBinding.grid.setAdapter(mAdapter);
    }

    public void bindUi() {
        RxView.clicks(mDataBinding.fab)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDataBinding.fab.setExpanded(true));
        RxView.clicks(mDataBinding.scrim)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDataBinding.fab.setExpanded(false));
        RxView.clicks(mEmptyBinding.emptyRly)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    mViewModel.getIsRefresh().postValue(true);
                    initRequest();
                });
        RxView.clicks(mDataBinding.startLive2Btn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    if (TCUtils.checkRecordPermission(getActivity())) {
                        Intent M2dIntent = new Intent(getActivity(), LiveRecordActivity.class);
                        M2dIntent.putExtra(Constants.INTENT_LIVE_TYPE, Constants.LIVE_TYPE_2D);
                        Objects.requireNonNull(getActivity()).startActivity(M2dIntent);
                    }
                });
        RxView.clicks(mDataBinding.startLive3Btn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    if (TCUtils.checkRecordPermission(getActivity())) {
                        Intent M2dIntent = new Intent(getActivity(), LiveRecordActivity.class);
                        M2dIntent.putExtra(Constants.INTENT_LIVE_TYPE, Constants.LIVE_TYPE_3D);
                        Objects.requireNonNull(getActivity()).startActivity(M2dIntent);
                    }
                });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            TCVideoInfo info = mViewModel.getListData().get(position);
            ListviewVideoItemBinding binding = DataBindingUtil.getBinding(view);
            if(binding == null || info == null) return;
            Intent intent = new Intent(getActivity(), LivePlayActivity.class);
            intent.putExtra("btn", info.frontCover);
            intent.putExtra(TCConstants.PUSHER_ID, info.userId !=null?info.userId :"");
            intent.putExtra(TCConstants.PUSHER_NAME, TextUtils.isEmpty(info.nickname) ? info.userId : info.nickname);
            intent.putExtra(TCConstants.PUSHER_AVATAR, info.avatar);
            intent.putExtra(TCConstants.HEART_COUNT, "" + info.likeCount);
            intent.putExtra(TCConstants.MEMBER_COUNT, "" + info.viewerCount);
            intent.putExtra(TCConstants.GROUP_ID, info.groupId);
            intent.putExtra(TCConstants.PLAY_TYPE, info.livePlay);
            intent.putExtra(TCConstants.FILE_ID, info.fileId !=null?info.fileId :"");
            intent.putExtra(TCConstants.COVER_PIC, info.frontCover);
            intent.putExtra(TCConstants.TIMESTAMP, info.createTime);
            intent.putExtra(TCConstants.ROOM_TITLE, info.title);
//            Pair<View,String> pair1 = new Pair<>((View)binding.anchorBtnCover, ViewCompat.getTransitionName(binding.anchorBtnCover));
//            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()), pair1).toBundle();
            getActivity().startActivity(intent);
        });
        mDataBinding.swipeLayout.setOnRefreshListener(() -> mViewModel.onRefresh());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void subscribeUi() {
        // 收到列表数据返回
        LiveEventBus.get(RequestTags.LIVEROOMLIST_REQ, BaseResponBean.class)
                .observe(this, baseResponBean -> {
                    if(baseResponBean != null && baseResponBean.getData() != null){
                        mViewModel.getIsRefresh().postValue(false);
                        mViewModel.getListData().clear();
                        mViewModel.getListData().addAll((List<TCVideoInfo>) baseResponBean.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                });
        mViewModel.getIsRefresh().observe(this, aBoolean -> mDataBinding.swipeLayout.setRefreshing(aBoolean));
    }

    @Override
    public void initRequest() {
        mViewModel.onRefresh();
    }

    @Override
    public void releaseBinding() {
        releaseBindingList(mBannerBinding, mDataBinding, mEmptyBinding);
    }

}

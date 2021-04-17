package com.futrtch.live.activitys.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.futrtch.live.R;
import com.futrtch.live.activitys.LivePlayActivity;
import com.futrtch.live.activitys.LiveRecordActivity;
import com.futrtch.live.activitys.LoginActivity;
import com.futrtch.live.activitys.VideoPlayActivity;
import com.futrtch.live.adapters.LiveListAdapter;
import com.futrtch.live.adapters.LiveReplayAdapter;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.databinding.FragmentLiveListBinding;
import com.futrtch.live.databinding.FragmentLiveReplayBinding;
import com.futrtch.live.databinding.LayoutEmptyListBinding;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.LiveReplayViewModel;
import com.futrtch.live.mvvm.vm.LiveReplayViewModelFactory;
import com.futrtch.live.mvvm.vm.VideoPlayViewModel;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.futrtch.live.utils.decoration.GridSectionAverageGapItemDecoration;
import com.futrtch.live.views.ViewsBuilder;
import com.futrtch.live.widgets.LoadingDialog;
import com.github.desfate.commonlib.tools.FileUtils;
import com.github.desfate.videokit.controls.VideoRequestControls;
import com.github.desfate.videokit.controls.VideoUploadControls;
import com.github.desfate.videokit.dates.VideoInfoDate;
import com.github.desfate.videokit.videoupload.TXUGCPublishTypeDef;
import com.jakewharton.rxbinding4.view.RxView;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 直播列表页面  主页 -> 推荐 -> 直播列表
 */
public class LiveReplayFragment extends MVVMFragment implements VideoUploadControls.ProgressCallBack {

    public final static int START_LIVE_PLAY = 10090;

    LiveReplayAdapter mAdapter;
    LiveReplayViewModel mViewModel;

    FragmentLiveReplayBinding mDataBinding;
    LayoutEmptyListBinding mEmptyBinding; // 页面为空

    private LoadingDialog.Builder mLoading; // 加载页面

    public static LiveReplayFragment getInstance(int index){
        LiveReplayFragment fragment = new LiveReplayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_replay, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new LiveReplayViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(LiveReplayViewModel.class);
        mViewModel.setUploadControls(new VideoUploadControls(getActivity(), this));
    }

    @Override
    public void init() {
        mLoading = new LoadingDialog.Builder(getActivity());
        mLoading.setMessage(getString(R.string.login_loading_text));
        mLoading.setType(LoadingDialog.Builder.PROGRESS);
        mLoading.create();

        mEmptyBinding = (LayoutEmptyListBinding) new ViewsBuilder()
                .setParent(mDataBinding.grid)
                .setInflater(getLayoutInflater())
                .setLayoutId(R.layout.layout_empty_list)
                .setAttachToParent(false)
                .build()
                .getDataBinding();

        mAdapter = new LiveReplayAdapter(R.layout.listview_video_item, getActivity(), mViewModel.getListData());
        mAdapter.setEmptyView(mEmptyBinding.getRoot());
        mDataBinding.grid.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mDataBinding.grid.addItemDecoration(new GridSectionAverageGapItemDecoration(10, 10, 20, 15));
        mDataBinding.grid.setAdapter(mAdapter);
    }

    public void bindUi() {
        RxView.clicks(mEmptyBinding.emptyRly)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    mDataBinding.swipeLayout.setRefreshing(true);
                    initRequest();
                });

        RxView.clicks(mDataBinding.fab)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    mViewModel.chooseVideo(Objects.requireNonNull(getActivity()));
                });

        mDataBinding.swipeLayout.setOnRefreshListener(() -> mViewModel.onRefresh());

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            VideoInfoDate info = mViewModel.getListData().get(position);
            if(info.getVideoPlayUrl() != null){
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtra("play_url", info.getVideoPlayUrl());
                startActivity(intent);
            }
        });
    }

    public void subscribeUi() {
        // 收到列表数据返回
        LiveEventBus.get(RequestTags.REPLAYLIST_REQ, BaseResponBean.class)
                .observe(this, baseResponBean -> {
                    mDataBinding.swipeLayout.setRefreshing(false);
                    mViewModel.getListData().clear();
                    mViewModel.getListData().addAll((List<VideoInfoDate>) baseResponBean.getData());
                    mAdapter.notifyDataSetChanged();
                });

        mViewModel.getUrl().observe(this, s -> {
            mLoading.setMessage("上传中, 请稍后");
            mLoading.getObj().show();
            mViewModel.getUploadControls().beginUpload(s);
        });

        LiveEventBus.get(RequestTags.SELECT_URL, Uri.class)
                .observe(this, baseResponBean -> {
                    mViewModel.getUrl().postValue(FileUtils.getRealPathFromUri(getActivity(), baseResponBean));
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

    @Override
    public void onProgress(long upload, long total) {
        if(mLoading != null) mLoading.setProgress(upload * 100 / total);
    }

    @Override
    public void success(TXUGCPublishTypeDef.TXPublishResult result) {
        Optional.ofNullable(mLoading).ifPresent(builder -> mLoading.getObj().dismiss());  // 取消 Loading
    }
}

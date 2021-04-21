package com.futrtch.live.activitys.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.futrtch.live.R;
import com.futrtch.live.activitys.VideoListPlayActivity;
import com.futrtch.live.adapters.LiveReplayAdapter;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.databinding.FragmentLiveReplayBinding;
import com.futrtch.live.databinding.LayoutEmptyListBinding;
import com.futrtch.live.databinding.LayoutUploadDialogBinding;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.repository.LoginRepository;
import com.futrtch.live.mvvm.vm.LiveReplayViewModel;
import com.futrtch.live.mvvm.vm.LiveReplayViewModelFactory;
import com.futrtch.live.utils.ThumbUtils;
import com.futrtch.live.utils.ToastUtil;
import com.futrtch.live.utils.decoration.GridSectionAverageGapItemDecoration;
import com.futrtch.live.views.BottomDialog;
import com.futrtch.live.views.ViewsBuilder;
import com.futrtch.live.widgets.LoadingDialog;
import com.github.desfate.commonlib.tools.FileUtils;
import com.github.desfate.videokit.controls.VideoUploadControls;
import com.github.desfate.videokit.dates.VideoInfoDate;
import com.github.desfate.videokit.videoupload.TXUGCPublishTypeDef;
import com.jakewharton.rxbinding4.view.RxView;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 直播列表页面  主页 -> 推荐 -> 直播列表
 */
public class LiveReplayFragment extends MVVMFragment implements VideoUploadControls.ProgressCallBack {

    public final static int START_LIVE_PLAY = 10090;

    LiveReplayAdapter mAdapter;
    LiveReplayViewModel mViewModel;

    FragmentLiveReplayBinding mDataBinding;
    LayoutEmptyListBinding mEmptyBinding; // 页面为空

    LayoutUploadDialogBinding mUploadBinding; // 上传dialog布局
    BottomDialog mDialog;

    private LoadingDialog.Builder mLoading; // 加载页面

    public static LiveReplayFragment getInstance(int index) {
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
        mUploadBinding = DataBindingUtil.inflate(inflater, R.layout.layout_upload_dialog, null, false);
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
        mDialog = new BottomDialog(Objects.requireNonNull(getActivity()), mUploadBinding);

        mLoading = new LoadingDialog.Builder(getActivity());
        mLoading.setMessage(getString(R.string.upload_text));
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
        mDataBinding.grid.setLayoutManager(new GridLayoutManager(getActivity(), 1));
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

        // 上传dialog

        RxView.clicks(mUploadBinding.chatBtn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    if (mUploadBinding.seachTv.getText().toString().trim().length() == 0) {
                        ToastUtil.showToast(getActivity(), "请填写视频名称");
                        return;
                    }
                    mLoading.setMessage("上传中, 请稍后");
                    mLoading.getObj().show();

                    mViewModel.getUploadControls().beginUpload(
                            mViewModel.getUrl().getValue(),
                            mViewModel.getThumbUrl(),
                            LoginRepository.getInstance().getLoginInfo().getmUserName() + " " + mUploadBinding.searchInput.getText().toString().trim());

                });

        mDataBinding.swipeLayout.setOnRefreshListener(() -> mViewModel.onRefresh());

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            VideoInfoDate info = mViewModel.getListData().get(position);
            if (info.getVideoPlayUrl() != null) {
//                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
//                intent.putExtra("play_url", info.getVideoPlayUrl());
//                startActivity(intent);

//                // 现在直接跳转类抖音播放器
                Intent intent = new Intent(getActivity(), VideoListPlayActivity.class);
                intent.putExtra("current_position", position);
                Bundle bu = new Bundle();
                bu.putParcelableArrayList("video_list", (ArrayList<? extends Parcelable>) mViewModel.getListData());
                intent.putExtras(bu);
                startActivity(intent);

            }
        });
    }

    public void subscribeUi() {
        // 收到列表数据返回
        LiveEventBus.get(RequestTags.REPLAYLIST_REQ, BaseResponBean.class)
                .observe(this, baseRespondBean -> {
                    mDataBinding.swipeLayout.setRefreshing(false);
                    mViewModel.getListData().clear();
                    mViewModel.getListData().addAll((List<VideoInfoDate>) baseRespondBean.getData());
                    mAdapter.notifyDataSetChanged();
                });

        mViewModel.getUrl().observe(this, s -> {
            mDialog.showDialog();
            Observable.create((ObservableOnSubscribe<String>) emitter -> emitter.onNext(ThumbUtils.getThumbFromVideo(mViewModel.getUrl().getValue(), Objects.requireNonNull(getActivity()))))
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                    .subscribe(s1 -> {
                        mViewModel.setThumbUrl(s1);
                        Glide.with(getActivity())
                                .load(s1)
                                .skipMemoryCache(true)//跳过内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE)//不要在disk硬盘缓存
                                .into(mUploadBinding.thumbView);
                    });
        });

        LiveEventBus.get(RequestTags.SELECT_URL, Uri.class)
                .observe(this, baseRespondBean -> {
                    mViewModel.getUrl().postValue(FileUtils.getRealPathFromUri(getActivity(), baseRespondBean));
                });
    }

    @Override
    public void initRequest() {
        mViewModel.onRefresh();
        mDataBinding.swipeLayout.setRefreshing(true);
    }

    @Override
    public void releaseBinding() {
        releaseBindingList(mDataBinding, mEmptyBinding);
    }

    @Override
    public void onProgress(long upload, long total) {
        if (mLoading != null) mLoading.setProgress(upload * 100 / total);
    }

    @Override
    public void success(TXUGCPublishTypeDef.TXPublishResult result) {
        if (result.retCode == 1015) {
            ToastUtil.showToast(getActivity(), "文件名中不能包含 / : * ? \" < >");
        }
        ToastUtil.showToast(getActivity(), "视频上传成功");
        Optional.ofNullable(mLoading).ifPresent(builder -> mLoading.getObj().dismiss());  // 取消 Loading
        mDialog.cancel();
        initRequest();
    }
}

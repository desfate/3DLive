package com.futrtch.live.activitys.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.transition.Explode;
import androidx.transition.Slide;
import androidx.transition.TransitionSet;

import com.futrtch.live.R;
import com.futrtch.live.activitys.LiveRecordActivity;
import com.futrtch.live.adapters.BannerImageAdapter;
import com.futrtch.live.adapters.LiveListAdapter;
import com.futrtch.live.beans.BannerBean;
import com.futrtch.live.databinding.FragmentLiveListBinding;
import com.futrtch.live.mvvm.vm.LiveListViewModel;
import com.futrtch.live.mvvm.vm.LiveListViewModelFactory;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.futrtch.live.utils.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 直播列表页面
 */
public class LiveListFragment extends Fragment {

    public static final int START_LIVE_PLAY = 100;

    FragmentLiveListBinding mDataBinding;
    LiveListAdapter mAdapter;
    LiveListViewModel mViewModel;
    BannerImageAdapter mBannerAdpater;

    public LiveListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_list, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewModelProvider.Factory factory = new LiveListViewModelFactory(getActivity().getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(LiveListViewModel.class);

        mAdapter = new LiveListAdapter(getActivity(), new DiffUtil.ItemCallback<TCVideoInfo>() {
            @Override
            public boolean areItemsTheSame(@NonNull TCVideoInfo oldItem, @NonNull TCVideoInfo newItem) {
                return oldItem.userId.equals(newItem.userId);
            }

            @Override
            public boolean areContentsTheSame(@NonNull TCVideoInfo oldItem, @NonNull TCVideoInfo newItem) {
                return oldItem.userId.equals(newItem.userId);
            }
        }, this::startPostponedEnterTransition);
        List<BannerBean> mBannerList = new ArrayList<>();
        mBannerList.add(new BannerBean(R.mipmap.banner1, ""));
        mBannerList.add(new BannerBean(R.mipmap.banner2, ""));
        mBannerAdpater = new BannerImageAdapter(mBannerList);
        mAdapter.setHeader(mBannerAdpater, this); // 有头部轮播图
        if (savedInstanceState != null) {
            mAdapter.restoreInstanceState(savedInstanceState);
        }

        if (mAdapter.expectsTransition()) {
            postponeEnterTransition(500, TimeUnit.MILLISECONDS);
        }

        int padding = 4;
        ViewCompat.setOnApplyWindowInsetsListener((View) view.getParent(), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {

                mDataBinding.grid.setPadding(padding + insets.getSystemWindowInsetLeft()
                        , 0
                        , padding + insets.getSystemWindowInsetRight()
                        , padding + insets.getSystemWindowInsetBottom());
                return insets;
            }
        });

        ((GridLayoutManager)mDataBinding.grid.getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mAdapter.getItemViewType(position);
                if(itemViewType == LiveListAdapter.HEAD_TYPE)
                    return  ((GridLayoutManager)mDataBinding.grid.getLayoutManager()).getSpanCount();
                else
                    return 1;
            }
        });



        mDataBinding.grid.addItemDecoration(
                new SpaceDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_tiny))
        );
        mDataBinding.grid.setAdapter(mAdapter);

        mDataBinding.fab.setOnClickListener(v -> mDataBinding.fab.setExpanded(true));

        mDataBinding.scrim.setOnClickListener(v -> mDataBinding.fab.setExpanded(false));

        mDataBinding.startLive3Btn.setOnClickListener(v -> {
            if (TCUtils.checkRecordPermission(getActivity())) {
                getActivity().startActivity(new Intent(getActivity(), LiveRecordActivity.class));
            }
        });

        setTransition();  // 设置
        subscribeUi();
    }

    private void subscribeUi(){
        // 绑定列表数据
        mViewModel.getLiveListData().observe(this, tcVideoInfo -> mAdapter.submitList(tcVideoInfo));
    }

    private void setTransition() {
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.setDuration(150L);
        transitionSet.setInterpolator(PathInterpolatorCompat.create(0.4f, 0f, 1f, 1f));
        // The app bar.
        Slide slide = new Slide(Gravity.TOP);
        slide.setMode(Slide.MODE_OUT);
        slide.addTarget(R.id.app_bar);
        transitionSet.addTransition(slide);
        // The grid items.
        Explode explode = new Explode();
        explode.setMode(Explode.MODE_OUT);
        transitionSet.excludeTarget(R.id.root_live, true);

        transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
        setExitTransition(transitionSet);   // 设置除ShareElement外其它View的退出方式

        TransitionSet reenterTransition = new TransitionSet();
        reenterTransition.setDuration(150L);
        reenterTransition.setInterpolator(PathInterpolatorCompat.create(0f, 0f, 0.2f, 1f));

        // The app bar.
        Slide slideIn = new Slide(Gravity.TOP);
        slide.setMode(Slide.MODE_IN);
        slide.addTarget(R.id.app_bar);
        reenterTransition.addTransition(slideIn);

        // The grid items.
        Explode explodeIn = new Explode();
        explodeIn.setStartDelay(125L);
        explodeIn.setMode(Explode.MODE_IN);
        reenterTransition.excludeTarget(R.id.root_live, true);
        reenterTransition.setOrdering(TransitionSet.ORDERING_TOGETHER);
        setReenterTransition(reenterTransition);

        int fabMargin = getResources().getDimensionPixelSize(R.dimen.spacing_medium);
        ViewCompat.setOnApplyWindowInsetsListener(mDataBinding.rootLayout, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)mDataBinding.fab.getLayoutParams();
                layoutParams.leftMargin = fabMargin + insets.getSystemWindowInsetLeft();
                layoutParams.rightMargin = fabMargin + insets.getSystemWindowInsetRight();
                layoutParams.bottomMargin = fabMargin + insets.getSystemWindowInsetBottom();
                mDataBinding.fab.setLayoutParams(layoutParams);

                CoordinatorLayout.LayoutParams layoutParamSheet = (CoordinatorLayout.LayoutParams)mDataBinding.sheet.getLayoutParams();
                layoutParamSheet.leftMargin = fabMargin + insets.getSystemWindowInsetLeft();
                layoutParamSheet.rightMargin = fabMargin + insets.getSystemWindowInsetRight();
                layoutParamSheet.bottomMargin = fabMargin + insets.getSystemWindowInsetBottom();
                mDataBinding.sheet.setLayoutParams(layoutParamSheet);
                return insets;
            }
        });
    }



    /**
     * 数据恢复
     * @param outState 需要恢复的数据
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}

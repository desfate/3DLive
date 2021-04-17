package com.futrtch.live.mvvm.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.R;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.BannerBean;
import com.futrtch.live.mvvm.repository.LiveListRepository;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.github.desfate.videokit.controls.VideoRequestControls;
import com.github.desfate.videokit.dates.VideoInfoDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 直播列表的ViewModel
 */
public class LiveReplayViewModel extends ViewModel {

    MutableLiveData<Boolean> isRefresh = new MutableLiveData<>();

    List<VideoInfoDate> mListData = new ArrayList<>();
    List<BannerBean> mBannerBean = new ArrayList<>();
    LiveListRepository repository;

    LiveReplayViewModel(LiveListRepository repository) {
        this.repository = repository;
        mBannerBean.add(new BannerBean(R.mipmap.banner1, ""));
        mBannerBean.add(new BannerBean(R.mipmap.banner2, ""));
    }

    /**
     * 下拉刷新
     */
    public void onRefresh() {
        repository.getVideoList();
    }

    public List<BannerBean> getmBannerBean() {
        return mBannerBean;
    }

    public List<VideoInfoDate> getListData() {
        return mListData;
    }

    public MutableLiveData<Boolean> getIsRefresh() {
        return isRefresh;
    }
}

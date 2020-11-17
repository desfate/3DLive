package com.futrtch.live.mvvm.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.R;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.BannerBean;
import com.futrtch.live.mvvm.repository.LiveListRepository;
import com.futrtch.live.tencent.live.TCVideoInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 直播列表的ViewModel
 */
public class LiveCareViewModel extends ViewModel {

    MutableLiveData<Boolean> isRefresh = new MutableLiveData<>();

    List<TCVideoInfo> mListData = new ArrayList<>();
    List<BannerBean> mBannerBean = new ArrayList<>();
    LiveListRepository repository;

    LiveCareViewModel(LiveListRepository repository) {
        this.repository = repository;
        List<TCVideoInfo> tcVideoInfos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int map = -1;
            switch (new Random().nextInt(3)){
                case 0:
                    map = R.mipmap.liveroom1;
                    break;
                case 1:
                    map = R.mipmap.liveroom2;
                    break;
                case 2:
                    map = R.mipmap.liveroom3;
                    break;
            }
            tcVideoInfos.add(new TCVideoInfo(
                    "userId",
                    "groupId",
                    true,
                    5,
                    5,
                    "直播",
                    "url",
                    "fileId",
                    "nickname",
                    String.valueOf(map),
                    "here",
                    "",
                    "",
                    ""
            ));
        }
        mBannerBean.add(new BannerBean(R.mipmap.banner1, ""));
        mBannerBean.add(new BannerBean(R.mipmap.banner2, ""));
        bean.setData(tcVideoInfos);
    }
    BaseResponBean bean = new BaseResponBean(0,"");
    /**
     * 下拉刷新
     */
    public void onRefresh() {
        repository.getLiveCareList();
    }

    public List<BannerBean> getmBannerBean() {
        return mBannerBean;
    }

    public List<TCVideoInfo> getListData() {
        return mListData;
    }

    public MutableLiveData<Boolean> getIsRefresh() {
        return isRefresh;
    }
}

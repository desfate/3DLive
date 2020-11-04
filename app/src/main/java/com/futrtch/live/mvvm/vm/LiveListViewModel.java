package com.futrtch.live.mvvm.vm;

import androidx.lifecycle.ViewModel;

import com.futrtch.live.R;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.BannerBean;
import com.futrtch.live.mvvm.repository.LiveListRepository;
import com.futrtch.live.tencent.live.TCVideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播列表的ViewModel
 */
public class LiveListViewModel extends ViewModel {
    List<TCVideoInfo> mListData = new ArrayList<>();
    List<BannerBean> mBannerBean = new ArrayList<>();
    LiveListRepository repository;

    LiveListViewModel(LiveListRepository repository) {
        this.repository = repository;
        List<TCVideoInfo> tcVideoInfos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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
                    "",
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
        repository.getLiveRoomList();
//        LiveEventBus.get(RequestTags.LIVEROOMLIST_REQ, BaseResponBean.class)
//                .post(bean);
    }


    public List<BannerBean> getmBannerBean() {
        return mBannerBean;
    }

    public List<TCVideoInfo> getListData() {
        return mListData;
    }


}

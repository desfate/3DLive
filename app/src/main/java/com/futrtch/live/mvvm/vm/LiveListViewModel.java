package com.futrtch.live.mvvm.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.tencent.live.TCVideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播列表的ViewModel
 */
public class LiveListViewModel extends ViewModel {

    MutableLiveData<List<TCVideoInfo>> mLiveListData = new MutableLiveData<>();  // 直播间列表数据

    LiveListViewModel() {
        List<TCVideoInfo> tcVideoInfos = new ArrayList<>();
        for(int i = 0 ; i < 100; i ++) {

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
        mLiveListData.postValue(tcVideoInfos);
    }
    public MutableLiveData<List<TCVideoInfo>> getLiveListData() {
        return mLiveListData;
    }


}

package com.futrtch.live.mvvm.vm;

import androidx.lifecycle.ViewModel;

import com.futrtch.live.mvvm.repository.LiveListRepository;
import com.futrtch.live.tencent.live.TCVideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播列表的ViewModel
 */
public class LiveListViewModel extends ViewModel {


    List<TCVideoInfo> mListData = new ArrayList<>();

    LiveListRepository repository;

    LiveListViewModel(LiveListRepository repository) {
        this.repository = repository;
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
    }

    /**
     * 下拉刷新
     */
    public void onRefresh(){
        repository.getLiveRoomList();
    }


    public List<TCVideoInfo> getListData(){
        return mListData;
    }




}

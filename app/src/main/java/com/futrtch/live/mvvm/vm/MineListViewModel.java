package com.futrtch.live.mvvm.vm;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.databinding.FragmentMineListBinding;
import com.futrtch.live.mvvm.repository.MineRepository;
import com.futrtch.live.tencent.live.TCVideoInfo;

import java.util.ArrayList;
import java.util.List;

public class MineListViewModel extends ViewModel {

    MineRepository mineRepository;

    MutableLiveData<List<TCVideoInfo>> mineListData = new MutableLiveData<>();  // 直播间列表数据

    public MineListViewModel(MineRepository mineRepository){
        this.mineRepository = mineRepository;
    }

    public void prepare(Activity activity, FragmentMineListBinding mDataBinding) {
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
        mineListData.postValue(tcVideoInfos);
    }

    public MutableLiveData<List<TCVideoInfo>> getMineListData() {
        return mineListData;
    }
}

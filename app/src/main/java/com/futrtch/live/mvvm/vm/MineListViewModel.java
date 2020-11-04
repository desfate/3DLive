package com.futrtch.live.mvvm.vm;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.futrtch.live.databinding.FragmentMineListBinding;
import com.futrtch.live.mvvm.repository.MineRepository;
import com.futrtch.live.tencent.live.TCVideoInfo;

import java.util.ArrayList;
import java.util.List;

public class MineListViewModel extends ViewModel {

    MineRepository mineRepository;

    List<TCVideoInfo> mListData = new ArrayList<>();

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
        mListData.addAll(tcVideoInfos);
    }

    public List<TCVideoInfo> getmListData() {
        return mListData;
    }
}

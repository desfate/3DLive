package com.futrtch.live.mvvm.vm;

import android.app.Activity;
import android.graphics.Color;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.databinding.FragmentMineBinding;
import com.futrtch.live.mvvm.repository.MineRepository;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

public class MineViewModel extends ViewModel {

    MineRepository mineRepository;

    MutableLiveData<Integer> avgColor = new MutableLiveData<>();
    MutableLiveData<Float> trans = new MutableLiveData<>();
    MutableLiveData<List<TCVideoInfo>> mineListData = new MutableLiveData<>();  // 直播间列表数据

    public MineViewModel(MineRepository mineRepository){
        this.mineRepository = mineRepository;
    }


    public void prepare(Activity activity, FragmentMineBinding mDataBinding) {

        mDataBinding.appbar.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, i) -> {
            float percent = (float) (Math.abs(i * 1.0f) / appBarLayout.getTotalScrollRange());
            trans.postValue(percent);
        });

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

    /**
     * 修改颜色值透明度
     * @param color 颜色值
     * @param fraction 透明度
     * @return 修改后的颜色值
     */
    public int changeAlpha(int color, float fraction){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }


    public MutableLiveData<Integer> getAvgColor() {
        return avgColor;
    }

    public MutableLiveData<Float> getTitleColor() {
        return trans;
    }

    public MutableLiveData<List<TCVideoInfo>> getMineListData() {
        return mineListData;
    }
}

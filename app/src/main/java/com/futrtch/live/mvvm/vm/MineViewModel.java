package com.futrtch.live.mvvm.vm;

import android.app.Activity;
import android.graphics.Color;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.databinding.FragmentUserBinding;
import com.futrtch.live.mvvm.repository.MineRepository;
import com.futrtch.live.mvvm.repository.ViewPagerRepository;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

public class MineViewModel extends ViewModel {

    MineRepository mineRepository;                                              // 我的数据仓库
    ViewPagerRepository viewPagerRepository;                                    // viewPager 数据仓库

    MutableLiveData<Integer> avgColor = new MutableLiveData<>();                // 平均颜色
    MutableLiveData<Float> trans = new MutableLiveData<>();                     // 动态透明度
    MutableLiveData<List<TCVideoInfo>> mineListData = new MutableLiveData<>();  // 直播间列表数据
    MutableLiveData<String> userName = new MutableLiveData<>();  //                当前用户名


    public MineViewModel(MineRepository mineRepository){
        this.mineRepository = mineRepository;
        viewPagerRepository = ViewPagerRepository.getInstance();
    }


    public void prepare(Activity activity, FragmentUserBinding mDataBinding) {
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

    public List<Integer> getmIndex() { return viewPagerRepository.getIndexThree(); }

    public String[] getmTitles() { return viewPagerRepository.getMineTitles(); }

    public MutableLiveData<Integer> getAvgColor() {
        return avgColor;
    }

    public MutableLiveData<Float> getTitleColor() {
        return trans;
    }

    public MutableLiveData<String> getUserName() { return userName; }

    public MutableLiveData<List<TCVideoInfo>> getMineListData() {
        return mineListData;
    }
}

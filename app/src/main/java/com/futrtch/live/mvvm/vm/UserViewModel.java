package com.futrtch.live.mvvm.vm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.databinding.FragmentOtherBinding;
import com.futrtch.live.mvvm.repository.MineRepository;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserViewModel extends ViewModel {

    public final static String USER_ID = "user_id";

    MineRepository mineRepository;

    MutableLiveData<Integer> avgColor = new MutableLiveData<>();
    MutableLiveData<Float> trans = new MutableLiveData<>();
    MutableLiveData<List<TCVideoInfo>> mineListData = new MutableLiveData<>();  // 直播间列表数据
    MutableLiveData<String> userName = new MutableLiveData<>();  // 当前用户名

    String[] mTitles = new String[]{
            "作品", "动态", "喜欢"
    };

    List<Integer> mIndex = new ArrayList<>(Arrays.asList(0, 1, 2));

    public UserViewModel(MineRepository mineRepository){
        this.mineRepository = mineRepository;
    }

    public void getIntent(Intent intent){
        String value = intent.getStringExtra(USER_ID);
        userName.postValue(value);
    }


    public void prepare(Activity activity, FragmentOtherBinding mDataBinding) {

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

    public List<Integer> getmIndex() { return mIndex; }

    public String[] getmTitles() { return mTitles; }

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
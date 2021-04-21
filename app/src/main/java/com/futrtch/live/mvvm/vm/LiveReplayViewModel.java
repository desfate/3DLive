package com.futrtch.live.mvvm.vm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.R;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.BannerBean;
import com.futrtch.live.mvvm.repository.LiveListRepository;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.github.desfate.videokit.controls.VideoRequestControls;
import com.github.desfate.videokit.controls.VideoUploadControls;
import com.github.desfate.videokit.dates.VideoInfoDate;
import com.github.desfate.videokit.videoupload.TXUGCPublishTypeDef;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 回放列表的ViewModel
 */
public class LiveReplayViewModel extends ViewModel {

    MutableLiveData<Boolean> isRefresh = new MutableLiveData<>();
    MutableLiveData<String> url = new MutableLiveData<>();
    String thumbUrl = "";

    VideoUploadControls uploadControls;

    List<VideoInfoDate> mListData = new ArrayList<>();
    List<BannerBean> mBannerBean = new ArrayList<>();
    LiveListRepository repository;
    public final int FILE_RESULT_CODE = 10086;

    LiveReplayViewModel(LiveListRepository repository) {
        this.repository = repository;
        mBannerBean.add(new BannerBean(R.mipmap.banner1, ""));
        mBannerBean.add(new BannerBean(R.mipmap.banner2, ""));
    }

    public void chooseVideo(Activity activity){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*"); //设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Intent wrapIntent = Intent.createChooser(intent, null);
        activity.startActivityForResult(wrapIntent, FILE_RESULT_CODE);
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

    public MutableLiveData<String> getUrl() {
        return url;
    }

    public VideoUploadControls getUploadControls() {
        return uploadControls;
    }

    public void setUploadControls(VideoUploadControls uploadControls) {
        this.uploadControls = uploadControls;
    }

    public String getThumbUrl(){
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl){
        this.thumbUrl = thumbUrl;
    }
}

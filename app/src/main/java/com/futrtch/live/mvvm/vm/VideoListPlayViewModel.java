package com.futrtch.live.mvvm.vm;

import android.content.Intent;
import android.os.Parcelable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.desfate.videokit.dates.VideoInfoDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class VideoListPlayViewModel extends ViewModel {

    List<VideoInfoDate> playUrls = new ArrayList<>();
    private int currentPosition;
    private MutableLiveData<String> currentVideoName = new MutableLiveData<>();

    public List<VideoInfoDate> getPlayUrls() {
        return playUrls;
    }

    public void init(Intent intent){
        currentPosition = intent.getIntExtra("current_position", 0);
        playUrls.clear();
        playUrls.addAll(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getParcelableArrayList("video_list")));
        currentVideoName.postValue(playUrls.get(currentPosition).getVideoName());
    }

    public String getNextUrl(){
        if(currentPosition < playUrls.size() - 1){
            currentPosition ++;
            currentVideoName.postValue(playUrls.get(currentPosition).getVideoName());
        }else{
            currentPosition = 0;
            currentVideoName.postValue(playUrls.get(currentPosition).getVideoName());
        }
        return getPlayUrls().get(currentPosition).getVideoPlayUrl();
    }


    public int getCurrentPosition(){
        return currentPosition;
    }

    public void setCurrentPosition(int position){
        this.currentPosition = position;
    }

    public MutableLiveData<String> getCurrentVideoName(){
        return currentVideoName;
    }

}

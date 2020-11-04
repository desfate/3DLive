package com.futrtch.live.mvvm.vm;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TitleMessageViewModel extends ViewModel {

    public final static String MESSAGE_KEY = "message_key";

    public final static String FANS = "粉丝";
    public final static String ASSIST = "赞";
    public final static String MINE = "我的";
    public final static String DISCUSS = "评论";

    MutableLiveData<String> title = new MutableLiveData<>();

    public void prepare(Intent intent){
        String value = intent.getStringExtra(MESSAGE_KEY);
        title.postValue(value);
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }
}

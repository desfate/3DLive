package com.futrtch.live.mvvm.vm;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.beans.MessageBean;
import com.futrtch.live.databinding.FragmentMessageBinding;

import java.util.ArrayList;
import java.util.List;

public class MessageListViewModel extends ViewModel {


    MutableLiveData<List<MessageBean>> messageListData = new MutableLiveData<>();  // 直播间列表数据

    public MessageListViewModel(){

    }

    public void prepare(Activity activity, FragmentMessageBinding mDataBinding) {
        List<MessageBean> tcVideoInfos = new ArrayList<>();
        tcVideoInfos.add(new MessageBean(1,
                "你关注的直播开始了","11-11"));
        tcVideoInfos.add(new MessageBean(2,
                "官方推荐你喜欢的直播","11-11"));
        for(int i = 0 ; i < 5; i ++) {
            tcVideoInfos.add(new MessageBean(3,
                    "你好 哈喽","11-11"));
        }
        messageListData.postValue(tcVideoInfos);
    }

    public MutableLiveData<List<MessageBean>> getMessageListData() {
        return messageListData;
    }
}

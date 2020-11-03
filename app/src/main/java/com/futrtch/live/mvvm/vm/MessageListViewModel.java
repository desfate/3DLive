package com.futrtch.live.mvvm.vm;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.futrtch.live.beans.MessageBean;
import com.futrtch.live.databinding.FragmentMessageBinding;

import java.util.ArrayList;
import java.util.List;

public class MessageListViewModel extends ViewModel {
    List<MessageBean> mListData = new ArrayList<>();

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
        mListData.addAll(tcVideoInfos);
    }

    public List<MessageBean> getmListData() {
        return mListData;
    }
}

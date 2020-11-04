package com.futrtch.live.mvvm.vm;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.beans.MessageItemBean;

import java.util.ArrayList;
import java.util.List;

public class MessageViewModel extends ViewModel {

    public final static String KEY_MESSAGE = "key_message";

    public final static int LIVE_MESSAGE = 10000;
    public final static int SYS_MESSAGE = 10001;

    List<MessageItemBean> mListData = new ArrayList<>();
    MutableLiveData<Integer> mMessageType = new MutableLiveData<>();

    public MessageViewModel(){
        mListData.add(new MessageItemBean("直播美好奇妙夜正在直播",
                "2020-10-16 20:20",
                "美好奇妙夜开始啦，晚会群星璀璨，蔡徐坤，王源，吴亦凡，THE9等都将带来精彩表演，还有强大的直播创作者阵容，点击进入直播间，让你的爱豆陪你度过一个奇妙而又美好的夜晚，看直播最高还可抢万元红包哦！ 点击观看>>"));
        mListData.add(new MessageItemBean("直播美好奇妙夜正在直播",
                "2020-10-17 10:20",
                "美好奇妙夜开始啦，晚会群星璀璨，蔡徐坤，王源，吴亦凡，THE9等都将带来精彩表演，还有强大的直播创作者阵容，点击进入直播间，让你的爱豆陪你度过一个奇妙而又美好的夜晚，看直播最高还可抢万元红包哦！ 点击观看>>"));
        mListData.add(new MessageItemBean("直播美好奇妙夜正在直播",
                "2020-10-18 22:20",
                "美好奇妙夜开始啦，晚会群星璀璨，蔡徐坤，王源，吴亦凡，THE9等都将带来精彩表演，还有强大的直播创作者阵容，点击进入直播间，让你的爱豆陪你度过一个奇妙而又美好的夜晚，看直播最高还可抢万元红包哦！ 点击观看>>"));
        mListData.add(new MessageItemBean("直播美好奇妙夜正在直播",
                "2020-10-19 23:20",
                "美好奇妙夜开始啦，晚会群星璀璨，蔡徐坤，王源，吴亦凡，THE9等都将带来精彩表演，还有强大的直播创作者阵容，点击进入直播间，让你的爱豆陪你度过一个奇妙而又美好的夜晚，看直播最高还可抢万元红包哦！ 点击观看>>"));
        mListData.add(new MessageItemBean("直播美好奇妙夜正在直播",
                "2020-10-20 20:20",
                "美好奇妙夜开始啦，晚会群星璀璨，蔡徐坤，王源，吴亦凡，THE9等都将带来精彩表演，还有强大的直播创作者阵容，点击进入直播间，让你的爱豆陪你度过一个奇妙而又美好的夜晚，看直播最高还可抢万元红包哦！ 点击观看>>"));
        mListData.add(new MessageItemBean("直播美好奇妙夜正在直播",
                "2020-10-20 20:20",
                "美好奇妙夜开始啦，晚会群星璀璨，蔡徐坤，王源，吴亦凡，THE9等都将带来精彩表演，还有强大的直播创作者阵容，点击进入直播间，让你的爱豆陪你度过一个奇妙而又美好的夜晚，看直播最高还可抢万元红包哦！ 点击观看>>"));
        mListData.add(new MessageItemBean("直播美好奇妙夜正在直播",
                "2020-10-20 20:20",
                "美好奇妙夜开始啦，晚会群星璀璨，蔡徐坤，王源，吴亦凡，THE9等都将带来精彩表演，还有强大的直播创作者阵容，点击进入直播间，让你的爱豆陪你度过一个奇妙而又美好的夜晚，看直播最高还可抢万元红包哦！ 点击观看>>"));
        mListData.add(new MessageItemBean("直播美好奇妙夜正在直播",
                "2020-10-20 20:20",
                "美好奇妙夜开始啦，晚会群星璀璨，蔡徐坤，王源，吴亦凡，THE9等都将带来精彩表演，还有强大的直播创作者阵容，点击进入直播间，让你的爱豆陪你度过一个奇妙而又美好的夜晚，看直播最高还可抢万元红包哦！ 点击观看>>"));
        mListData.add(new MessageItemBean("直播美好奇妙夜正在直播",
                "2020-10-20 20:20",
                "美好奇妙夜开始啦，晚会群星璀璨，蔡徐坤，王源，吴亦凡，THE9等都将带来精彩表演，还有强大的直播创作者阵容，点击进入直播间，让你的爱豆陪你度过一个奇妙而又美好的夜晚，看直播最高还可抢万元红包哦！ 点击观看>>"));
        mListData.add(new MessageItemBean("直播美好奇妙夜正在直播",
                "2020-10-20 20:20",
                "美好奇妙夜开始啦，晚会群星璀璨，蔡徐坤，王源，吴亦凡，THE9等都将带来精彩表演，还有强大的直播创作者阵容，点击进入直播间，让你的爱豆陪你度过一个奇妙而又美好的夜晚，看直播最高还可抢万元红包哦！ 点击观看>>"));
        mListData.add(new MessageItemBean("直播美好奇妙夜正在直播",
                "2020-10-20 20:20",
                "美好奇妙夜开始啦，晚会群星璀璨，蔡徐坤，王源，吴亦凡，THE9等都将带来精彩表演，还有强大的直播创作者阵容，点击进入直播间，让你的爱豆陪你度过一个奇妙而又美好的夜晚，看直播最高还可抢万元红包哦！ 点击观看>>"));
    }

    public void prepare(Intent intent){
        int messageType = intent.getIntExtra(KEY_MESSAGE, 0);
        if(messageType != 0) mMessageType.postValue(messageType);
    }

    public  MutableLiveData<Integer> getMessageType(){
        return mMessageType;
    }

    public List<MessageItemBean> getmListData() {
        return mListData;
    }
}

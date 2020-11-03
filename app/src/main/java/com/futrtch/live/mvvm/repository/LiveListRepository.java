package com.futrtch.live.mvvm.repository;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.tencent.liveroom.MLVBLiveRoom;
import com.futrtch.live.tencent.services.LiveListService;
import com.jeremyliao.liveeventbus.LiveEventBus;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 直播列表数据仓库
 */
public class LiveListRepository {

    LifecycleOwner lifecycleOwner;
    MLVBLiveRoom liveRoom;
    Context context;

    public LiveListRepository(LifecycleOwner lifecycleOwner, Context context){
        this.lifecycleOwner = lifecycleOwner;
        this.context = context;
        liveRoom = MLVBLiveRoom.sharedInstance(context);
    }

    /**
     * 获取直播间信息列表
     */
    public void getLiveRoomList(){
        LiveListService.fetchLiveList(liveRoom)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
                .subscribe(listBaseResponBean -> {
                    LiveEventBus.get(RequestTags.LIVEROOMLIST_REQ, BaseResponBean.class)
                            .post(listBaseResponBean);
                });
    }



}
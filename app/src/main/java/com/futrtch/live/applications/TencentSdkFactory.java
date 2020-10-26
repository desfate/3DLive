package com.futrtch.live.applications;

import android.app.Application;

import com.futrtch.live.tencent.liveroom.MLVBLiveRoomImpl;
import com.tencent.rtmp.TXLiveBase;

/**
 * 腾讯云直播需要初始化的SDK
 */
public class TencentSdkFactory implements InitSDK {

    // 如何获取License? 请参考官网指引 https://cloud.tencent.com/document/product/454/34750
    public final static String LICENCE_URL = "http://license.vod2.myqcloud.com/license/v1/b7b707311bad39bfbca95d4ac21a2e97/TXLiveSDK.licence";
    public final static String LICENCE_KEY = "cacf0df6afb3b1f4b2ca251dbae44691";

    TencentSdkFactory(){

    }

    @Override
    public void initSDK(Application application) {
        if(application == null) return;
        // 必须：初始化 LiteAVSDK Licence。 用于直播推流鉴权。
        TXLiveBase.getInstance().setLicence(application, LICENCE_URL, LICENCE_KEY);
        // 必须：初始化 MLVB 组件
        MLVBLiveRoomImpl.sharedInstance(application);
    }
}

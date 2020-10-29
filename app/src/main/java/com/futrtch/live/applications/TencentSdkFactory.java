package com.futrtch.live.applications;

import android.app.Application;

import com.futrtch.live.tencent.liveroom.MLVBLiveRoomImpl;
import com.futrtch.live.tencent.login.TCUserMgr;
import com.tencent.rtmp.TXLiveBase;

/**
 * 腾讯云直播需要初始化的SDK
 */
public class TencentSdkFactory implements InitSDK {

    // 如何获取License? 请参考官网指引 https://cloud.tencent.com/document/product/454/34750
    public final static String LICENCE_URL =
            "http://license.vod2.myqcloud.com/license/v1/fbc229653a09ab6ec9e0d2f9d30db945/TXLiveSDK.licence";
    public final static String LICENCE_KEY = "7ebeb7a8f2cd241daf84cfa2e8a66191";

    TencentSdkFactory(){

    }

    @Override
    public void initSDK(Application application) {
        if(application == null) return;
        // 必须：初始化 LiteAVSDK Licence。 用于直播推流鉴权。
        TXLiveBase.getInstance().setLicence(application, LICENCE_URL, LICENCE_KEY);
//        // 必须：初始化 MLVB 组件
        MLVBLiveRoomImpl.sharedInstance(application);
        // 必须：初始化全局的 用户信息管理类，记录个人信息。
        TCUserMgr.getInstance().initContext(application);
    }
}

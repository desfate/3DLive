package com.futrtch.live.applications;

import androidx.multidex.MultiDexApplication;

import java.util.Optional;

import static com.futrtch.live.configs.LiveConfigs.liveSDKType;

/**
 * 直播Application
 */
public class LiveApplication extends MultiDexApplication {

    private final static String TAG = "LiveApplication";
    private InitSDK mSdk;

    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
    }

    /**
     * 根据LiveConfigs中配置的sdk类型  初始化sdk
     */
    private void initSDK() {
        switch (liveSDKType){
            case QINIU:
                mSdk = new QiniuSdkFactory();
                break;
            case TENCENT:
                mSdk = new TencentSdkFactory();
                break;
        }
        Optional.ofNullable(mSdk).ifPresent(initSDK -> initSDK.initSDK(LiveApplication.this));
    }






}

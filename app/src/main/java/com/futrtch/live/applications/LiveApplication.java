package com.futrtch.live.applications;

import androidx.multidex.MultiDexApplication;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tencent.bugly.crashreport.CrashReport;

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

        CrashReport.initCrashReport(getApplicationContext(), "b0aa43dc1c", false);  // 异常上报

        LiveEventBus
                .config()
                .autoClear(true) //                        配置在没有Observer关联的时候是否自动清除LiveEvent以释放内存
                .lifecycleObserverAlwaysActive(false);  // 激活状态（Started）可以实时收到消息，非激活状态（Stoped）无法实时收到消息
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

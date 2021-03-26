package com.futrtch.live.base;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tencent.bugly.crashreport.CrashReport;

public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        // bugly初始化
        CrashReport.initCrashReport(getApplicationContext(), "3048fae6fc", false);  // 异常上报

        // LiveDataBus 初始化
        LiveEventBus
                .config()
                .autoClear(true) //                        配置在没有Observer关联的时候是否自动清除LiveEvent以释放内存
                .lifecycleObserverAlwaysActive(false);  // 激活状态（Started）可以实时收到消息，非激活状态（Stoped）无法实时收到消息

        // xlog初始化
        XLog.init(LogLevel.ALL);
    }
}

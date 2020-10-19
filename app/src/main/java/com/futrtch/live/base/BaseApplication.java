package com.futrtch.live.base;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class BaseApplication extends Application {
    public BaseApplication() {
    }

    /**
     * 分包处理逻辑
     * @param base
     */
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

package com.futrtch.live.utils;


import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class StyleUtils {

    /**
     * 观看直播页面主题
     * @param activity
     */
    public static void initStyle(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}

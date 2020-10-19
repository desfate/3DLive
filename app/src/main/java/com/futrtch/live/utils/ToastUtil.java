package com.futrtch.live.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 用于管理全局Toast
 *
 */
public class ToastUtil {

    public static final int LONG_DELAY = 3500;
    public static final int SHORT_DELAY = 2000;

    private static Toast mToast;

    private static Handler mHandler = new Handler();
    private static Runnable mRunnable = new Runnable() {
        public void run() {
            mToast.cancel();
            mToast = null;
            mHandler.removeCallbacks(mRunnable);
        }
    };

    public static void showToast(Context context, String message, int duration) {
        mHandler.removeCallbacks(mRunnable);
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), message, duration);
        } else {
            mToast.setText(message);
        }
        mToast.show();
        mHandler.postDelayed(mRunnable, duration == Toast.LENGTH_SHORT ? LONG_DELAY : SHORT_DELAY);
    }

    public static void showToast(Context context, String message) {
        if (context == null)
            return;
        showToast(context, message, Toast.LENGTH_SHORT);
    }
}
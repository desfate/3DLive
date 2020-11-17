package com.futrtch.live.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * 用于管理全局Toast
 *
 */
public class ToastUtil {

    public static final int LONG_DELAY = 3500;
    public static final int SHORT_DELAY = 2000;
    public static final int CUS_DELAY = 1500;

    public static Toast mToast;

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

    public static void showToastGravity(Context context, String message, int duration, int gravity){
        mHandler.removeCallbacks(mRunnable);
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), message, duration);
            mToast.setGravity(gravity, 0, 0);
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

    public static void customToastExit(Context context, View view){
        showCustomToast(context, "", SHORT_DELAY, view);
    }

    public static void showCustomToast(Context context, String message, int duration, View view){
        mHandler.removeCallbacks(mRunnable);
        mToast = Toast.makeText(context.getApplicationContext(), message, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setView(view);
        mToast.show();
        mHandler.postDelayed(mRunnable, SHORT_DELAY);
    }

    public static Toast getCustomToast(Context context, String message,  View view){
        mToast = Toast.makeText(context.getApplicationContext(), message, (int)SHORT_DELAY);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setView(view);
        return mToast;
    }

}
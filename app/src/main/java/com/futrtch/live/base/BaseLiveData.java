package com.futrtch.live.base;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;

/**
 * 这个类用于解决LiveData的粘性事件问题
 * <p>
 * LiveData每个注册者  都会重新回调所有事件
 * 在网络回调时 这种问题会导致出现大量的异常
 *
 * 这里利用反射 强制设置为版本为-1 就一直是单次通知
 */
public class BaseLiveData<T> extends MutableLiveData<T> {
    private final static String TAG = "BaseLiveData";

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        hookVersion(this);
        super.observe(owner, observer);
    }

    private void hookVersion(BaseLiveData data) {
        Class<?> liveDataClass = LiveData.class;
        Field mVersion = null;
        try {
            mVersion = liveDataClass.getDeclaredField("mVersion");
            mVersion.setAccessible(true);
            if(data == null) return;
            int version = ((int) mVersion.get(data));
            Log.e(TAG, "hookVersion:LiveData.mVersion =  " + version);
            if (version != -1) {
                mVersion.set(data, -1);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
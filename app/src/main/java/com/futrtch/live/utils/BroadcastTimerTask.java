package com.futrtch.live.utils;

import androidx.lifecycle.LifecycleOwner;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

/**
 * 计时任务
 */
public class BroadcastTimerTask {

    long timeValue;
    TimeCallBack timeCallBack; //           时间变动回调
    LifecycleOwner lifecycleOwner;//        生命周期控制

    public BroadcastTimerTask(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * 开始计时
     */
    public void interval(Long timeValue, @NonNull TimeUnit unit) {
        Observable.interval(timeValue, unit)//                                               每次间隔1000 毫秒 发送一次任务
                .subscribeOn(AndroidSchedulers.mainThread())//                                      结果发还给主线程 因为大多数情况是进行页面刷新
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this.lifecycleOwner))) //              绑定创建类的生命周期
                .subscribe(aLong -> {
                    BroadcastTimerTask.this.timeValue = aLong;
                    Optional.ofNullable(timeCallBack).ifPresent(timeCallBack -> timeCallBack.onNext(aLong));//                   回调时间结果
                });
    }

    public void setTimeCallBack(TimeCallBack timeCallBack) {
        this.timeCallBack = timeCallBack;
    }

    public long getTimeValue() {
        return timeValue;
    }

    public interface TimeCallBack {
        void onNext(long time);
    }
}

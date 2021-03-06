package com.futrtch.live.base;

import androidx.lifecycle.LifecycleOwner;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 基础数据管理类
 * 这里一般只会有几种数据访问模式
 * 1. 网络数据请求
 * 2. 本地数据请求（数据库）
 * 3. 本地数据请求（content provider）
 * 4. 内存数据请求（某些单例 本地数据）
 */
public class BaseRepository {

    private String token;   //  建权 token
    private String userId;  //  当前登录的账号

    /**
     * 请求网络
     * @param flowable
     * @param <T>
     * @return
     */
    public <T> BaseHttpSubscriber<T> request(Flowable<BaseResponBean<T>> flowable){
        BaseHttpSubscriber<T> baseHttpSubscriber = new BaseHttpSubscriber<>(); //RxJava Subscriber回调
        flowable.subscribeOn(Schedulers.io()) //解决背压
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseHttpSubscriber);
        return baseHttpSubscriber;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

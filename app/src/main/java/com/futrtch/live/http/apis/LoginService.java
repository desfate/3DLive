package com.futrtch.live.http.apis;

import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.LoginResponBean;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    /**
     * 登录请求
     * @param requestBody 自己封装请求包
     * @return
     */
    @POST("/login")
    Flowable<BaseResponBean<LoginResponBean>> login(@Body RequestBody requestBody);

    /**
     * 注册请求
     * @param requestBody 自己封装请求包
     * @return
     */
    @POST("/register")
    Flowable<BaseResponBean> register(@Body RequestBody requestBody);
}

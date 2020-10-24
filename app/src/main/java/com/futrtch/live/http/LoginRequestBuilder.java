package com.futrtch.live.http;

import androidx.core.util.Pair;

import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.base.RequestBodyMaker;
import com.futrtch.live.base.RetrofitTools;
import com.futrtch.live.beans.AccountInfoBean;
import com.futrtch.live.beans.LoginResponBean;
import com.futrtch.live.http.apis.AccountService;
import com.futrtch.live.http.apis.LoginService;
import com.futrtch.live.tencent.common.utils.TCUtils;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.RequestBody;

public class LoginRequestBuilder {
    /**
     * 登录请求的被观察者
     *
     * @param userName 账号
     * @param passWord 密码
     * @return 被观察者
     */
    public static Flowable<BaseResponBean<LoginResponBean>> loginFlowable(String userName, String passWord) {
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("userid", userName);
        requestParam.put("password", TCUtils.md5(TCUtils.md5(passWord) + userName));
        return RetrofitTools.getInstance(LoginService.class)
                .login(RequestBodyMaker.getRequestBodyForParams(requestParam));
    }

    /**
     * 账户信息请求的被观察者
     *
     * @return 被观察者
     */
    public static Flowable<BaseResponBean<AccountInfoBean>> accountFlowable(String userName, String token) {
        Pair<RequestBody, String> requestInfo =
                RequestBodyMaker.getRequestBodyAndToken(null, userName, token);
        return RetrofitTools.getInstance(AccountService.class)
                .accountInfo(requestInfo.second, requestInfo.first); //                             请求账户信息
    }

    /**
     * 注册账号请求的被观察者
     *
     * @param userName 账号
     * @param passWord 密码
     * @return 被观察者
     */
    public static Flowable<BaseResponBean> registerFlowable(String userName, String passWord) {
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("userid", userName);
        requestParam.put("password", TCUtils.md5(TCUtils.md5(passWord) + userName));
        return RetrofitTools.getInstance(LoginService.class)
                .register(RequestBodyMaker.getRequestBodyForParams(requestParam));
    }



}

package com.futrtch.live.http.flowables;

import androidx.core.util.Pair;

import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.base.RequestBodyMaker;
import com.futrtch.live.base.RetrofitTools;
import com.futrtch.live.beans.AccountInfoBean;
import com.futrtch.live.http.apis.AccountService;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.RequestBody;

public class AccountReqFlowable {

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
     * 更新账户信息
     * @param bean AccountInfoBean
     * @param userName
     * @param token
     * @return 更新返回被觀察者
     */
    public static Flowable<BaseResponBean> updateAccountFlowable(AccountInfoBean bean, String userName, String token){
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("nickname", bean.getNickname());
        requestBody.put("avatar", bean.getAvatar());
        requestBody.put("sex", String.valueOf(bean.getSex()));
        requestBody.put("frontcover", bean.getFrontcover());
        Pair<RequestBody, String> requestInfo =
                RequestBodyMaker.getRequestBodyAndToken(requestBody, userName, token);
        return RetrofitTools.getInstance(AccountService.class)
                .updateInfo(requestInfo.second, requestInfo.first);
    }
}

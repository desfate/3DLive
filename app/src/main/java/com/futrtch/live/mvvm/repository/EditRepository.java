package com.futrtch.live.mvvm.repository;

import androidx.lifecycle.LifecycleOwner;

import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.AccountInfoBean;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.http.apis.AccountService;
import com.futrtch.live.http.flowables.AccountReqFlowable;
import com.futrtch.live.tencent.services.LiveListService;
import com.jeremyliao.liveeventbus.LiveEventBus;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class EditRepository {

    LifecycleOwner lifecycleOwner;

    public EditRepository(LifecycleOwner lifecycleOwner){
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * 编辑账户信息
     * @param userInfo 需要修改的用户信息
     */
    public void editAccountInfo(AccountInfoBean userInfo){
        AccountReqFlowable.updateAccountFlowable(userInfo,
                LoginRepository.getInstance().getUserId(),
                LoginRepository.getInstance().getToken())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
                .subscribe(listBaseResponBean -> LiveEventBus.get(RequestTags.EDIT_ACCOUNTINFO, BaseResponBean.class)
                        .post(listBaseResponBean));
    }
}

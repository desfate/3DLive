package com.futrtch.live.mvvm.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.futrtch.live.beans.LoginSaveBean;
import com.futrtch.live.http.flowables.AccountReqFlowable;
import com.futrtch.live.http.flowables.LoginReqFlowable;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.base.ApiException;
import com.futrtch.live.base.BaseRepository;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.AccountInfoBean;
import com.futrtch.live.beans.LoginResponBean;
import com.futrtch.live.tencent.liveroom.IMLVBLiveRoomListener;
import com.futrtch.live.tencent.liveroom.MLVBLiveRoom;
import com.futrtch.live.tencent.liveroom.roomutil.commondef.LoginInfo;
import com.futrtch.live.tencent.login.TCUserMgr;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tencent.rtmp.TXLog;

import java.util.Optional;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;


/**
 * 登录 数据仓库
 * 这个类是单例模式
 * 1.网络请求
 * 2.本地化数据
 */
public class LoginRepository extends BaseRepository {

    private final static String TAG = "LoginRepository";

    private final static String PREFERENCE_USERID = "userid";
    private final static String PREFERENCE_USERPWD = "userpwd";

    /**
     * 单例模式
     */
    @SuppressLint("StaticFieldLeak")
    private static volatile LoginRepository singleton = null;

    /**********************************     本地数据缓存    **************************************/
    private LoginResponBean mUserInfo = new LoginResponBean();  // 登录返回后 用户信息存在这
    private final LoginSaveBean loginSaveBean = new LoginSaveBean();  // 用于保存用户登录信息
    private TCUserMgr.CosInfo mCosInfo = new TCUserMgr.CosInfo();   // COS 存储的 sdkappid

    private Context mContext;              //                                       初始化一些组件需要使用

    /**
     * 初始化缓存数据
     */
    private void initData() {
        loadUserInfo(); //  是否有缓存账号数据
    }

    private void loadUserInfo() {
        if (mContext == null) return;
        TXLog.d(TAG, "xzb_process: load local user info");
        SharedPreferences settings = mContext.getSharedPreferences("TCUserInfo", Context.MODE_PRIVATE);
        loginSaveBean.setmUserId(settings.getString(PREFERENCE_USERID, ""));
        loginSaveBean.setmUserPwd(settings.getString(PREFERENCE_USERPWD, ""));
    }

    private void saveUserInfo() {
        if (mContext == null) return;
        TXLog.d(TAG, "xzb_process: save local user info");
        SharedPreferences settings = mContext.getSharedPreferences("TCUserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFERENCE_USERID, loginSaveBean.getmUserId());
        editor.putString(PREFERENCE_USERPWD, loginSaveBean.getmUserPwd());
        editor.apply();
    }

    /**
     * 登录请求
     *
     * @param userName 账号
     * @param passWord 密码
     */
    public void loginReq(LifecycleOwner lifecycleOwner, String userName, String passWord) {
        LoginReqFlowable.loginFlowable(userName, passWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<BaseResponBean<LoginResponBean>, Flowable<BaseResponBean<AccountInfoBean>>>) loginBean -> {
                    if (loginBean != null) { // 登录成功
                        Optional.ofNullable(loginBean.getData()).ifPresent(userInfo -> mUserInfo = userInfo); //                        保存返回的数据
                        if (loginBean.getMessage() != null) {
                            LiveEventBus.get(RequestTags.LOGIN_REQ, BaseResponBean.class)
                                    .post(new BaseResponBean<>(loginBean.getCode(), loginBean.getMessage()));         // 页面要处理的逻辑（注册返回）
                        }
                        if (loginBean.getCode() == 200
                                && loginBean.getData() != null
                                && loginBean.getData().getToken() != null
                                && loginBean.getData().getRoomservice_sign() != null
                                && loginBean.getData().getRoomservice_sign().getUserID() != null) {
                            setToken(loginBean.getData().getToken());  //                                              Token 保存到本地 用于后期请求鉴权
                            setUserId(loginBean.getData().getRoomservice_sign().getUserID());//                        UserId 保存到本地 当前登录的账号
                            initMLVB();//                                                                              初始化直播SDK
                            return AccountReqFlowable.accountFlowable(getUserId(), getToken()); //                     请求账户信息
                        } else {
                            return Flowable.error(new ApiException(loginBean.getCode(), loginBean.getMessage()));  // 抛出登录异常  不会继续链式调用
                        }
                    }
                    return Flowable.error(new ApiException(-1, "网络异常"));  // 抛出登录异常  不会继续链式调用
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
                .subscribe(new DisposableSubscriber<BaseResponBean<AccountInfoBean>>() {
                    @Override
                    public void onNext(BaseResponBean<AccountInfoBean> accountBean) {
                        if (accountBean != null && accountBean.getCode() == 200) {  // 查询账户信息返回
                            if (accountBean.getData() != null) {
                                if (accountBean.getData().getAvatar() != null)
                                    loginSaveBean.setmUserAvatar(accountBean.getData().getAvatar());  //      保存用户头像信息
                                if (accountBean.getData().getNickname() != null)
                                    loginSaveBean.setmUserName(accountBean.getData().getNickname()); //       用户称呼
                                if (accountBean.getData().getFrontcover() != null)
                                    loginSaveBean.setmCoverPic(accountBean.getData().getFrontcover());//      直播封面？
                                if (accountBean.getData().getSex() >= 0) {
                                    loginSaveBean.setmSex(accountBean.getData().getSex());//                  用户性别
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof ApiException) {
                            Log.e("TAG", "request error" + ((ApiException) t).getStatusDesc());
                        } else {
                            Log.e("TAG", "request error" + t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    /**
     * 注册账号请求
     *
     * @param username 账户名
     * @param password 密码
     */
    public void registerReq(LifecycleOwner lifecycleOwner,String username, String password) {
        LoginReqFlowable.registerFlowable(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
                .subscribe(new DisposableSubscriber<BaseResponBean>() {
                    @Override
                    public void onNext(BaseResponBean registerBean) {
                        if (registerBean != null) {
                            LiveEventBus.get(RequestTags.REGISTER_REQ, BaseResponBean.class)
                                    .post(new BaseResponBean<>(registerBean.getCode(), registerBean.getMessage()));         // 页面要处理的逻辑（登录返回）
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 初始化直播SDK
     */
    public void initMLVB() {
        // 校验数据完整性
        if (mUserInfo == null || mContext == null
                || mUserInfo.getRoomservice_sign() == null
                || mUserInfo.getRoomservice_sign().getSdkAppID() == 0
                || mUserInfo.getRoomservice_sign().getUserID() == null
                || mUserInfo.getRoomservice_sign().getUserSig() == null) return;

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.sdkAppID = mUserInfo.getRoomservice_sign().getSdkAppID();
        loginInfo.userID = getUserId();
        loginInfo.userSig = mUserInfo.getRoomservice_sign().getUserSig();

        String userName = loginSaveBean.getmUserName();
        loginInfo.userName = !TextUtils.isEmpty(userName) ? userName : getUserId();
        loginInfo.userAvatar = loginSaveBean.getmUserAvatar();
        MLVBLiveRoom liveRoom = MLVBLiveRoom.sharedInstance(mContext);
        liveRoom.login(loginInfo, new IMLVBLiveRoomListener.LoginCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                Log.i(TAG, "MLVB init onError: errorCode = " + errInfo + " info = " + errInfo);
            }

            @Override
            public void onSuccess() {
                Log.i(TAG, "MLVB init onSuccess: ");
            }
        });
    }

    /**
     * 自动登录
     */
    public void autoLogin() {

    }

    public void setmContext(Context context) {
        this.mContext = context;
        initData();
    }

    public LoginSaveBean getLoginInfo(){
        return loginSaveBean;
    }

    public static LoginRepository getInstance() {
        if (singleton == null) {
            synchronized (LoginRepository.class) {
                if (singleton == null) {
                    singleton = new LoginRepository();
                }
            }
        }
        return singleton;
    }
}

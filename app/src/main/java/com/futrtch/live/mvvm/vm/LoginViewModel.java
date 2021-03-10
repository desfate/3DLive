package com.futrtch.live.mvvm.vm;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.beans.LoginSaveBean;
import com.futrtch.live.mvvm.repository.LoginRepository;
import com.futrtch.live.tencent.common.utils.TCUtils;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_PASSWORD_ERROR;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_SUCCESS_PASS;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_USERNAME_ERROR;

/**
 * 这里记录当前登录状态 为以后游客登录做准备
 */
public class LoginViewModel extends ViewModel {

    private final LoginRepository repository;
    private final LifecycleOwner lifecycleOwner;
    private final MutableLiveData<Integer> loginState = new MutableLiveData<>();  // 登录失败

    public LoginViewModel(LoginRepository repository,LifecycleOwner lifecycleOwner) {
        this.repository = repository;
        this.lifecycleOwner = lifecycleOwner;
        LoginRepository.getInstance().loadUserInfo();
    }

    /**
     * 登录行为
     *
     * @param userName 账号
     * @param passWord 密码
     */
    public void Login(String userName, String passWord) {
        if (checkInfo(userName, passWord)) {
            loginState.postValue(ERROR_CUSTOMER_SUCCESS_PASS);
            repository.loginReq(lifecycleOwner, userName, passWord);
        }
    }

    /**
     * 获取上次登录的账号密码
     */
    public LoginSaveBean loadUserInfo(){
        return LoginRepository.getInstance().getLoginInfo();
    }

    /**
     * 检测用户输入的账号密码是否合法
     *
     * @param userName 账号
     * @param passWord 密码
     * @return true：通过检测 false：未通过
     */
    private boolean checkInfo(String userName, String passWord) {
        if (!TCUtils.isUsernameVaild(userName)) {
            loginState.postValue(ERROR_CUSTOMER_USERNAME_ERROR);
            return false;
        }
        if (!TCUtils.isPasswordValid(passWord)) {
            loginState.postValue(ERROR_CUSTOMER_PASSWORD_ERROR);
            return false;
        }
        return true;
    }

    public LiveData<Integer> getLoginState() {
        return loginState;
    }


    public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        //如果使用我们的ui，没有申请模板编号的情况下需传null
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    // 国家代码，如“86”
                    String country = (String) phoneMap.get("country");
                    // 手机号码，如“13800138000”
                    String phone = (String) phoneMap.get("phone");
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else{
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }
}

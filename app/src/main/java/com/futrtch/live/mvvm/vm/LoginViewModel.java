package com.futrtch.live.mvvm.vm;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.mvvm.repository.LoginRepository;
import com.futrtch.live.tencent.common.utils.TCUtils;

import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_PASSWORD_ERROR;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_SUCCESS_PASS;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_USERNAME_ERROR;

/**
 * 这里记录当前登录状态 为以后游客登录做准备
 */
public class LoginViewModel extends ViewModel {

    private LoginRepository repository;
    private LifecycleOwner lifecycleOwner;
    private MutableLiveData<Integer> loginState = new MutableLiveData<>();  // 登录失败

    public LoginViewModel(LoginRepository repository,LifecycleOwner lifecycleOwner) {
        this.repository = repository;
        this.lifecycleOwner = lifecycleOwner;
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
}

package com.futrtch.live.mvvm.vm;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.mvvm.repository.LoginRepository;
import com.futrtch.live.tencent.common.utils.TCUtils;

import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_PASSWORD_ERROR;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_REPEAT_ERROR;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_SUCCESS_PASS;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_USERNAME_ERROR;

/**
 * 注册viewModel
 */
public class RegisterViewModel extends ViewModel {

    LoginRepository loginRepository;
    LifecycleOwner lifecycleOwner;

    private MutableLiveData<Integer> registerState = new MutableLiveData<>();  // 注册状态


    RegisterViewModel(LoginRepository loginRepository, LifecycleOwner lifecycleOwner){
        this.loginRepository = loginRepository;
        this.lifecycleOwner = lifecycleOwner;
    }


    public void register(String userName, String passWord, String repeatPassword){
        if(checkInfo(userName, passWord, repeatPassword)){  // 通过格式校验
            loginRepository.registerReq(lifecycleOwner, userName, passWord);
            registerState.postValue(ERROR_CUSTOMER_SUCCESS_PASS);  // 通过校验
        }
    }

    private boolean checkInfo(String userName, String passWord, String repeatPassword){
        if(!TCUtils.isUsernameVaild(userName)) {
            registerState.postValue(ERROR_CUSTOMER_USERNAME_ERROR);  // 用户名错误
            return false;
        }
        if(!TCUtils.isPasswordValid(passWord)) {
            registerState.postValue(ERROR_CUSTOMER_PASSWORD_ERROR); // 密码错误
            return false;
        }
        if(!passWord.equals(repeatPassword)){
            registerState.postValue(ERROR_CUSTOMER_REPEAT_ERROR);  // 两次密码不一致
            return false;
        }
        return true;
    }

    public void login(String username, String password){
        loginRepository.loginReq(lifecycleOwner ,username, password);
    }

    public MutableLiveData<Integer> getRegisterState(){
        return registerState;
    }
}

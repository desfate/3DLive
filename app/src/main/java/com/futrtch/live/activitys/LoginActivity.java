package com.futrtch.live.activitys;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.base.PermissionTools;
import com.futrtch.live.databinding.ActivityLoginBinding;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.mvvm.vm.LoginViewModel;
import com.futrtch.live.mvvm.vm.LoginViewModelFactory;
import com.futrtch.live.tencent.common.utils.TCErrorConstants;
import com.futrtch.live.utils.ActivityUtils;
import com.futrtch.live.utils.ToastUtil;
import com.futrtch.live.widgets.LoadingDialog;
import com.hjq.permissions.Permission;
import com.jakewharton.rxbinding4.view.RxView;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.Optional;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_PASSWORD_ERROR;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_SUCCESS_PASS;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_USERNAME_ERROR;

/**
 * 登录页面
 */
public class LoginActivity extends MVVMActivity {
    private static final String TAG = "LoginActivity";

    private LoadingDialog.Builder mLoading; // 加载页面
    private ActivityLoginBinding mDataBinding;
    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViewModel() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ViewModelProvider.Factory factory = new LoginViewModelFactory(getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
    }

    @Override
    public void init(){
        mLoading = new LoadingDialog.Builder(LoginActivity.this);
        mLoading.setMessage(getString(R.string.login_loading_text));
        mLoading.create();

        mDataBinding.userNameEdt.setText(mViewModel.loadUserInfo().getmUserId());
        mDataBinding.passwordEdt.setText(mViewModel.loadUserInfo().getmUserPwd());
    }

    @Override
    public void bindUi(){
        // 登录请求
        RxView.clicks(mDataBinding.loginBtn)
                .subscribeOn(AndroidSchedulers.mainThread())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit ->
                        PermissionTools.requestPermission(this, () ->             //                       校验读写权限
                                        mViewModel.Login(mDataBinding.userNameEdt.getText().toString().trim()  //  登录请求
                                                , mDataBinding.passwordEdt.getText().toString().trim())
                                , Permission.READ_PHONE_STATE));
        // 注册按钮
        RxView.clicks(mDataBinding.registerImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));  //  跳转注册页面

        RxView.clicks(mDataBinding.phoneImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mViewModel.sendCode(this));
    }

    /**
     * 不带粘性消息
     */
    @Override
    public void subscribeUi() {
        // 页面状态变化通知  带粘性消息
        mViewModel.getLoginState().observe(this, state -> {
            switch (state) {
                case ERROR_CUSTOMER_SUCCESS_PASS:   // 通过校验
                    mLoading.getObj().show();
                    break;
                case ERROR_CUSTOMER_PASSWORD_ERROR: // 账号错误
                case ERROR_CUSTOMER_USERNAME_ERROR: // 密码错误
                    mDataBinding.passwordEdt.setText("");  // 清空密码输入框
                    ToastUtil.showToast(this, TCErrorConstants.getErrorInfo(state));
                    break;
            }
        });

        // 登录信息返回通知
        LiveEventBus.get(RequestTags.LOGIN_REQ, BaseResponBean.class)
                .observe(this, bean -> {
                    Optional.ofNullable(mLoading).ifPresent(builder -> mLoading.getObj().dismiss());  // 取消 Loading
                    if (bean.getCode() == 200) { // 登录成功
                        ToastUtil.showToast(LoginActivity.this, "登录成功！");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {                     // 登录失败
                        ToastUtil.showToast(LoginActivity.this, "登录失败:" + TCErrorConstants.getErrorInfo(bean.getCode()));
                        mDataBinding.passwordEdt.setText("");  // 清空密码输入框
                    }
                });
    }

    @Override
    public void initRequest() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBinding.unbind();
        Optional.ofNullable(mLoading).ifPresent(builder -> mLoading.getObj().dismiss()); // 取消 Loading
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.finishAllActivity();
        super.onBackPressed();
    }
}

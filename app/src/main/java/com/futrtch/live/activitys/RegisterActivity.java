package com.futrtch.live.activitys;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.databinding.ActivityRegisterBinding;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.tencent.common.utils.TCErrorConstants;
import com.futrtch.live.utils.ToastUtil;
import com.futrtch.live.mvvm.vm.RegisterViewModel;
import com.futrtch.live.mvvm.vm.RegisterViewModelFactory;
import com.futrtch.live.widgets.LoadingDialog;
import com.jakewharton.rxbinding4.view.RxView;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.Optional;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_PASSWORD_ERROR;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_REPEAT_ERROR;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_SUCCESS_PASS;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_USERNAME_ERROR;

/**
 * 注册页面
 */
public class RegisterActivity extends AppCompatActivity {

    private final static String TAG = "RegisterActivity";

    LoadingDialog.Builder mLoading; // 加载页面
    ActivityRegisterBinding mDataBinding;
    RegisterViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        ViewModelProvider.Factory factory = new RegisterViewModelFactory(getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(RegisterViewModel.class);
        mLoading = new LoadingDialog.Builder(RegisterActivity.this);
        mLoading.setMessage(getString(R.string.register_loading));
        mLoading.create();

        // 点击上方关闭按钮
        RxView.clicks(mDataBinding.closeImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> finish());

        // 点击注册按钮
        RxView.clicks(mDataBinding.submitBtn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mViewModel.register(mDataBinding.registerUserNameEdt.getText().toString().trim()
                        , mDataBinding.registerPasswordEdt.getText().toString().trim()
                        , mDataBinding.repeatRegisterPasswordEdt.getText().toString().trim()));

        subscribeUi();
    }

    private void subscribeUi() {
        // 注册页面状态更变通知
        mViewModel.getRegisterState().observe(this, state -> {
            switch (state) {
                case ERROR_CUSTOMER_SUCCESS_PASS:
                    mLoading.getObj().show(); // 通过校验 开始网络请求
                    break;
                case ERROR_CUSTOMER_PASSWORD_ERROR: // 账号错误
                case ERROR_CUSTOMER_USERNAME_ERROR: // 密码错误
                case ERROR_CUSTOMER_REPEAT_ERROR: //   账号密码不一致
                    ToastUtil.showToast(this, TCErrorConstants.getErrorInfo(state));
                    break;

            }
        });

        // 注册接口回调通知
        LiveEventBus.get(RequestTags.REGISTER_REQ, BaseResponBean.class)
                .observe(this, bean -> {
                    Optional.ofNullable(mLoading).ifPresent(builder -> mLoading.getObj().dismiss());  // 取消 Loading
                    if (bean != null && bean.getCode() == 200) { //  注册成功 就开始自动登录
                        ToastUtil.showToast(RegisterActivity.this, "注册成功！");
                        mLoading.setMessage(getString(R.string.login_loading_text)).create().show();  // 显示登录中的loading
                        mViewModel.login(mDataBinding.registerUserNameEdt.getText().toString().trim() // 注册成功后 进行登录请求
                                , mDataBinding.registerPasswordEdt.getText().toString().trim());
                    } else {
                        ToastUtil.showToast(RegisterActivity.this, "注册失败:" + TCErrorConstants.getErrorInfo(bean.getCode()));
                    }
                });

        // 登录接口回调通知
        LiveEventBus.get(RequestTags.LOGIN_REQ, BaseResponBean.class)
                .observe(this, bean -> {
                    if (bean == null) return;
                    Optional.ofNullable(mLoading).ifPresent(builder -> mLoading.getObj().dismiss());  // 取消 Loading
                    if (bean.getCode() == 200) { // 登录成功
                        ToastUtil.showToast(this, "登录成功！");
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    } else {                     // 登录失败
                        ToastUtil.showToast(this, "登录失败:" + TCErrorConstants.getErrorInfo(bean.getCode()));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Optional.ofNullable(mLoading).ifPresent(builder -> mLoading.getObj().dismiss()); // 取消 Loading
    }
}

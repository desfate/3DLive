package com.futrtch.live.activitys;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivitySplashBinding;
import com.futrtch.live.mvvm.MVVMActivity;

import java.util.concurrent.TimeUnit;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 广告页面
 */
public class SplashActivity extends MVVMActivity {

    ActivitySplashBinding mDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
    }

    @Override
    public void initViewModel() {

    }

    @Override
    public void init(){
        Observable.timer(800, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(aLong -> jumpActivity());
    }

    @Override
    public void bindUi() {

    }

    @Override
    public void subscribeUi() {

    }

    @Override
    public void initRequest() {

    }

    private void jumpActivity(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        // 禁止使用返回键
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBinding.unbind();
    }
}

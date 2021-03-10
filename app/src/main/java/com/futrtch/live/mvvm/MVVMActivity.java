package com.futrtch.live.mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.futrtch.live.utils.ActivityUtils;

public abstract class MVVMActivity extends AppCompatActivity {

    public abstract void initViewModel();

    public abstract void init();

    public abstract void bindUi();

    public abstract void subscribeUi();

    /**
     * 请求网络数据
     */
    public abstract void initRequest();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityUtils.addActivity(this);
        super.onCreate(savedInstanceState);
        initViewModel();
        init();
        subscribeUi();
        initRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindUi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.finishActivity(this);
    }
}

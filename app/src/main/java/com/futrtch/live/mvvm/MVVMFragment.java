package com.futrtch.live.mvvm;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * MVVM框架的基类
 */
public abstract class MVVMFragment extends Fragment {

    /**
     * 初始化viewModel 放在这
     */
    public abstract void initViewModel();

    /**
     * 其他View的初始化 放在这
     */
    public abstract void init();

    /**
     * 控件的事件流 放在这
     */
    public abstract void bindUi();

    /**
     * 数据驱动的双向绑定  放在这
     */
    public abstract void subscribeUi();

    /**
     * 请求网络数据
     */
    public abstract void initRequest();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        init();
        subscribeUi();
        initRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        bindUi();
    }
}

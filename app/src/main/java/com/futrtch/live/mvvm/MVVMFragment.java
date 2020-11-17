package com.futrtch.live.mvvm;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import java.util.Optional;

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

    /**
     * dataBinding解绑
     */
    public abstract void releaseBinding();

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseBinding();
    }

    /**
     * 释放dataBinding
     * @param dataBindings 需要释放的列表
     */
    public void releaseBindingList(ViewDataBinding... dataBindings){
        if(dataBindings != null && dataBindings.length > 0) {
            for (ViewDataBinding binding : dataBindings) {
                Optional.ofNullable(binding).ifPresent(ViewDataBinding::unbind);
            }
        }
    }
}

package com.futrtch.live.activitys;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivityMessageTitleBinding;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.mvvm.vm.TitleMessageViewModel;
import com.futrtch.live.mvvm.vm.TitleMessageViewModelFactory;
import com.jakewharton.rxbinding4.view.RxView;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 消息标题 粉丝 赞 我的 评论
 */
public class MessageTitleActivity extends MVVMActivity {

    TitleMessageViewModel mViewModel;
    ActivityMessageTitleBinding mDataBinding;

    @Override
    public void initViewModel() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_message_title);
        ViewModelProvider.Factory factory = new TitleMessageViewModelFactory(getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(TitleMessageViewModel.class);
    }

    @Override
    public void init() {
        mViewModel.prepare(getIntent());
    }

    @Override
    public void bindUi() {
        RxView.clicks(mDataBinding.backImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> finish());
    }

    @Override
    public void subscribeUi() {
        mViewModel.getTitle().observe(this, s -> mDataBinding.editTitle.setText(s));
    }

    @Override
    public void initRequest() {

    }
}

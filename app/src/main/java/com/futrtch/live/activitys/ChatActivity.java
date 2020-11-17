package com.futrtch.live.activitys;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivityChatBinding;
import com.futrtch.live.databinding.LayoutChatDialogBinding;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.mvvm.vm.ChatViewModel;
import com.futrtch.live.mvvm.vm.ChatViewModelFactory;
import com.futrtch.live.views.BottomDialog;
import com.jakewharton.rxbinding4.view.RxView;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class ChatActivity extends MVVMActivity {

    ActivityChatBinding mDataBinding;
    LayoutChatDialogBinding mDialogBinding;
    ChatViewModel mViewModel;
    BottomDialog mDialog;

    @Override
    public void initViewModel() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        mDialogBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_chat_dialog, null , false);
        ViewModelProvider.Factory factory = new ChatViewModelFactory(getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(ChatViewModel.class);
    }

    @Override
    public void init() {
        mDialog = new BottomDialog(this, mDialogBinding);
    }

    @Override
    public void bindUi() {
        RxView.clicks(mDataBinding.backImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> finish());

        RxView.clicks(mDataBinding.moreImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDialog.showDialog());

        RxView.clicks(mDialogBinding.cancelTv)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDialog.cancel());


    }

    @Override
    public void subscribeUi() {

    }

    @Override
    public void initRequest() {

    }
}

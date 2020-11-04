package com.futrtch.live.activitys;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.futrtch.live.R;
import com.futrtch.live.adapters.MessageAdapter;
import com.futrtch.live.databinding.ActivityMessageBinding;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.mvvm.vm.MessageViewModel;
import com.futrtch.live.mvvm.vm.MessageViewModelFactory;
import com.jakewharton.rxbinding4.view.RxView;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 消息页面
 */
public class MessageActivity extends MVVMActivity {

    MessageViewModel mViewModel;
    ActivityMessageBinding mDataBinding;
    MessageAdapter mAdapter;

    @Override
    public void initViewModel() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        ViewModelProvider.Factory factory = new MessageViewModelFactory(getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(MessageViewModel.class);
        mViewModel.prepare(getIntent());
    }

    @Override
    public void init() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mAdapter = new MessageAdapter( this, R.layout.layout_message_sign, mViewModel.getmListData());
        mDataBinding.recyclerList.setAdapter(mAdapter);
        mDataBinding.recyclerList.setLayoutManager(manager);
    }

    @Override
    public void bindUi() {
        // 返回按钮
        RxView.clicks(mDataBinding.backImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> finish());
    }

    @Override
    public void subscribeUi() {
        mViewModel.getMessageType().observe(this, integer -> {
            switch (integer){
                case MessageViewModel.LIVE_MESSAGE:
                    mDataBinding.editTitle.setText("直播通知");
                    break;
                case MessageViewModel.SYS_MESSAGE:
                    mDataBinding.editTitle.setText("系统通知");
                    break;
            }
        });
    }

    @Override
    public void initRequest() {

    }
}

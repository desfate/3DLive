package com.futrtch.live.activitys.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.futrtch.live.R;
import com.futrtch.live.activitys.ChatActivity;
import com.futrtch.live.activitys.MessageActivity;
import com.futrtch.live.activitys.MessageTitleActivity;
import com.futrtch.live.adapters.MessageListAdapter;
import com.futrtch.live.beans.MessageBean;
import com.futrtch.live.databinding.FragmentMessageBinding;
import com.futrtch.live.databinding.LayoutMessageDialogBinding;
import com.futrtch.live.databinding.LayoutMessageTitleBinding;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.MessageListViewModel;
import com.futrtch.live.mvvm.vm.MessageListViewModelFactory;
import com.futrtch.live.mvvm.vm.MessageViewModel;
import com.futrtch.live.mvvm.vm.TitleMessageViewModel;
import com.futrtch.live.views.BottomDialog;
import com.futrtch.live.views.ViewsBuilder;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.Objects;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 消息页面  主页 -> 消息
 */
public class MessageFragment extends MVVMFragment {

    MessageListAdapter mAdapter;
    MessageListViewModel mViewModel;
    LinearLayoutManager manager;
    LayoutMessageTitleBinding mHeadBinding;
    FragmentMessageBinding mDataBinding;
    LayoutMessageDialogBinding mDialogBinding;
    BottomDialog mDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        mDialogBinding = DataBindingUtil.inflate(inflater, R.layout.layout_message_dialog, null,false);
        return mDataBinding.getRoot();
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new MessageListViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(MessageListViewModel.class);
    }

    @Override
    public void init() {
        mDialog = new BottomDialog(Objects.requireNonNull(getActivity()), mDialogBinding);
        manager = new LinearLayoutManager(getActivity());
        mDataBinding.anchorRvAvatar.setLayoutManager(manager);
        mAdapter = new MessageListAdapter(R.layout.layout_message_item, getActivity(), mViewModel.getmListData());
        mHeadBinding = (LayoutMessageTitleBinding) new ViewsBuilder()
                .setInflater(getLayoutInflater())
                .setLayoutId(R.layout.layout_message_title)
                .setParent(mDataBinding.anchorRvAvatar)
                .setAttachToParent(false)
                .build()
                .getDataBinding();
        mAdapter.setHeaderView(mHeadBinding.getRoot());
        mDataBinding.anchorRvAvatar.setAdapter(mAdapter);
        mViewModel.prepare(getActivity(), mDataBinding);
        manager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    public void bindUi() {
        // 粉丝
        RxView.clicks(mHeadBinding.fensImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    Intent intent = new Intent(getActivity(), MessageTitleActivity.class);
                    intent.putExtra(TitleMessageViewModel.MESSAGE_KEY, TitleMessageViewModel.FANS);
                    startActivity(intent);
                });
        // 赞
        RxView.clicks(mHeadBinding.assistImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    Intent intent = new Intent(getActivity(), MessageTitleActivity.class);
                    intent.putExtra(TitleMessageViewModel.MESSAGE_KEY, TitleMessageViewModel.ASSIST);
                    startActivity(intent);
                });
        // # 我的
        RxView.clicks(mHeadBinding.mineImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    Intent intent = new Intent(getActivity(), MessageTitleActivity.class);
                    intent.putExtra(TitleMessageViewModel.MESSAGE_KEY, TitleMessageViewModel.MINE);
                    startActivity(intent);
                });
        // # 评论
        RxView.clicks(mHeadBinding.discussImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    Intent intent = new Intent(getActivity(), MessageTitleActivity.class);
                    intent.putExtra(TitleMessageViewModel.MESSAGE_KEY, TitleMessageViewModel.DISCUSS);
                    startActivity(intent);
                });
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        // 消息列表点击事件
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (mViewModel.getmListData().get(position).getMessageType()){
                case MessageBean.LIVE_MESSAGE:
                    intent.putExtra(MessageViewModel.KEY_MESSAGE, MessageViewModel.LIVE_MESSAGE);
                    startActivity(intent);
                    break;
                case MessageBean.SYSTEM_MESSAGE:
                    intent.putExtra(MessageViewModel.KEY_MESSAGE, MessageViewModel.SYS_MESSAGE);
                    startActivity(intent);
                    break;
                case MessageBean.USER_MESSAGE:
                    Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                    startActivity(chatIntent);
                    break;
            }
        });
        // 群聊
        RxView.clicks(mDataBinding.addGroupChat)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDialog.showDialog());

        RxView.clicks(mDialogBinding.chatBtn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDialog.cancel());
    }

    @Override
    public void subscribeUi() {

    }

    @Override
    public void initRequest() {

    }

    @Override
    public void releaseBinding() {
        releaseBindingList(mDataBinding,mDialogBinding,mHeadBinding);
    }
}

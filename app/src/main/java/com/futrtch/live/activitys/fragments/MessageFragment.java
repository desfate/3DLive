package com.futrtch.live.activitys.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.futrtch.live.R;
import com.futrtch.live.adapters.MessageListAdapter;
import com.futrtch.live.beans.MessageBean;
import com.futrtch.live.databinding.FragmentMessageBinding;
import com.futrtch.live.mvvm.vm.MessageListViewModel;
import com.futrtch.live.mvvm.vm.MessageListViewModelFactory;
import com.jakewharton.rxbinding4.view.RxView;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class MessageFragment extends Fragment {

    MessageListAdapter mAdapter;
    FragmentMessageBinding mDataBinding;
    MessageListViewModel mViewModel;
    LinearLayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewModelProvider.Factory factory = new MessageListViewModelFactory(getActivity().getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(MessageListViewModel.class);
        init();
        bindUi();
        subscribeUi();
    }

    private void init() {
        manager = new LinearLayoutManager(getActivity());
        mDataBinding.anchorRvAvatar.setLayoutManager(manager);
        mAdapter = new MessageListAdapter(getActivity(), new DiffUtil.ItemCallback<MessageBean>() {
            @Override
            public boolean areItemsTheSame(@NonNull MessageBean oldItem, @NonNull MessageBean newItem) {
                return oldItem.getMessageContent().equals(newItem.getMessageContent());
            }

            @Override
            public boolean areContentsTheSame(@NonNull MessageBean oldItem, @NonNull MessageBean newItem) {
                return oldItem.getMessageContent().equals(newItem.getMessageContent());
            }
        }, v -> {

        });

        mAdapter.setHeader(); // 有头部轮播图
        mDataBinding.anchorRvAvatar.setAdapter(mAdapter);
        mViewModel.prepare(getActivity(), mDataBinding);
        manager.scrollToPositionWithOffset(0, 0);
    }

    private void bindUi() {
        RxView.clicks(mDataBinding.addGroupChat)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> manager.scrollToPositionWithOffset(0, 0));
    }

    private void subscribeUi() {
        // 绑定列表数据
        mViewModel.getMessageListData().observe(this, messageBeans -> mAdapter.submitList(messageBeans));
    }

}

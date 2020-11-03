package com.futrtch.live.activitys.fragments;

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
import com.futrtch.live.adapters.MessageListAdapter;
import com.futrtch.live.databinding.FragmentMessageBinding;
import com.futrtch.live.databinding.LayoutMessageTitleBinding;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.MessageListViewModel;
import com.futrtch.live.mvvm.vm.MessageListViewModelFactory;

import java.util.Objects;

public class MessageFragment extends MVVMFragment {

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
    public void initViewModel() {
        ViewModelProvider.Factory factory = new MessageListViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(MessageListViewModel.class);
    }

    @Override
    public void init() {
        manager = new LinearLayoutManager(getActivity());
        mDataBinding.anchorRvAvatar.setLayoutManager(manager);
        mAdapter = new MessageListAdapter(R.layout.layout_message_item, getActivity(), mViewModel.getmListData());
        mAdapter.setHeaderView(getHeadView());
        mDataBinding.anchorRvAvatar.setAdapter(mAdapter);
        mViewModel.prepare(getActivity(), mDataBinding);
        manager.scrollToPositionWithOffset(0, 0);
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

    public View getHeadView(){
        LayoutMessageTitleBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_message_title, mDataBinding.anchorRvAvatar, false);
        return binding.getRoot();
    }

}

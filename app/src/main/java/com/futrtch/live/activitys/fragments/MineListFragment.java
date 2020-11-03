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
import androidx.recyclerview.widget.DiffUtil;

import com.futrtch.live.R;
import com.futrtch.live.adapters.MineListAdapter;
import com.futrtch.live.databinding.FragmentMineListBinding;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.MineListViewModel;
import com.futrtch.live.mvvm.vm.MineListViewModelFactory;
import com.futrtch.live.tencent.live.TCVideoInfo;

import java.util.Objects;

public class MineListFragment extends MVVMFragment {

    MineListViewModel mViewModel;
    FragmentMineListBinding mDataBinding;
    MineListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine_list, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new MineListViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), this);
        mViewModel = ViewModelProviders.of(this,factory).get(MineListViewModel.class);
        mViewModel.prepare(getActivity(), mDataBinding);
    }

    @Override
    public void init() {
        mAdapter = new MineListAdapter(new DiffUtil.ItemCallback<TCVideoInfo>() {
            @Override
            public boolean areItemsTheSame(@NonNull TCVideoInfo oldItem, @NonNull TCVideoInfo newItem) {
                return oldItem.userId.equals(newItem.userId);
            }

            @Override
            public boolean areContentsTheSame(@NonNull TCVideoInfo oldItem, @NonNull TCVideoInfo newItem) {
                return oldItem.userId.equals(newItem.userId);
            }
        });
        mDataBinding.recyclerList.setAdapter(mAdapter);
    }

    @Override
    public void bindUi() {

    }

    @Override
    public void subscribeUi() {
        mViewModel.getMineListData().observe(this, tcVideo -> mAdapter.submitList(tcVideo));
    }

    @Override
    public void initRequest() {

    }


}

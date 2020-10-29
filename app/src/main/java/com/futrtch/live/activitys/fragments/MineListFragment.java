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

import com.futrtch.live.R;
import com.futrtch.live.adapters.MineListAdapter;
import com.futrtch.live.databinding.FragmentMineListBinding;
import com.futrtch.live.mvvm.vm.MineListViewModel;
import com.futrtch.live.mvvm.vm.MineListViewModelFactory;
import com.futrtch.live.tencent.live.TCVideoInfo;

public class MineListFragment extends Fragment {

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
        ViewModelProvider.Factory factory = new MineListViewModelFactory(getActivity().getApplication(), this);
        mViewModel = ViewModelProviders.of(this,factory).get(MineListViewModel.class);
        mViewModel.prepare(getActivity(), mDataBinding);

        init();
        bindUi();
        subscribeUi();
    }

    private void init() {
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

    private void bindUi() {

    }

    private void subscribeUi() {
        mViewModel.getMineListData().observe(this, tcVideo -> mAdapter.submitList(tcVideo));
    }




}

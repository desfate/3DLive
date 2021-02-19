package com.futrtch.live.activitys.fragments.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.adapters.MineListAdapter;
import com.futrtch.live.databinding.FragmentMineListBinding;
import com.futrtch.live.databinding.LayoutEmptyListBinding;
import com.futrtch.live.mvvm.MVVMFragment;
import com.futrtch.live.mvvm.vm.MineListViewModel;
import com.futrtch.live.mvvm.vm.MineListViewModelFactory;
import com.futrtch.live.views.ViewsBuilder;

import java.util.Objects;

public class MineListFragment extends MVVMFragment {

    MineListViewModel mViewModel;
    FragmentMineListBinding mDataBinding;
    MineListAdapter mAdapter;
    LayoutEmptyListBinding mEmptyBinding; // 页面为空

    public static MineListFragment getInstance(int index){
        MineListFragment fragment = new MineListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

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
        mAdapter = new MineListAdapter(R.layout.listview_mine_item, mViewModel.getmListData());
        mDataBinding.recyclerList.setAdapter(mAdapter);

        mEmptyBinding = (LayoutEmptyListBinding) new ViewsBuilder()
                .setParent(mDataBinding.recyclerList)
                .setInflater(getLayoutInflater())
                .setLayoutId(R.layout.layout_empty_list)
                .setAttachToParent(false)
                .build()
                .getDataBinding();

        mAdapter.setEmptyView(mEmptyBinding.getRoot());
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

    @Override
    public void releaseBinding() {
        releaseBindingList(mDataBinding);
    }


}

package com.futrtch.live.activitys;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.futrtch.live.R;
import com.futrtch.live.adapters.AddressBookAdapter;
import com.futrtch.live.databinding.ActivityAddressBookBinding;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.mvvm.vm.AddressBookViewModel;
import com.futrtch.live.mvvm.vm.AddressBookViewModelFactory;
import com.jakewharton.rxbinding4.view.RxView;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class AddressBookActivity extends MVVMActivity {

    AddressBookViewModel mViewModel;
    ActivityAddressBookBinding mDataBinding;
    AddressBookAdapter mAdapter;

    @Override
    public void initViewModel() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_address_book);
        ViewModelProvider.Factory factory = new AddressBookViewModelFactory(getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(AddressBookViewModel.class);
    }

    @Override
    public void init() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mAdapter = new AddressBookAdapter(this, R.layout.layout_address_book_item, mViewModel.getmListData());
        mDataBinding.recyclerList.setAdapter(mAdapter);
        mDataBinding.recyclerList.setLayoutManager(manager);
        mDataBinding.swipeLayout.setOnRefreshListener(this::initRequest);
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

    }

    @Override
    public void initRequest() {
        mDataBinding.swipeLayout.setRefreshing(false);
    }
}

package com.futrtch.live.adapters;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.R;
import com.futrtch.live.beans.AddressBookBean;
import com.futrtch.live.databinding.LayoutAddressBookItemBinding;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AddressBookAdapter extends BaseQuickAdapter<AddressBookBean, BaseDataBindingHolder<LayoutAddressBookItemBinding>> {

    Context context;

    public AddressBookAdapter(Context context, int layoutResId, @Nullable List<AddressBookBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<LayoutAddressBookItemBinding> layoutAddressBookItemBindingBaseDataBindingHolder, AddressBookBean addressBookBean) {
        LayoutAddressBookItemBinding binding = DataBindingUtil.getBinding(layoutAddressBookItemBindingBaseDataBindingHolder.itemView);
        if(binding == null || addressBookBean == null) return;
        binding.messageName.setText(addressBookBean.getUserName());
        binding.messageContent.setText(addressBookBean.getRealName());
        RxView.clicks(binding.careBtn)
                .subscribe(unit -> {
                    if (addressBookBean.isCare()) { // 已关注
                        addressBookBean.setCare(false);
                        setCareState(binding, false);
                    } else { // 关注
                        addressBookBean.setCare(true);
                        setCareState(binding, true);
                    }
                });
        setCareState(binding, addressBookBean.isCare());
    }

    private void setCareState(LayoutAddressBookItemBinding binding, boolean isCare) {
        if (!isCare) {
            binding.careBtn.setBackground(context.getDrawable(R.drawable.friend_care_background));
            binding.careBtn.setText("关注");
        } else {
            binding.careBtn.setBackground(context.getDrawable(R.drawable.friend_cared_background));
            binding.careBtn.setText("已关注");
        }
    }
}

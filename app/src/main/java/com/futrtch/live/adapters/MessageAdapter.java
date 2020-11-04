package com.futrtch.live.adapters;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.beans.MessageItemBean;
import com.futrtch.live.databinding.LayoutMessageSignBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MessageAdapter extends BaseQuickAdapter<MessageItemBean, BaseDataBindingHolder<LayoutMessageSignBinding>> {

    Context context;

    public MessageAdapter(Context context, int layoutResId, @Nullable List<MessageItemBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<LayoutMessageSignBinding> layoutMessageSignBindingBaseDataBindingHolder, MessageItemBean messageItemBean) {
        LayoutMessageSignBinding binding = DataBindingUtil.getBinding(layoutMessageSignBindingBaseDataBindingHolder.itemView);
        if(binding == null || messageItemBean == null) return;
        binding.titleTv.setText(messageItemBean.getMessageTitle());
        binding.timeTv.setText(messageItemBean.getMessageTime());
        binding.messageContent.setText(messageItemBean.getMessageContent());
    }
}

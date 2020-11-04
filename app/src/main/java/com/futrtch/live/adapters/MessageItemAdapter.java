package com.futrtch.live.adapters;

import android.content.Context;

import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.beans.MessageInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageItemAdapter extends BaseMultiItemQuickAdapter<MessageInfoBean, BaseDataBindingHolder<ViewDataBinding>> {

    Context context;

    MessageItemAdapter(Context context, List<MessageInfoBean> mList){
        super(mList);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ViewDataBinding> viewDataBindingBaseDataBindingHolder, MessageInfoBean messageInfoBean) {
        switch (viewDataBindingBaseDataBindingHolder.getItemViewType()){
            case MessageInfoBean.MESSAGE_TYPE_FANS: // 粉丝类型
                break;
            case MessageInfoBean.MESSAGE_TYPE_ASSIST: // 赞
                break;
            case MessageInfoBean.MESSAGE_TYPE_MINE:// 我的
                break;
            case MessageInfoBean.MESSAGE_TYPE_DISCUSS:// 评论
                break;
        }
    }
}

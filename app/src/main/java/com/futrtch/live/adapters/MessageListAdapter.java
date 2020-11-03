package com.futrtch.live.adapters;

import android.content.Context;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.R;
import com.futrtch.live.beans.MessageBean;
import com.futrtch.live.databinding.LayoutMessageItemBinding;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.List;

/**
 * 消息列表
 */
public class MessageListAdapter extends BaseQuickAdapter<MessageBean, BaseDataBindingHolder<LayoutMessageItemBinding>> {

    private Context context;

    public MessageListAdapter(int layoutResId, Context context, List<MessageBean> mList) {
        super(layoutResId, mList);
        this.context = context;
    }
    @Override
    protected void convert(@NotNull BaseDataBindingHolder<LayoutMessageItemBinding> layoutMessageItemBindingBaseDataBindingHolder, MessageBean messageBean) {
        MessageBean info = messageBean;
        LayoutMessageItemBinding binding = DataBindingUtil.getBinding(layoutMessageItemBindingBaseDataBindingHolder.itemView);
        if (binding == null) return;
        if (info != null) {
            switch (info.getMessageType()) {
                case 1:
                    binding.messageImg.setImageResource(R.mipmap.live_message_icon);
                    binding.messageName.setText("直播通知");
                    binding.messageContent.setText(MessageFormat.format("{0}.{1}", info.getMessageContent(), info.getMessageTime()));
                    binding.signText.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    binding.messageImg.setImageResource(R.mipmap.system_message_icon);
                    binding.messageName.setText("系统通知");
                    binding.messageContent.setText(MessageFormat.format("{0}.{1}", info.getMessageContent(), info.getMessageTime()));
                    binding.signText.setVisibility(View.VISIBLE);
                    break;
                default:
                    binding.messageImg.setImageResource(R.mipmap.face);
                    binding.messageName.setText("用户私信");
                    binding.messageContent.setText(MessageFormat.format("{0}.{1}", info.getMessageContent(), info.getMessageTime()));
                    binding.signText.setVisibility(View.INVISIBLE);


            }
        }
    }
}

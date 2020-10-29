package com.futrtch.live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.futrtch.live.R;
import com.futrtch.live.beans.MessageBean;
import com.futrtch.live.databinding.LayoutMessageItemBinding;
import com.futrtch.live.databinding.LayoutMessageTitleBinding;

import java.text.MessageFormat;

/**
 * 消息列表
 */
public class MessageListAdapter extends ListAdapter<MessageBean, MessageListAdapter.MessageHolder> {

    public final static String STATE_LAST_SELECTED_ID = "last_selected_id";

    public boolean hasHeader = false; // true : 用户添加了头部  false: 没有头部

    public final static int HEAD_TYPE = 10086;
    public final static int ITEM_TYPE = 10010;

    View.OnClickListener callBack;

    public MessageListAdapter(Context context, @NonNull DiffUtil.ItemCallback<MessageBean> diffCallback, View.OnClickListener callBack) {
        super(diffCallback);
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD_TYPE) {
            LayoutMessageTitleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_message_title, parent, false);
            if(callBack != null) {
                binding.assistImg.setOnClickListener(callBack);
                binding.fensImg.setOnClickListener(callBack);
                binding.mineImg.setOnClickListener(callBack);
                binding.discussImg.setOnClickListener(callBack);
            }
            return new MessageHolder(binding.getRoot());
        } else {
            LayoutMessageItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_message_item, parent, false);
            return new MessageHolder(binding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        if (getItemViewType(position) == HEAD_TYPE) {

        } else {
            MessageBean info = getItem(position - 1);
            LayoutMessageItemBinding binding = DataBindingUtil.getBinding(holder.itemView);
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

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && hasHeader) {
            // 我是头部
            return HEAD_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasHeader ? 1 : 0);
    }

    public void setHeader() {
        hasHeader = true;
    }

    static class MessageHolder extends RecyclerView.ViewHolder {
        public MessageHolder(@NonNull View view) {
            super(view);
        }
    }
}

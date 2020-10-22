package com.futrtch.live.tencent.common.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.futrtch.live.R;
import com.futrtch.live.tencent.common.msg.TCSimpleUserInfo;
import com.futrtch.live.tencent.common.utils.TCUtils;

import java.util.LinkedList;

/**
 *
 * 这里处理一下逻辑
 * Module:   TCUserAvatarListAdapter
 *
 * Function: 直播头像列表Adapter
 *
 */
public class RTCUserAvatarListAdapter extends ListAdapter<TCSimpleUserInfo, RecyclerView.ViewHolder> {
    Context mContext;
    //主播id
    private String mPusherId;
    //最大容纳量
    private final static int TOP_STORGE_MEMBER = 50;


    public RTCUserAvatarListAdapter(Context context, String pusherId) {
        super(new DiffUtil.ItemCallback<TCSimpleUserInfo>() {
            @Override
            public boolean areItemsTheSame(@NonNull TCSimpleUserInfo oldItem, @NonNull TCSimpleUserInfo newItem) {
                return oldItem.userid.equals(newItem.userid);
            }

            @Override
            public boolean areContentsTheSame(@NonNull TCSimpleUserInfo oldItem, @NonNull TCSimpleUserInfo newItem) {
                return oldItem.userid.equals(newItem.userid);
            }
        });
        this.mContext = context;
        this.mPusherId = pusherId;
    }

    /**
     * 添加用户信息
     * @param userInfo 用户基本信息
     * @return 存在重复或头像为主播则返回false
     */
    public boolean addItem(TCSimpleUserInfo userInfo) {

        //去除主播头像
        if(userInfo.userid.equals(mPusherId))
            return false;


        //始终显示新加入item为第一位
        getCurrentList().add(0, userInfo);
        //超出时删除末尾项
        if(getCurrentList().size() > TOP_STORGE_MEMBER) {
            getCurrentList().remove(TOP_STORGE_MEMBER);
            notifyItemRemoved(TOP_STORGE_MEMBER);
        }
        notifyItemInserted(0);
        return true;
    }

    public void removeItem(String userId) {
        TCSimpleUserInfo tempUserInfo = null;

        for(TCSimpleUserInfo userInfo : getCurrentList())
            if(userInfo.userid.equals(userId))
                tempUserInfo = userInfo;

        if(null != tempUserInfo) {
            getCurrentList().remove(tempUserInfo);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_user_avatar, parent, false);

        final AvatarViewHolder avatarViewHolder = new AvatarViewHolder(view);
        avatarViewHolder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TCSimpleUserInfo userInfo = getItem(avatarViewHolder.getAdapterPosition());
                Toast.makeText(mContext.getApplicationContext(),"当前点击用户： " + userInfo.userid, Toast.LENGTH_SHORT).show();
            }
        });

        return avatarViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TCUtils.showPicWithUrl(mContext, ((AvatarViewHolder)holder).ivAvatar,getItem(position).avatar,
                R.mipmap.face);

    }

    private class AvatarViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar;

        public AvatarViewHolder(View itemView) {
            super(itemView);

            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }
}

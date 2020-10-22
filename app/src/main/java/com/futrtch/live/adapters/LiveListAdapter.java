package com.futrtch.live.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import com.futrtch.live.R;
import com.futrtch.live.activitys.LivePlayActivity;
import com.futrtch.live.databinding.LayoutBannerBinding;
import com.futrtch.live.databinding.ListviewVideoItemBinding;
import com.futrtch.live.tencent.common.utils.TCConstants;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.live.AdapterCallBack;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.youth.banner.indicator.CircleIndicator;

import java.util.Optional;
import java.util.Random;


public class LiveListAdapter extends ListAdapter<TCVideoInfo, LiveListAdapter.LiveVideoHolder> {

    public final static String STATE_LAST_SELECTED_ID = "last_selected_id";

    public boolean hasHeader = false; // true : 用户添加了头部  false: 没有头部

    public final static int HEAD_TYPE = 10086;
    public final static int ITEM_TYPE = 10010;

    private String lastSelectedId = "";
    private Context mContext;
    private final AdapterCallBack callBack;

    BannerImageAdapter bannerImageAdapter;
    LifecycleOwner lifecycleOwner;

    public LiveListAdapter(Context context, @NonNull DiffUtil.ItemCallback<TCVideoInfo> diffCallback, AdapterCallBack callBack) {
        super(diffCallback);
        this.mContext = context;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public LiveVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == HEAD_TYPE){
            LayoutBannerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_banner, parent, false);
            if(bannerImageAdapter != null) {
                binding.banner.addBannerLifecycleObserver(lifecycleOwner)
                        .setAdapter(bannerImageAdapter)
                        .setIndicator(new CircleIndicator(LiveListAdapter.this.mContext));
            }
            return new LiveVideoHolder(binding.getRoot());
        }else {
            ListviewVideoItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.listview_video_item, parent, false);
            return new LiveVideoHolder(binding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LiveVideoHolder holder, int position) {
        if(getItemViewType(position) == HEAD_TYPE){

        }else {
            TCVideoInfo info = getItem(position - 1);
            ListviewVideoItemBinding binding = DataBindingUtil.getBinding(holder.itemView);
            if(binding == null) return;
            ViewCompat.setTransitionName(binding.anchorTvTitle, "title-" + info.userId);
            ViewCompat.setTransitionName(binding.anchorBtnCover, "btn");
            ViewCompat.setTransitionName(binding.hostName, "host-" + info.userId);
            ViewCompat.setTransitionName(binding.liveLbs, "libs-" + info.userId);
            ViewCompat.setTransitionName(binding.liveMembers, "members-" + info.userId);

            binding.squareLayout.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, LivePlayActivity.class);
                intent.putExtra("btn", info.frontCover);
                intent.putExtra(TCConstants.PUSHER_ID, info.userId !=null?info.userId :"");
                intent.putExtra(TCConstants.PUSHER_NAME, TextUtils.isEmpty(info.nickname) ? info.userId : info.nickname);
                intent.putExtra(TCConstants.PUSHER_AVATAR, info.avatar);
                intent.putExtra(TCConstants.HEART_COUNT, "" + info.likeCount);
                intent.putExtra(TCConstants.MEMBER_COUNT, "" + info.viewerCount);
                intent.putExtra(TCConstants.GROUP_ID, info.groupId);
                intent.putExtra(TCConstants.PLAY_TYPE, info.livePlay);
                intent.putExtra(TCConstants.FILE_ID, info.fileId !=null?info.fileId :"");
                intent.putExtra(TCConstants.COVER_PIC, info.frontCover);
                intent.putExtra(TCConstants.TIMESTAMP, info.createTime);
                intent.putExtra(TCConstants.ROOM_TITLE, info.title);
                Pair<View,String> pair1 = new Pair<>((View)binding.anchorBtnCover,ViewCompat.getTransitionName(binding.anchorBtnCover));
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, pair1);
                ActivityCompat.startActivity(mContext,intent,activityOptionsCompat.toBundle());
            });

            //直播封面
            String cover = info.frontCover;
            if (TextUtils.isEmpty(cover)) {
                switch (new Random().nextInt(3)){
                    case 0:
                        binding.anchorBtnCover.setImageResource(R.mipmap.liveroom1);
                        break;
                    case 1:
                        binding.anchorBtnCover.setImageResource(R.mipmap.liveroom2);
                        break;
                    case 2:
                        binding.anchorBtnCover.setImageResource(R.mipmap.liveroom3);
                        break;
                }
            } else {
//            RequestManager req = Glide.with(mActivity);
//            req.load(cover).placeholder(R.drawable.bg).into(holder.ivCover);
            }

            //主播头像
//        TCUtils.showPicWithUrl(mContext,binding.,cover.avatar,R.drawable.face);
            //主播昵称
            if (TextUtils.isEmpty(info.nickname)) {
                binding.hostName.setText(TCUtils.getLimitString(info.userId, 10));
            } else {
                binding.hostName.setText(TCUtils.getLimitString(info.nickname, 10));
            }
            //主播地址
            if (TextUtils.isEmpty(info.location)) {
//            binding.liveLbs.setText(mContext.getString(R.string.live_unknown));
            } else {
                binding.liveLbs.setText(TCUtils.getLimitString(info.location, 9));
            }

            //直播标题
            binding.anchorTvTitle.setText(TCUtils.getLimitString(info.title, 10));
            //直播观看人数
            binding.liveMembers.setText("" + info.viewerCount);

            // Load the image asynchronously. See CheeseDetailFragment.kt about "dontTransform()"
            if (info.userId.equals(lastSelectedId)) {
                Glide.with(mContext).load(cover).dontTransform().priority(Priority.IMMEDIATE)
                        .into(binding.anchorBtnCover);
                Optional.ofNullable(callBack).ifPresent(AdapterCallBack::callBack);
                lastSelectedId = null;
            }
        }
    }

    public void saveInstanceState(Bundle outState) {
        if (lastSelectedId != null) {
            outState.getString(STATE_LAST_SELECTED_ID, lastSelectedId);
        }
    }

    public void restoreInstanceState(Bundle state) {
        if (lastSelectedId == null && state.containsKey(STATE_LAST_SELECTED_ID)) {
            lastSelectedId = state.getString(LiveListAdapter.STATE_LAST_SELECTED_ID);
        }
    }

    public Boolean expectsTransition() {
        return !lastSelectedId.equals("");
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

    public void setHeader(BannerImageAdapter bannerImageAdapter, LifecycleOwner lifecycleOwner) {
        hasHeader = true;
        this.bannerImageAdapter = bannerImageAdapter;
        this.lifecycleOwner = lifecycleOwner;
    }

    static class LiveVideoHolder extends RecyclerView.ViewHolder {
        public LiveVideoHolder(@NonNull View view) {
            super(view);
        }
    }
}

package com.futrtch.live.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.R;
import com.futrtch.live.activitys.LivePlayActivity;
import com.futrtch.live.databinding.ListviewVideoItemBinding;
import com.futrtch.live.tencent.common.utils.TCConstants;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class LiveListAdapter extends BaseQuickAdapter<TCVideoInfo, BaseDataBindingHolder<ListviewVideoItemBinding>>{
    private Context mContext;

    public LiveListAdapter(int layoutResId, Context context, List<TCVideoInfo> mList) {
        super(layoutResId, mList);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ListviewVideoItemBinding> bindingBaseDataBindingHolder, TCVideoInfo info) {
        ListviewVideoItemBinding binding = DataBindingUtil.getBinding(bindingBaseDataBindingHolder.itemView);
        if(binding == null || info == null) return;
        ViewCompat.setTransitionName(binding.anchorTvTitle, "title-" + info.userId);
        ViewCompat.setTransitionName(binding.anchorBtnCover, "btn");
        ViewCompat.setTransitionName(binding.hostName, "host-" + info.userId);
        ViewCompat.setTransitionName(binding.liveLbs, "libs-" + info.userId);
        ViewCompat.setTransitionName(binding.liveMembers, "members-" + info.userId);

        RxView.clicks(binding.squareLayout).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(unit -> {
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

    }
}

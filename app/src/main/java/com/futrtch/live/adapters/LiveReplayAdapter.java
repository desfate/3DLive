package com.futrtch.live.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.databinding.ListviewVideoItemBinding;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.utils.ThumbUtils;
import com.github.desfate.videokit.dates.VideoInfoDate;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class LiveReplayAdapter extends BaseQuickAdapter<VideoInfoDate, BaseDataBindingHolder<ListviewVideoItemBinding>> {


    Context context;

    public LiveReplayAdapter(int layoutResId, Context context, List<VideoInfoDate> mList) {
        super(layoutResId, mList);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ListviewVideoItemBinding> bindingBaseDataBindingHolder, VideoInfoDate info) {
        ListviewVideoItemBinding binding = DataBindingUtil.getBinding(bindingBaseDataBindingHolder.itemView);
        if (binding == null || info == null) return;

        ViewCompat.setTransitionName(binding.anchorBtnCover, "btn");

        //视频封面
//        String cover = info.frontCover;
//        String cover = RandomUtils.RandomStringPic();
//        if(!cover.isEmpty()) {'


        Glide.with(context).load(info.getVideoPicUrl()).into(binding.anchorBtnCover);
//            binding.anchorBtnCover.setImageResource(RandomUtils.RandomPic());
//        }

        //视频标题
        binding.anchorTvTitle.setText(TCUtils.getLimitString(info.getVideoName(), 10));
    }
}

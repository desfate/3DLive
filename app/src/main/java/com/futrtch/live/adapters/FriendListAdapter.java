package com.futrtch.live.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.R;
import com.futrtch.live.beans.FriendBean;
import com.futrtch.live.databinding.LayoutFriendItemBinding;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class FriendListAdapter extends BaseQuickAdapter<FriendBean, BaseDataBindingHolder<LayoutFriendItemBinding>>{

    private Context context;

    public FriendListAdapter(int layoutResId, Context context, List<FriendBean> mList) {
        super(layoutResId, mList);
        this.context = context;
    }



    @Override
    protected void convert(@NotNull BaseDataBindingHolder<LayoutFriendItemBinding> layoutFriendItemBindingBaseDataBindingHolder, FriendBean bean) {

        LayoutFriendItemBinding binding = DataBindingUtil.getBinding(layoutFriendItemBindingBaseDataBindingHolder.itemView);
        if (binding == null) return;
        binding.userInfoRly.setVisibility(View.VISIBLE);
        binding.messageName.setText(bean.getUserName());

        setCareState(binding, bean.getIsCare());
        if(bean.isCareShow()){
            binding.careBtn.setVisibility(View.VISIBLE);
            binding.closeImg.setVisibility(View.VISIBLE);
        }else{
            binding.careBtn.setVisibility(View.INVISIBLE);
            binding.closeImg.setVisibility(View.INVISIBLE);
        }

        RxView.clicks(binding.closeImg)
                .subscribe(unit -> removeData(binding.userInfoRly, getItemPosition(bean)));

        RxView.clicks(binding.careBtn)
                .subscribe(unit -> {
                    if (bean.getIsCare()) { // 已关注
                        bean.setIsCare(false);
                        setCareState(binding, false);
                    } else { // 关注
                        bean.setIsCare(true);
                        setCareState(binding, true);
                    }
                });
    }

    private void setCareState(LayoutFriendItemBinding binding, boolean isCare) {
        if (!isCare) {
            binding.careBtn.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.friend_care_background, null));
            binding.careBtn.setText("关注");
            binding.closeImg.setVisibility(View.VISIBLE);
        } else {
            binding.careBtn.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.friend_cared_background, null));
            binding.careBtn.setText("已关注");
            binding.closeImg.setVisibility(View.GONE);
        }
    }

    //  删除动画
    public void removeData(ConstraintLayout layout, final int position) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(layout, "TranslationX", 0, 1000);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(layout, "alpha", 1, 0);
        scaleX.setDuration(500);
        set.play(scaleX).with(scaleY);
        set.start();
        scaleX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                removeAt(position);
                layout.setVisibility(View.INVISIBLE);
                layout.setTranslationX(0f);
                layout.setAlpha(1f);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeAt(position);
                layout.setVisibility(View.INVISIBLE);
                layout.setTranslationX(0f);
                layout.setAlpha(1f);
            }

        });
    }

}

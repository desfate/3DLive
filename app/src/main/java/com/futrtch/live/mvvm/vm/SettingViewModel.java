package com.futrtch.live.mvvm.vm;

import androidx.lifecycle.ViewModel;

import com.futrtch.live.R;
import com.futrtch.live.beans.SettingBean;

import java.util.ArrayList;
import java.util.List;

public class SettingViewModel extends ViewModel {



    List<SettingBean> mListData = new ArrayList<>();


    public void prepare(){
        mListData.add(new SettingBean(1,"账号", 0,false));
        mListData.add(new SettingBean(2, "账号与安全", R.mipmap.setting_account_icon, false));
        mListData.add(new SettingBean(2,"隐私设置", R.mipmap.setting_password_icon, true));
        mListData.add(new SettingBean(1, "通用", 0, false));
        mListData.add(new SettingBean(2, "通知设置", R.mipmap.setting_sign_icon, false));
        mListData.add(new SettingBean(2, "动态壁纸", R.mipmap.setting_theme_icon, false));
        mListData.add(new SettingBean(2,"通用设置", R.mipmap.setting_set_icon, true));
        mListData.add(new SettingBean(1, "账号互通", 0, false));
        mListData.add(new SettingBean(2, "3D看看", R.mipmap.setting_sign_icon, false));
        mListData.add(new SettingBean(1, "关于", 0, false));
        mListData.add(new SettingBean(2, "用户协议", R.mipmap.setting_sign_icon, false));
        mListData.add(new SettingBean(2, "社区自律公约", R.mipmap.setting_sign_icon, false));
        mListData.add(new SettingBean(2, "隐私政策", R.mipmap.setting_sign_icon, false));
        mListData.add(new SettingBean(2, "第三方SDK列表", R.mipmap.setting_sign_icon, false));
        mListData.add(new SettingBean(2, "关于抖音", R.mipmap.setting_sign_icon, false));
        mListData.add(new SettingBean(2, "反馈与帮助", R.mipmap.setting_sign_icon, false));
        mListData.add(new SettingBean(2, "清理占用空间", R.mipmap.setting_sign_icon, false));
        mListData.add(new SettingBean(2, "账号转换", R.mipmap.setting_sign_icon, false));
        mListData.add(new SettingBean(2, "退出登录", R.mipmap.setting_sign_icon, false));
    }

    public List<SettingBean> getmListData() {
        return mListData;
    }
}

package com.futrtch.live.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class SettingBean implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;
    public static final int CLICK_ITEM_CHILD_VIEW = 2;

    private int settingType;        // 设置类型  1： 标题 2： 选项
    private String settingName;     // 设置名称
    private int settingPic;         // 设置图片
    private int settingPoint;       // 设置原点是否显示
    private boolean showLine;       // 是否显示底部横线

    public SettingBean(int settingType, String settingName, int settingPic, boolean showLine) {
        this.settingType = settingType;
        this.settingName = settingName;
        this.settingPic = settingPic;
        this.showLine = showLine;
    }

    public int getSettingType() {
        return settingType;
    }

    public void setSettingType(int settingType) {
        this.settingType = settingType;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public int getSettingPic() {
        return settingPic;
    }

    public void setSettingPic(int settingPic) {
        this.settingPic = settingPic;
    }

    public int getSettingPoint() {
        return settingPoint;
    }

    public void setSettingPoint(int settingPoint) {
        this.settingPoint = settingPoint;
    }

    public boolean isShowLine() {
        return showLine;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }

    @Override
    public int getItemType() {
        return settingType;
    }
}

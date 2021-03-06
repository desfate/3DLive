package com.futrtch.live.beans;

public class BannerBean {
    private int imageRes;
    private String imageUrl;

    public int getImageRes() {
        return imageRes;
    }

    public BannerBean(int imageRes, String imageUrl) {
        this.imageRes = imageRes;
        this.imageUrl = imageUrl;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

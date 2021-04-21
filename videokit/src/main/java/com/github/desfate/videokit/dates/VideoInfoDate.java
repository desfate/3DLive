package com.github.desfate.videokit.dates;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 视频信息
 */
public class VideoInfoDate implements Parcelable {

    private String videoPicUrl;    //  视频预览图
    private String videoPlayUrl;   //  视频播放url
    private String videoName;      //  视频名称
    private String videoAuthor;    //  视频作者

    protected VideoInfoDate(Parcel in) {
        videoPicUrl = in.readString();
        videoPlayUrl = in.readString();
        videoName = in.readString();
        videoAuthor = in.readString();
    }

    public VideoInfoDate(){

    }

    public static final Creator<VideoInfoDate> CREATOR = new Creator<VideoInfoDate>() {
        @Override
        public VideoInfoDate createFromParcel(Parcel in) {
            return new VideoInfoDate(in);
        }

        @Override
        public VideoInfoDate[] newArray(int size) {
            return new VideoInfoDate[size];
        }
    };

    public String getVideoPicUrl() {
        return videoPicUrl;
    }

    public void setVideoPicUrl(String videoPicUrl) {
        this.videoPicUrl = videoPicUrl;
    }

    public String getVideoPlayUrl() {
        return videoPlayUrl;
    }

    public void setVideoPlayUrl(String videoPlayUrl) {
        this.videoPlayUrl = videoPlayUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoAuthor() {
        return videoAuthor;
    }

    public void setVideoAuthor(String videoAuthor) {
        this.videoAuthor = videoAuthor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoPicUrl);
        dest.writeString(videoPlayUrl);
        dest.writeString(videoName);
        dest.writeString(videoAuthor);
    }
}

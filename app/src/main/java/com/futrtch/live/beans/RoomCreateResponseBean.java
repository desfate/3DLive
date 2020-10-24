package com.futrtch.live.beans;

/**
 * 直接创建直播房间返回数据集
 */
public class RoomCreateResponseBean {
    int returnCode = -1;
    String errInfo = "";
    String RoomID;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    public String getRoomID() {
        return RoomID;
    }

    public void setRoomID(String roomID) {
        RoomID = roomID;
    }
}

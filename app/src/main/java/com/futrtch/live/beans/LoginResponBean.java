package com.futrtch.live.beans;

public class LoginResponBean {
    /**
     * refresh_token : 137570582
     * expires : 86400
     * roomservice_sign : {"sdkAppID":1400425586,"userSig":"eJwtzFELgjAUhuH-sttCzuY2TOgiLwwpSNKIuqt2qqMkY44oov*eqZff88H7YeW6CJ7oWMxEAGzabzLYeLpSzx5bP3pr6pO1ZFjMJYAUSkV6ePBlyWHnSikBAIN6evxNg9BSy1CMFbp12cwd00m*8bZK99Vym9WrxZ0wanbl4ZInvC2S89vOwpQX9Zx9f-hKMQk_","userID":"test"}
     * cos_info : {"Appid":1302092948,"Bucket":"liveinfo-1302092948","SecretId":"AKIDOHYaQxCZT8bpnbdUecKquJRL3UnqAJrB","Region":"ap-shanghai"}
     * token : 1073803848
     */
    private String refresh_token;
    private int expires;
    private Roomservice_signEntity roomservice_sign;
    private Cos_infoEntity cos_info;
    private String token;

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public void setRoomservice_sign(Roomservice_signEntity roomservice_sign) {
        this.roomservice_sign = roomservice_sign;
    }

    public void setCos_info(Cos_infoEntity cos_info) {
        this.cos_info = cos_info;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public int getExpires() {
        return expires;
    }

    public Roomservice_signEntity getRoomservice_sign() {
        return roomservice_sign;
    }

    public Cos_infoEntity getCos_info() {
        return cos_info;
    }

    public String getToken() {
        return token;
    }

    public class Roomservice_signEntity {
        /**
         * sdkAppID : 1400425586
         * userSig : eJwtzFELgjAUhuH-sttCzuY2TOgiLwwpSNKIuqt2qqMkY44oov*eqZff88H7YeW6CJ7oWMxEAGzabzLYeLpSzx5bP3pr6pO1ZFjMJYAUSkV6ePBlyWHnSikBAIN6evxNg9BSy1CMFbp12cwd00m*8bZK99Vym9WrxZ0wanbl4ZInvC2S89vOwpQX9Zx9f-hKMQk_
         * userID : test
         */
        private int sdkAppID;
        private String userSig;
        private String userID;

        public void setSdkAppID(int sdkAppID) {
            this.sdkAppID = sdkAppID;
        }

        public void setUserSig(String userSig) {
            this.userSig = userSig;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public int getSdkAppID() {
            return sdkAppID;
        }

        public String getUserSig() {
            return userSig;
        }

        public String getUserID() {
            return userID;
        }
    }

    public class Cos_infoEntity {
        /**
         * Appid : 1302092948
         * Bucket : liveinfo-1302092948
         * SecretId : AKIDOHYaQxCZT8bpnbdUecKquJRL3UnqAJrB
         * Region : ap-shanghai
         */
        private int Appid;
        private String Bucket;
        private String SecretId;
        private String Region;

        public void setAppid(int Appid) {
            this.Appid = Appid;
        }

        public void setBucket(String Bucket) {
            this.Bucket = Bucket;
        }

        public void setSecretId(String SecretId) {
            this.SecretId = SecretId;
        }

        public void setRegion(String Region) {
            this.Region = Region;
        }

        public int getAppid() {
            return Appid;
        }

        public String getBucket() {
            return Bucket;
        }

        public String getSecretId() {
            return SecretId;
        }

        public String getRegion() {
            return Region;
        }
    }
}

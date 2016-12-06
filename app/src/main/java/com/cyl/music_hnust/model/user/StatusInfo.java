package com.cyl.music_hnust.model.user;

/**
 * Created by 永龙 on 2016/3/15.
 * 个人信息类
 */
public class StatusInfo {
    int num;
    int agree;
    //上传图片的url
    String imgurl;
    String message;
    int status;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAgree() {
        return agree;
    }

    public void setAgree(int agree) {
        this.agree = agree;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "StatusInfo{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

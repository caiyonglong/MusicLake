package com.cyl.music_hnust.model;

/**
 * Created by 永龙 on 2016/3/15.
 * 个人信息类
 */
public class UserInfo {
    User user;
    String message;
    int status;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "message='" + message + '\'' +
                ", user=" + user +
                ", status='" + status + '\'' +
                '}';
    }
}

package com.cyl.musiclake.ui.zone;

import com.cyl.musiclake.ui.my.user.User;

/**
 * Created by 永龙 on 2016/3/15.
 * 评论类
 */
public class Comment {

    private User user;
    private String user_id;
    private String secret_id;
    private String comment;
    private String comment_time;
    private String img_id;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getSecret_id() {
        return secret_id;
    }

    public void setSecret_id(String secret_id) {
        this.secret_id = secret_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + comment + '\'' +
                ", user=" + user +
                ", user_id='" + user_id + '\'' +
                ", secret_id='" + secret_id + '\'' +
                ", comment_time='" + comment_time + '\'' +
                ", img_id='" + img_id + '\'' +
                '}';
    }
}

package com.cyl.music_hnust.bean;

/**
 * Created by 永龙 on 2016/3/15.
 * 评论类
 */
public class Comment {

    private User user;
    private String commentContent;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

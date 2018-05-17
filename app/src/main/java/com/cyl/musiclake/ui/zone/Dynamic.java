package com.cyl.musiclake.ui.zone;


import com.cyl.musiclake.ui.my.user.User;

/**
 * 动态 Dynamic
 *
 */
public class Dynamic {
    private User user;
    private String user_id;
    private String secret_id;
    private String secret_content;
    private String secret_time;
    private String secret_agreeNum;
    private String secret_replyNum;
    private String secret_status;
    private String report_num;
    private int isAgree;

    private String content;
    private String time;
    private int love;
    private int comment;
    private boolean myLove;//赞
    private String dynamic_id;

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public boolean isMyLove() {
        return myLove;
    }

    public void setMyLove(boolean myLove) {
        this.myLove = myLove;
    }
}

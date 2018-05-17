package com.cyl.musiclake.ui.zone;

import com.cyl.musiclake.ui.my.user.User;

/**
 * 作者：yonglong on 2016/10/1 10:27
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */

public class Secret {
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

    public int getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(int isAgree) {
        this.isAgree = isAgree;
    }

    public String getReport_num() {
        return report_num;
    }

    public void setReport_num(String report_num) {
        this.report_num = report_num;
    }

    public String getSecret_agreeNum() {
        return secret_agreeNum;
    }

    public void setSecret_agreeNum(String secret_agreeNum) {
        this.secret_agreeNum = secret_agreeNum;
    }

    public String getSecret_content() {
        return secret_content;
    }

    public void setSecret_content(String secret_content) {
        this.secret_content = secret_content;
    }

    public String getSecret_id() {
        return secret_id;
    }

    public void setSecret_id(String secret_id) {
        this.secret_id = secret_id;
    }

    public String getSecret_replyNum() {
        return secret_replyNum;
    }

    public void setSecret_replyNum(String secret_replyNum) {
        this.secret_replyNum = secret_replyNum;
    }

    public String getSecret_status() {
        return secret_status;
    }

    public void setSecret_status(String secret_status) {
        this.secret_status = secret_status;
    }

    public String getSecret_time() {
        return secret_time;
    }

    public void setSecret_time(String secret_time) {
        this.secret_time = secret_time;
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

    @Override
    public String toString() {
        return "Secret{" +
                "isAgree=" + isAgree +
                ", user=" + user +
                ", user_id='" + user_id + '\'' +
                ", secret_id='" + secret_id + '\'' +
                ", secret_content='" + secret_content + '\'' +
                ", secret_time='" + secret_time + '\'' +
                ", secret_agreeNum='" + secret_agreeNum + '\'' +
                ", secret_replyNum='" + secret_replyNum + '\'' +
                ", secret_status='" + secret_status + '\'' +
                ", report_num='" + report_num + '\'' +
                '}';
    }
}

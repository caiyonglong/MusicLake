package com.cyl.music_hnust.model;

/**
 * Created by 永龙 on 2016/3/15.
 * 个人信息类
 */
public class User {
    private String user_id;  //学号
    private String user_name;  //姓名
    private String user_sex;   //性别
    private String user_college;  //学院
    private String user_major;  //专业
    private String user_class;  //班级

    private String user_img;   //头像
    private String user_email;  //邮箱
    private String phone;  //手机号
    private String nick;  //昵称

    private boolean IsSecret;  //用户是否保密


    public String getUser_class() {
        return user_class;
    }

    public void setUser_class(String user_class) {
        this.user_class = user_class;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUser_college() {
        return user_college;
    }

    public void setUser_college(String user_college) {
        this.user_college = user_college;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUser_major() {
        return user_major;
    }

    public void setUser_major(String user_major) {
        this.user_major = user_major;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public boolean isSecret() {
        return IsSecret;
    }

    public void setSecret(boolean secret) {
        IsSecret = secret;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

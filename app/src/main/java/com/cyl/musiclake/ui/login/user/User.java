package com.cyl.musiclake.ui.login.user;

import java.io.Serializable;

/**
 * Created by 永龙 on 2016/3/15.
 * 个人信息类
 */
public class User implements Serializable {
    private String id = "";  //id
    private String name = "";  //姓名
    private String sex = "";   //性别
    private String college = "";  //学院
    private String major = "";  //专业
    private String classes = "";  //班级

    private String url = "";   //头像
    private String email = "";  //邮箱
    private String phone = "";  //手机号
    private String nick = "";  //昵称
    private String password = "";  //密码
    private int secret = 0;  //用户是否保密

    public User() {
    }

    public User(String id, String name, String sex, String college, String major, String classes, String img, String email, String phone, String nick, String password, int secret) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.college = college;
        this.major = major;
        this.classes = classes;
        this.url = img;
        this.email = email;
        this.phone = phone;
        this.nick = nick;
        this.password = password;
        this.secret = secret;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSecret() {
        return secret;
    }

    public void setSecret(int secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "User{" +
                "nick='" + nick + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", college='" + college + '\'' +
                ", major='" + major + '\'' +
                ", class='" + classes + '\'' +
                ", img='" + url + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", secret=" + secret +
                '}';
    }
}

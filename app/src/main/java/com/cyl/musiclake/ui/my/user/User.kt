package com.cyl.musiclake.ui.my.user

import java.io.Serializable

/**
 * Created by 永龙 on 2016/3/15.
 * 个人信息类
 */
class User : Serializable {
    var id = ""  //id
    var name = ""  //姓名
    var sex = ""   //性别
    var college = ""  //学院
    var major = ""  //专业
    var classes = ""  //班级
    var type = ""  //类型

    var avatar = ""   //头像
    var email = ""  //邮箱
    var phone = ""  //手机号
    var nick = ""  //昵称
    var token: String? = null//访问令牌
    var secret = 0  //用户是否保密

    constructor() {}

    constructor(id: String, name: String, sex: String, college: String, major: String, classes: String, img: String, email: String, phone: String, nickname: String, secret: Int) {
        this.id = id
        this.name = name
        this.sex = sex
        this.college = college
        this.major = major
        this.classes = classes
        this.avatar = img
        this.email = email
        this.phone = phone
        this.nick = nickname
        this.secret = secret
    }

    override fun toString(): String {
        return "User{" +
                "nickname='" + nick + '\''.toString() +
                ", id='" + id + '\''.toString() +
                ", name='" + name + '\''.toString() +
                ", sex='" + sex + '\''.toString() +
                ", college='" + college + '\''.toString() +
                ", major='" + major + '\''.toString() +
                ", class='" + classes + '\''.toString() +
                ", img='" + avatar + '\''.toString() +
                ", email='" + email + '\''.toString() +
                ", phone='" + phone + '\''.toString() +
                ", password='" + token + '\''.toString() +
                ", secret=" + secret +
                ", token=" + token +
                '}'.toString()
    }
}

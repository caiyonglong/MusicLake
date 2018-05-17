package com.cyl.musiclake.view.custom;

/**
 * Created by Mr_immortalZ on 2016/5/3.
 * email : mr_immortalz@qq.com
 */
public class Info {
    private String portraitId;//头像id
    private String name;//名字
    private boolean sex;//false为男，true为女
    private float distance;//距离

    public String getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(String portraitId) {
        this.portraitId = portraitId;
    }


    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}

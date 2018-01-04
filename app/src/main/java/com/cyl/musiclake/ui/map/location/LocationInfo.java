package com.cyl.musiclake.ui.map.location;

import java.util.List;

/**
 * Created by 永龙 on 2016/3/15.
 * 评论类
 */
public class LocationInfo {

    List<Location> data;
    String message;
    int status;

    public List<Location> getData() {
        return data;
    }

    public void setData(List<Location> data) {
        this.data = data;
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
}

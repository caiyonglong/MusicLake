package com.cyl.musiclake.ui.map.location;

import com.cyl.musiclake.ui.my.user.User;

/**
 * Created by 永龙 on 2016/3/22.
 */
public class Location {
    private User user;
    private String user_id;
    private String user_song;
    private String location_time;
    private double location_longitude; //纬度
    private double location_latitude; //经度

    public double getLocation_latitude() {
        return location_latitude;
    }

    public void setLocation_latitude(double location_latitude) {
        this.location_latitude = location_latitude;
    }

    public double getLocation_longitude() {
        return location_longitude;
    }

    public void setLocation_longitude(double location_longitude) {
        this.location_longitude = location_longitude;
    }

    public String getLocation_time() {
        return location_time;
    }

    public void setLocation_time(String location_time) {
        this.location_time = location_time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUser_song() {
        return user_song;
    }

    public void setUser_song(String user_song) {
        this.user_song = user_song;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

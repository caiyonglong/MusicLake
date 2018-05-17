package com.cyl.musiclake.data.download;

import android.content.ContentValues;

/**
 * Created by yonglong on 2018/1/23.
 */

public class TasksManagerModel {
    public final static String ID = "id";
    public final static String MID = "mid";
    public final static String NAME = "name";
    public final static String URL = "url";
    public final static String PATH = "path";
    public final static String FINISH = "finish";

    private int id;
    private String mid;
    private String name;
    private String url;
    private String path;
    private boolean finish;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(ID, id);
        cv.put(MID, mid);
        cv.put(NAME, name);
        cv.put(URL, url);
        cv.put(PATH, path);
        cv.put(FINISH, finish);
        return cv;
    }
}
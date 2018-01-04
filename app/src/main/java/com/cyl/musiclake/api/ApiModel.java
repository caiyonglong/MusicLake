package com.cyl.musiclake.api;

/**
 * Created by D22434 on 2018/1/3.
 */

public class ApiModel<T> {

    private String message;
    private String status;
    private T data;

    public ApiModel(String message, String status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

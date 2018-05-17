package com.cyl.musiclake.ui.zone;

import java.util.List;

/**
 * 作者：yonglong on 2016/10/1 10:29
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */

public class SecretInfo {
    List<Secret> data;
    String message;
    int status;

    public List<Secret> getData() {
        return data;
    }

    public void setData(List<Secret> data) {
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

    @Override
    public String toString() {
        return "SecretInfo{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

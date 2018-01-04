package com.cyl.musiclake.ui.zone;

import java.util.List;

/**
 * Created by 永龙 on 2016/3/15.
 * 评论类
 */
public class CommentInfo {

    Secret data;
    List<Comment> comment;
    String message;
    int status;

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public Secret getData() {
        return data;
    }

    public void setData(Secret data) {
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
        return "CommentInfo{" +
                "comment=" + comment +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}

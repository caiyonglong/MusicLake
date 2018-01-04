package com.cyl.musiclake.ui.music.model;

/**
 * Created by yonglong on 2016/12/28.
 */

public class Singer {
    /**
     * "code": 0,
     * "status": "success",
     * "msg": "数据请求成功",
     * "data": {
     * "singername": "陈奕迅",
     * "image": "http://img1.music.response.itmf.cn/uploadpic/pass/softhead/400/20151103/20151103182436333557.jpg"
     * }
     */
    public int code;
    public String status;
    public String msg;
    public Data data;

    public class Data {
        public String singername;
        public String image;

        @Override
        public String toString() {
            return "Data{" +
                    "singername='" + singername + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Singer{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}

package com.cyl.music_hnust.model.music;

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
    int code;
    String status;
    String msg;
    Data data;

    public class Data {
        String singername;
        String image;

        public String getSingername() {
            return singername;
        }

        public void setSingername(String singername) {
            this.singername = singername;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "singername='" + singername + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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

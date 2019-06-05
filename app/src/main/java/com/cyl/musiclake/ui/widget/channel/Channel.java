package com.cyl.musiclake.ui.widget.channel;

/**
 * @author caochengzhi
 * @date 19/03/13
 */
public class Channel {
    String channelName;
    int channelBelong;
    Object obj;
    int code = -1;

    public Channel(String channelName) {
        this(channelName, 1, null);
    }

    /**
     * @param channelName   频道名称
     * @param channelBelong 频道归属板块
     * @param obj           频道额外属性
     */
    public Channel(String channelName, int channelBelong, Object obj) {
        this.channelName = channelName;
        this.channelBelong = channelBelong;
        this.obj = obj;
    }

    /**
     * @param channelName   频道名称
     * @param channelBelong 频道归属板块
     */
    public Channel(String channelName, int channelBelong) {
        this(channelName, channelBelong, null);
    }

    /**
     * @param channelName 频道名称
     * @param obj         频道额外属性
     */
    public Channel(String channelName, Object obj) {
        this(channelName, 1, obj);
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", obj=" + obj +
                '}';
    }
}

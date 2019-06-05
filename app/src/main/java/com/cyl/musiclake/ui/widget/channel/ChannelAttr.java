package com.cyl.musiclake.ui.widget.channel;

import android.graphics.PointF;

class ChannelAttr {
    static final int TITLE = 0x01;
    static final int CHANNEL = 0x02;

    /**
     * view类型
     */
    int type;

    /**
     * view坐标
     */
    PointF coordinate;

    /**
     * view所在的channelGroups位置
     */
    int groupIndex;

    /**
     * 频道实体
     */
    Channel channel;
}

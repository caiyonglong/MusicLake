package com.cyl.musiclake.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by master on 2018/4/26.
 */

public class QueryInfo extends DataSupport {
    private String name;

    public QueryInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

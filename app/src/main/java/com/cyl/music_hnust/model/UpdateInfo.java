package com.cyl.music_hnust.model;

/**
 * 作者：yonglong on 2016/9/23 09:45
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class UpdateInfo {
    public String name;
    public String version;
    public String changelog;
    public int updated_at;
    public String versionShort;
    public String build;
    public String installUrl;
    public String install_url;
    public String direct_install_url;
    public String update_url;
    public BinaryBean binary;

    public static class BinaryBean {
        public int fsize;
    }
}

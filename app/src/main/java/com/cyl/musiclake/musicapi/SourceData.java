package com.cyl.musiclake.musicapi;

import java.util.List;

/**
 * Created by master on 2018/4/7.
 */

public class SourceData {
    /**
     * cp : false
     * id : 1769253963
     * name : 认真的雪
     * album : {"id":358513,"name":"未完成的歌","cover":"https://pic.xiami.net/images/album/img44/7244/3585131260327414_2.jpg"}
     * source : xiami
     * artists : [{"id":7244,"name":"薛之谦","avatar":"http://pic.xiami.net/images/artistlogo/24/14688337551624_1.jpg"}]
     * commentId : 1769253963
     */

    private boolean cp;
    private String id;
    private String name;
    private AlbumBean album;
    private String source;
    private List<ArtistsBean> artists;

    public boolean isCp() {
        return cp;
    }

    public void setCp(boolean cp) {
        this.cp = cp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlbumBean getAlbum() {
        return album;
    }

    public void setAlbum(AlbumBean album) {
        this.album = album;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<ArtistsBean> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistsBean> artists) {
        this.artists = artists;
    }

    public static class AlbumBean {
        /**
         * id : 358513
         * name : 未完成的歌
         * cover : https://pic.xiami.net/images/album/img44/7244/3585131260327414_2.jpg
         */

        private String id;
        private String name;
        private String cover;

        public AlbumBean(String id, String name, String cover) {
            this.id = id;
            this.name = name;
            this.cover = cover;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }

    public static class ArtistsBean {
        /**
         * id : 7244
         * name : 薛之谦
         * avatar : http://pic.xiami.net/images/artistlogo/24/14688337551624_1.jpg
         */
        public ArtistsBean(String id, String name) {
            this.id = id;
            this.name = name;
        }

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "ArtistsBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SourceDataBean{" +
                "cp=" + cp +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", album=" + album +
                ", source='" + source + '\'' +
                ", artists=" + artists +
                '}';
    }
}

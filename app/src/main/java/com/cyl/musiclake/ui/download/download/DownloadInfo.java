package com.cyl.musiclake.ui.download.download;

/**
 * 作者：yonglong on 2016/9/9 04:08
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class DownloadInfo {
    private JBitrate bitrate;
    private SongInfo songinfo;

    public SongInfo getSonginfo() {
        return songinfo;
    }

    public void setSonginfo(SongInfo songinfo) {
        this.songinfo = songinfo;
    }

    public JBitrate getBitrate() {
        return bitrate;
    }

    public void setBitrate(JBitrate bitrate) {
        this.bitrate = bitrate;
    }

    public static class JBitrate {
        int file_duration;
        String file_link;

        public int getFile_duration() {
            return file_duration;
        }

        public void setFile_duration(int file_duration) {
            this.file_duration = file_duration;
        }

        public String getFile_link() {
            return file_link;
        }

        public void setFile_link(String file_link) {
            this.file_link = file_link;
        }
    }
    public static class SongInfo {
        private String pic_big;
        private String pic_small;
        private String lrclink;
        private String song_id;
        private String title;
        private String ting_uid;
        private String album_title;
        private String author;

        public String getAlbum_title() {
            return album_title;
        }

        public void setAlbum_title(String album_title) {
            this.album_title = album_title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getLrclink() {
            return lrclink;
        }

        public void setLrclink(String lrclink) {
            this.lrclink = lrclink;
        }

        public String getPic_big() {
            return pic_big;
        }

        public void setPic_big(String pic_big) {
            this.pic_big = pic_big;
        }

        public String getPic_small() {
            return pic_small;
        }

        public void setPic_small(String pic_small) {
            this.pic_small = pic_small;
        }

        public String getSong_id() {
            return song_id;
        }

        public void setSong_id(String song_id) {
            this.song_id = song_id;
        }

        public String getTing_uid() {
            return ting_uid;
        }

        public void setTing_uid(String ting_uid) {
            this.ting_uid = ting_uid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

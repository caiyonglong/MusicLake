package com.cyl.music_hnust.model.music;

import java.util.List;

/**
 * 排行榜歌单
 */
public class OnlinePlaylists {
    private List<Billboard> content;

    public List<Billboard> getContent() {
        return content;
    }

    public void setContent(List<Billboard> content) {
        this.content = content;
    }

    public class Billboard {
        private List<MusicLists> content;
        String type;
        String name;
        String comment;
        String pic_s192;

        public List<MusicLists> getContent() {
            return content;
        }

        public void setContent(List<MusicLists> content) {
            this.content = content;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic_s192() {
            return pic_s192;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public class MusicLists {
            String title;
            String author;
            String song_id;
            String album_id;
            String album_title;
            String rank_change;
            String all_rate;

            public String getAlbum_id() {
                return album_id;
            }

            public void setAlbum_id(String album_id) {
                this.album_id = album_id;
            }

            public String getAlbum_title() {
                return album_title;
            }

            public void setAlbum_title(String album_title) {
                this.album_title = album_title;
            }

            public String getAll_rate() {
                return all_rate;
            }

            public void setAll_rate(String all_rate) {
                this.all_rate = all_rate;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getRank_change() {
                return rank_change;
            }

            public void setRank_change(String rank_change) {
                this.rank_change = rank_change;
            }

            public String getSong_id() {
                return song_id;
            }

            public void setSong_id(String song_id) {
                this.song_id = song_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            @Override
            public String toString() {
                return "MusicLists{" +
                        "title='" + title + '\'' +
                        ", author='" + author + '\'' +
                        ", song_id='" + song_id + '\'' +
                        ", album_id='" + album_id + '\'' +
                        ", album_title='" + album_title + '\'' +
                        ", rank_change='" + rank_change + '\'' +
                        ", all_rate='" + all_rate + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "OnlinePlaylists{" +
                "content=" + content +
                '}';
    }
}

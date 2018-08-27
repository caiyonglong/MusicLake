//package com.cyl.musiclake.api.baidu;
//
//import java.util.List;
//
///**
// * 百度音乐
// * 排行榜歌单
// */
//public class BaiduMusicList {
//    /**
//     * 1.新歌榜
//     * 2.热歌榜
//     * <p>
//     * 20.华语金曲榜
//     * 21.欧美金曲榜
//     * 24.影视金曲榜
//     * 23.情歌对唱榜
//     * 25.网络歌曲榜
//     * 22.经典老歌榜
//     * 11.摇滚榜
//     * <p>
//     * 6.KTV热歌榜
//     * 8.Billboard
//     * 7.叱咤歌曲榜
//     */
//    private List<Billboard> content;
//
//    public List<Billboard> getContent() {
//        return content;
//    }
//
//    public void setContent(List<Billboard> content) {
//        this.content = content;
//    }
//
//    public class Billboard {
//        private List<MusicLists> content;
//        String type;
//        String name;
//        String comment;
//        String pic_s192;
//        String pic_s210;
//        String pic_s260;
//        String pic_s444;
//
//        public List<MusicLists> getContent() {
//            return content;
//        }
//
//        public void setContent(List<MusicLists> content) {
//            this.content = content;
//        }
//
//        public String getComment() {
//            return comment;
//        }
//
//        public void setComment(String comment) {
//            this.comment = comment;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getPic_s192() {
//            return pic_s192;
//        }
//
//        public void setPic_s192(String pic_s192) {
//            this.pic_s192 = pic_s192;
//        }
//
//        public String getPic_s210() {
//            return pic_s210;
//        }
//
//        public void setPic_s210(String pic_s210) {
//            this.pic_s210 = pic_s210;
//        }
//
//        public String getPic_s260() {
//            return pic_s260;
//        }
//
//        public void setPic_s260(String pic_s260) {
//            this.pic_s260 = pic_s260;
//        }
//
//        public String getPic_s444() {
//            return pic_s444;
//        }
//
//        public void setPic_s444(String pic_s444) {
//            this.pic_s444 = pic_s444;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public class MusicLists {
//            String title;
//            String author;
//            String song_id;
//            String album_id;
//            String album_title;
//            String rank_change;
//            String all_rate;
//
//            public String getAlbum_id() {
//                return album_id;
//            }
//
//            public void setAlbum_id(String album_id) {
//                this.album_id = album_id;
//            }
//
//            public String getAlbum_title() {
//                return album_title;
//            }
//
//            public void setAlbum_title(String album_title) {
//                this.album_title = album_title;
//            }
//
//            public String getAll_rate() {
//                return all_rate;
//            }
//
//            public void setAll_rate(String all_rate) {
//                this.all_rate = all_rate;
//            }
//
//            public String getAuthor() {
//                return author;
//            }
//
//            public void setAuthor(String author) {
//                this.author = author;
//            }
//
//            public String getRank_change() {
//                return rank_change;
//            }
//
//            public void setRank_change(String rank_change) {
//                this.rank_change = rank_change;
//            }
//
//            public String getSong_id() {
//                return song_id;
//            }
//
//            public void setSong_id(String song_id) {
//                this.song_id = song_id;
//            }
//
//            public String getTitle() {
//                return title;
//            }
//
//            public void setTitle(String title) {
//                this.title = title;
//            }
//
//            @Override
//            public String toString() {
//                return "MusicLists{" +
//                        "title='" + title + '\'' +
//                        ", author='" + author + '\'' +
//                        ", song_id='" + song_id + '\'' +
//                        ", album_id='" + album_id + '\'' +
//                        ", album_title='" + album_title + '\'' +
//                        ", rank_change='" + rank_change + '\'' +
//                        ", all_rate='" + all_rate + '\'' +
//                        '}';
//            }
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "BaiduMusicList{" +
//                "content=" + content +
//                '}';
//    }
//}

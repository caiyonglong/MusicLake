package com.cyl.musiclake.api.doupan;

import java.util.List;

/**
 * Author   : D22434
 * version  : 2018/3/30
 * function :
 */

public class DoubanMusic {

    /**
     * count : 1
     * start : 0
     * total : 278
     * musics : [{"rating":{"max":10,"average":"5.7","numRaters":1708,"min":0},"author":[{"name":"薛之谦"}],"alt_title":"","image":"https://img1.doubanio.com/view/subject/s/public/s29421939.jpg","tags":[{"count":443,"name":"薛之谦"},{"count":163,"name":"华语"},{"count":161,"name":"流行"},{"count":145,"name":"2017"},{"count":124,"name":"单曲"},{"count":104,"name":"男声"},{"count":97,"name":"内地"},{"count":88,"name":"男歌手"}],"mobile_link":"https://m.douban.com/music/subject/3729914/","attrs":{"publisher":["海蝶音乐"],"singer":["薛之谦"],"pubdate":["2017-04-11"],"title":["暧昧"],"media":["数字(Digital)"],"tracks":["暧昧"],"version":["单曲"]},"title":"暧昧","alt":"https://music.douban.com/subject/3729914/","id":"3729914"}]
     */

    private int count;
    private int start;
    private int total;
    private List<MusicsBean> musics;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<MusicsBean> getMusics() {
        return musics;
    }

    public void setMusics(List<MusicsBean> musics) {
        this.musics = musics;
    }

    public static class MusicsBean {
        /**
         * rating : {"max":10,"average":"5.7","numRaters":1708,"min":0}
         * author : [{"name":"薛之谦"}]
         * alt_title :
         * image : https://img1.doubanio.com/view/subject/s/public/s29421939.jpg
         * tags : [{"count":443,"name":"薛之谦"},{"count":163,"name":"华语"},{"count":161,"name":"流行"},{"count":145,"name":"2017"},{"count":124,"name":"单曲"},{"count":104,"name":"男声"},{"count":97,"name":"内地"},{"count":88,"name":"男歌手"}]
         * mobile_link : https://m.douban.com/music/subject/3729914/
         * attrs : {"publisher":["海蝶音乐"],"singer":["薛之谦"],"pubdate":["2017-04-11"],"title":["暧昧"],"media":["数字(Digital)"],"tracks":["暧昧"],"version":["单曲"]}
         * title : 暧昧
         * alt : https://music.douban.com/subject/3729914/
         * id : 3729914
         */

        private RatingBean rating;
        private String alt_title;
        private String image;
        private String mobile_link;
        private AttrsBean attrs;
        private String title;
        private String alt;
        private String id;
        private List<AuthorBean> author;
        private List<TagsBean> tags;

        public RatingBean getRating() {
            return rating;
        }

        public void setRating(RatingBean rating) {
            this.rating = rating;
        }

        public String getAlt_title() {
            return alt_title;
        }

        public void setAlt_title(String alt_title) {
            this.alt_title = alt_title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getMobile_link() {
            return mobile_link;
        }

        public void setMobile_link(String mobile_link) {
            this.mobile_link = mobile_link;
        }

        public AttrsBean getAttrs() {
            return attrs;
        }

        public void setAttrs(AttrsBean attrs) {
            this.attrs = attrs;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<AuthorBean> getAuthor() {
            return author;
        }

        public void setAuthor(List<AuthorBean> author) {
            this.author = author;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public static class RatingBean {
            /**
             * max : 10
             * average : 5.7
             * numRaters : 1708
             * min : 0
             */

            private int max;
            private String average;
            private int numRaters;
            private int min;

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public String getAverage() {
                return average;
            }

            public void setAverage(String average) {
                this.average = average;
            }

            public int getNumRaters() {
                return numRaters;
            }

            public void setNumRaters(int numRaters) {
                this.numRaters = numRaters;
            }

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }
        }

        public static class AttrsBean {
            private List<String> publisher;
            private List<String> singer;
            private List<String> pubdate;
            private List<String> title;
            private List<String> media;
            private List<String> tracks;
            private List<String> version;

            public List<String> getPublisher() {
                return publisher;
            }

            public void setPublisher(List<String> publisher) {
                this.publisher = publisher;
            }

            public List<String> getSinger() {
                return singer;
            }

            public void setSinger(List<String> singer) {
                this.singer = singer;
            }

            public List<String> getPubdate() {
                return pubdate;
            }

            public void setPubdate(List<String> pubdate) {
                this.pubdate = pubdate;
            }

            public List<String> getTitle() {
                return title;
            }

            public void setTitle(List<String> title) {
                this.title = title;
            }

            public List<String> getMedia() {
                return media;
            }

            public void setMedia(List<String> media) {
                this.media = media;
            }

            public List<String> getTracks() {
                return tracks;
            }

            public void setTracks(List<String> tracks) {
                this.tracks = tracks;
            }

            public List<String> getVersion() {
                return version;
            }

            public void setVersion(List<String> version) {
                this.version = version;
            }
        }

        public static class AuthorBean {
            /**
             * name : 薛之谦
             */

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class TagsBean {
            /**
             * count : 443
             * name : 薛之谦
             */

            private int count;
            private String name;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}

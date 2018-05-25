package com.cyl.musiclake.musicapi;

import java.util.List;

/**
 * Created by master on 2018/5/15.
 */

public class SearchResult {

    /**

     */

    private boolean status;
    private DataBean data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**

         */

        private NeteaseBean netease;
        private QqBean qq;
        private XiamiBean xiami;

        public NeteaseBean getNetease() {
            return netease;
        }

        public void setNetease(NeteaseBean netease) {
            this.netease = netease;
        }

        public QqBean getQq() {
            return qq;
        }

        public void setQq(QqBean qq) {
            this.qq = qq;
        }

        public XiamiBean getXiami() {
            return xiami;
        }

        public void setXiami(XiamiBean xiami) {
            this.xiami = xiami;
        }

        public static class NeteaseBean {
            /**
             */

            private int total;
            private List<SongsBean> songs;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<SongsBean> getSongs() {
                return songs;
            }

            public void setSongs(List<SongsBean> songs) {
                this.songs = songs;
            }

            public static class SongsBean {
                /**

                 */

                private AlbumBean album;
                private String name;
                private int id;
                private int commentId;
                private boolean cp;
                private List<ArtistsBean> artists;

                public AlbumBean getAlbum() {
                    return album;
                }

                public void setAlbum(AlbumBean album) {
                    this.album = album;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getCommentId() {
                    return commentId;
                }

                public void setCommentId(int commentId) {
                    this.commentId = commentId;
                }

                public boolean isCp() {
                    return cp;
                }

                public void setCp(boolean cp) {
                    this.cp = cp;
                }

                public List<ArtistsBean> getArtists() {
                    return artists;
                }

                public void setArtists(List<ArtistsBean> artists) {
                    this.artists = artists;
                }

                public static class AlbumBean {
                    /**
                     * id : 38534319
                     * name : TEST
                     * cover : http://p1.music.126.net/R9Zx40i5oCv1xCnM2HBvDQ==/109951163275466550.jpg
                     */

                    private int id;
                    private String name;
                    private String cover;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
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
                     * id : 13790052
                     * name : AKA-趸趸
                     * tns : []
                     * alias : []
                     */

                    private int id;
                    private String name;
                    private List<?> tns;
                    private List<?> alias;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public List<?> getTns() {
                        return tns;
                    }

                    public void setTns(List<?> tns) {
                        this.tns = tns;
                    }

                    public List<?> getAlias() {
                        return alias;
                    }

                    public void setAlias(List<?> alias) {
                        this.alias = alias;
                    }
                }
            }
        }

        public static class QqBean {
            /**

             */

            private String keyword;
            private int total;
            private List<SongsBeanX> songs;

            public String getKeyword() {
                return keyword;
            }

            public void setKeyword(String keyword) {
                this.keyword = keyword;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<SongsBeanX> getSongs() {
                return songs;
            }

            public void setSongs(List<SongsBeanX> songs) {
                this.songs = songs;
            }

            public static class SongsBeanX {
                /**

                 */

                private AlbumBeanX album;
                private String name;
                private String id;
                private int commentId;
                private boolean cp;
                private List<ArtistsBeanX> artists;

                public AlbumBeanX getAlbum() {
                    return album;
                }

                public void setAlbum(AlbumBeanX album) {
                    this.album = album;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public int getCommentId() {
                    return commentId;
                }

                public void setCommentId(int commentId) {
                    this.commentId = commentId;
                }

                public boolean isCp() {
                    return cp;
                }

                public void setCp(boolean cp) {
                    this.cp = cp;
                }

                public List<ArtistsBeanX> getArtists() {
                    return artists;
                }

                public void setArtists(List<ArtistsBeanX> artists) {
                    this.artists = artists;
                }

                public static class AlbumBeanX {
                    /**
                     * id : 1290825
                     * name : Smooth
                     * cover : https://y.gtimg.cn/music/photo_new/T002R300x300M000003jp3h81eT9Qn.jpg
                     */

                    private int id;
                    private String name;
                    private String cover;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
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

                public static class ArtistsBeanX {
                    /**

                     */

                    private int id;
                    private String mid;
                    private String name;
                    private String title;
                    private String title_hilight;
                    private int type;
                    private int uin;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getTitle_hilight() {
                        return title_hilight;
                    }

                    public void setTitle_hilight(String title_hilight) {
                        this.title_hilight = title_hilight;
                    }

                    public int getType() {
                        return type;
                    }

                    public void setType(int type) {
                        this.type = type;
                    }

                    public int getUin() {
                        return uin;
                    }

                    public void setUin(int uin) {
                        this.uin = uin;
                    }
                }
            }
        }

        public static class XiamiBean {
            /**
             * total : 5261
             * songs : [{"album":{"id":497593,"name":"海猿～オリジナル・サウンドトラック","cover":"https://pic.xiami.net/images/album/img19/32319/4975931330507218_2.jpg"},"artists":[{"id":32319,"name":"佐藤直紀","avatar":"http://pic.xiami.net/images/artist/12320839104109_1.jpg"}],"name":"Test","id":1770842424,"commentId":1770842424,"cp":false},{"album":{"id":2102710027,"name":"Mike Gao - Shifty ft. Anderson Paak and Doja Cat","cover":"https://pic.xiami.net/images/album/img48/125148/2818601489281860_2.jpg"},"artists":[{"id":125148,"name":"Mike Gao","avatar":"http://pic.xiami.net/images/artistlogo/43/14892798745043_1.jpg"}],"name":"Mike Gao - Shifty ft. Anderson Paak & Doja Cat","id":1795630336,"commentId":1795630336,"cp":false},{"album":{"id":2102660539,"name":"Da Rhyizm","cover":"https://pic.xiami.net/images/album/img22/131/5847ac282dfef_6569022_1481092136_2.jpg"},"artists":[{"id":120840,"name":"Da-little","avatar":"http://pic.xiami.net/images/artistlogo/51/13518357891951_1.jpg"}],"name":"glow feat. [TEST]","id":1795318198,"commentId":1795318198,"cp":false},{"album":{"id":180757,"name":"Fast Times at Barrington High","cover":"https://pic.xiami.net/images/album/img9/26709/180757_2.jpg"},"artists":[{"id":26709,"name":"The Academy Is...","avatar":"http://pic.xiami.net/images/artist/12196331048090_1.jpg"}],"name":"Test ","id":3031975,"commentId":3031975,"cp":false},{"album":{"id":8746,"name":"Jade-1","cover":"https://pic.xiami.net/images/album/img69/1569/87461350287062_2.jpg"},"artists":[{"id":1569,"name":"关心妍","avatar":"http://pic.xiami.net/images/artistlogo/73/14359202865773_1.jpg"}],"name":"考验","id":108720,"commentId":108720,"cp":false},{"album":{"id":2102726006,"name":"WWE: The Music, Vol. 4","cover":"https://pic.xiami.net/images/album/img47/535947/15060931490535947_2.jpg"},"artists":[{"id":111645,"name":"Jim Johnston","avatar":"http://pic.xiami.net/images/artistlogo/3/13395894167103_1.png"}],"name":"This Is a Test","id":1795736633,"commentId":1795736633,"cp":false},{"album":{"id":2102669778,"name":"Test","cover":"https://pic.xiami.net/images/album/img72/105/5858b952c0d42_5290672_1482209618_2.jpg"},"artists":[{"id":2110190497,"name":"Saro","avatar":"http://pic.xiami.net/images/artistlogo/38/14822092714038_1.jpg"}],"name":"Test","id":1795405468,"commentId":1795405468,"cp":false},{"album":{"id":1928386911,"name":"Dfsda","cover":"https://pic.xiami.net/images/album/img10/878106010/19283869111428386911_2.jpg"},"artists":[{"id":878106010,"name":"我是红茶至尊官方账号","avatar":"http://pic.xiami.net/images/artistlogo/31/15258315582531_1.jpg"}],"name":"test","id":1774144132,"commentId":1774144132,"cp":false},{"album":{"id":2102669774,"name":"In Loving Memory","cover":"https://pic.xiami.net/images/album/img40/2/5858b85fe57d9_105840_1482209375_2.jpg"},"artists":[{"id":2110190497,"name":"Saro","avatar":"http://pic.xiami.net/images/artistlogo/38/14822092714038_1.jpg"}],"name":"Test","id":1795405442,"commentId":1795405442,"cp":false},{"album":{"id":469689,"name":"Portal 2 (Songs to Test By) (Volume 3)","cover":"https://pic.xiami.net/images/album/img71/98071/4696891318359739_2.jpg"},"artists":[{"id":98071,"name":"Aperture Science Psychoacoustics Laboratory","avatar":"http://pic.xiami.net/images/artistlogo/93/13182725835693_1.jpg"}],"name":"TEST","id":1770532276,"commentId":1770532276,"cp":false},{"album":{"id":1500354893,"name":"Best Of","cover":"https://pic.xiami.net/images/album/img92/354892/3548921400354892_2.jpg"},"artists":[{"id":43565,"name":"Little Dragon","avatar":"http://pic.xiami.net/images/artist/79/12601802321679_1.jpg"}],"name":"Test","id":1773196224,"commentId":1773196224,"cp":false},{"album":{"id":253590,"name":"Little Dragon","cover":"https://pic.xiami.net/images/album/img90/253590/j06237k1ybz_2.jpg"},"artists":[{"id":43565,"name":"Little Dragon","avatar":"http://pic.xiami.net/images/artist/79/12601802321679_1.jpg"}],"name":"Test","id":2904033,"commentId":2904033,"cp":false},{"album":{"id":2102760301,"name":"Test","cover":"https://pic.xiami.net/images/album/img30/297030/1928361496297030_2.jpg"},"artists":[{"id":1630974860,"name":"Gelvetta","avatar":"http://pic.xiami.net/images/artistlogo/48/14309748606548_1.jpg"}],"name":"Test","id":1795948776,"commentId":1795948776,"cp":false},{"album":{"id":126055014,"name":"Test (Original Motion Picture Soundtrack)","cover":"https://pic.xiami.net/images/album/img14/626055014/1260550141426055014_2.jpg"},"artists":[{"id":626055014,"name":"Ceiri Torjussen","avatar":"http://pic.xiami.net/images/artistlogo/36/14260550141236_1.jpg"}],"name":"Xxx Dance","id":1774055962,"commentId":1774055962,"cp":false},{"album":{"id":2100331602,"name":"测试-TEST","cover":"https://pic.xiami.net/images/album/img88/2100022988/21003316021462647732_2.jpg"},"artists":[{"id":2100022988,"name":"Yippee","avatar":"http://pic.xiami.net/images/artistlogo/47/14626997427747_1.jpg"}],"name":"朋友，再见(Bye,see u)","id":1776043572,"commentId":1776043572,"cp":false},{"album":{"id":566351813,"name":"泡泡糖test","cover":"https://pic.xiami.net/images/album/img16/166351116/5663518131366351822_2.jpg"},"artists":[{"id":166351116,"name":"大大泡泡糖","avatar":"http://pic.xiami.net/images/artistlogo/65/13663526734065_1.jpg"}],"name":"01 - Girl's Carnival (Live)","id":1771824356,"commentId":1771824356,"cp":false},{"album":{"id":400153,"name":"Devotion, Discipline, And Denial","cover":"https://pic.xiami.net/images/album/img13/72513/4001531283410212_2.jpg"},"artists":[{"id":72513,"name":"ESA","avatar":"http://pic.xiami.net/images/artist/56/12694887073356_1.jpg"}],"name":"Test","id":1769744462,"commentId":1769744462,"cp":false},{"album":{"id":430231,"name":"The House","cover":"https://pic.xiami.net/images/album/img85/87185/4302311299819472_2.jpg"},"artists":[{"id":87185,"name":"Romantic Couch","avatar":"http://pic.xiami.net/images/artistlogo/73/13014734264173_1.jpg"}],"name":"Test (Makes No Sense)","id":1770084411,"commentId":1770084411,"cp":false},{"album":{"id":278675,"name":"Underwear","cover":"https://pic.xiami.net/images/album/img7/49307/2786751237780423_2.jpg"},"artists":[{"id":49307,"name":"Bobo Stenson","avatar":"http://pic.xiami.net/images/artist/12270632508521_1.jpg"}],"name":"Test","id":3132914,"commentId":3132914,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484888,"commentId":1770484888,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484889,"commentId":1770484889,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484890,"commentId":1770484890,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484891,"commentId":1770484891,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484892,"commentId":1770484892,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484894,"commentId":1770484894,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484895,"commentId":1770484895,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484897,"commentId":1770484897,"cp":false},{"album":{"id":465556,"name":"Best Of Chesky Classics & Jazz & Audiophile Test Disc, Vol. 2","cover":"https://pic.xiami.net/images/album/img58/23258/4655561315905930_2.jpg"},"artists":[{"id":23258,"name":"Various Artists","avatar":"http://pic.xiami.net/images/artistlogo/33/13517382586833_1.jpg"}],"name":"Test","id":1770484898,"commentId":1770484898,"cp":false},{"album":{"id":468881,"name":"Stuck In This Ocean","cover":"https://pic.xiami.net/images/album/img9/97609/4688811317286130_2.jpg"},"artists":[{"id":97609,"name":"Airship","avatar":"http://pic.xiami.net/images/artistlogo/31/13172753748831_1.jpg"}],"name":"Test","id":1770522595,"commentId":1770522595,"cp":false},{"album":{"id":1275767700,"name":"Dino Valente","cover":"https://pic.xiami.net/images/album/img72/2075755272/12757677001375767700_2.jpg"},"artists":[{"id":2075755272,"name":"Dino Valente","avatar":"http://pic.xiami.net/images/artistlogo/38/13757552726038_1.png"}],"name":"Test","id":1772076510,"commentId":1772076510,"cp":false}]
             */

            private int total;
            private List<SongsBeanXX> songs;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<SongsBeanXX> getSongs() {
                return songs;
            }

            public void setSongs(List<SongsBeanXX> songs) {
                this.songs = songs;
            }

            public static class SongsBeanXX {
                /**
                 * album : {"id":497593,"name":"海猿～オリジナル・サウンドトラック","cover":"https://pic.xiami.net/images/album/img19/32319/4975931330507218_2.jpg"}
                 * artists : [{"id":32319,"name":"佐藤直紀","avatar":"http://pic.xiami.net/images/artist/12320839104109_1.jpg"}]
                 * name : Test
                 * id : 1770842424
                 * commentId : 1770842424
                 * cp : false
                 */

                private AlbumBeanXX album;
                private String name;
                private int id;
                private int commentId;
                private boolean cp;
                private List<ArtistsBeanXX> artists;

                public AlbumBeanXX getAlbum() {
                    return album;
                }

                public void setAlbum(AlbumBeanXX album) {
                    this.album = album;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getCommentId() {
                    return commentId;
                }

                public void setCommentId(int commentId) {
                    this.commentId = commentId;
                }

                public boolean isCp() {
                    return cp;
                }

                public void setCp(boolean cp) {
                    this.cp = cp;
                }

                public List<ArtistsBeanXX> getArtists() {
                    return artists;
                }

                public void setArtists(List<ArtistsBeanXX> artists) {
                    this.artists = artists;
                }

                public static class AlbumBeanXX {
                    /**
                     * id : 497593
                     * name : 海猿～オリジナル・サウンドトラック
                     * cover : https://pic.xiami.net/images/album/img19/32319/4975931330507218_2.jpg
                     */

                    private int id;
                    private String name;
                    private String cover;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
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

                public static class ArtistsBeanXX {
                    /**
                     * id : 32319
                     * name : 佐藤直紀
                     * avatar : http://pic.xiami.net/images/artist/12320839104109_1.jpg
                     */

                    private int id;
                    private String name;
                    private String avatar;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getAvatar() {
                        return avatar;
                    }

                    public void setAvatar(String avatar) {
                        this.avatar = avatar;
                    }
                }
            }
        }
    }
}

//package com.cyl.musicapi.xiami;
//
//import com.cyl.musiclake.bean.Music;
//import com.cyl.musiclake.common.Constants;
//import com.cyl.musiclake.api.net.ApiManager;
//import com.cyl.musiclake.utils.FileUtils;
//import com.cyl.musiclake.utils.LogUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import io.reactivex.Observable;
//
///**
// * Created by yonglong on 2018/1/15.
// */
//
//public class XiamiServiceImpl {
//
//    private static final String TAG = "XiamiServiceImpl";
//
//    public static XiamiService getApiService() {
//        return ApiManager.getInstance().create(XiamiService.class, Constants.BASE_XIAMI_URL);
//    }
//
//    /**
//     * 搜索虾米音乐
//     *
//     * @param key   关键字
//     * @param limit
//     * @param page
//     * @return
//     */
//    @SuppressWarnings({"unchecked", "varargs"})
//    public static Observable<List<Music>> search(String key, int limit, int page) {
//        Map<String, String> params = new HashMap<>();
//        params.put("v", "2.0"); //page
//        params.put("page", String.valueOf(page)); //page
//        params.put("limit", String.valueOf(limit));//limit
//        params.put("key", key);// key
//        params.put("r", "search/songs");
//        params.put("app_key", "1");
//        params.put("format", "json");
//        return getApiService().searchByXiaMi(params)
//                .flatMap(xiaMiModel -> {
//                    List<Music> musicList = new ArrayList<>();
//                    List<XiamiModel.DataBean.SongsBean> songs = xiaMiModel.getData().getSongs();
//                    for (int i = 0; i < songs.size(); i++) {
//                        XiamiModel.DataBean.SongsBean song = songs.get(i);
//                        Music music = new Music();
//                        music.setType(Constants.XIAMI);
//                        music.setOnline(true);
//                        music.setMid(String.valueOf(song.getSong_id()));
//                        music.setTitle(song.getSong_name());
//                        music.setArtist(song.getArtist_name());
//                        music.setArtistId(String.valueOf(song.getArtist_id()));
//                        music.setAlbum(song.getAlbum_name());
//                        music.setAlbumId(String.valueOf(song.getAlbum_id()));
//                        music.setUri(song.getListen_file());
//                        String cover = song.getAlbum_logo() + "@1e_1c_0i_1o_100Q_250w_250h";
//                        String coverBig = song.getAlbum_logo() + "@1e_1c_0i_1o_100Q_400w_400h";
//                        String coverSmall = song.getAlbum_logo() + "@1e_1c_0i_1o_100Q_150w_150h";
//                        music.setCoverUri(cover);
//                        music.setCoverBig(coverBig);
//                        music.setCoverSmall(coverSmall);
//                        music.setLyric(song.getLyric());
//                        musicList.add(music);
//                    }
//                    LogUtil.e("search", page + "--" + limit + "xiami :" + musicList.size());
//                    return Observable.fromArray(musicList);
//                });
//    }
//
//    @SuppressWarnings({"unchecked", "varargs"})
//    public static Observable<Music> getMusicInfo(Music musicInfo) {
//        Map<String, String> params = new HashMap<>();
//        params.put("v", "2.0"); //page
//        params.put("page", "1"); //page
//        params.put("limit", "1");//limit
//        params.put("key", musicInfo.getTitle() + "-" + musicInfo.getArtist());// key
//        params.put("r", "search/songs");
//        params.put("app_key", "1");
//        params.put("format", "json");
//        return getApiService().searchByXiaMi(params)
//                .flatMap(xiaMiModel -> {
//                    List<Music> musicList = new ArrayList<>();
//                    List<XiamiModel.DataBean.SongsBean> songs = xiaMiModel.getData().getSongs();
//                    for (int i = 0; i < songs.size(); i++) {
//                        XiamiModel.DataBean.SongsBean song = songs.get(i);
//                        Music music = new Music();
//                        music.setType(Constants.XIAMI);
//                        music.setOnline(true);
//                        music.setMid(String.valueOf(song.getSong_id()));
//                        music.setTitle(song.getSong_name());
//                        music.setArtist(song.getArtist_name());
//                        music.setArtistId(String.valueOf(song.getArtist_id()));
//                        music.setAlbum(song.getAlbum_name());
//                        music.setAlbumId(String.valueOf(song.getAlbum_id()));
//                        music.setUri(song.getListen_file());
//                        String cover = song.getAlbum_logo() + "@1e_1c_0i_1o_100Q_250w_250h";
//                        String coverBig = song.getAlbum_logo() + "@1e_1c_0i_1o_100Q_400w_400h";
//                        String coverSmall = song.getAlbum_logo() + "@1e_1c_0i_1o_100Q_150w_150h";
//                        music.setCoverUri(cover);
//                        music.setCoverBig(coverBig);
//                        music.setCoverSmall(coverSmall);
//                        music.setLyric(song.getLyric());
//                        musicList.add(music);
//                    }
//                    LogUtil.e("search", "xiami :" + musicList.size());
//                    return Observable.fromArray(musicList.get(0));
//                });
//    }
//
//
//    @SuppressWarnings({"unchecked", "varargs"})
//    public static Observable<String> getXimaiLyric(Music music) {
//        //本地歌词路径
//        String mLyricPath = FileUtils.getLrcDir() + music.getTitle() + "-" + music.getArtist() + ".lrc";
//        //网络歌词
//        String mLyricUrl = music.getLyric();
//        if (FileUtils.exists(mLyricPath)) {
//            return Observable.create(emitter -> {
//                try {
//                    String lyric = FileUtils.readFile(mLyricPath);
//                    emitter.onNext(lyric);
//                    emitter.onComplete();
//                } catch (Exception e) {
//                    emitter.onError(e);
//                }
//            });
//        }
//        return getApiService().getXiamiLyric(mLyricUrl)
//                .flatMap(xiaMiLyricInfo -> {
//                    String lyric = xiaMiLyricInfo.string();
//                    //保存文件
//                    boolean save = FileUtils.writeText(mLyricPath, lyric);
//                    LogUtil.e("保存网络歌词：" + save);
//                    return Observable.fromArray(lyric);
//                });
//    }
//}

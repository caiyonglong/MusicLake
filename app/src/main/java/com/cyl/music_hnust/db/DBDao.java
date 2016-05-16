package com.cyl.music_hnust.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyl.music_hnust.list.FavoriteList;
import com.cyl.music_hnust.list.FolderList;
import com.cyl.music_hnust.list.LyricList;
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.utils.FolderInfo;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.utils.ScanInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 永龙 on 2016/2/23.
 */
public class DBDao {
    private DBHelper helper;
    private SQLiteDatabase db;

    /**
     * 创建和初始化数据库，使用完记得调用close方法关闭数据库
     *
     * @param context
     */
    public DBDao(Context context) {
        // TODO Auto-generated constructor stub
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 新增单条音乐数据信息
     *
     * @param fileName    文件名(目的是用来作为唯一名称用以判断是否存在)
     * @param musicName   音乐名称
     * @param musicPath   音乐路径
     * @param musicFolder 音乐隶属文件夹
     * @param isFavorite  是否为最喜爱音乐
     * @param musicTime   音乐时长
     * @param musicSize   音乐文件大小
     * @param musicArtist 音乐艺术家
     * @param musicFormat 音乐格式(编码类型)
     * @param musicAlbum  音乐专辑
     * @param musicYears  音乐年代
     * @return 新增成功的条数，失败返回-1
     */
    public long add(String fileName, String musicName, String musicPath,
                    String musicFolder, boolean isFavorite, String musicTime,
                    String musicSize, String musicArtist, String musicFormat,
                    String musicAlbum,String musicAlbumPic, String musicYears) {
        ContentValues values = new ContentValues();
        values.put(DBData.MUSIC_FILE, fileName);
        values.put(DBData.MUSIC_NAME, musicName);
        values.put(DBData.MUSIC_PATH, musicPath);
        values.put(DBData.MUSIC_FOLDER, musicFolder);
        values.put(DBData.MUSIC_FAVORITE, isFavorite ? 1 : 0);// 数据库定义字段数据为整型
        values.put(DBData.MUSIC_TIME, musicTime);
        values.put(DBData.MUSIC_SIZE, musicSize);
        values.put(DBData.MUSIC_ARTIST, musicArtist);
        values.put(DBData.MUSIC_FORMAT, musicFormat);
        values.put(DBData.MUSIC_ALBUM, musicAlbum);
        values.put(DBData.MUSIC_YEARS, musicYears);
        values.put(DBData.MUSIC_ALBUM_PIC, musicAlbumPic);
        long result = db.insert(DBData.MUSIC_TABLENAME, DBData.MUSIC_FILE,
                values);
        return result;
    }

    /**
     * 新增单条音乐歌词信息
     *
     * @param fileName 文件名(目的是用来作为唯一名称用以判断是否存在)
     * @param lrcPath  歌词路径
     * @return 新增成功的条数，失败返回-1
     */
    public long addLyric(String fileName, String lrcPath) {
        ContentValues values = new ContentValues();
        values.put(DBData.LYRIC_FILE, fileName);
        values.put(DBData.LYRIC_PATH, lrcPath);
        long result = db.insert(DBData.LYRIC_TABLENAME, DBData.LYRIC_FILE,
                values);
        return result;
    }

    /**
     * 更新音乐相关记录，只更新用户是否标记为最喜爱音乐
     *
     * @param musicName  音乐名称
     * @param isFavorite 是否为最喜爱音乐(true:1 else false:0)
     * @return 影响的行数
     */
    public int update(String musicName, boolean isFavorite) {
        ContentValues values = new ContentValues();
        values.put(DBData.MUSIC_FAVORITE, isFavorite ? 1 : 0);// 数据库定义字段数据为整型
        int result = db.update(DBData.MUSIC_TABLENAME, values,
                DBData.MUSIC_NAME + "=?", new String[]{musicName});
        return result;
    }

    /**
     * 查询对应条件的数据库信息是否存在
     * <p/>
     * 建议此处不要写SQL语句，即rawQuery查询。因为某些文件名中就带有'，所以肯定报错！
     *
     * @param fileName    音乐名称
     * @param musicFolder 音乐隶属文件夹
     * @return 是否存在
     */
    public boolean queryExist(String fileName, String musicFolder) {
        boolean isExist = false;
        /*
		 * 保留本句供各位参考对比，fileName中若含有'，此处必报错。用下句替代，用SQL语句是会发生问题的
		 *
		 * Cursor cursor = db.rawQuery("SELECT * FROM " + DBData.MUSIC_TABLENAME
		 * + " WHERE " + DBData.MUSIC_FILE + "=" + fileName + " AND " +
		 * DBData.MUSIC_FOLDER + "=" + musicFolder + "", null);
		 */
        Cursor cursor = db.query(DBData.MUSIC_TABLENAME, null,
                DBData.MUSIC_FILE + "=? AND " + DBData.MUSIC_FOLDER + "=?",
                new String[]{fileName, musicFolder}, null, null, null);
        if (cursor.getCount() > 0) {
            isExist = true;
        }
        return isExist;
    }

    /**
     * 查询数据库保存的各媒体库目录下所有音乐信息和歌词
     *
     * @param scanList 音乐媒体库所有目录
     */
    public void queryAll(List<ScanInfo> scanList) {
        MusicList.list.clear();
        FolderList.list.clear();
        FavoriteList.list.clear();

        final int listSize = scanList.size();
        Cursor cursor = null;
        // 查询各媒体库目录下所有音乐信息
        for (int i = 0; i < listSize; i++) {
            final String folder = scanList.get(i).getFolderPath();
            cursor = db.rawQuery("SELECT * FROM " + DBData.MUSIC_TABLENAME
                            + " WHERE " + DBData.MUSIC_FOLDER + "='" + folder + "'",
                    null);
            List<MusicInfo> listInfo = new ArrayList<MusicInfo>();
            if (cursor != null && cursor.getCount() > 0) {
                FolderInfo folderInfo = new FolderInfo();
                while (cursor.moveToNext()) {
                    MusicInfo musicInfo = new MusicInfo();
                    final String id = cursor.getString(cursor.
                            getColumnIndex(DBData.MUSIC_ID));
                    final String file = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_FILE));
                    final String name = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_NAME));
                    final String path = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_PATH));
                    final int favorite = cursor.getInt(cursor
                            .getColumnIndex(DBData.MUSIC_FAVORITE));
                    final String time = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_TIME));
                    final String size = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_SIZE));
                    final String artist = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_ARTIST));
                    final String format = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_FORMAT));
                    final String album = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_ALBUM));
                    final String albumPic = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_ALBUM_PIC));
                    final String years = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_YEARS));

                    musicInfo.setId(id);
                    musicInfo.setFile(file);
                    musicInfo.setName(name);
                    musicInfo.setPath(path);
                    musicInfo.setFavorite(favorite == 1 ? true : false);
                    musicInfo.setTime(time);
                    musicInfo.setSize(size);
                    musicInfo.setArtist(artist);
                    musicInfo.setFormat(format);
                    musicInfo.setAlbum(album);
                    musicInfo.setAlbumPic(albumPic);
                    musicInfo.setYears(years);
                    // 加入所有歌曲列表
                    MusicList.list.add(musicInfo);
                    // 加入文件夹临时列表
                    listInfo.add(musicInfo);
                    // 加入我的最爱列表
                    if (favorite == 1) {
                        FavoriteList.list.add(musicInfo);
                    }
                }
                // 设置文件夹列表文件夹路径
                folderInfo.setMusicFolder(folder);
                // 设置文件夹列表歌曲信息
                folderInfo.setMusicList(listInfo);
                // 加入文件夹列表
                FolderList.list.add(folderInfo);
            }
        }
        // 查询歌词
        cursor = db.rawQuery("SELECT * FROM " + DBData.LYRIC_TABLENAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                final String file = cursor.getString(cursor
                        .getColumnIndex(DBData.LYRIC_FILE));
                final String path = cursor.getString(cursor
                        .getColumnIndex(DBData.LYRIC_PATH));
                LyricList.map.put(file, path);
            }
        }
        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * 查询数据库下所有音乐信息和歌词
     */
    public void queryAll() {
        MusicList.list.clear();
        FavoriteList.list.clear();
        LyricList.map.clear();
        Cursor cursor = null;

        // 查询各媒体库目录下所有音乐信息
        cursor = db.rawQuery("SELECT * FROM " + DBData.MUSIC_TABLENAME,
                null);
        List<MusicInfo> listInfo = new ArrayList<MusicInfo>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                MusicInfo musicInfo = new MusicInfo();
                final String id = cursor.getString(cursor.
                        getColumnIndex(DBData.MUSIC_ID));
                final String file = cursor.getString(cursor
                        .getColumnIndex(DBData.MUSIC_FILE));
                final String name = cursor.getString(cursor
                        .getColumnIndex(DBData.MUSIC_NAME));
                final String path = cursor.getString(cursor
                        .getColumnIndex(DBData.MUSIC_PATH));
                final int favorite = cursor.getInt(cursor
                        .getColumnIndex(DBData.MUSIC_FAVORITE));
                final String time = cursor.getString(cursor
                        .getColumnIndex(DBData.MUSIC_TIME));
                final String size = cursor.getString(cursor
                        .getColumnIndex(DBData.MUSIC_SIZE));
                final String artist = cursor.getString(cursor
                        .getColumnIndex(DBData.MUSIC_ARTIST));
                final String format = cursor.getString(cursor
                        .getColumnIndex(DBData.MUSIC_FORMAT));
                final String album = cursor.getString(cursor
                        .getColumnIndex(DBData.MUSIC_ALBUM));
                final String albumPic = cursor.getString(cursor
                        .getColumnIndex(DBData.MUSIC_ALBUM_PIC));
                final String years = cursor.getString(cursor
                        .getColumnIndex(DBData.MUSIC_YEARS));

                musicInfo.setId(id);
                musicInfo.setFile(file);
                musicInfo.setName(name);
                musicInfo.setPath(path);
                musicInfo.setFavorite(favorite == 1 ? true : false);
                musicInfo.setTime(time);
                musicInfo.setSize(size);
                musicInfo.setArtist(artist);
                musicInfo.setFormat(format);
                musicInfo.setAlbum(album);
                musicInfo.setAlbumPic(albumPic);
                musicInfo.setYears(years);
                // 加入所有歌曲列表
                MusicList.list.add(musicInfo);
                // 加入文件夹临时列表
                listInfo.add(musicInfo);
                // 加入我的最爱列表
                if (favorite == 1) {
                    FavoriteList.list.add(musicInfo);
                }
            }
        }
        // 查询歌词
        cursor = db.rawQuery("SELECT * FROM " + DBData.LYRIC_TABLENAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                final String file = cursor.getString(cursor
                        .getColumnIndex(DBData.LYRIC_FILE));
                final String path = cursor.getString(cursor
                        .getColumnIndex(DBData.LYRIC_PATH));
                LyricList.map.put(file, path);
            }
        }
        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * 根据文件路径来删除音乐信息
     *
     * @param filePath 文件路径
     * @return 成功删除的条数
     */
    public int delete(String filePath) {
		/*
		 * 不晓得这里的'会不会出问题，删除的方法只有这个吧???
		 */
        int result = db.delete(DBData.MUSIC_TABLENAME, DBData.MUSIC_PATH + "='"
                + filePath + "'", null);
        return result;
    }

    /**
     * 删除歌词信息表
     */
    public void deleteLyric() {
        // 可能不存在该表，需要拋异常
        try {
            // 清空表并将表序号置零
            db.execSQL("delete from " + DBData.LYRIC_TABLENAME + ";");
            db.execSQL("update sqlite_sequence set seq=0 where name='"
                    + DBData.LYRIC_TABLENAME + "';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建歌单或者新增歌曲to 歌单
     * @param playlist
     * @param music_id
     * @return
     */

    public void addPlaylist(String playlist,int[] music_id) {
        for (int i =0;i<music_id.length;i++) {
            ContentValues values = new ContentValues();
            values.put(DBData.PLAYLIST_TITLE, playlist);
            values.put(DBData.MUSIC_ID, music_id[i]);
//            db.query()
            db.insert(DBData.PLAYLIST_TABLENAME, DBData.PLAYLIST_ID,
                    values);
        }
    }

    /**
     * 删除歌单
     * @param playlist
     * @param music_id
     * @return
     */
    public int deletePlaylist(String playlist,int music_id) {
		/*
		 * 不晓得这里的'会不会出问题，删除的方法只有这个吧???
		 */
        if(music_id ==-1) {
            int result = db.delete(DBData.PLAYLIST_TABLENAME, DBData.PLAYLIST_TITLE + "='"
                    + playlist + "'", null);
            return result;
        }else {
            int result = db.delete(DBData.PLAYLIST_TABLENAME, DBData.MUSIC_ID + "='"
                    + music_id + "'", null);
            return result;
        }
    }

    public void queryAllPlaylist() {
//        MusicList.list.clear();
        MusicList.playlist.clear();
//        FolderList.list.clear();
//        FavoriteList.list.clear();
//        LyricList.map.clear();

        String sql = "select distinct "+DBData.PLAYLIST_TITLE+" from "+DBData.PLAYLIST_TABLENAME;
        Cursor cursor=db.rawQuery(sql, null);
        List<Integer> music_id = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name =cursor.getString(cursor.getColumnIndex(DBData.PLAYLIST_TITLE));
                MusicList.playlist.add(name);
            }
        }
        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }
    }
    /**
     * 查询歌单中歌曲信息
     * @param playlist
     */
    public void queryAllPlaylist(String playlist) {
        MusicList.list.clear();
        FolderList.list.clear();
        FavoriteList.list.clear();
        LyricList.map.clear();


        String sql = "select * from "+DBData.PLAYLIST_TABLENAME+" where "+
                DBData.PLAYLIST_TITLE+" = '"+playlist+"'";
        Cursor cursor_id=db.rawQuery(sql, null);

        List<Integer> music_id = new ArrayList<>();

        if (cursor_id!=null&&cursor_id.getCount()>0){

            while (cursor_id.moveToNext()){

                if (cursor_id.getInt(cursor_id.getColumnIndex(DBData.MUSIC_ID))!=-1){
                    music_id.add(cursor_id.getInt(cursor_id.getColumnIndex(DBData.MUSIC_ID)));
                }
            }
        }

        Cursor cursor = null;
        // 查询各媒体库目录下所有音乐信息
        for (int i = 0; i < music_id.size(); i++) {
            cursor = db.rawQuery("SELECT * FROM " + DBData.MUSIC_TABLENAME
                            + " WHERE " + DBData.MUSIC_ID + "='" +music_id.get(i)+ "'",
                    null);
            List<MusicInfo> listInfo = new ArrayList<MusicInfo>();
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    MusicInfo musicInfo = new MusicInfo();
                    final String id = cursor.getString(cursor.
                            getColumnIndex(DBData.MUSIC_ID));
                    final String file = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_FILE));
                    final String name = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_NAME));
                    final String path = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_PATH));
                    final int favorite = cursor.getInt(cursor
                            .getColumnIndex(DBData.MUSIC_FAVORITE));
                    final String time = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_TIME));
                    final String size = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_SIZE));
                    final String artist = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_ARTIST));
                    final String format = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_FORMAT));
                    final String album = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_ALBUM));
                    final String albumPic = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_ALBUM_PIC));
                    final String years = cursor.getString(cursor
                            .getColumnIndex(DBData.MUSIC_YEARS));

                    musicInfo.setId(id);
                    musicInfo.setFile(file);
                    musicInfo.setName(name);
                    musicInfo.setPath(path);
                    musicInfo.setFavorite(favorite == 1 ? true : false);
                    musicInfo.setTime(time);
                    musicInfo.setSize(size);
                    musicInfo.setArtist(artist);
                    musicInfo.setFormat(format);
                    musicInfo.setAlbum(album);
                    musicInfo.setAlbumPic(albumPic);
                    musicInfo.setYears(years);
                    // 加入所有歌曲列表
                    MusicList.list.add(musicInfo);
                    // 加入文件夹临时列表
                    listInfo.add(musicInfo);
                    // 加入我的最爱列表
                    if (favorite == 1) {
                        FavoriteList.list.add(musicInfo);
                    }
                }
            }
        }
        // 查询歌词
        cursor = db.rawQuery("SELECT * FROM " + DBData.LYRIC_TABLENAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                final String file = cursor.getString(cursor
                        .getColumnIndex(DBData.LYRIC_FILE));
                final String path = cursor.getString(cursor
                        .getColumnIndex(DBData.LYRIC_PATH));
                LyricList.map.put(file, path);
            }
        }
        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }
    }


    /**
     * 使用完数据库必须关闭
     */
    public void close() {
        db.close();
        db = null;
    }
}

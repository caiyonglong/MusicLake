package com.cyl.music_hnust.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.db.DBDao;
import com.cyl.music_hnust.list.LyricList;
import com.cyl.music_hnust.list.MusicList;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * By CWD 2013 Open Source Project
 * <p/>
 * <br>
 * <b>扫描管理器，负责扫描数据库或者SD卡的歌曲文件(耗时操作，请使用异步线程执行)，
 * 由于在一个循环中同时判断出歌曲对应的歌词难度太大，只能新建数据库表来分别存储</b></br>
 *
 * @author CWD
 * @version 2013.05.18 v1.0 实现扫描SD卡，数据库增加及查询操作<br>
 *          2013.06.15 v2.0 实现获取详细mp3标签，修改数据库记录信息<br>
 *          2013.06.16 v2.1 修正数据库查询不存在bug<br>
 *          2013.06.23 v2.2 新增对歌词的扫描<br>
 *          2013.08.05 v2.3 新增对专辑名称的扫描，修复扫描出现的几个错误<br>
 *          2013.08.05 v2.4 修正扫描文件夹歌曲列表的一个错误<br>
 *          2013.08.29 v2.5 修正真机上扫描报错的问题，原因是路径被全被格成小写导致空指针<br>
 *          2013.08.30 v2.6 修正多次扫描后文件夹列表重复的问题</br>
 */
public class ScanUtil {

    private Context context;
    private DBDao db;

    public ScanUtil(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    /**
     * 查询音乐媒体库所有目录，缺点是影响一点效率，没有找到直接提供媒体库目录的方法
     */
    public List<ScanInfo> searchAllDirectory() {
        List<ScanInfo> list = new ArrayList<ScanInfo>();
        StringBuffer sb = new StringBuffer();
        String[] projection = {MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA};
        Cursor cr = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, MediaStore.Audio.Media.DISPLAY_NAME);
        String displayName = null;
        String data = null;
        while (cr.moveToNext()) {
            displayName = cr.getString(cr
                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            data = cr.getString(cr.getColumnIndex(MediaStore.Audio.Media.DATA));
            data = data.replace(displayName, "");// 替换文件名留下它的上一级目录
            if (!sb.toString().contains(data)) {
                list.add(new ScanInfo(data, true));
                sb.append(data);
            }
        }
        cr.close();
        return list;
    }

    /**
     * 扫描SD卡音乐，录入数据库并加入歌曲列表，缺点是假如系统媒体库没有更新媒体库目录则扫描不到
     * <p/>
     * 音乐媒体库所有目录
     */
    public void scanMusicFromSD(List<String> folderList, Handler handler) {
        int count = 0;// 统计新增数
        db = new DBDao(context);
        db.deleteLyric();// 不做歌词是否已经存在判断，全部删除后重新扫描

        List<MusicInfo> listInfos = new ArrayList<MusicInfo>();
        listInfos = scanMusicTag();

        for (int i = 0; i < listInfos.size(); i++) {
            // 是文件才保存，里面还有文件夹的，那就算了吧...

            MusicInfo musicInfo = listInfos.get(i);
            String folder = musicInfo.getPath().replace(musicInfo.getFile(), "");
            Log.e("ddd", folder);
            String fileName = listInfos.get(i).getFile();
            final String path = listInfos.get(i).getPath();
            final String end = fileName.substring(
                    fileName.lastIndexOf(".") + 1, fileName.length());
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            // 记录歌曲信息
            if (end.equalsIgnoreCase("mp3")) {// 不区分大小写
                // 查询不存在则记录
                if (!db.queryExist(fileName, folder)) {
                    // 第一次扫描最喜爱肯定为false
                    db.add(fileName, musicInfo.getName(), path, folder,
                            false, musicInfo.getTime(),
                            musicInfo.getSize(), musicInfo.getArtist(),
                            musicInfo.getFormat(),
                            musicInfo.getAlbum(),musicInfo.getAlbumPic(), musicInfo.getYears());
                    // 加入所有歌曲列表
                    MusicList.list.add(musicInfo);
                    count++;
                }
                if (handler != null) {
                    Message msg = handler.obtainMessage();
                    msg.obj = fileName;
                    // Message从handler获取，可以直接向该handler对象发送消息
                    msg.sendToTarget();
                }
            }
            // 记录歌词信息(只识别LRC歌词)
            if (end.equalsIgnoreCase("lrc")) {// 不区分大小写
                db.addLyric(fileName, path);
                LyricList.map.put(fileName, path);
            }
        }
        if (handler != null)

        {
            Message msg = handler.obtainMessage();
            msg.obj = "扫描完成，新增歌曲" + count + "首";
            // Message从handler获取，可以直接向该handler对象发送消息
            msg.sendToTarget();
        }

        db.close();
    }

    /**
     * 查新数据库记录的所有歌曲
     */
    public void scanMusicFromDB() {
        db = new DBDao(context);
        db.queryAll(searchAllDirectory());
        db.close();
    }
    /**
     * 查新数据库记录的所有歌单
     */
    public void scanPlaylistFromDB() {
        db = new DBDao(context);
        db.queryAllPlaylist();
        db.close();
    }
    /**
     * 查新数据库记录的歌单中所有的歌曲
     */
    public void scanPlaylistSongFromDB(String playlist) {
        db = new DBDao(context);
        db.queryAllPlaylist(playlist);
        db.close();
    }
    /**
     * 删除数据库记录的歌单中所有的歌曲
     */
    public void deleteplaylist(String playlist,int music_id) {
        db = new DBDao(context);
        db.deletePlaylist(playlist, music_id);
        db.close();
    }
    /**
     * 增加数据库记录的歌单中歌曲
     */
    public void addplaylist(String playlist,int[] music_id) {
        db = new DBDao(context);
        db.addPlaylist(playlist,music_id);
        db.close();
    }

    public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return query(context, uri, projection, selection, selectionArgs, sortOrder, 0);
    }

    public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, int limit) {
        try {
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                return null;
            }
            if (limit > 0) {
                uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
            }
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (UnsupportedOperationException ex) {
            return null;
        }
    }

    /**
     * 关键一步，获取MP3详细信息，比如歌名、歌手、比特率之类的
     */
    private List<MusicInfo> scanMusicTag() {
        List<MusicInfo> infos = new ArrayList<>();
        Cursor c = query(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        while (c.moveToNext()) {

            MusicInfo info = new MusicInfo();
            info.setName(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            info.setArtist(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            info.setAlbum(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            info.setPath(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
            info.setSize(c.getString(c.getColumnIndex(MediaStore.Audio.Media.SIZE)));
            info.setTime(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            info.setYears(c.getString(c.getColumnIndex(MediaStore.Audio.Media.YEAR)));
            info.setMp3Duration(c.getInt(c.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            info.setFile(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));

            String displayName =c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

            if (displayName.contains(".mp3")) {
                String[] displayNameArr = displayName.split(".mp3");
                displayName = displayNameArr[0].trim();
            }
            info.setAlbumPic(getAlbumPicPath(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA))
            ,displayName));

            infos.add(info);
        }

        return infos;
    }
    public String getAlbumPicPath(final String filePath, String fileName) {

        String path = null;
        Bitmap bitmap= null;

        bitmap = AlbumUtil.scanAlbumImage(filePath);
//        //能够获取多媒体文件元数据的类
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        try {
//            retriever.setDataSource(filePath); //设置数据源
//            byte[] embedPic = retriever.getEmbeddedPicture(); //得到字节型数据
//
//            bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片

            if (bitmap!=null)
            path = CommonUtils.imageToLocal(bitmap, fileName);

//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                retriever.release();
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }

        return path;
    }

}

package com.cyl.musiclake.data

import android.content.Context
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.cyl.musiclake.bean.FolderInfo
import io.reactivex.Observable

/**
 * Created by D22434 on 2018/1/8.
 */

object AppRepository {

    fun getPlayHistoryRepository(): Observable<List<BaseMusicInfo>> {
        return Observable.create { PlayHistoryLoader.getPlayHistory() }
    }

    fun getFolderInfosRepository(mContext: Context): Observable<List<FolderInfo>> {
        return FolderLoader.getFoldersWithSong(mContext)
    }

    fun getFolderSongsRepository(mContext: Context, path: String): Observable<List<BaseMusicInfo>> {
        return Observable.create { SongLoader.getSongListInFolder(mContext, path) }
    }

}

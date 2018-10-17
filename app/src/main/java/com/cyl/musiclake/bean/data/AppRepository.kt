package com.cyl.musiclake.bean.data

import android.content.Context
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.FolderInfo
import io.reactivex.Observable

/**
 * Created by D22434 on 2018/1/8.
 */

object AppRepository {

    fun getPlayHistoryRepository(): Observable<List<Music>> {
        return Observable.create({ PlayHistoryLoader.getPlayHistory() })
    }

    fun getFolderInfosRepository(mContext: Context): Observable<List<FolderInfo>> {
        return FolderLoader.getFoldersWithSong(mContext)
    }

    fun getFolderSongsRepository(mContext: Context, path: String): Observable<List<Music>> {
        return Observable.create({ SongLoader.getSongListInFolder(mContext, path) })
    }

}

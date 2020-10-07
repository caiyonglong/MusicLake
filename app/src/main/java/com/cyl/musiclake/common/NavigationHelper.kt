package com.cyl.musiclake.common

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.media.audiofx.AudioEffect
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Pair
import android.view.View
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.FolderInfo
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.player.MusicPlayerService
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.download.ui.DownloadFragment
import com.cyl.musiclake.ui.main.MainActivity
import com.cyl.musiclake.ui.music.artist.activity.ArtistDetailActivity
import com.cyl.musiclake.ui.music.local.fragment.LocalMusicFragment
import com.cyl.musiclake.ui.music.local.fragment.LocalVideoFragment
import com.cyl.musiclake.ui.music.playlist.detail.PlaylistDetailActivity
import com.cyl.musiclake.ui.music.playlist.history.RecentlyFragment
import com.cyl.musiclake.ui.music.playlist.love.LoveFragment
import com.cyl.musiclake.ui.music.playpage.PlayerActivity
import com.cyl.musiclake.ui.music.playqueue.PlayQueueFragment
import com.cyl.musiclake.utils.FileUtils
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * Created by yonglong on 2016/12/24.
 * 导航工具类
 */

object NavigationHelper {
    fun navigateToLocalMusic(context: Activity, transitionViews: Pair<View, String>?) {
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        val fragment: androidx.fragment.app.Fragment

        if (transitionViews != null) {
            transaction.addSharedElement(transitionViews.first, transitionViews.second)
            fragment = LocalMusicFragment.newInstance("local")
        } else {
            //            transaction.setCustomAnimations(R.anim.activity_fade_in,
            //                    R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
            fragment = LocalMusicFragment.newInstance("local")
        }
        context.supportFragmentManager.findFragmentById(R.id.fragment_container)?.let { transaction.hide(it) }
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(fragment.getTag()).commit()
    }

    fun navigateToVideo(context: Activity, transitionViews: Pair<View, String>?) {
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        val fragment: androidx.fragment.app.Fragment

        if (transitionViews != null) {
            transaction.addSharedElement(transitionViews.first, transitionViews.second)
            fragment = LocalVideoFragment.newInstance("")
        } else {
            //            transaction.setCustomAnimations(R.anim.activity_fade_in,
            //                    R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
            fragment = LocalVideoFragment.newInstance("")
        }
        context.supportFragmentManager.findFragmentById(R.id.fragment_container)?.let { transaction.hide(it) }
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(fragment.getTag()).commit()
    }

    fun navigateToSoundEffect(context: Activity) {
        try {
            val effects = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
            effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, PlayManager.getAudioSessionId())
            context.startActivityForResult(effects, 666)
        } catch (e: Exception) {
            ToastUtils.show("设备不支持均衡！")
        }
    }

    /**
     * 打开指定文件夹
     */
    fun navigateToFolderSongs(context: Activity, folderInfo: FolderInfo) {
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        val fragment: androidx.fragment.app.Fragment
        fragment = LocalVideoFragment.newInstance(folderInfo.folderPath, folderInfo.folderName)
        context.supportFragmentManager.findFragmentById(R.id.fragment_container)?.let { transaction.hide(it) }
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(fragment.getTag()).commit()
    }

    fun navigateRecentlyMusic(context: Activity) {
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        val fragment: androidx.fragment.app.Fragment
        fragment = RecentlyFragment.newInstance()
        transaction.hide(context.supportFragmentManager.findFragmentById(R.id.fragment_container)!!)
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(fragment.getTag()).commit()
    }

    fun navigatePlayQueue(context: Activity) {
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        val fragment = PlayQueueFragment.newInstance()
        transaction.hide(context.supportFragmentManager.findFragmentById(R.id.fragment_container)!!)
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(fragment.tag).commit()
    }

    fun navigateToLoveMusic(context: Activity, transitionViews: Pair<View, String>) {
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        val fragment: androidx.fragment.app.Fragment

        fragment = LoveFragment.newInstance()
        transaction.hide(context.supportFragmentManager.findFragmentById(R.id.fragment_container)!!)
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(fragment.getTag()).commit()
    }

    fun navigateToDownload(context: Activity, isCache: Boolean = false, transitionViews: Pair<View, String>? = null) {
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        val fragment: androidx.fragment.app.Fragment

        if (transitionViews != null) {
            transaction.addSharedElement(transitionViews.first, transitionViews.second)
            fragment = DownloadFragment.newInstance(isCache)
        } else {
            //            transaction.setCustomAnimations(R.anim.activity_fade_in,
            //                    R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
            fragment = DownloadFragment.newInstance(isCache)
        }
        transaction.hide((context.supportFragmentManager.findFragmentById(R.id.fragment_container)!!))
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(fragment.getTag()).commit()
    }

    fun navigateToPlaylist(context: Activity, playlist: Playlist, transitionViews: Pair<View, String>?) {
        val intent = Intent(context, PlaylistDetailActivity::class.java)
        intent.putExtra(Extras.PLAYLIST, playlist)
        if (transitionViews != null) {
            val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                    transitionViews.first, transitionViews.second)
            ActivityCompat.startActivity(context, intent, compat.toBundle())
        } else {
            context.startActivity(intent)
        }
    }

    fun navigateToArtist(context: Activity, artist: Artist, transitionViews: Pair<View, String>?) {
        val intent = Intent(context, ArtistDetailActivity::class.java)
        intent.putExtra(Extras.ARTIST, artist)
        if (transitionViews != null) {
            val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                    transitionViews.first, transitionViews.second)
            ActivityCompat.startActivity(context, intent, compat.toBundle())
        } else {
            context.startActivity(intent)
        }
    }

    fun navigateToPlaylist(context: Activity, artist: Artist, transitionViews: Pair<View, String>?) {
        val intent = Intent(context, PlaylistDetailActivity::class.java)
        intent.putExtra(Extras.ARTIST, artist)
        if (transitionViews != null) {
            val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                    transitionViews.first, transitionViews.second)
            ActivityCompat.startActivity(context, intent, compat.toBundle())
        } else {
            context.startActivity(intent)
        }
    }

    fun navigateToPlaylist(context: Activity, album: Album, transitionViews: Pair<View, String>? = null) {
        val intent = Intent(context, PlaylistDetailActivity::class.java)
        intent.putExtra(Extras.ALBUM, album)
        if (transitionViews != null) {
            val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                    transitionViews.first, transitionViews.second)
            ActivityCompat.startActivity(context, intent, compat.toBundle())
        } else {
            context.startActivity(intent)
        }
    }


    fun navigateFragment(context: Activity, fragment: androidx.fragment.app.Fragment) {
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.hide(context.supportFragmentManager.findFragmentById(R.id.fragment_container)!!)
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(fragment.tag).commit()
    }


    fun navigateToPlaying(context: Activity, transitionView: View? = null) {
        val intent = Intent(context, PlayerActivity::class.java)
        if (transitionView != null) {
            val compat = ActivityOptionsCompat.makeScaleUpAnimation(transitionView,
                    transitionView.width / 2, transitionView.height / 2, 0, 0)
//            val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
//                    transitionView, MusicApp.getAppContext().getString(R.string.transition_cover))
            ActivityCompat.startActivity(context, intent, compat.toBundle())
        } else {
            context.startActivity(intent)
        }
    }


    fun getNowPlayingIntent(context: Context): Intent {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = Constants.DEAULT_NOTIFICATION
        return intent
    }


    fun getUpdateWidgetIntent(context: Context): Intent {
        return Intent("com.cyl.music_lake.appwidget_update")
    }

    fun getLyricIntent(context: Context): Intent {
        val intent = Intent(MusicPlayerService.ACTION_LYRIC)
        intent.component = ComponentName(context, MusicPlayerService::class.java)
        return intent
    }

    /**
     * 下一首
     */
    fun getNextIntent(context: Context): Intent {
        val intent = Intent(MusicPlayerService.ACTION_NEXT)
        intent.component = ComponentName(context, MusicPlayerService::class.java)
        return intent
    }

    /**
     * 上一首
     */
    fun getPrevIntent(context: Context): Intent {
        val intent = Intent(MusicPlayerService.ACTION_PREV)
        intent.component = ComponentName(context, MusicPlayerService::class.java)
        return intent
    }

    /**
     * 暂停
     */
    fun getPlayPauseIntent(context: Context): Intent {
        val intent = Intent(MusicPlayerService.ACTION_PREV)
        intent.component = ComponentName(context, MusicPlayerService::class.java)
        return intent
    }

    /**
     * 扫描文件夹
     *
     * @param ctx
     * @param filePath
     */
    fun scanFileAsync(ctx: Context, filePath: String) {
        LogUtil.d("NavigationHelper", "ACTION_MEDIA_SCANNER_SCAN_FILE$filePath")
        val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        scanIntent.data = Uri.fromFile(File(filePath))
        ctx.sendBroadcast(scanIntent)
        scanFileAsync(ctx)
    }

    /**
     * 扫描文件夹
     *
     * @param ctx
     * @param filePath
     */
    fun scanFileAsync(ctx: Context) {
        //MediaScannerConnectionClient 是媒体扫描服务在MediaScannerConnection类中返回新添加文件的 uri  和 path
        val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        scanIntent.data = Uri.fromFile(File(FileUtils.getMusicDir()))
        ctx.sendBroadcast(scanIntent)

        MediaScannerConnection.scanFile(MusicApp.mContext, arrayOf(FileUtils.getMusicDir()), null,
                object : MediaScannerConnection.MediaScannerConnectionClient {
                    override fun onMediaScannerConnected() {
                        // MediaScanner service 创建后回调
                        LogUtil.d("NavigationHelper", "MediaScannerConnection onMediaScannerConnected ${FileUtils.getMusicDir()}")
                    }

                    override fun onScanCompleted(path: String?, uri: Uri?) {
                        // 当MediaScanner完成文件扫描后回调
                        LogUtil.d("NavigationHelper", "MediaScannerConnection onScanCompleted $path")
                        EventBus.getDefault().post(PlaylistEvent(Constants.PLAYLIST_LOCAL_ID))
                    }
                })
    }
}

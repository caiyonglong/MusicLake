package com.cyl.musiclake.ui.music.player

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cyl.musiclake.R
import com.cyl.musiclake.player.MusicPlayerService
import com.cyl.musiclake.player.PlayManager
import kotlinx.android.synthetic.main.frag_player_lrcview.*
import org.jetbrains.anko.support.v4.intentFor
import java.io.File

/**
 * Des    :
 * Author : master.
 * Date   : 2018/6/6 .
 */
class LyricFragment : Fragment() {
    var lyricInfo: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_player_lrcview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showLyric(MusicPlayerService.lyric, false)
    }


    fun showLyric(lyricInfo: String?, isFilePath: Boolean) {
        this.lyricInfo = lyricInfo
        //初始化歌词配置
        //        mLrcView.set(15.0f);
        //        mLrcView.setTextSize(17.0f);
        lyricShow?.setTouchable(true)
        //        mLrcView.setPlayable(true);
        lyricShow?.setOnPlayerClickListener { progress, content ->
            PlayManager.seekTo(progress.toInt())
            if (!PlayManager.isPlaying()) {
                PlayManager.playPause()
            }
        }
        if (lyricInfo != null) {
            if (isFilePath) {
                lyricShow?.setLyricFile(File(lyricInfo), "utf-8")
            } else {
                lyricShow?.setLyricContent(lyricInfo)
            }
        } else {
            lyricShow?.reset()
        }
    }

    fun setCurrentTimeMillis(toLong: Long) {
        lyricShow?.setCurrentTimeMillis(toLong)
    }

    companion object {
        fun newInstance(): LyricFragment {
            val args = Bundle()
            val fragment = LyricFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

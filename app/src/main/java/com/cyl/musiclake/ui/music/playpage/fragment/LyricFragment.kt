package com.cyl.musiclake.ui.music.playpage.fragment

import com.cyl.musiclake.R
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.ui.widget.LyricView
import com.cyl.musiclake.utils.SPUtils
import kotlinx.android.synthetic.main.frag_player_lrcview.*

class LyricFragment : BaseFragment<BasePresenter<BaseContract.BaseView>>() {

    val lyricTv by lazy { rootView?.findViewById<LyricView>(R.id.lyricShow) }

    override fun getLayoutId(): Int {
        return R.layout.frag_player_lrcview
    }

    override fun initInjector() {
    }

    override fun initViews() {
        super.initViews()

    }

    /**
     *显示歌词
     */
    fun showLyric(lyric: String?, init: Boolean) {
        if (init) {
            //初始化歌词配置
            lyricShow?.setTextSize(SPUtils.getFontSize())
            lyricShow?.setHighLightTextColor(SPUtils.getFontColor())
            lyricShow?.setTouchable(true)
            lyricShow?.setOnPlayerClickListener { progress, _ ->
                PlayManager.seekTo(progress.toInt())
                if (!PlayManager.isPlaying()) {
                    PlayManager.playPause()
                }
            }
        }
        lyricShow?.setLyricContent(lyric)
    }

    fun setCurrentTimeMillis(current: Long = 0) {
        lyricShow?.setCurrentTimeMillis(current)
    }
}
package com.cyl.musiclake.ui.music.player

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.cyl.musiclake.R
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.player.PlayManager
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.frag_player_coverview.*

/**
 * Des    :
 * Author : master.
 * Date   : 2018/6/6 .
 */
class CoverFragment : RxFragment() {

    var operatingAnim: ObjectAnimator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_player_coverview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAlbumPic(civ_cover)
    }

    fun updateMusicType(music: Music?) {
        tv_source?.text = music?.type
        when (music?.type) {
            Constants.QQ -> {
                tv_source?.setText(R.string.res_qq)
            }
            Constants.BAIDU -> {
                tv_source?.setText(R.string.res_baidu)
            }
            Constants.NETEASE -> {
                tv_source?.setText(R.string.res_wangyi)
            }
            Constants.XIAMI -> {
                tv_source?.setText(R.string.res_xiami)
            }
        }
    }

    fun setCover(bitmap: Bitmap?) {
        this.bitmap = bitmap
        civ_cover?.setImageBitmap(bitmap)
    }

    var bitmap: Bitmap? = null
    override fun onResume() {
        super.onResume()
        bitmap?.let {
            civ_cover?.setImageBitmap(bitmap)
            updateMusicType(PlayManager.getPlayingMusic())
        }
    }

    /**
     * 初始化旋转动画
     */
    fun initAlbumPic(view: View?) {
        if (view == null) return
        operatingAnim = ObjectAnimator.ofFloat(view, "rotation", 0F, 359F)
        operatingAnim?.duration = (20 * 1000).toLong()
        operatingAnim?.repeatCount = -1
        operatingAnim?.repeatMode = ObjectAnimator.RESTART
        operatingAnim?.interpolator = LinearInterpolator()
    }

    companion object {
        fun newInstance(): CoverFragment {
            val args = Bundle()
            val fragment = CoverFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
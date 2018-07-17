package com.cyl.musiclake.ui.music.playqueue

import android.content.Context
import android.widget.ImageView
import com.cyl.musiclake.R
import com.cyl.musiclake.player.playqueue.PlayQueueManager
import com.cyl.musiclake.utils.ToastUtils

class Utils {
    /**
     * 改变播放模式
     */
    fun updatePlayMode(context: Context, imageView: ImageView, isChange: Boolean = false) {
        try {
            var playMode = PlayQueueManager.getPlayMode()
            if (isChange) playMode = PlayQueueManager.updatePlayMode()
            when (playMode) {
                PlayQueueManager.PLAY_MODE_LOOP -> {
                    imageView.setImageResource(R.drawable.ic_repeat)
                    if (isChange) ToastUtils.show(context.resources.getString(R.string.play_mode_loop))
                }
                PlayQueueManager.PLAY_MODE_REPEAT -> {
                    imageView.setImageResource(R.drawable.ic_repeat_one)
                    if (isChange) ToastUtils.show(context.resources.getString(R.string.play_mode_repeat))
                }
                PlayQueueManager.PLAY_MODE_RANDOM -> {
                    imageView.setImageResource(R.drawable.ic_shuffle)
                    if (isChange) ToastUtils.show(context.resources.getString(R.string.play_mode_random))
                }
            }
        } catch (e: Throwable) {

        }
    }
}
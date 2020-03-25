package com.music.lake.musiclib.player

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import androidx.media.AudioAttributesCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import com.music.lake.musiclib.playback.PlaybackListener
import com.music.lake.musiclib.utils.LogUtil

/**
 * Created by cyl on 2018/5/11.
 */

class MusicExoPlayer(var context: Context) : BasePlayer(), Player.EventListener {

    private val TAG = "MusicExoPlayer"

    //exoPlayer播放器
    private var exoPlayer: SimpleExoPlayer? = null
    private var playbackListener: PlaybackListener? = null
    private var mediaDataSourceFactory: DataSource.Factory? = null

    private var bandwidthMeter: DefaultBandwidthMeter = DefaultBandwidthMeter()
    private var videoTrackSelectionFactory: AdaptiveTrackSelection.Factory = AdaptiveTrackSelection.Factory(bandwidthMeter)
    private val renderFactory = DefaultRenderersFactory(context, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
    private val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
    private var loadControl: LoadControl? = null

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val audioAttributes = AudioAttributesCompat.Builder()
            .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributesCompat.USAGE_MEDIA)
            .build()

    init {
        initPlayer(true)
    }

    override fun setPlayBackListener(listener: PlaybackListener?) {
        super.setPlayBackListener(listener)
        playbackListener = listener;
    }

    /**
     *
     * 初始化播放器
     */
    fun initPlayer(playOnReady: Boolean) {
        //生成数据原实力
        mediaDataSourceFactory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "MusicLakeApp"))
        //创建 player
        loadControl = DefaultLoadControl()
        exoPlayer = SimpleExoPlayer.Builder(context).build()
        exoPlayer?.playWhenReady = playOnReady
        exoPlayer?.addAnalyticsListener(PlayerEventLogger())
        exoPlayer?.addListener(this)
    }

    fun bindView(playerView: PlayerView) {
        playerView.player = exoPlayer
    }

    fun bindControlView(controlView: PlayerControlView) {
        controlView.player = exoPlayer
    }

    fun addPlaybackListener(listener: PlaybackListener) {
        playbackListener = listener
    }

    fun removePlayBackListener() {
        playbackListener = null
    }

    override fun start() {
        super.start()
        exoPlayer?.playWhenReady = true
    }

    override fun setVolume(vol: Float) {
        super.setVolume(vol)
        LogUtil.e("Volume", "vol = $vol")
        try {
            exoPlayer?.volume = vol
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setDataSource(uri: String?) {
        super.setDataSource(uri)
        val mediaSource = uri?.let { buildMediaSource(it) }
        exoPlayer?.playWhenReady = playWhenReady
        //准备播放来源。
        mediaSource?.let { exoPlayer?.prepare(it) }
    }

    /**
     * 单首歌曲转化成mediaSource
     */
    private fun buildMediaSource(url: String): MediaSource? {
        LogUtil.d("exoplayer ", url)
        val uri = Uri.parse(url)
        return buildMediaSource(uri)
    }

    override fun isPlaying(): Boolean {
        exoPlayer?.let {
            return it.playbackState == Player.STATE_READY && it.playWhenReady
        }
        return false
    }

    /**
     * 返回正在播放状态
     */
    fun isLoading(): Boolean {
        exoPlayer?.let {
            return it.playbackState == Player.STATE_BUFFERING
        }
        return false
    }

    /**
     * 播放暂停
     */
    fun playPause() {
        exoPlayer?.playWhenReady = !isPlaying()
    }

    override fun stop() {
        super.stop()
        exoPlayer?.stop(true)
    }

    override fun pause() {
        super.pause()
        exoPlayer?.playWhenReady = false
    }

    override fun position(): Long {
        exoPlayer?.let {
            return it.currentPosition
        }
        return 0
    }

    override fun isPrepared(): Boolean {
        exoPlayer?.let {
            return it.playbackState != Player.STATE_IDLE
        }
        return super.isPrepared()
    }


    /**
     * 滑动播放位置
     */
    override fun seekTo(positionMillis: Long) {
        super.seekTo(positionMillis)
        exoPlayer?.let {
            LogUtil.e(TAG, "seekTo $positionMillis ${it.duration}")
            if (positionMillis < 0 || positionMillis > it.duration)
                return
            it.seekTo(positionMillis)
        }
    }

    override fun duration(): Long {
        exoPlayer?.duration.let {
            if (it == null || it <= 0) return 0
            return it
        }
    }

    /**
     * 播放位置
     */
    override fun bufferedPercentage(): Int {
        super.bufferedPercentage()
        exoPlayer?.bufferedPercentage.let {
            if (it == null || it <= 0) return 0
            return it
        }
    }


    /**
     * 播放sessionId
     */
    override fun getAudioSessionId(): Int {
        exoPlayer?.audioSessionId.let {
            if (it == null || it <= 0) return 0
            return it
        }
    }


    /**
     * 释放player
     */
    private fun destroyPlayer() {
        LogUtil.d(TAG, "destroyPlayer() called")
        if (exoPlayer != null) {
            exoPlayer?.stop()
            exoPlayer?.release()
        }
    }


    override fun release() {
        super.release()
        destroyPlayer()
        exoPlayer = null
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        return ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri)
    }

    /*********************************************************************************
     *  Audio监听事件
     *********************************************************************************
     */
    /**
     * 获取audioSessionId 均衡器
     */

    /*********************************************************************************
     *  播放监听事件
     *********************************************************************************
     */
    private inner class PlayerEventLogger : EventLogger(trackSelector) {
        override fun onAudioSessionId(eventTime: AnalyticsListener.EventTime, audioSessionId: Int) {
            super.onAudioSessionId(eventTime, audioSessionId)
            LogUtil.d(TAG, "onAudioSessionId ${eventTime.realtimeMs} $audioSessionId")
        }

        override fun onTimelineChanged(eventTime: AnalyticsListener.EventTime, reason: Int) {
            super.onTimelineChanged(eventTime, reason)
            LogUtil.d(TAG, "onTimelineChanged ${eventTime.realtimeMs} $reason")
            playbackListener?.onPlaybackProgress(eventTime.currentPlaybackPositionMs, eventTime.realtimeMs, eventTime.totalBufferedDurationMs)
        }


        override fun onLoadingChanged(eventTime: AnalyticsListener.EventTime, isLoading: Boolean) {
            super.onLoadingChanged(eventTime, isLoading)
            LogUtil.d(TAG, "onLoadingChanged ${eventTime.realtimeMs} $isLoading")
            playbackListener?.onLoading(isLoading)
        }

        override fun onPlayerStateChanged(eventTime: AnalyticsListener.EventTime, playWhenReady: Boolean, state: Int) {
            super.onPlayerStateChanged(eventTime, playWhenReady, state)
            LogUtil.d(TAG, "onPlayerStateChanged ${eventTime.realtimeMs} $playWhenReady $state")
            if (state == Player.STATE_ENDED) {
                playbackListener?.onCompletionNext()
            } else if (state == Player.STATE_READY) {
                playbackListener?.onPlayerStateChanged(playWhenReady)
            }
        }

        override fun onPlayerError(eventTime: AnalyticsListener.EventTime, error: ExoPlaybackException) {
            super.onPlayerError(eventTime, error)
            LogUtil.d(TAG, "onPlayerError ${eventTime.realtimeMs} ${error.message}")
            playbackListener?.onError()
        }
    }

}


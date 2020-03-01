//package com.music.lake.musiclib.service
//
//import android.content.Context
//import android.media.AudioManager
//import android.net.Uri
//import android.support.v4.media.AudioAttributesCompat
//import com.google.android.exoplayer2.*
//import com.google.android.exoplayer2.analytics.AnalyticsListener
//import com.google.android.exoplayer2.source.ExtractorMediaSource
//import com.google.android.exoplayer2.source.MediaSource
//import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
//import com.google.android.exoplayer2.ui.PlayerControlView
//import com.google.android.exoplayer2.ui.PlayerView
//import com.google.android.exoplayer2.upstream.*
//import com.google.android.exoplayer2.upstream.cache.*
//import com.google.android.exoplayer2.util.EventLogger
//import java.io.File
//
//
///**
// * Created by cyl on 2018/5/11.
// */
//
//class ExoPlayer(var context: Context) {
//
//    private val TAG = "ExoPlayer"
//
//    //exoPlayer播放器
//    private var exoPlayer: SimpleExoPlayer? = null
//    private var playbackListener: PlaybackListener? = null
//    private var audioFocusWrapper: AudioFocusWrapper? = null
//    private var mediaDataSourceFactory: DataSource.Factory? = null
//
//    private var bandwidthMeter: DefaultBandwidthMeter = DefaultBandwidthMeter()
//    private var videoTrackSelectionFactory: AdaptiveTrackSelection.Factory = AdaptiveTrackSelection.Factory(bandwidthMeter)
//    private val renderFactory = DefaultRenderersFactory(context, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
//    private val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
//    private var loadControl: LoadControl? = null
//
//    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//    private val audioAttributes = AudioAttributesCompat.Builder()
//            .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
//            .setUsage(AudioAttributesCompat.USAGE_MEDIA)
//            .build()
//
//    /**
//     *
//     * 初始化播放器
//     */
//    fun initPlayer(playOnReady: Boolean) {
//        //创建缓存
//        mediaDataSourceFactory = buildDataSourceFactory(true)
//        //创建 player
//        loadControl = DefaultLoadControl()
//        exoPlayer = ExoPlayerFactory.newSimpleInstance(renderFactory, trackSelector)
//        exoPlayer?.playWhenReady = playOnReady
//        exoPlayer?.addAnalyticsListener(PlayerEventLogger())
//        exoPlayer?.let {
//            audioFocusWrapper = AudioFocusWrapper(audioAttributes, audioManager, it)
//        }
//    }
//
//    private fun buildDataSourceFactory(useBandwidthMeter: Boolean): DataSource.Factory? {
//        return buildDataSourceFactory(bandwidthMeter)
//    }
//
//
//    fun initPlayback(playOnReady: Boolean) {
//        destroyPlayer()
//        initPlayer(playOnReady)
//    }
//
//    fun bindView(playerView: PlayerView) {
//        // Bind the players to the view.
//        playerView.player = exoPlayer
//    }
//
//    fun bindControlView(controlView: PlayerControlView) {
//        controlView.player = exoPlayer
//    }
//
//    fun addPlaybackListener(listener: PlaybackListener) {
//        playbackListener = listener
//    }
//
//    fun removePlayBackListener() {
//        playbackListener = null
//    }
//
//    /**
//     * 加载播放地址
//     */
//    fun prepare(uri: String) {
//        val mediaSource = buildMediaSource(uri)
//        // 这是代表媒体播放的MediaSource
//        audioFocusWrapper?.setPlayWhenReady(true)
//        //准备播放来源。
//        exoPlayer?.prepare(mediaSource)
//    }
//
//    /**
//     * 单首歌曲转化成mediaSource
//     */
//    private fun buildMediaSource(url: String): MediaSource? {
//        logE("exoplayer ", url)
//        val uri = Uri.parse(url)
//        return buildMediaSource(uri)
//    }
//
//
//    /**
//     * 返回正在播放状态
//     */
//    fun isPlaying(): Boolean {
//        exoPlayer?.let {
//            return it.playbackState == Player.STATE_READY && it.playWhenReady
//        }
//        return false
//    }
//
//    /**
//     * 返回正在播放状态
//     */
//    fun isLoading(): Boolean {
//        exoPlayer?.let {
//            return it.playbackState == Player.STATE_BUFFERING
//        }
//        return false
//    }
//
//    /**
//     * 播放暂停
//     */
//    fun playPause() {
//        exoPlayer?.playWhenReady = !isPlaying()
//        audioFocusWrapper?.setPlayWhenReady(isPlaying())
//    }
//
//    /**
//     * 停止播放
//     */
//    fun stop() {
//        exoPlayer?.stop(true)
//        destroyPlayer()
//    }
//
//    /**
//     * 停止播放
//     */
//    fun pause() {
//        exoPlayer?.playWhenReady = false
//        audioFocusWrapper?.setPlayWhenReady(false)
//    }
//
//    /**
//     * 播放进度时间
//     */
//    fun position(): Long {
//        exoPlayer?.let {
//            return it.currentPosition
//        }
//        return 0
//    }
//
//    /**
//     * 判断播放器状态 STATE_IDLE 暂无音频播放
//     */
//    fun isPrepared(): Boolean {
//        exoPlayer?.let {
//            return it.playbackState != Player.STATE_IDLE
//        }
//        return false
//    }
//
//    /**
//     * 滑动播放位置
//     */
//    fun seekTo(positionMillis: Long) {
//        exoPlayer?.let {
//            if (positionMillis < 0 || positionMillis > it.duration)
//                return
//            it.seekTo(positionMillis)
//        }
//    }
//
//    /**
//     * 播放位置
//     */
//    fun duration(): Long {
//        exoPlayer?.duration.let {
//            if (it == null || it <= 0) return 0
//            return it
//        }
//    }
//
//    /**
//     * 播放位置
//     */
//    fun bufferedPercentage(): Int {
//        exoPlayer?.bufferedPercentage.let {
//            if (it == null || it <= 0) return 0
//            return it
//        }
//    }
//
//
//    /**
//     * 播放sessionId
//     */
//    fun getAudioSessionId(): Int {
//        exoPlayer?.audioSessionId.let {
//            if (it == null || it <= 0) return 0
//            return it
//        }
//    }
//
//
//    /**
//     * 释放player
//     */
//    private fun destroyPlayer() {
//        logE(TAG, "destroyPlayer() called")
//        if (exoPlayer != null) {
//            exoPlayer?.stop()
//            exoPlayer?.release()
//        }
//    }
//
//    fun destory() {
//        destroyPlayer()
//        exoPlayer = null
//        exoplayerCache?.release()
//        exoplayerCache = null
//    }
//
//    private fun buildMediaSource(uri: Uri): MediaSource? {
//        return ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri)
//    }
//
//    /*********************************************************************************
//     *  Audio监听事件
//     *********************************************************************************
//     */
//    /**
//     * 获取audioSessionId 均衡器
//     */
//
//    /*********************************************************************************
//     *  播放监听事件
//     *********************************************************************************
//     */
//    private inner class PlayerEventLogger : EventLogger(trackSelector) {
//        override fun onAudioSessionId(eventTime: AnalyticsListener.EventTime, audioSessionId: Int) {
//            super.onAudioSessionId(eventTime, audioSessionId)
//        }
//
//        override fun onTimelineChanged(eventTime: AnalyticsListener.EventTime, reason: Int) {
//            super.onTimelineChanged(eventTime, reason)
//            playbackListener?.onPlaybackProgress(eventTime?.currentPlaybackPositionMs, eventTime?.realtimeMs, eventTime?.totalBufferedDurationMs)
//        }
//
//
//        override fun onLoadingChanged(eventTime: AnalyticsListener.EventTime, isLoading: Boolean) {
//            super.onLoadingChanged(eventTime, isLoading)
//            playbackListener?.onLoading(isLoading)
//        }
//
//        override fun onPlayerStateChanged(eventTime: AnalyticsListener.EventTime, playWhenReady: Boolean, state: Int) {
//            super.onPlayerStateChanged(eventTime, playWhenReady, state)
//
//        }
//
//
//        override fun onPlayerError(eventTime: AnalyticsListener.EventTime, error: ExoPlaybackException) {
//            super.onPlayerError(eventTime, error)
//        }
//    }
//
//    /**
//     * 默认缓冲
//     */
//
//    /** Returns a [DataSource.Factory].  */
//    private fun buildDataSourceFactory(listener: TransferListener): DataSource.Factory? {
//        val upstreamFactory = DefaultDataSourceFactory(MusicApp.getAppContext(), listener, buildHttpDataSourceFactory(listener))
//        getPlayerCache()?.let { return buildReadOnlyCacheDataSource(upstreamFactory, it) }
//        return null
//    }
//
//    /** Returns a [HttpDataSource.Factory].  */
//    private fun buildHttpDataSourceFactory(
//            listener: TransferListener): HttpDataSource.Factory {
//        return DefaultHttpDataSourceFactory(MusicApp.userAgent, listener)
//    }
//
//    private var exoplayerCache: Cache? = null
//    private val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
//
//
//    private var downloadDirectory: File? = null
//    private fun getPlayerCache(): Cache? {
//        if (exoplayerCache == null) {
//            val downloadContentDirectory = File(getDownloadDirectory(), DOWNLOAD_CONTENT_DIRECTORY)
//            exoplayerCache = SimpleCache(downloadContentDirectory, NoOpCacheEvictor())
//        }
//        return exoplayerCache
//    }
//
//    private fun getDownloadDirectory(): File? {
//        if (downloadDirectory == null) {
//            downloadDirectory = MusicApp.getAppContext().externalCacheDir
//            if (downloadDirectory == null) {
//                downloadDirectory = MusicApp.getAppContext().filesDir
//            }
//        }
//        return downloadDirectory
//    }
//
//    private fun buildReadOnlyCacheDataSource(
//            upstreamFactory: DefaultDataSourceFactory, cache: Cache): CacheDataSourceFactory {
//        return CacheDataSourceFactory(
//                cache,
//                upstreamFactory,
//                FileDataSourceFactory(),
//                /* eventListener= */ null,
//                CacheDataSource.CACHE_IGNORED_REASON_UNSET_LENGTH, null)/* cacheWriteDataSinkFactory= */
//    }
//}
//

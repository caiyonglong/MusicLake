//package com.cyl.music_hnust.service;
//
//import android.os.Binder;
//
//import com.cyl.music_hnust.utils.MusicInfo;
//
//
///**
// * By CWD 2013 Open Source Project
// *<br>
// * <b>控制播放Binder类</b></br>
// *
// * @author CWD
// * @version 2013.06.23 v1.0 对应各种接口的实现<br>
// *          2013.07.24 v1.1 新增控制命令接口</br> 2013.07.30 v1.2 新增控制SeekBar接口</br>
// */
//public class MediaBinder extends Binder {
//
//	private OnPlayStartListener onPlayStartListener;
//	private OnPlayingListener onPlayingListener;
//	private OnPlayPauseListener onPlayPauseListener;
//	private OnPlayCompleteListener onPlayCompleteListener;
//	private OnPlayErrorListener onPlayErrorListener;
//	private OnModeChangeListener onModeChangeListener;
//
//	private OnServiceBinderListener onServiceBinderListener;
//
//	protected void playStart(MusicInfo info) {
//		if (onPlayStartListener != null) {
//			onPlayStartListener.onStart(info);
//		}
//	}
//
//	protected void playUpdate(int currentPosition) {
//		if (onPlayingListener != null) {
//			onPlayingListener.onPlay(currentPosition);
//		}
//	}
//
//	protected void playPause() {
//		if (onPlayPauseListener != null) {
//			onPlayPauseListener.onPause();
//		}
//	}
//
//	protected void playComplete() {
//		if (onPlayCompleteListener != null) {
//			onPlayCompleteListener.onPlayComplete();
//		}
//	}
//
//	protected void playError() {
//		if (onPlayErrorListener != null) {
//			onPlayErrorListener.onPlayError();
//		}
//	}
//
//	protected void modeChange(int mode) {
//		if (onModeChangeListener != null) {
//			onModeChangeListener.onModeChange(mode);
//		}
//	}
//
//	/**
//	 * 触及SeekBar时响应
//	 */
//	public void seekBarStartTrackingTouch() {
//		if (onServiceBinderListener != null) {
//			onServiceBinderListener.seekBarStartTrackingTouch();
//		}
//	}
//
//	/**
//	 * 离开SeekBar时响应
//	 *
//	 * @param progress
//	 *            当前进度
//	 */
//	public void seekBarStopTrackingTouch(int progress) {
//		if (onServiceBinderListener != null) {
//			onServiceBinderListener.seekBarStopTrackingTouch(progress);
//		}
//	}
//
//
//
//	/**
//	 * 设置控制命令
//	 *
//	 * @param command
//	 *            控制命令
//	 */
//	public void setControlCommand(int command) {
//		if (onServiceBinderListener != null) {
//			onServiceBinderListener.control(command);
//		}
//	}
//
//	public void setOnPlayStartListener(OnPlayStartListener onPlayStartListener) {
//		this.onPlayStartListener = onPlayStartListener;
//	}
//
//	public void setOnPlayingListener(OnPlayingListener onPlayingListener) {
//		this.onPlayingListener = onPlayingListener;
//	}
//
//	public void setOnPlayPauseListener(OnPlayPauseListener onPlayPauseListener) {
//		this.onPlayPauseListener = onPlayPauseListener;
//	}
//
//	public void setOnPlayCompletionListener(
//			OnPlayCompleteListener onPlayCompleteListener) {
//		this.onPlayCompleteListener = onPlayCompleteListener;
//	}
//
//	public void setOnPlayErrorListener(OnPlayErrorListener onPlayErrorListener) {
//		this.onPlayErrorListener = onPlayErrorListener;
//	}
//
//	public void setOnModeChangeListener(
//			OnModeChangeListener onModeChangeListener) {
//		this.onModeChangeListener = onModeChangeListener;
//	}
//
//	protected void setOnServiceBinderListener(
//			OnServiceBinderListener onServiceBinderListener) {
//		this.onServiceBinderListener = onServiceBinderListener;
//	}
//
//	/**
//	 * 开始播放回调接口
//	 */
//	public interface OnPlayStartListener {
//		/**
//		 * 开始播放
//		 *
//		 * @param info
//		 *            歌曲详细信息
//		 */
//		public void onStart(MusicInfo info);
//	}
//
//	/**
//	 * 正在播放回调接口
//	 */
//	public interface OnPlayingListener {
//		/**
//		 * 开始播放
//		 *
//		 * @param currentPosition
//		 *            当前播放时间(String类型)
//		 */
//		public void onPlay(int currentPosition);
//	}
//
//	/**
//	 * 暂停播放回调接口
//	 */
//	public interface OnPlayPauseListener {
//		/**
//		 * 暂停播放
//		 */
//		public void onPause();
//	}
//
//	/**
//	 * 播放完成回调接口
//	 */
//	public interface OnPlayCompleteListener {
//		/**
//		 * 播放完成
//		 */
//		public void onPlayComplete();
//	}
//
//	/**
//	 * 播放出错回调接口
//	 */
//	public interface OnPlayErrorListener {
//		/**
//		 * 播放出错
//		 */
//		public void onPlayError();
//	}
//
//	/**
//	 * 播放模式更改回调接口
//	 */
//	public interface OnModeChangeListener {
//		/**
//		 * 播放模式变更
//		 */
//		public void onModeChange(int mode);
//	}
//
//	/**
//	 * 回调接口，只允许service使用
//	 */
//	protected interface OnServiceBinderListener {
//		/**
//		 * 触及SeekBar时响应
//		 */
//		void seekBarStartTrackingTouch();
//
//		/**
//		 * 离开SeekBar时响应
//		 *
//		 * @param progress
//		 *            当前进度
//		 */
//		void seekBarStopTrackingTouch(int progress);
//
//		/**
//		 * 设置歌词
//		 *
//		 * @param lyricView
//		 *            歌词视图
//		 * @param isKLOK
//		 *            是否属于卡拉OK模式
//		 */
//		void lrc(LyricView lyricView, boolean isKLOK);
//
//		/**
//		 * 播放控制(播放、暂停、上一首、下一首、播放模式切换)
//		 *
//		 * @param command
//		 *            控制命令
//		 */
//		void control(int command);
//	}
//
//}

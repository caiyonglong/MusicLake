package com.cyl.music_hnust.lyric;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ScrollView;
import android.widget.TextView;

public class LyricView extends ScrollView {

	private int index = 0;// 索引，第几句歌词
	private int lyricSize = 0;// 歌词总句数

	private int currentTime = 0;// 当前歌曲的播放位置
	private int dunringTime = 0;// 当前句歌词的持续时间
	private int startTime = 0;// 当前句歌词开始的时间

	private float width = 0;// 获得的画布宽
	private float height = 0;// 获得的画布高
	private float tempW = 0;// 计算画布的中间位置(宽)
	private float tempH = 0;// 计算画布的中间位置(高)
	private float tempYHigh = 0;// 计算卡拉OK模式第一句的Y轴位置
	private float tempYLow = 0;// 计算卡拉OK模式第二句的Y轴位置

	private float textHeight = 35;// 单行字高度
	private float textSize = 20;// 字体大小

	private Paint currentPaint = null;// 当前句画笔
	private Paint defaultPaint = null;// 非当前句画笔
	private List<LyricItem> mSentenceEntities = new ArrayList<LyricItem>();
	private int[] paintColorsCurrent = { Color.argb(250, 251, 248, 29),
			Color.argb(250, 255, 255, 255) };// 卡拉OK模式绘画中画笔颜色数组
	private int[] paintColorsDefault = { Color.argb(250, 255, 255, 255),
			Color.argb(250, 255, 255, 255) };// 卡拉OK模式默认画笔颜色数组

	private boolean isKLOK = false;

	public LyricView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public LyricView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public LyricView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setFocusable(true);

		// 高亮部分
		currentPaint = new Paint();
		currentPaint.setAntiAlias(true);
		currentPaint.setTextAlign(Paint.Align.CENTER);
		currentPaint.setColor(Color.argb(250, 251, 248, 29));
		currentPaint.setTextSize(textSize);
		currentPaint.setTypeface(Typeface.SERIF);

		// 非高亮部分
		defaultPaint = new Paint();
		defaultPaint.setAntiAlias(true);
		defaultPaint.setTextAlign(Paint.Align.CENTER);
		defaultPaint.setColor(Color.argb(250, 255, 255, 255));
		defaultPaint.setTextSize(textSize);
		defaultPaint.setTypeface(isKLOK ? Typeface.SERIF : Typeface.DEFAULT);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		if (canvas == null || lyricSize <= 0
				|| index >= mSentenceEntities.size()) {
			return;
		}

		if (isKLOK) {
			int nextIndex = index + 1;// 下一句
			String text = mSentenceEntities.get(index).getLyric();// 获得歌词
			float len = this.getTextWidth(currentPaint, text);// 该句歌词精确长度
			float position = dunringTime == 0 ? 0
					: ((float) currentTime - (float) startTime)
					/ (float) dunringTime;// 计算当前位置

			if (index % 2 == 0) {
				float start1 = len / 2;// 第一句的起点位置
				LinearGradient gradient = new LinearGradient(0, 0, len, 0,
						paintColorsCurrent, new float[] { position, position },
						TileMode.CLAMP);// 重绘渐变
				currentPaint.setShader(gradient);
				canvas.drawText(text, start1, tempYHigh, currentPaint);

				if (nextIndex < lyricSize) {
					text = mSentenceEntities.get(nextIndex).getLyric();// 下一句歌词
					len = this.getTextWidth(currentPaint, text);// 该句歌词精确长度
					float start2 = width - len / 2;// 第二句的起点位置
					gradient = new LinearGradient(start2, 0, width, 0,
							paintColorsDefault, null, TileMode.CLAMP);// 重绘渐变
					defaultPaint.setShader(gradient);
					canvas.drawText(text, start2, tempYLow, defaultPaint);
				}
			} else {
				float start2 = width - len / 2;// 第二句的起点位置
				float w = width > len ? width - len : 0;// 第二句的渐变起点位置
				LinearGradient gradient = new LinearGradient(w, 0, width, 0,
						paintColorsCurrent, new float[] { position, position },
						TileMode.CLAMP);// 重绘渐变
				defaultPaint.setShader(gradient);
				canvas.drawText(text, start2, tempYLow, defaultPaint);

				if (nextIndex < lyricSize) {
					text = mSentenceEntities.get(nextIndex).getLyric();// 下一句歌词
					len = this.getTextWidth(currentPaint, text);// 该句歌词精确长度
					float start1 = len / 2;// 第一句的起点位置
					gradient = new LinearGradient(0, 0, len, 0,
							paintColorsDefault, null, TileMode.CLAMP);// 重绘渐变
					currentPaint.setShader(gradient);
					canvas.drawText(text, start1, tempYHigh, currentPaint);
				}
			}
		} else {
			float plus = dunringTime == 0 ? 0
					: (((float) currentTime - (float) startTime) / (float) dunringTime)
					* (float) 30;
			// 向上滚动 这个是根据歌词的时间长短来滚动，整体上移
			canvas.translate(0, -plus);

			try {
				canvas.drawText(mSentenceEntities.get(index).getLyric(), tempW,
						tempH, currentPaint);

				float tempY = tempH;
				// 画出本句之前的句子
				for (int i = index - 1; i >= 0; i--) {
					// 向上推移
					tempY = tempY - textHeight;

					canvas.drawText(mSentenceEntities.get(i).getLyric(), tempW,
							tempY, defaultPaint);
				}
				tempY = tempH;
				// 画出本句之后的句子
				for (int i = index + 1; i < lyricSize; i++) {
					// 往下推移
					tempY = tempY + textHeight;
					if (tempY > height) {
						break;
					}
					canvas.drawText(mSentenceEntities.get(i).getLyric(), tempW,
							tempY, defaultPaint);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);

		this.width = w;
		this.height = h;
		this.tempW = w / 2;
		this.tempH = h / 2;
		this.tempYHigh = tempH;
		this.tempYLow = tempH + textHeight;
	}

	/**
	 * 精确计算文字宽度
	 *
	 * @param paint
	 * @param str
	 * @return
	 */
	private int getTextWidth(Paint paint, String str) {
		int iRet = 0;
		if (str != null && str.length() > 0) {
			int len = str.length();
			float[] widths = new float[len];
			paint.getTextWidths(str, widths);
			for (int j = 0; j < len; j++) {
				iRet += (int) Math.ceil(widths[j]);
			}
		}
		return iRet;
	}

	/**
	 * 是否属于卡拉OK模式
	 *
	 * @param isKLOK
	 *            true:是
	 */
	public void setKLOK(boolean isKLOK) {
		this.isKLOK = isKLOK;
	}

	/**
	 * 更改歌词高亮色
	 *
	 * @param color
	 *            颜色值
	 */
	public void setLyricHighlightColor(int color) {
		paintColorsCurrent = new int[] { color, Color.argb(250, 255, 255, 255) };
		currentPaint.setColor(color);
	}

	/**
	 * 引进歌词
	 *
	 * @param mSentenceEntities
	 *            歌词集合
	 */
	public void setSentenceEntities(List<LyricItem> mSentenceEntities) {
		this.mSentenceEntities = mSentenceEntities;
		this.lyricSize = mSentenceEntities.size();
	}

	/**
	 * 歌词索引信息
	 *
	 * @param indexInfo
	 *            <b>{歌词索引,当前歌曲的播放位置,当前句歌词的持续时间,当前句歌词开始的时间}</b>
	 */
	public void setIndex(int[] indexInfo) {
		this.index = indexInfo[0];
		this.currentTime = indexInfo[1];
		this.startTime = indexInfo[2];
		this.dunringTime = indexInfo[3];
	}

	/**
	 * 清空操作
	 *
	 * (这里的清空貌似有问题，会把service里的List<LyricItem>也清空，难道共用内存了，赋值后指向了同一块内存地址??)
	 */
	public void clear() {
		this.mSentenceEntities.clear();
		this.index = 0;
		this.lyricSize = 0;
		this.invalidate();
	}
}

package com.cyl.music_hnust.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.download.FileState;
import com.cyl.music_hnust.download.SqliteDao;
import com.cyl.music_hnust.service.DownloadService;

/**
 * 
 * 项目名称：MultithreadedDownload 类名称：DownloadManageAdapter 类描述： 下载管理的适配器 创建人：wpy
 * 创建时间：2014-10-11 下午6:30:10
 * 
 */
public class DownloadManageAdapter extends BaseAdapter {
	private Context context;
	private List<FileState> fileStates;
	private SqliteDao dao;
	private LayoutInflater inflater;
	private ViewHolder holder;

	public DownloadManageAdapter(Context context, List<FileState> fileStates,
			SqliteDao dao) {
		this.context = context;
		this.fileStates = fileStates;
		this.dao = dao;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return fileStates.size();
	}

	@Override
	public Object getItem(int position) {
		return fileStates.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_download, parent,
					false);
			holder = new ViewHolder();
//			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_per = (TextView) convertView.findViewById(R.id.tv_per);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			holder.progressBar.setMax(100);
			holder.btn_stop = (Button) convertView.findViewById(R.id.btn_stop);
			holder.btn_continue = (Button) convertView
					.findViewById(R.id.btn_continue);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final FileState fileState = fileStates.get(position);
		holder.tv_name.setText(fileState.getName());
		if (0 == fileState.getState()) {// 下载完成
			holder.progressBar.setVisibility(View.INVISIBLE);
			holder.btn_stop.setText("已下载");
			holder.btn_stop.setClickable(false);
		} else if (1 == fileState.getState()) {// 正在下载

			int completeSize = fileState.getCompleteSize();
			// Log.e("test>>", "progressBar  completeSize当前进度：" + completeSize);
			int fileSize = fileState.getFileSize();
			// Log.e("test>>", "progressBar文件大小：" + fileSize);
			float num = (float) completeSize / (float) fileSize;
			int result = (int) (num * 100);
			holder.progressBar.setProgress(result);
			holder.tv_per.setText(result+"%");

			Log.e("test>>", "progressBar当前进度：" + result);

			holder.btn_stop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setChange(fileState);
					holder.btn_stop.setVisibility(View.GONE);
					holder.btn_continue.setVisibility(View.VISIBLE);
				}
			});
			holder.btn_continue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setChange(fileState);
					holder.btn_continue.setVisibility(View.GONE);
					holder.btn_stop.setVisibility(View.VISIBLE);
				}
			});
		}
		// 当文件下载完成
		if (fileState.getCompleteSize() == fileState.getFileSize()) {
			fileState.setState(0);
			fileStates.set(position, fileState);
			holder.progressBar.setVisibility(ProgressBar.INVISIBLE);
			holder.btn_stop.setText("已下载");
			holder.tv_per.setVisibility(View.GONE);
			holder.btn_stop.setClickable(false);
		}
		return convertView;
	}

	/**
	 * 设置（更新）list数据
	 * 
	 * @param data
	 */
	public void setList(List<FileState> data) {
		this.fileStates = data;
	}

	private void setChange(FileState fileState) {
		Intent intent = new Intent();
		intent.setClass(context, DownloadService.class);
		intent.putExtra("downloadUrl", fileState.getUrl());
		intent.putExtra("name", fileState.getName());
		intent.putExtra("flag", "changeState");
		context.startService(intent);
	}

	private class ViewHolder {
		private ImageView iv_icon;
		private TextView tv_name;
		private TextView tv_per;
		private ProgressBar progressBar;
		private Button btn_stop;
		private Button btn_continue;
	}
}

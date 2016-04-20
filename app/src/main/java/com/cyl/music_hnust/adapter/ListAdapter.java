package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.download.DownloadFile;
import com.cyl.music_hnust.download.FileState;
import com.cyl.music_hnust.utils.MusicInfo;

import java.util.List;


public class ListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<MusicInfo> files;
    private ViewHolder viewHolder = null;
    private int i =0;

    public ListAdapter(Context context, List<MusicInfo> files, int i) {
        this.context = context;
        this.files = files;
        this.i = i;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_download1, parent, false);
            viewHolder = new ViewHolder();

//			viewHolder.icon = (ImageView) convertView
//					.findViewById(R.id.iv_icon);
            viewHolder.music_item_tv_name = (TextView) convertView
                    .findViewById(R.id.music_item_tv_name);
            viewHolder.music_item_tv_artist = (TextView) convertView
                    .findViewById(R.id.music_item_tv_artist);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MusicInfo file = files.get(position);
        viewHolder.music_item_tv_name.setText(file.getName());
        viewHolder.music_item_tv_artist.setText(file.getArtist()+"-"+file.getAlbum());

        if (i==1){
            viewHolder.music_item_tv_name.setTextColor(R.color.main_white);
            viewHolder.music_item_tv_artist.setTextColor(R.color.main_white);
        }else if (i==0){
            viewHolder.music_item_tv_name.setTextColor(R.color.gray);
            viewHolder.music_item_tv_artist.setTextColor(R.color.gray);
        }


        return convertView;
    }

    private class ViewHolder {
        public ImageView icon;
        public TextView music_item_tv_name;
        public TextView music_item_tv_artist;
    }
}

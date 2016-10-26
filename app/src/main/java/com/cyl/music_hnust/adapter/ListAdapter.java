package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.MusicInfo;

import java.util.List;


public class ListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<MusicInfo> files;
    private ViewHolder viewHolder = null;

    public ListAdapter(Context context, List<MusicInfo> files) {
        this.context = context;
        this.files = files;
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
        viewHolder.music_item_tv_artist.setText(file.getArtist() + "-" + file.getAlbum());


        return convertView;
    }

    private class ViewHolder {
        public ImageView icon;
        public TextView music_item_tv_name;
        public TextView music_item_tv_artist;
    }
}

package com.cyl.music_hnust.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.view.RoundedImageView;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    public ImageView playlist_album;
    public TextView playlist_name;
    public CardView playlist_container;

    public MyRecyclerViewHolder(View mView) {
        super(mView);
        playlist_album = (ImageView) mView.findViewById(R.id.playlist_album);
        playlist_name = (TextView) mView.findViewById(R.id.playlist_name);
        playlist_container = (CardView) mView.findViewById(R.id.playlist_container);
    }
}

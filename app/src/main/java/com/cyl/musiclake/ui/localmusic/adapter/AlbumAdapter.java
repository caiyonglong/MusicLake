package com.cyl.musiclake.ui.localmusic.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.data.model.Album;
import com.cyl.musiclake.utils.ImageUtils;
import com.cyl.musiclake.ui.common.NavigateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monkey on 2015/6/29.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyRecyclerViewHolder> {

    private Activity mContext;
    public List<Album> albums = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public AlbumAdapter(Activity mContext, List<Album> albums) {
        this.mContext = mContext;
        this.albums = albums;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_playlist_grid, parent, false);
        MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(mView);
        return mViewHolder;
    }

    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {
        holder.album.setVisibility(View.VISIBLE);
        holder.name.setText(albums.get(position).getName());
        holder.artist.setText(albums.get(position).getArtistName());
        loadBitmap(ImageUtils.getAlbumArtUri(albums.get(position).getId()).toString(), holder.album);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.album.setTransitionName("transition_album_art");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("album", albums.get(position).getId() + "");
                NavigateUtil.navigateToAlbum(mContext,
                        albums.get(position).getId(),
                        true,
                        albums.get(position).getName(),
                        new Pair<View, String>(holder.album, "transition_album_art"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    private void loadBitmap(String uri, ImageView img) {
        Log.e("uri", uri + ".........-");
        if (uri != null) {
            try {
                GlideApp.with(mContext).load(uri)
                        .error(R.drawable.default_cover)
                        .placeholder(R.drawable.default_cover)
                        .centerCrop()
                        .into(img);
            } catch (Exception e) {
                Log.e("EEEE", uri);
            }
        }
    }


    private void loadArtist(String title, final ImageView imgView) {
//        OkHttpUtils.get().url("http://apis.baidu.com/geekery/music/singer")
//                .addHeader("apikey", "0bbd28df93933b00fdbbd755f8769f1b")
//                .addParams("name", title)
//                .build()
//                .execute(new SingerCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Singer response) {
//                        if (response.code == 0)
//                            loadBitmap(response.data.image, imgView);
//
//                    }
//                });
    }


    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        public ImageView album;
        public TextView name, artist;
        public CardView playlist_container;

        public MyRecyclerViewHolder(View mView) {
            super(mView);
            album = (ImageView) mView.findViewById(R.id.album);
            name = (TextView) mView.findViewById(R.id.name);
            artist = (TextView) mView.findViewById(R.id.artist);
        }
    }


}

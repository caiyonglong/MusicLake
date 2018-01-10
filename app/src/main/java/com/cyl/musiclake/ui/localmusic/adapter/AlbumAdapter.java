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

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.data.model.Album;
import com.cyl.musiclake.ui.common.NavigateUtil;
import com.cyl.musiclake.utils.CoverLoader;

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
        Album albumInfo = albums.get(position);

        holder.album.setVisibility(View.VISIBLE);
        holder.name.setText(albumInfo.getName());
        holder.artist.setText(albumInfo.getArtistName());

        GlideApp.with(mContext)
                .load(CoverLoader.getInstance().getCoverUri(mContext, albumInfo.getId()))
                .error(R.drawable.default_cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.album);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.album.setTransitionName("transition_album_art");
        }
        holder.itemView.setOnClickListener(v -> {
            Log.e("album", albumInfo.getId() + "");
            NavigateUtil.navigateToAlbum(mContext,
                    albumInfo.getId(),
                    true,
                    albumInfo.getName(),
                    new Pair<View, String>(holder.album, "transition_album_art"));
        });

    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
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

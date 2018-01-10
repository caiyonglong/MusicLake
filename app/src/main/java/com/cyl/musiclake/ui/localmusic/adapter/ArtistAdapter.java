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
import com.cyl.musiclake.data.model.Artist;
import com.cyl.musiclake.ui.common.NavigateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monkey on 2015/6/29.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyRecyclerViewHolder> {


    private Activity mContext;
    public List<Artist> artists = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public ArtistAdapter(Activity mContext, List<Artist> artists) {
        this.mContext = mContext;
        this.artists = artists;
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
        Artist artistInfo = artists.get(position);
        holder.name.setText(artistInfo.getName());
        holder.artist.setText(artistInfo.getCount() + "首歌");
//        holder.album.setImageBitmap(
//                CoverLoader.getInstance().loadThumbnail(mContext, artistInfo.getId()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.album.setTransitionName("transition_album_art");
        }

        holder.itemView.setOnClickListener(v -> NavigateUtil.navigateToAlbum(mContext,
                artists.get(position).getId(),
                false,
                artists.get(position).getName(),
                new Pair<View, String>(holder.album, "transition_album_art")));
    }

    @Override
    public int getItemCount() {
        return artists.size();
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

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
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

package com.cyl.music_hnust.ui.adapter;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.api.GlideApp;
import com.cyl.music_hnust.bean.music.Music;
import com.cyl.music_hnust.service.PlayManager;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D22434 on 2017/9/26.
 */

public class PlayQueueAdapter extends RecyclerView.Adapter<PlayQueueAdapter.ItemHolder> {

    private List<Music> arraylist = new ArrayList<>();
    private AppCompatActivity mContext;

    public PlayQueueAdapter(AppCompatActivity context, List<Music> arraylist) {
        this.arraylist = arraylist;
        this.mContext = context;
    }

    @Override
    public PlayQueueAdapter.ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View song = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_music, viewGroup, false);
        return new ItemHolder(song);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        Music localItem = arraylist.get(position);

        holder.title.setText(localItem.getTitle());
        holder.artist.setText(
                FileUtils.getArtistAndAlbum(localItem.getArtist(), localItem.getAlbum()));

        if (localItem.getType() == Music.Type.LOCAL) {
            loadBitmap(ImageUtils.getAlbumArtUri(localItem.getAlbumId()).toString(),
                    holder.albumArt);
        } else {
            if (localItem.getCoverUri() != null) {
//                GlideApp.with(this).load()
            } else if (localItem.getCover() != null) {
                holder.albumArt.setImageBitmap(localItem.getCover());
            } else {
                holder.albumArt.setImageResource(R.drawable.default_cover);
            }
        }

        if (PlayManager.getPlayingMusic() != null
                && PlayManager.getPlayingMusic().equals(localItem)) {
            holder.v_playing.setVisibility(View.VISIBLE);
        } else {
            holder.v_playing.setVisibility(View.GONE);
        }
        setOnClickListener(holder, position);

        holder.popupmenu.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_clear));
        holder.popupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayManager.removeFromQueue(position);
                arraylist = PlayManager.getPlayList();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }


    public void setSongList(List<Music> arraylist) {
        this.arraylist = arraylist;
        notifyDataSetChanged();
    }

    private void loadBitmap(String uri, ImageView img) {
        try {
            GlideApp.with(mContext).load(uri)
                    .error(R.drawable.default_cover)
                    .placeholder(R.drawable.default_cover)
                    .centerCrop()
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOnClickListener(PlayQueueAdapter.ItemHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PlayManager.setPlayList(arraylist);
                        PlayManager.play(position);
                        notifyDataSetChanged();
                    }
                }, 100);
            }
        });
    }

    public void setPaletteSwatch(Palette.Swatch mSwatch) {
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView artist;
        private ImageView albumArt;
        private ImageView popupmenu;
        private View v_playing;

        public ItemHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.tv_title);
            this.artist = (TextView) view.findViewById(R.id.tv_artist);
            this.albumArt = (ImageView) view.findViewById(R.id.iv_cover);
            this.popupmenu = (ImageView) view.findViewById(R.id.iv_more);
            this.v_playing = view.findViewById(R.id.v_playing);

        }
    }
}

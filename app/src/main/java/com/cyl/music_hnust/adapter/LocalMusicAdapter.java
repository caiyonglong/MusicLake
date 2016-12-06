package com.cyl.music_hnust.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.MainActivity;
import com.cyl.music_hnust.activity.PlaylistDetailActivity;
import com.cyl.music_hnust.dataloaders.PlaylistLoader;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.model.music.Playlist;
import com.cyl.music_hnust.utils.CoverLoader;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.view.AddPlaylistDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：本地歌曲item
 * 作者：yonglong on 2016/8/8 19:44
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.ItemHolder> {

    private AppCompatActivity context;
    private List<Music> musicInfos = new ArrayList<>();
    private int currentlyPlayingPosition =0;


    public LocalMusicAdapter(AppCompatActivity context, List<Music> musicInfos) {
        this.context = context;
        this.musicInfos = musicInfos;
    }

    public void setMusicInfos(List<Music> musicInfos) {
        this.musicInfos = musicInfos;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent,false);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;

    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        Music localItem = musicInfos.get(position);
        if (localItem.getType()== Music.Type.LOCAL) {
            loadBitmap(ImageUtils.getAlbumArtUri(localItem.getAlbumId()).toString(),
                    holder.albumArt);
        }else {
            if (localItem.getCover() != null) {
                holder.albumArt.setImageBitmap(localItem.getCover());
            } else {
                holder.albumArt.setImageResource(R.drawable.default_cover);
            }
        }
        holder.title.setText(FileUtils.getTitle(localItem.getTitle()));
        holder.artist.setText(FileUtils.getArtistAndAlbum(localItem.getArtist(),localItem.getAlbum()));

        if(MainActivity.mPlayService.getPlayingMusic().getId()==musicInfos.get(position).getId())
        {
            holder.v_playing.setVisibility(View.VISIBLE);
        }else {
            holder.v_playing.setVisibility(View.GONE);
        }

        setOnPopupMenuListener(holder, position);
    }


    private void loadBitmap(String uri, ImageView img) {
        try {
            ImageLoader.getInstance().displayImage(uri, img,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .showImageOnFail(R.drawable.default_cover)
                            .showImageForEmptyUri(R.drawable.default_cover)
                            .showImageOnLoading(R.drawable.default_cover)
                            .resetViewBeforeLoading(true)
                            .build());
        } catch (Exception e) {
            Log.e("EEEE", uri);
        }
    }

    private void setOnPopupMenuListener(ItemHolder holder, final int position) {
        holder.popupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu mPopupmenu = new PopupMenu(context, v);
                mPopupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_song_play:
                                MainActivity.mPlayService.setMyMusicList(musicInfos);
                                MainActivity.mPlayService.playMusic(position);
                                break;
                            case R.id.popup_song_detail:
                                getMusicInfo(musicInfos.get(position));
                                break;
                            case R.id.popup_song_goto_album:
                                Log.e("album",musicInfos.get(position).getAlbumId()+"");
                                Intent intent = new Intent(context, PlaylistDetailActivity.class);
                                intent.putExtra(Extras.ALBUM_ID,musicInfos.get(position).getAlbumId());
                                intent.putExtra(Extras.PLAYLIST_NAME,musicInfos.get(position).getAlbum()+"");
                                intent.putExtra(Extras.ALBUM,0);
                                context.startActivity(intent);
                                break;
                            case R.id.popup_song_goto_artist:
                                break;
                            case R.id.popup_song_addto_queue:
                                AddPlaylistDialog.newInstance(musicInfos.get(position)).show(context.getSupportFragmentManager(), "ADD_PLAYLIST");
                                break;
                        }
                        return false;
                    }
                });
                mPopupmenu.inflate(R.menu.popup_song);
                mPopupmenu.show();
            }
        });

    }

    private void getMusicInfo(Music music) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(FileUtils.getTitle(music.getTitle()));
        StringBuilder sb = new StringBuilder();
        sb.append("艺术家：")
                .append(music.getArtist())
                .append("\n\n")
                .append("专辑：")
                .append(music.getAlbum())
                .append("\n\n")
                .append("播放时长：")
                .append(SystemUtils.formatTime(music.getDuration()))
                .append("\n\n")
                .append("文件名称：")
                .append(music.getFileName())
                .append("\n\n")
                .append("文件大小：")
                .append(SystemUtils.formatSize(music.getFileSize()))
                .append("\n\n")
                .append("文件路径：")
                .append(new File(music.getUri()).getParent());
        dialog.setMessage(sb.toString());
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return (null != musicInfos ? musicInfos.size() : 0);
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView title, artist;
        protected ImageView albumArt;
        protected ImageView popupmenu;
        protected View v_playing;

        public ItemHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.tv_title);
            this.artist = (TextView) view.findViewById(R.id.tv_artist);
            this.albumArt = (ImageView) view.findViewById(R.id.iv_cover);
            this.popupmenu = (ImageView) view.findViewById(R.id.iv_more);
            this.v_playing = view.findViewById(R.id.v_playing);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("lllllaaaaaaaa",musicInfos.size()+"====");
                    MainActivity.mPlayService.setMyMusicList(musicInfos);
                    MainActivity.mPlayService.playMusic(getAdapterPosition());

                    Log.e("LOCal",MainActivity.mPlayService.getMusicList().size()+"====");

                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(currentlyPlayingPosition);
                            notifyItemChanged(getAdapterPosition());
                            currentlyPlayingPosition =getAdapterPosition();
                        }
                    }, 50);
                }
            }, 100);
        }
    }
}
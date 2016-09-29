package com.cyl.music_hnust.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import com.cyl.music_hnust.model.LocalPlaylist;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.utils.CoverLoader;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.MusicUtils;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.view.AddPlaylistDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/9/27 21:20
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlayingQueueAdapter extends RecyclerView.Adapter<PlayingQueueAdapter.ItemHolder> {

    private AppCompatActivity context;
    private List<Music> musicInfos = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public LocalMusicAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(LocalMusicAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public PlayingQueueAdapter(AppCompatActivity context, List<Music> musicInfos) {
        this.context = context;
        this.musicInfos = musicInfos;
    }

    public void setMusicInfos(List<Music> musicInfos) {
        this.musicInfos = musicInfos;
    }

    @Override
    public PlayingQueueAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music1, null);
        PlayingQueueAdapter.ItemHolder itemHolder = new PlayingQueueAdapter.ItemHolder(v);
        return itemHolder;

    }

    @Override
    public void onBindViewHolder(final PlayingQueueAdapter.ItemHolder holder, final int position) {
        Music localItem = musicInfos.get(position);

        Bitmap cover = CoverLoader.getInstance().loadThumbnail(localItem.getCoverUri());
        holder.albumArt.setImageBitmap(cover);

        holder.title.setText(localItem.getTitle());
        holder.artist.setText(localItem.getArtist());
        if(MainActivity.mPlayService.getmPlayingMusicId()==localItem.getAlbumId()){
            holder.v_playing.setVisibility(View.VISIBLE);
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        setOnPopupMenuListener(holder, position);
    }

    private void setOnPopupMenuListener(PlayingQueueAdapter.ItemHolder holder, final int position) {
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
                                Intent intent = new Intent(context, PlaylistDetailActivity.class);
                                intent.putExtra(Extras.ALBUM_ID,musicInfos.get(position).getAlbumId());
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
        dialog.setTitle(music.getTitle());
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

    private void getPlaylistInfo(Music music) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        List<LocalPlaylist> playlists = MusicUtils.scanPlaylist(context);
        dialog.setTitle("歌单列表");
        if (playlists.size() == 0) {
            dialog.setMessage("暂无歌单,请新建!");
        } else {
            String msg = "";
            for (int i = 0; i < playlists.size(); i++) {
                msg += playlists.get(i).getName() + "\n";
            }
            dialog.setMessage(msg);
        }
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return (null != musicInfos ? musicInfos.size() : 0);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
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
        }
    }
}
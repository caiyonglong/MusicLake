package com.cyl.musiclake.ui.music.local.adapter;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.music.local.dialog.AddPlaylistDialog;
import com.cyl.musiclake.utils.ConvertUtils;
import com.cyl.musiclake.utils.FormatUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：本地歌曲item
 * 作者：yonglong on 2016/8/8 19:44
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class AlbumMusicAdapter extends RecyclerView.Adapter<AlbumMusicAdapter.ItemHolder> {

    private AppCompatActivity context;
    private List<Music> musicInfos = new ArrayList<>();

    public AlbumMusicAdapter(AppCompatActivity context, List<Music> musicInfos) {
        this.context = context;
        this.musicInfos = musicInfos;
    }

    public void setMusicInfos(List<Music> musicInfos) {
        this.musicInfos = musicInfos;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        Music localItem = musicInfos.get(position);

        holder.title.setText(ConvertUtils.getTitle(localItem.getTitle()));
        holder.artist.setText(ConvertUtils.getArtistAndAlbum(localItem.getArtist(), localItem.getAlbum()));
        if (PlayManager.getPlayingMusic() != null
                && PlayManager.getPlayingMusic().equals(localItem)) {
            holder.v_playing.setVisibility(View.VISIBLE);
        } else {
            holder.v_playing.setVisibility(View.GONE);
        }
        setOnPopupMenuListener(holder, position);
    }

    private void setOnPopupMenuListener(ItemHolder holder, final int position) {
        holder.popupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu mPopupmenu = new PopupMenu(context, v);
                mPopupmenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.popup_song_play:
                            PlayManager.setPlayList(musicInfos);
                            PlayManager.play(position);
                            break;
                        case R.id.popup_song_detail:
                            getMusicInfo(musicInfos.get(position));
                            break;
                        case R.id.popup_song_addto_queue:
                            AddPlaylistDialog.newInstance(musicInfos.get(position)).show(context.getSupportFragmentManager(), "ADD_PLAYLIST");
                            break;
                    }
                    return false;
                });
                mPopupmenu.inflate(R.menu.popup_album);
                mPopupmenu.show();
            }
        });

    }

    private void getMusicInfo(Music music) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(ConvertUtils.getTitle(music.getTitle()));
        StringBuilder sb = new StringBuilder();
        sb.append("艺术家：")
                .append(music.getArtist())
                .append("\n\n")
                .append("专辑：")
                .append(music.getAlbum())
                .append("\n\n")
                .append("播放时长：")
                .append(FormatUtil.formatTime(music.getDuration()))
                .append("\n\n")
                .append("文件名称：")
                .append(music.getFileName())
                .append("\n\n")
                .append("文件大小：")
                .append(FormatUtil.formatSize(music.getFileSize()))
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
                    PlayManager.setPlayList(musicInfos);
                    PlayManager.play(getAdapterPosition());
                    notifyDataSetChanged();
                }
            }, 100);
        }
    }
}
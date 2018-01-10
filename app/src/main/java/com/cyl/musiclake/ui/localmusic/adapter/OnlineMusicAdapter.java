package com.cyl.musiclake.ui.localmusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.ui.onlinemusic.activity.ArtistInfoActivity;
import com.cyl.musiclake.ui.onlinemusic.model.OnlineMusicInfo;
import com.cyl.musiclake.utils.Extras;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：在线音乐列表适配器
 * 作者：yonglong on 2016/9/7 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineMusicAdapter extends RecyclerView.Adapter<OnlineMusicAdapter.ItemHolder> {

    private Context context;
    public List<OnlineMusicInfo> musicInfos = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public OnlineMusicAdapter(Context context, List<OnlineMusicInfo> musicInfos) {
        this.context = context;
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
        OnlineMusicInfo localItem = musicInfos.get(position);
        GlideApp.with(context)
                .load(localItem.getPic_small())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.albumArt);
        holder.title.setText(localItem.getTitle());
        holder.artist.setText(localItem.getArtist_name());

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

    private void setOnPopupMenuListener(ItemHolder holder, final int position) {
        holder.popupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu mPopupmenu = new PopupMenu(context, v);
                mPopupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_song_detail:
                                getMusicInfo(musicInfos.get(position));
                                break;
                            case R.id.popup_song_goto_artist:
                                Intent intent = new Intent(context, ArtistInfoActivity.class);
                                intent.putExtra(Extras.TING_UID, musicInfos.get(position).getTing_uid());
                                context.startActivity(intent);
                                break;
                            case R.id.popup_song_download:
//                                conver(musicInfos.get(position));
                                break;
                        }
                        return false;
                    }
                });
                mPopupmenu.inflate(R.menu.popup_song_online);
                mPopupmenu.show();
            }
        });

    }


    private void getMusicInfo(OnlineMusicInfo onlineMusicInfo) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(onlineMusicInfo.getTitle());
        StringBuilder sb = new StringBuilder();
        sb.append("艺术家：")
                .append(onlineMusicInfo.getArtist_name())
                .append("\n\n")
                .append("专辑：")
                .append(onlineMusicInfo.getAlbum_title())
                .append("\n\n")
                .append("歌曲id：")
                .append(onlineMusicInfo.getSong_id())
                .append("\n\n")
                .append("文件名称：")
                .append(onlineMusicInfo.getTitle());
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
//            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//
        }
    }


//    private void conver(OnlineMusicInfo onlineMusicInfo) {
//
//        new PlayOnlineMusic(context, onlineMusicInfo) {
//            @Override
//            public void onPrepare() {
//
//            }
//
//            @SuppressLint("StringFormatInvalid")
//            @Override
//            public void onSuccess(Music music) {
//
//                ToastUtils.show(context, "正在下载");
////                PreferencesUtils.
//                Intent intent = new Intent(context, DownloadService.class);
//                intent.putExtra("downloadUrl", music.getUri());
//                intent.putExtra("name", FileUtils.getMp3FileName(music.getArtist(), music.getTitle()));
//                intent.putExtra("flag", "startDownload");
//
//                context.startService(intent);
//            }
//
//            @Override
//            public void onFail(Call call, Exception e) {
//                ToastUtils.show(context, "00000");
//            }
//        }.execute();
//    }
}
package com.cyl.music_hnust.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.model.OnlineMusicInfo;
import com.cyl.music_hnust.utils.MusicUtils;

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

    public OnlineMusicAdapter(Context context, RecyclerView mRecyclerView, List<OnlineMusicInfo> musicInfos) {
        this.context = context;
        this.musicInfos = musicInfos;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music1, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;

    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        OnlineMusicInfo localItem = musicInfos.get(position);
//        ImageLoader.getInstance().displayImage(ImageUtils.getAlbumArtUri(Long.parseLong(localItem.geta())).toString(), holder.albumArt, ImageUtils.getAlbumDisplayOptions());
        holder.title.setText(localItem.getTitle());
        holder.artist.setText(localItem.getArtist_name());

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }
        setOnPopupMenuListener(holder,position);
    }

    private void setOnPopupMenuListener(ItemHolder holder, final int position) {
        holder.popupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu mPopupmenu = new PopupMenu(context,v);
                mPopupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.popup_song_play:
//                                MainActivity.mPlayService.playMusic(position);
                                break;
                            case R.id.popup_song_detail:
                                getMusicInfo(musicInfos.get(position));
                                break;
                            case R.id.popup_song_goto_artist:
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
    private void getPlaylistInfo(OnlineMusicInfo onlineMusicInfo) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        List<String> playlists= MusicUtils.scanPlaylist(context);
        dialog.setTitle("歌单列表");
        if (playlists.size()==0) {
            dialog.setMessage("暂无歌单,请新建!");
        }else {
            String msg="";
            for (int i=0; i<playlists.size();i++){
                msg=playlists.get(i)+"\n";
            }
            dialog.setMessage(msg);
        }
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
}
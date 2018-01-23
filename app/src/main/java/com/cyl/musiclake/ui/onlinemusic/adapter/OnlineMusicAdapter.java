package com.cyl.musiclake.ui.onlinemusic.adapter;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.api.baidu.BaiduMusicInfo;
import com.cyl.musiclake.data.model.Music;

import java.util.List;

/**
 * 功能：在线音乐列表适配器
 * 作者：yonglong on 2016/9/7 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineMusicAdapter extends BaseQuickAdapter<BaiduMusicInfo, BaseViewHolder> {
//        RecyclerView.Adapter<OnlineMusicAdapter.ItemHolder> {

    public OnlineMusicAdapter(List<BaiduMusicInfo> musicInfos) {
        super(R.layout.item_music, musicInfos);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaiduMusicInfo baiduMusicInfo) {
        Music music = new Music();
        music.setTitle(baiduMusicInfo.getTitle());
        music.setArtist(baiduMusicInfo.getArtist_name());
        music.setAlbum(baiduMusicInfo.getAlbum_title());
        music.setCoverUri(baiduMusicInfo.getPic_big());
        music.setId(baiduMusicInfo.getSong_id());
        music.setLrcPath(baiduMusicInfo.getLrclink());

        holder.setText(R.id.tv_title, baiduMusicInfo.getTitle());
        holder.setText(R.id.tv_artist, baiduMusicInfo.getArtist_name());

        GlideApp.with(mContext)
                .load(music.getCoverUri())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) holder.getView(R.id.iv_cover));
//        holder.getView(R.id.iv_more);
//        holder.getView(R.id.v_playing);
//        setOnPopupMenuListener();
    }

    //    private void setOnPopupMenuListener(ItemHolder holder, final int position) {
//        holder.popupmenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PopupMenu mPopupmenu = new PopupMenu(context, v);
//                mPopupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.popup_song_detail:
//                                getMusicInfo(musicInfos.get(position));
//                                break;
//                            case R.id.popup_song_goto_artist:
//                                Intent intent = new Intent(context, ArtistInfoActivity.class);
//                                intent.putExtra(Extras.TING_UID, musicInfos.get(position).getTing_uid());
//                                context.startActivity(intent);
//                                break;
//                            case R.id.popup_song_download:
////                                conver(musicInfos.get(position));
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                mPopupmenu.inflate(R.menu.popup_song_online);
//                mPopupmenu.show();
//            }
//        });
//
//    }
//    private void getMusicInfo(BaiduMusicInfo onlineMusicInfo) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//        dialog.setTitle(onlineMusicInfo.getTitle());
//        StringBuilder sb = new StringBuilder();
//        sb.append("艺术家：")
//                .append(onlineMusicInfo.getArtist_name())
//                .append("\n\n")
//                .append("专辑：")
//                .append(onlineMusicInfo.getAlbum_title())
//                .append("\n\n")
//                .append("歌曲id：")
//                .append(onlineMusicInfo.getSong_id())
//                .append("\n\n")
//                .append("文件名称：")
//                .append(onlineMusicInfo.getTitle());
//        dialog.setMessage(sb.toString());
//        dialog.show();
//    }


//    private void conver(BaiduMusicInfo onlineMusicInfo) {
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
//                intent.putExtra("name", ConvertUtils.getMp3FileName(music.getArtist(), music.getTitle()));
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
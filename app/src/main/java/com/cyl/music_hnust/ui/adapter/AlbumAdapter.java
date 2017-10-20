package com.cyl.music_hnust.ui.adapter;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.callback.SingerCallback;
import com.cyl.music_hnust.bean.music.Album;
import com.cyl.music_hnust.bean.music.Singer;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.NavigateUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

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
        View mView = mLayoutInflater.inflate(R.layout.item_playlist, parent, false);
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
                Glide.with(mContext).load(uri)
                        .error(R.drawable.default_cover)
                        .placeholder(R.drawable.default_cover)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into(img);
//                ImageLoader.getInstance().displayImage(uri, img,
//                        new DisplayImageOptions.Builder().cacheInMemory(true)
//                                .showImageOnFail(R.drawable.default_cover)
//                                .showImageForEmptyUri(R.drawable.default_cover)
//                                .showImageOnLoading(R.drawable.default_cover)
//                                .build());
            } catch (Exception e) {
                Log.e("EEEE", uri);
            }
        }
    }


    private void loadArtist(String title, final ImageView imgView) {
        OkHttpUtils.get().url("http://apis.baidu.com/geekery/music/singer")
                .addHeader("apikey", "0bbd28df93933b00fdbbd755f8769f1b")
                .addParams("name", title)
                .build()
                .execute(new SingerCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Singer response) {
                        if (response.getCode() == 0)
                            loadBitmap(response.getData().getImage(), imgView);

                    }
                });
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

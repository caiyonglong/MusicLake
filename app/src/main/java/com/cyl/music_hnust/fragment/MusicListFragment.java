package com.cyl.music_hnust.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyl.music_hnust.PlayerActivity;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.ListAdapter;
import com.cyl.music_hnust.http.HttpByGet;
import com.cyl.music_hnust.utils.CommonUtils;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.view.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 永龙 on 2016/4/2.
 */
public class MusicListFragment extends Fragment {
    View mView;
    int flag;
    public static RoundedImageView iv_album;
    public static  Animation operatingAnim;
    private ListView song_list;
    private List<MusicInfo> datas = new ArrayList<>();
//    public  LrcView lyric;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_player, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        flag = (int) getArguments().get("flag");
        if (flag ==0){
            song_list.setVisibility(View.VISIBLE);
        }else if (flag==1){
            iv_album.setVisibility(View.VISIBLE);
            initAlbum();
        }else {
         //   lyric.setVisibility(View.VISIBLE);

        }
    }

    private void initAlbum() {
        operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.widget_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        final MusicInfo song = PlayerActivity.mService.getSong();
        if (song != null) {
            Bitmap bitmap=null;
            if (HttpByGet.isURL(song.getAlbumPic())){
                final Bitmap[] bitmap1 = {null};
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bitmap1[0] =HttpByGet.getHttpBitmap(song.getAlbumPic());
                    }
                }).start();
                bitmap =bitmap1[0];
            }else {
                bitmap = CommonUtils.scaleBitmap(getActivity(), song.getAlbumPic());
            }
            if (bitmap!=null)
            iv_album.setImageBitmap(bitmap);
        }
      //  iv_album.startAnimation(operatingAnim);
    }

    private void initView() {
        iv_album = (RoundedImageView) mView.findViewById(R.id.iv_album);
        song_list = (ListView) mView.findViewById(R.id.song_list);
        datas = PlayerActivity.mService.getSongs();
        final ListAdapter adapter = new ListAdapter(getContext(),datas,1);
        song_list.setAdapter(adapter);
        song_list.setSelection(PlayerActivity.mService.getCurrentListItme());
        song_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayerActivity.mService.setCurrentListItme(position);
                PlayerActivity.mService.playMusic(datas.get(position).getPath());
                adapter.getView(position, view, parent).setAlpha((float) 0.5);
            }
        });
       // lyric = (LrcView) mView.findViewById(R.id.LyricShow);
    }


}

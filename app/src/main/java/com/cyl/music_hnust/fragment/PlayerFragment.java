package com.cyl.music_hnust.fragment;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyl.music_hnust.MyActivity;
import com.cyl.music_hnust.PlayerActivity;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.ListAdapter;
import com.cyl.music_hnust.http.HttpByGet;
import com.cyl.music_hnust.lyric.ILrcViewListener;
import com.cyl.music_hnust.lyric.LyricItem;
import com.cyl.music_hnust.lyric.LyricParser;
import com.cyl.music_hnust.lyric.LyricView;
import com.cyl.music_hnust.utils.CommonUtils;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.view.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 永龙 on 2016/4/2.
 */
public class PlayerFragment extends Fragment {
    View mView;
    int flag;
    public static RoundedImageView iv_album;
    private ListView song_list;
    private List<MusicInfo> datas = new ArrayList<>();
    public static LyricView lyricView;
    private static Bitmap bitmap = null;
    /**
     * 歌词
     */
    public static boolean hasLyric = false;// 是否有歌词

    public static List<LyricItem> lyricList;// 歌词列表


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_player, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lyricList = new ArrayList<>();
        mContext = getContext();
        initView();
        // 启动
        handler.post(updateThread);

        flag = (int) getArguments().get("flag");
        if (flag == 0) {
            song_list.setVisibility(View.VISIBLE);
        } else if (flag == 1) {
            iv_album.setVisibility(View.VISIBLE);
            initAlbumpic();
        }
        else {
            lyricView.setVisibility(View.VISIBLE);
            initLrc();
        }
    }

    private void initView() {
        iv_album = (RoundedImageView) mView.findViewById(R.id.iv_album);
        song_list = (ListView) mView.findViewById(R.id.song_list);
        if ( PlayerActivity.mService.getSongs()!=null&& PlayerActivity.mService.getSongs().size()>0)
        datas = PlayerActivity.mService.getSongs();

        final ListAdapter adapter = new ListAdapter(getContext(), datas);
        song_list.setAdapter(adapter);
        song_list.setSelection(PlayerActivity.mService.getCurrentListItme());
        song_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayerActivity.mService.setCurrentListItme(position);
                PlayerActivity.mService.playMusic(datas.get(position).getPath());
//                PlayerActivity.init();
                //   adapter.getView(position, view, parent).setAlpha((float) 0.5);
            }
        });
        lyricView = (LyricView) mView.findViewById(R.id.LyricShow);
    }


    private static Context mContext;
    public static ObjectAnimator operatingAnim;


    public static void initAlbumpic() {
        /**
         * 旋转动画
         */
        LinearInterpolator lin = new LinearInterpolator();

        operatingAnim = ObjectAnimator.ofFloat(iv_album,"rotation",0,360);
        operatingAnim.setDuration(10000);
        operatingAnim.setRepeatCount(-1);
        operatingAnim.setRepeatMode(ObjectAnimator.RESTART);
        operatingAnim.setInterpolator(lin);

//        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.widget_rotate);

//        operatingAnim.setInterpolator(lin);


        if (MyActivity.mService.getSongs() != null && MyActivity.mService.getSongs().size()>0) {


            String pic = MyActivity.mService.getSong().getAlbumPic();
            if (!HttpByGet.isURL(pic)&&pic!=null) {

                    bitmap = CommonUtils.scaleBitmap(mContext, MyActivity.mService.getSong().getAlbumPic());
                    iv_album.setImageBitmap(bitmap);
            }
        }
        operatingAnim.start();
   //     iv_album.setAnimation(operatingAnim);
//
    }
    public static void initLrc(){
        String lyricpath =null;
        if (MyActivity.mService.hasLyric){
            lyricpath = MyActivity.mService.LrcPath;
            if (lyricpath.endsWith(".lrc")){
                initLrc(lyricpath);
            }

        }



    }
    /**
     * 初始化歌词
     */
    private static void initLrc(String lyricPath) {
        hasLyric = false;
        lyricList = null;
        if (lyricPath != null&&lyricPath.endsWith(".lrc")) {
            try {
                LyricParser parser = new LyricParser(lyricPath);
                lyricList = parser.parser();
                if (lyricList.size()>0){
                    hasLyric = true;
                    PlayerFragment.lyricView.setLrc(lyricList);
                    //设置自定义的LrcView上下拖动歌词时监听
                    PlayerFragment.lyricView.setListener(new ILrcViewListener() {
                        //当歌词被用户上下拖动的时候回调该方法,从高亮的那一句歌词开始播放
                        public void onLrcSeeked(int newPosition, LyricItem row) {
                            if (MyActivity.mService.mMediaPlayer != null) {

                                Log.d("当歌词被用户上下拖动的时候", "onLrcSeeked:" + row.getTime());
                                MyActivity.mService.movePlay(row.getTime());
                            }
                        }
                    });
                }else  if ( PlayerFragment.lyricView != null) {
                    PlayerFragment.lyricView.setLoadingTipText("暂无歌词");
                    PlayerFragment.lyricView.setLrc(null);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else  if ( PlayerFragment.lyricView != null) {
            PlayerFragment.lyricView.setLoadingTipText("暂无歌词");
            PlayerFragment.lyricView.setLrc(null);
        }
    }
    private static Handler handler = new Handler();
    Runnable updateThread = new Runnable() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        public void run() {
            // 获得歌曲的长度并设置成播放进度条的最大值
            //获取歌曲播放的位置
            final long timePassed = MyActivity.mService.getCurrent();
            //滚动歌词
            if (lyricView!=null)
                lyricView.seekLrcToTime(timePassed,
                        MyActivity.mService.getDuration());
            if (MyActivity.mService.mMediaPlayer.isPlaying()){
                if (operatingAnim.isPaused()){
                    operatingAnim.resume();
                }
            }else {
                if (operatingAnim.isRunning()){
                        operatingAnim.pause();
                }
              //  iv_album.clearAnimation();
            }
            // 每次延迟100毫秒再启动线程
            handler.postDelayed(updateThread, 100);
        }
    };
}

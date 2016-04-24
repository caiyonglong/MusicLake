package com.cyl.music_hnust;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyl.music_hnust.adapter.AddMusicRecyclerviewAdapter;
import com.cyl.music_hnust.adapter.MusicRecyclerViewAdapter;
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.utils.ScanUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AddSong2PlaylistActivity extends AppCompatActivity implements AddMusicRecyclerviewAdapter.OnItemClickListener {

    private TextView tv_finish, tv_back;
    private RecyclerView recyclerview;

    public ImageButton btn_back;
    private String playlist;// 当前歌单

    private List<MusicInfo> songs;// 得到全部歌曲
    private List<Boolean> Is_check;// 得到全部歌曲

    private AddMusicRecyclerviewAdapter adapter;

    private final int UPDATEADAPTER = 111;

    private ScanUtil scanUtil;
    private MyHandler handler;


    private RecyclerView.LayoutManager mLayoutManager;


    class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            final Activity activity = mActivityReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case UPDATEADAPTER:
                        adapter.Is_check = Is_check;
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        finish();
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsong2playlist);
        songs = new ArrayList<>();
        Is_check = new ArrayList<>();

        tempSong = new ArrayList<Integer>();

        scanUtil = new ScanUtil(this);
        handler = new MyHandler(this);

        Intent it = getIntent();
        playlist = it.getStringExtra("playlist");

        initData();
        initView();
        initListener();
    }


    public void initView() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview = (RecyclerView) findViewById(R.id.id_recyclerview);

        tv_finish = (TextView) findViewById(R.id.tv_finish);
        tv_back = (TextView) findViewById(R.id.tv_back);
        btn_back = (ImageButton) findViewById(R.id.back_btn);

        adapter = new AddMusicRecyclerviewAdapter(getApplicationContext(), songs, Is_check);

        adapter.setOnItemClickListener(this);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(mLayoutManager);

    }

    List<MusicInfo> songListForPlaylist;
    ArrayList<Integer> tempSong;
    private void initListener() {
        // 完成添加
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                scanUtil.scanPlaylistSongFromDB(playlist);
//                songListForPlaylist = MusicList.list;



                for (int i = 0; i < Is_check.size(); i++) {
                    Boolean hasMusic = false;
                    if (Is_check.get(i)) {
//                        for (int j = 0; j< songListForPlaylist.size(); j++) {
//                            if (songListForPlaylist.get(j).getId().equals(songs.get(i).getId())) {
//                                hasMusic = true;
//                                Log.e("song:",songs.get(i).getId()+"");
//
//                                break;
//                            }
//                        }
                        Log.e("songs===",songs.get(i).getId()+"::::"+songs.get(i).getName());
                        tempSong.add(Integer.valueOf(songs.get(i).getId()));
//                        if (!hasMusic) {
//                            Log.e("song:",songs.size()+"");
////                            if (songs!=null){
////                          //      tempSong.add(Integer.valueOf(songs.get(i).getId()));
////                            }
//                         //
//                        }
                    }
                }

                int[] addSongTemp = null;
                for (int i = 0; i < tempSong.size(); i++) {

                    Log.e("ADDDDDDDDD===", tempSong.get(i) + "==" + playlist);
                }
                addSongTemp = new int[tempSong.size()];
                for (int i = 0; i < tempSong.size(); i++) {
                    addSongTemp[i] = tempSong.get(i);
                }
                scanUtil.addplaylist(playlist, addSongTemp);

                handler.sendEmptyMessage(1);


            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 返回适配器所要加载的数据集合
     */
    private void initData() {

        /**
         * 获取数据库中所有的歌曲
         */
        scanUtil.scanMusicFromDB();
        songs = MusicList.list;

        for (int i = 0; i < songs.size(); i++) {
            Is_check.add(false);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.container:
                if (Is_check.get(position)) {
                    Is_check.set(position, false);
                } else {
                    Is_check.set(position, true);
                }
                handler.sendEmptyMessage(UPDATEADAPTER);
                break;
        }

    }

}

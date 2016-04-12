package com.cyl.music_hnust;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cyl.music_hnust.adapter.ListViewAdapter;
import com.cyl.music_hnust.db.DBDao;
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.utils.MusicUtils;
import com.cyl.music_hnust.utils.ScanUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AddSong2PlaylistActivity extends AppCompatActivity {

    private TextView tv_finish, tv_back;
    private ListView listView;
    public ImageButton  btn_back;
    private String playlist;// 当前歌单
    private List<MusicInfo> songs;// 得到全部歌曲
    private ArrayList<String> addSongIds = new ArrayList<String>();// 将要添加的歌曲的id集合
    private int[] addSongs;// 将要添加的歌曲的id集合
    private ListViewAdapter listViewAdapter;
    private List<Map<String, Object>> listItems;// 传进适配器的数据
    private ArrayList<String> songIds;// 全部歌曲的id
    private final int SETADAPTER = 111;
    private Timer timer;
    private TimerTask myTimerTask;
    private ScanUtil scanUtil;
    private MyHandler handler;

    class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference= new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            final Activity activity = mActivityReference.get();
            if (activity!=null) {
                switch (msg.what) {
                    case SETADAPTER:
                        setAdapter();
                        break;
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsong2playlist);
        songs=new ArrayList<>();
        scanUtil = new ScanUtil(this);
        handler = new MyHandler(this);

        Intent it = getIntent();
        playlist = it.getStringExtra("playlist");

        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 以下是定时器0.1秒后再跳到handler加载适配器
        timer = new Timer();
        myTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = SETADAPTER;
                handler.sendMessage(message);
            }
        };
        timer.schedule(myTimerTask, 100);
    }

    public void initView() {
        listView = (ListView) findViewById(R.id.listView);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        tv_back = (TextView) findViewById(R.id.tv_back);
        btn_back = (ImageButton) findViewById(R.id.back_btn);
    }

    private void initListener() {
        // 完成添加
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (addSongIds.size() > 0) {
                    addSongs = new int[addSongIds.size()];
                    for (int i = 0; i < addSongIds.size(); i++) {
                        addSongs[i] = Integer.parseInt(addSongIds.get(i));
                    }

                    Log.e("plau====",playlist+"");
                    scanUtil.scanPlaylistSongFromDB(playlist);
                    List<MusicInfo> songListForPlaylist = MusicList.list;

                    ArrayList<Integer> tempSong = new ArrayList<Integer>();

                    int[] addSongTemp = null;
                    if (songListForPlaylist.isEmpty()) {
                        addSongTemp = addSongs;
                    } else {
                        for (int i = 0; i < addSongs.length; i++) {
                            boolean playListContain = false;

                            for (MusicInfo tempMp3 : songListForPlaylist) {
                                long sqlId = tempMp3.getAllSongIndex();
                                Log.i("PLAYLIST", "CHECK: mp3: " + sqlId + " ADDID: " + addSongs[i]);
                                if (sqlId == addSongs[i]) {
                                    playListContain = true;
                                    break;
                                }
                            }

                            if (!playListContain) {
                                tempSong.add(new Integer(addSongs[i]));
                            }
                        }

                        addSongTemp = new int[tempSong.size()];
                        for (int i = 0; i < tempSong.size(); i++) {
                            addSongTemp[i] =tempSong.get(i);
                        }
                    }

                    for (int i =0;i<addSongTemp.length;i++){
                        Log.e("ADDDDDDDDD===",addSongTemp[i]+"=="+playlist);
                    }
                    scanUtil.addplaylist(playlist,addSongTemp);
                   // MusicUtils.addToPlaylist(AddSong2PlaylistActivity.this, addSongTemp, playlistId);
                }
                finish();
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

    public void setAdapter() {

        listItems = getListItems();
        if (null != listViewAdapter) {
            listViewAdapter.setListItems(listItems);
        } else {
            listViewAdapter = new ListViewAdapter(this, listItems, R.layout.pl_songs_add); // 创建适配器
        }
        listViewAdapter.setSongIds(songIds);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                if (isSelected(Integer.parseInt(songs.get(position).getId()))) {
                    addSongIds.remove(String.valueOf(songs.get(position).getId()));
                } else {
                    addSongIds.add(songs.get(position).getId() + "");
                }

                listViewAdapter.setAddSongIds(addSongIds);

                listViewAdapter.notifyDataSetChanged();
            }
        });

    }

    public boolean isSelected(int songID) {
        return addSongIds.contains(String.valueOf(songID));
    }

    /**
     * 返回适配器所要加载的数据集合
     */
    private List<Map<String, Object>> getListItems() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

        /**
         * 获取数据库中所有的歌曲
         */
        scanUtil.scanMusicFromDB();
        songs = MusicList.list;

        songIds = new ArrayList<String>();
        for (int i = 0; i < songs.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            songIds.add(songs.get(i).getId() + "");
            map.put("deleteIcon", R.drawable.checkbox_default);
            map.put("songName", songs.get(i).getName()); // 歌曲名

            if (songs.get(i).getName().equals("<unknown>")) {
                map.put("singerName", "----");
            } else {
                map.put("singerName", songs.get(i).getArtist()); // 歌手名
            }
            listItems.add(map);
        }

        return listItems;
    }

}

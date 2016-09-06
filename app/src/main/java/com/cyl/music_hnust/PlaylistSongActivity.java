package com.cyl.music_hnust;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.cyl.music_hnust.adapter.MusicRecyclerViewAdapter;
import com.cyl.music_hnust.db.DBDao;
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PlaylistSongActivity extends AppCompatActivity implements View.OnClickListener, MusicRecyclerViewAdapter.OnItemClickListener {
    private ImageButton back;
    private TextView playlist_title, playlist_edit;
    private Button edit_back, add_song;
    private static RecyclerView recyclerView;

    public static boolean idEdit = false;//判断是不是编辑模式，是的话显示删除图标
//    private long playlistId;//当前播放列表id
    private static List<MusicInfo> songs=new ArrayList<>();//储存当前播放列表所有歌曲
    private static MusicRecyclerViewAdapter adapter;//适配器
    private static String playlist;//歌单名

    private ArrayList<String> pl_songIds;// 列表歌曲的id集合

    private Timer timer;//定时器
    private TimerTask myTimerTask;//定时器任务
    private final static int SETADAPTER = 111;
    private MusicPlayService mService;
    MyHandler handler;

    private static RecyclerView.LayoutManager mLayoutManager;

    static class MyHandler extends Handler {
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
        setContentView(R.layout.local_music);

        mService =MyActivity.mService;
        idEdit = false;

        handler =new MyHandler(PlaylistSongActivity.this);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        Intent intent = getIntent();
        if (intent != null) {
            //判断是不是从添加列表界面跳过来的，是的话就点击一下添加歌曲按钮，跳到添加歌曲界面
            boolean addSong = intent.getBooleanExtra("autoAddSong", false);
            if (addSong) {
                playlist = intent.getStringExtra("playlist");
                Log.e("==========addSong",playlist);
            } else {
                /**
                 * 获取歌单名
                 */
                playlist = intent.getStringExtra("playlist");
                Log.e("==========get",playlist);
            }
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //以下是定时器0.1秒后再跳到handler加载适配器
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

    private void initView() {
        back = (ImageButton) findViewById(R.id.back);
        playlist_title = (TextView) findViewById(R.id.title);
        playlist_edit = (TextView) findViewById(R.id.playlist_edit);
        edit_back = (Button) findViewById(R.id.edit_back);
        add_song = (Button) findViewById(R.id.add_song);
        recyclerView = (RecyclerView) findViewById(R.id.music_list);
        //listView = (ListView) findViewById(R.id.listView);

        playlist_edit.setVisibility(View.VISIBLE);
        playlist_title.setText(playlist+"");

        playlist_edit.setOnClickListener(this);
        back.setOnClickListener(this);
        edit_back.setOnClickListener(this);
        add_song.setOnClickListener(this);

        adapter = new MusicRecyclerViewAdapter(this, songs);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.playlist_edit:
                edit_back.setVisibility(View.VISIBLE);
                add_song.setVisibility(View.VISIBLE);
                idEdit = true;
                setAdapter();
                break;
            case R.id.edit_back:
                edit_back.setVisibility(View.GONE);
                add_song.setVisibility(View.GONE);
                idEdit = false;
                setAdapter();
                break;
            case R.id.add_song:
                Intent intent = new Intent(PlaylistSongActivity.this, AddSong2PlaylistActivity.class);
                intent.putExtra("playlist", playlist);
                startActivity(intent);
                break;
        }

    }

    public static void setAdapter() {
        songs =getListItems();
        adapter.mDatas=songs;
        adapter.notifyDataSetChanged();


    }

    /**
     * 根据歌单名
     * 得到歌曲信息
     */
    private static List<MusicInfo> getListItems() {

        List<MusicInfo> list = MusicList.list;
        Log.e("result____list",list.size()+"");
        return list;
    }

    @Override
    public void onItemClick(View view, final int position) {

        final int positionInt = position;

        if (idEdit) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(PlaylistSongActivity.this);
            dialog.setTitle("移出歌曲!")
                    .setMessage("是否将歌曲从"+playlist+"歌单\n移除!")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    songs= getListItems();
                    adapter.mDatas = songs;
                    adapter.notifyDataSetChanged();
                }

            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();// 取消弹出
                }

            }).create().show();

        } else {
            switch (view.getId()) {
                case R.id.music_container:
                    mService.setCurrentListItme(position);
                    mService.setSongs(songs);
                    mService.playMusic(songs.get(position).getPath());
                    MyActivity.mService= mService;
                    break;
                case R.id.list_black_btn:
                    singleChoice(view, position);
                    break;
            }
        }
    }

    public void singleChoice(View source, final int position) {
        String[] item = getResources().getStringArray(R.array.song_list);
        ListAdapter items = new ArrayAdapter<String>(this,
                R.layout.item_songs, item);
//        int items;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("歌曲")
                .setAdapter(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            DBDao dbDao = new DBDao(getApplicationContext());

                            if (!songs.get(position).isFavorite()) {
                                ToastUtil.show(getApplicationContext(), "添加成功");
                            } else {
                                ToastUtil.show(getApplicationContext(), "已添加");
                            }

                        } else {
                            String msg = "歌曲名: "+songs.get(position).getName() + "\n" +
                                    "歌手名: "+songs.get(position).getArtist() + "\n"+
                                    "专辑名: "+ songs.get(position).getAlbum() + "\n"+
                                    "歌曲路径: "+songs.get(position).getPath() + "\n";
                            detailsshow(msg);
                        }

                    }
                });
//        builder.setPositiveButton();
        builder.create();
        builder.show();
    }

    public void detailsshow(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("歌曲信息")
                .setMessage(msg);
        setPositiveButton(builder)
                .create()
                .show();
    }

    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        return builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
}

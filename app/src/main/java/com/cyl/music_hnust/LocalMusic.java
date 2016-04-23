package com.cyl.music_hnust;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.cyl.music_hnust.adapter.MusicRecyclerViewAdapter;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.db.DBDao;
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.utils.Album;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.utils.ScanUtil;
import com.cyl.music_hnust.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 永龙 on 2016/3/12.
 */
public class LocalMusic extends AppCompatActivity implements View.OnClickListener, MusicRecyclerViewAdapter.OnItemClickListener {
    private ImageButton back;
    private RecyclerView music_list;

    //    private ListView listView;
    private TextView title;
    private TextView tv_no_songs;
    private int type = -1;
    private List<MusicInfo> songs;// 歌曲集合
    private MusicPlayService mService;
    public static final int LOCAL_LIST = 1;//适配器加载的数据是歌曲列表
    public static final int FAVOR_LIST = 2;//适配器加载的数据是歌曲列表


    private RecyclerView.LayoutManager mLayoutManager;

    List<MusicInfo> mDatas;
    Intent it;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_music);
        mService = MyActivity.mService;


        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        back = (ImageButton) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        tv_no_songs = (TextView) findViewById(R.id.tv_no_songs);
        music_list = (RecyclerView) findViewById(R.id.music_list);
        back.setOnClickListener(this);
        tv_no_songs.setOnClickListener(this);

        it = getIntent();
        if ("local".equals(it.getStringExtra("action"))) {
            type = LOCAL_LIST;
            title.setText("本地音乐");
            tv_no_songs.setText("播放列表为空\n请扫描歌曲");
        } else if ("favor".equals(it.getStringExtra("action"))) {
            type = FAVOR_LIST;
            title.setText("我的最爱");

            tv_no_songs.setText("播放列表为空\n请添加喜欢");
        }

        initData();
        if (mDatas.size() == 0) {
            music_list.setVisibility(View.GONE);
            tv_no_songs.setVisibility(View.VISIBLE);
        }
        MusicRecyclerViewAdapter adapter = new MusicRecyclerViewAdapter(getApplicationContext(), mDatas);
        adapter.setOnItemClickListener(this);
        music_list.setAdapter(adapter);


        music_list.setLayoutManager(mLayoutManager);


    }

    private void initData() {
        ScanUtil scanUtil = new ScanUtil(this);
        scanUtil.scanMusicFromDB();
        songs = MusicList.list;
        mDatas = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            if (type == FAVOR_LIST) {
                if (songs.get(i).isFavorite()) {
                    mDatas.add(songs.get(i));
                }
            } else {
                mDatas = songs;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_no_songs:
                if (type == LOCAL_LIST){
                    Intent intent =new Intent(this,ScanActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }


    @Override
    public void onItemClick(View view, int position) {
//
//        if (null == mService) {
//            mService = application.getmService();
//        }
        switch (view.getId()) {
            case R.id.music_container:
                mService.setCurrentListItme(position);
                mService.setSongs(mDatas);
                mService.playMusic(mDatas.get(position).getPath());
                MyActivity.mService =mService;
                break;
            case R.id.list_black_btn:
                singleChoice(view, position);

                break;
        }

    }

    public void singleChoice(View source, final int position) {
        String[] item = getResources().getStringArray(R.array.song_list);
        ListAdapter items = new ArrayAdapter<String>(this,
                R.layout.item_songs, item);
//        int items;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("歌曲")
                .setIcon(R.mipmap.ic_launcher)
                .setAdapter(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            DBDao dbDao = new DBDao(getApplicationContext());

                            if (!songs.get(position).isFavorite()) {
                                dbDao.update(songs.get(position).getName(), true);
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
                .setMessage(msg)
                .setIcon(R.mipmap.ic_launcher);
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

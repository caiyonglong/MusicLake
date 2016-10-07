package com.cyl.music_hnust;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyl.music_hnust.activity.EditActivity;
import com.cyl.music_hnust.adapter.MyStaggeredViewAdapter;
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.utils.MusicInfo;

import java.util.List;

/**
 * Created by 永龙 on 2016/3/13.
 */
public class PlaylistActivity extends AppCompatActivity implements View.OnClickListener, MyStaggeredViewAdapter.OnItemClickListener {

    private ImageButton back;
    private TextView tv_name;
    private TextView tv_finish;
    private EditText edt_playlist;
    private RecyclerView rv_playlist;

    private Intent it;
    private String ACTION_PLAYLIST_ADD = "add"; //增加歌单
    private String ACTION_PLAYLIST_MANAGE = "manage"; //歌单管理
    private int type = -1; // 1:增加歌单,2:歌单管理
    private List<String> al_playlist;// 播放列表集合


    private StaggeredGridLayoutManager mLayoutManager;

    private MyStaggeredViewAdapter playlistadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_manage);

        initView();
        it = getIntent();
        String info = it.getStringExtra("action");

       if(ACTION_PLAYLIST_MANAGE.equals(info)) {
            type = 2;
            tv_finish.setVisibility(View.GONE);

            al_playlist = MusicList.playlist;

            mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

            playlistadapter = new MyStaggeredViewAdapter(getApplicationContext(), al_playlist, type);
            playlistadapter.setOnItemClickListener(this);
            rv_playlist.setAdapter(playlistadapter);

            rv_playlist.setLayoutManager(mLayoutManager);
            //设置Item增加、移除动画
            rv_playlist.setItemAnimator(new DefaultItemAnimator());

        }

    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.backImageButton);
        tv_name = (TextView) findViewById(R.id.palylist_title);
        tv_finish = (TextView) findViewById(R.id.palylist_success);
        edt_playlist = (EditText) findViewById(R.id.playlist_name);
        rv_playlist = (RecyclerView) findViewById(R.id.rv_playlist);

        back.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backImageButton:
                finish();
                 break;
        }

    }


    String targetStr="";
    int pos = 0;
    @Override
    public void onItemClick(View view, final int position) {
        targetStr = al_playlist.get(position);
        pos= position;
        show(targetStr);


    }
    public void show(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("歌单管理")
                .setMessage(msg);
        setPositiveButton(builder);
        setNegativeButton(builder);
        setNeutralButton(builder)
                .create()
                .show();
    }

    private AlertDialog.Builder setNeutralButton(AlertDialog.Builder builder) {
     return builder.setNeutralButton("删除歌单", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
             showmsg(targetStr);
         }
     });
    }



    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        return builder.setPositiveButton("分享歌单", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareplaylist(targetStr);
            }
        });
    }

    private void shareplaylist(String targetStr) {

        List<MusicInfo> list = MusicList.list;
        String sharecontent = "我分享的歌单："+targetStr;
        if (list.size()>0){
            for (int i=0;i<list.size() ;i++){
                sharecontent+="\n"+list.get(i).getName()+"--"+list.get(i).getArtist();
            }
        }
        Log.e("sharecontent",sharecontent);
        Intent in = new Intent(this,EditActivity.class);
        in.putExtra("sharecontent",sharecontent);
        startActivity(in);

    }

    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {
        return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public void showmsg(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this)
                .setTitle("删除歌单")
                .setMessage(msg);
        setPositiveButton1(builder1)
                .create()
                .show();
    }

    private AlertDialog.Builder setPositiveButton1(AlertDialog.Builder builder) {
        return builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // reuse previous dialog instance
                Log.e("删除歌单","dddd===="+pos);
                playlistadapter.mDatas.remove(pos);
                playlistadapter.notifyDataSetChanged();
            }
        });
    }

}

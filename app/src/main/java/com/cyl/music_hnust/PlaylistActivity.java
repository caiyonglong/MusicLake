package com.cyl.music_hnust;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyl.music_hnust.adapter.MyStaggeredViewAdapter;
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.utils.ScanUtil;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
    private ScanUtil scanUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_manage);

        scanUtil =new ScanUtil(getApplicationContext());
        initView();
        it = getIntent();
        String info = it.getStringExtra("action");

       if(ACTION_PLAYLIST_MANAGE.equals(info)) {
            type = 2;
            tv_finish.setVisibility(View.GONE);

            scanUtil.scanPlaylistFromDB();
            al_playlist = MusicList.playlist;

            mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

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



    @Override
    public void onItemClick(View view, final int position) {
        final String targetStr = al_playlist.get(position);
      //  playlistId = MusicUtils.getPlayListId(this, targetStr);

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("确认删除歌单?")
                .setContentText("删除后不能恢复!")
                .setConfirmText("确定！")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance
                        scanUtil.deleteplaylist(targetStr,-1);
                        Log.e("删除歌单","dddd");

                        playlistadapter.mDatas.remove(position);

                        playlistadapter.notifyDataSetChanged();
                        sDialog.setTitleText("已删除!")
                                .setContentText("歌单已经删除!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .show();
    }

}

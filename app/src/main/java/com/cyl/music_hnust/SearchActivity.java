package com.cyl.music_hnust;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cyl.music_hnust.Json.JsonParsing;
import com.cyl.music_hnust.adapter.MusicRecyclerViewAdapter;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.download.NetworkUtil;
import com.cyl.music_hnust.http.HttpByGet;
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.service.DownloadService;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.utils.ToastUtil;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 永龙 on 2016/3/14.
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener, MusicRecyclerViewAdapter.OnItemClickListener {

    private ImageButton back;
    private EditText search_edit_info;
    private Button search_go_btn;
    private RecyclerView search_result;
    MusicRecyclerViewAdapter adapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private String search_info;
    public static List<MusicInfo> infos = new ArrayList<MusicInfo>();

    private MusicPlayService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mService = MyActivity.mService;

        initView();

    }


    private void initView() {
        back = (ImageButton) findViewById(R.id.backImageButton);
        search_edit_info = (EditText) findViewById(R.id.search_edit_info);
        search_go_btn = (Button) findViewById(R.id.search_go_btn);
        search_result = (RecyclerView) findViewById(R.id.search_result);

        back.setOnClickListener(this);
        search_go_btn.setOnClickListener(this);

        adapter = new MusicRecyclerViewAdapter(getApplicationContext(), infos);
        search_result.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        search_result.setLayoutManager(mLayoutManager);


    }

    private SweetAlertDialog pDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backImageButton:
                finish();
                break;
            case R.id.search_go_btn:
                NetworkUtil networkUtil = new NetworkUtil(this);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String permis = networkUtil.getconnectinfo();

                boolean isOnlyWifi= prefs.getBoolean("wifi_switch", true);

                search_info = search_edit_info.getText().toString().trim();
                pDialog = new SweetAlertDialog(SearchActivity.this, SweetAlertDialog.ERROR_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));

                if (!TextUtils.isEmpty(search_info)){
                    if (permis.equals("WiFi")) {
                        initSearch();
                    } else if (permis.equals("Normal")&&!isOnlyWifi) {
                        show("当前操作会消耗流量,是否继续!");
                    }  else if (permis.equals("Normal")&&isOnlyWifi) {
                        show("wifi未连接,请关闭 仅wifi联网!");
                    } else {
                        pDialog.setTitleText("请检查网络连接");
                        pDialog.setCancelable(true);
                        pDialog.show();
                    }
                }else {
                    pDialog.setTitleText("请输入搜索信息");
                    pDialog.setCancelable(true);
                    pDialog.show();
                }

                break;
        }
    }


    private void initSearch() {
        //AsyncTask<执行异步任务时所必须的参数（如果必须的话），异步任务执行的进度，异步任务执行完毕后的返回值（就是backgroud方法的返回值类型）>——————>这里这三个值只能写数据类型，不能写具体对象
        new AsyncTask<OutputStream, Integer, Boolean>() {
            //后台执行的任务（所需参数的类型，可变参数数组（内含所需参数的值））
            boolean flag = false;

            @Override
            protected Boolean doInBackground(OutputStream... params) {
                try {

                    String path = Constants.DEFAULT_MUSIC_LIST_URL + search_info;
                    Log.e("paht",path);
                    String json = HttpByGet.requestByHttpGet( Constants.DEFAULT_MUSIC_LIST_URL,search_info)
                            + "";
                    if ("ERROR".equals(json)) {
                        flag = false;
                    } else {
                        flag = true;

                        infos = JsonParsing.getmusicId(json);
                        Log.e("T", infos.get(0).getId() + ":" + infos.size());
                        for (int i = 0; i < infos.size(); i++) {
                            String json2 = HttpByGet.requestByHttpGet2(infos
                                    .get(i).getId());
                            Log.e("JsonParsing", "======" + json2);
                            JsonParsing.getMusicInfo(json2, infos.get(i));
                        }
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            //后台任务执行前的操作
            @Override
            protected void onPreExecute() {
                pDialog = new SweetAlertDialog(SearchActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("搜索中...");
                pDialog.setCancelable(true);
                pDialog.show();
                super.onPreExecute();
            }

            //后台任务执行后的操作
            @Override
            protected void onPostExecute(Boolean result) {
//                    pDialog.dismiss();
                if (result) {
                    if (flag) {
                        pDialog.dismiss();
//                        pDialog.setTitleText("搜索完成!")
//                                .setConfirmText("OK")
//                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }else {
                        pDialog.setTitleText("网络连接异常!")
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                } else {
                    pDialog.setTitleText("网络连接异常!")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                }
                adapter.mDatas = infos;
                adapter.notifyDataSetChanged();
                super.onPostExecute(result);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                // TODO Auto-generated method stub
                super.onProgressUpdate(values);
            }
        }.execute();


    }

    @Override
    public void onItemClick(View view, final int position) {
        switch (view.getId()) {
            case R.id.music_container:
                MusicInfo musicInfo = infos.get(position);
                Log.e("TEST", position + "-----" + musicInfo.getAlbum());
                Log.e("TEST", position + "-----" + musicInfo.getArtist());
                Log.e("TEST", position + "-----" + musicInfo.getName());
                Log.e("TEST", position + "-----" + musicInfo.getLrcPath());
                Log.e("TEST", position + "-----" + musicInfo.getPath());
                Log.e("TEST", position + "-----" + musicInfo.getSize());
                Log.e("TEST", position + "-----" + musicInfo.getTime());
                Log.e("TEST", position + "-----" + musicInfo.getSize());
                Log.e("TEST", position + "-----" + musicInfo.getAlbumPic());

                List<MusicInfo> list = mService.getSongs();
                if (list == null) {
                    list = new ArrayList<>();
                    list.add(musicInfo);
                    mService.setCurrentListItme(0);
                    mService.setSongs(list);
                    mService.playMusic(infos.get(position).getPath());
                    MyActivity.mService = mService;
                } else {
                    int listitem = mService.getCurrentListItme() + 1;
                    list.add(listitem, musicInfo);
                    mService.setSongs(list);
                    MyActivity.mService = mService;
                    ToastUtil.show(getApplicationContext(), "已添加到播放队列");
                }

                break;
            case R.id.list_black_btn:
                singleChoice(view, position);
                Log.e("TEST", position + "-----" + infos.get(position).getPath());
                break;
        }

    }
    public void show(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(msg)
                .setIcon(R.mipmap.ic_launcher);
                setPositiveButton(builder);
                setNegativeButton(builder)
                .create()
                .show();
    }

    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        return builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initSearch();
            }
        });
    }
    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {
        return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public void singleChoice(View source, final int pos) {
        String[] item = getResources().getStringArray(R.array.song_download);
        ListAdapter items = new ArrayAdapter<String>(this,
                R.layout.item_songs, item);
//        int items;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("歌曲下载")
                .setIcon(R.mipmap.ic_launcher)
                .setAdapter(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            ToastUtil.show(getApplicationContext(), "已添加到下载队列");
                            Intent intent = new Intent();
                            intent.setClass(getApplicationContext(), DownloadService.class);
                            intent.putExtra("downloadUrl", infos.get(pos).getPath());
                            intent.putExtra("name", infos.get(pos).getName() + ".mp3");
                            intent.putExtra("flag", "startDownload");
                            Log.e("go", infos.get(pos).getName() + ">>>>>" + infos.get(pos).getPath());
                            startService(intent);
                        } else {
                            String msg = "歌曲名: "+infos.get(pos).getName() + "\n" +
                                    "歌手名: "+infos.get(pos).getArtist() + "\n"+
                                    "专辑名: "+ infos.get(pos).getAlbum() + "\n"+
                                    "歌曲路径: "+infos.get(pos).getPath() + "\n";
                            show(msg);
                        }
                    }
                });
        builder.create();
        builder.show();


    }



}

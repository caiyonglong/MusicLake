package com.cyl.music_hnust;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

    List<Map<String, Object>> mDatas;

    private MusicPlayService mService;
    PopupWindow popuWindow1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        MyApplication application = (MyApplication) getApplication();
        mService = application.getmService();

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
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                boolean permis = prefs.getBoolean("wifi_switch", true);

                search_info = search_edit_info.getText().toString().trim();
                pDialog = new SweetAlertDialog(SearchActivity.this, SweetAlertDialog.ERROR_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                if (permis) {
                    pDialog.setTitleText("仅Wifi联网");
                    pDialog.setCancelable(true);
                    pDialog.show();
                } else if (TextUtils.isEmpty(search_info)) {
                    pDialog.setTitleText("仅Wifi联网");
                    pDialog.setCancelable(true);
                    pDialog.show();
                } else {
                    initSearch();
                }
                break;
        }
    }


    private void initSearch() {
        //AsyncTask<执行异步任务时所必须的参数（如果必须的话），异步任务执行的进度，异步任务执行完毕后的返回值（就是backgroud方法的返回值类型）>——————>这里这三个值只能写数据类型，不能写具体对象
        new AsyncTask<OutputStream, Integer, Boolean>() {
            //后台执行的任务（所需参数的类型，可变参数数组（内含所需参数的值））
            // String search_info;

            @Override
            protected Boolean doInBackground(OutputStream... params) {
                try {
                    //params[0]=fos=execute所接受的参数

                    //在后台任务中调用自定义的工具方法，该工具方法需要接受一个抽象类的对象（自己设计的抽象类）
                    /**
                     * 这里的第二个参数必须传一个自定义接口的对象，所以必须实现接口中的抽象方法才能创建出对象，相当于这里的这个对象是出生就
                     * 带着两个抽象方法的，把这个带着抽象方法的接口对象传给工具方法后，工具方法拿着传过来的这个接口对象想调用抽象方法，
                     * 就只能用我们创建接口对象时已经实现好的方法，也就是说，它是借用的我们的抽象方法去办事，它自己没有，因为它自己没实现
                     * 这个借用我们的抽象方法的过程就叫做：回调，这种抽象方法就叫做回调函数。
                     * 比如：listener.beforeBackup(cursor.getCount());这句话就是用了我们实现好的方法，而且给我们的方法传进去了一个参数
                     * 它回调时传的参数，我们这边可以立马捕获，因为它的对象本来就是我们的对象的引用
                     * 要始终记得：我们把自定义接口类型的对象（带着两个方法）传递给工具类的方法后，工具方法拿着这个自定义接口类型对象的引用调用抽象方法时，调用的不是工具类
                     * 中的抽象方法体，而是借用的我们已经实现了的抽象方法去做事，它用我们的方法办事，我们这边当然都可以拿到
                     */

                    try {
                        Log.e("info", search_info + "");
                        String json = HttpByGet.requestByHttpGet(search_info)
                                + "";

                        infos = JsonParsing.getmusicId(json);
                        Log.e("T", infos.get(0).getId() + ":" + infos.size());
                        for (int i = 0; i < infos.size(); i++) {
                            String json2 = HttpByGet.requestByHttpGet2(infos
                                    .get(i).getId());
                            Log.e("JsonParsing", "======" + json2);
                            JsonParsing.getMusicInfo(json2, infos.get(i));
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
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
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(true);
                pDialog.show();
                super.onPreExecute();
            }

            //后台任务执行后的操作
            @Override
            protected void onPostExecute(Boolean result) {
//                    pDialog.dismiss();
                if (result) {
                    pDialog.setTitleText("搜索完成!")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                } else {
                    Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_SHORT).show();
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
//

                List<MusicInfo> list = mService.getSongs();
                if (list == null) {
                    list = new ArrayList<>();
                    list.add(musicInfo);
                    mService.setCurrentListItme(0);
                    mService.setSongs(list);
                    mService.playMusic(infos.get(position).getPath());
                }else {
                    int listitem = mService.getCurrentListItme() + 1;
                    list.add(listitem, musicInfo);
                    mService.setSongs(list);
                    ToastUtil.show(getApplicationContext(), "已添加到播放队列");
                }

                break;
            case R.id.list_black_btn:
                initPopuWindow1(view, position);
                Log.e("TEST", position + "-----" + infos.get(position).getPath());
                break;
        }

    }

    View contentView1;

    private void initPopuWindow1(View parent, final int position) {
        if (popuWindow1 == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            contentView1 = mLayoutInflater.inflate(R.layout.popuwindow, null);
            popuWindow1 = new PopupWindow(contentView1, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        TextView download = (TextView) contentView1.findViewById(R.id.group_menu1_download);
        TextView details = (TextView) contentView1.findViewById(R.id.group_menu1_details);
        TextView delete = (TextView) contentView1.findViewById(R.id.group_menu1_delete);
        //popupWindow 点击事件
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUtil networkUtil = new NetworkUtil(getApplicationContext());
                if (networkUtil.isNetworkConnected()) {
                    if (networkUtil.getNetworkType() == 1) {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), DownloadService.class);
                        intent.putExtra("downloadUrl", infos.get(position).getPath());
                        intent.putExtra("name", infos.get(position).getName() + ".mp3");
                        intent.putExtra("flag", "startDownload");
                        Log.e("go", infos.get(position).getName() + ">>>>>" + infos.get(position).getPath());
                        startService(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "当前的网络状态不是wifi，请先设置",
                                Toast.LENGTH_LONG).show();
                    }

                }
                popuWindow1.dismiss();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                Toast.makeText(getApplicationContext(), "已加入下载队列",
                        Toast.LENGTH_LONG).show();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "group_menu1_details",
                        Toast.LENGTH_LONG).show();
            }
        });
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "group_menu1_delete",
                        Toast.LENGTH_LONG).show();
            }
        });
        ColorDrawable cd = new ColorDrawable(0x000000);
        popuWindow1.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        popuWindow1.setOutsideTouchable(true);
        popuWindow1.setFocusable(true);
        popuWindow1.showAtLocation((View) parent.getParent(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);


        popuWindow1.update();
        popuWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }
}

package com.cyl.music_hnust;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cyl.music_hnust.adapter.DownloadManageAdapter;
import com.cyl.music_hnust.adapter.ListAdapter;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.db.DBDao;
import com.cyl.music_hnust.download.Constant;
import com.cyl.music_hnust.download.FileState;
import com.cyl.music_hnust.download.SqliteDao;
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.utils.ScanInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 永龙 on 2016/3/12.
 */
public class DownloadActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager vPager;
    private RadioGroup group;
    private RadioButton btnQu, btnGui;
    private ArrayList<Fragment> fragmentList;
    private ImageButton back;
    public MusicInfo songinfo;

    private UpdateReceiver receiver;

//    public List<Downloadinfo> downloadinfos = new ArrayList<>();

    public static MusicPlayService mService;
    public int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        MyApplication application = (MyApplication) getApplication();
        mService = application.getmService();
        initView();
        initViewPager();


        receiver = new UpdateReceiver();
        receiver.registerAction(Constant.DOWNLOADMANAGEACTION);
    }

    private void initView() {
        // TODO Auto-generated method stub
        vPager = (ViewPager) findViewById(R.id.viewPager);
        btnQu = (RadioButton) findViewById(R.id.btnQ);
        group = (RadioGroup) findViewById(R.id.group);
        btnGui = (RadioButton) findViewById(R.id.btnG);
        back = (ImageButton) findViewById(R.id.backImageButton);
        back.setOnClickListener(this);
        group.setOnCheckedChangeListener(new myCheckChangeListener());
    }

    private void initViewPager() {
        // TODO Auto-generated method stub
        DownloadedFragment quFragment = new DownloadedFragment();
        DownloadingFragment guiFragment = new DownloadingFragment();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(quFragment);
        fragmentList.add(guiFragment);
        vPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList));
        vPager.setCurrentItem(0);

        vPager.setOnPageChangeListener(new myOnPageChangeListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backImageButton:
                finish();
                break;
        }
    }


    private class myCheckChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            switch (checkedId) {
                case R.id.btnQ:
                    vPager.setCurrentItem(0, false);
                    break;
                case R.id.btnG:
                    vPager.setCurrentItem(1, false);
                    break;
            }
        }
    }

    private class myOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 0:

                    btnQu.setBackgroundResource(R.drawable.btn_shape_left);
                    btnGui.setBackgroundResource(R.drawable.btn_shape);
                    btnQu.setTextColor(Color.parseColor("#000000"));
                    btnGui.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case 1:

                    btnQu.setBackgroundResource(R.drawable.btn_shape_left1);
                    btnGui.setBackgroundResource(R.drawable.btn_shape1);
                    btnQu.setTextColor(Color.parseColor("#ffffff"));
                    btnGui.setTextColor(Color.parseColor("#000000"));
                    break;
            }
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }
    }

    public static class DownloadedFragment extends Fragment {
        private static final String FILE_NAME = "/hnustmusic";
        View mView;
        private ListView list_download;

        private DBDao dao;
        private ListAdapter adapter;

        private List<ScanInfo> data;
        private List<MusicInfo> listinfo;


        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mView = inflater.inflate(R.layout.frag_download, container, false);
            return mView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            dao = new DBDao(getActivity());
            List<ScanInfo> list = new ArrayList<ScanInfo>();
            String path= "/storage/emulated/0/hkmusic/";
            list.add(new ScanInfo(path, true));
            dao.queryAll(list);
            listinfo  = MusicList.list;
            initView();
        }
        /**
         * 初始化控件
         */
        private void initView() {
            list_download = (ListView) mView.findViewById(R.id.list_download);
            adapter = new ListAdapter(getActivity(), listinfo, 0);
            list_download.setAdapter(adapter);
            list_download.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   // listinfo.get(position).
                   // mService.playMusic();
                    mService.setSongs(listinfo);
                    mService.setCurrentListItme(position);
                    mService.playMusic(listinfo.get(position).getPath());
                }
            });

        }


    }

    public static class DownloadingFragment extends Fragment {
        private ListView list_download;
        /**
         * 存放要显示列表的数据
         */
        public static List<FileState> data;
        public static DownloadManageAdapter adapter;

        private SqliteDao dao;

        View mView;
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mView = inflater.inflate(R.layout.frag_download, container, false);
            return mView;
        }


        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            dao = new SqliteDao(getActivity());

            data = dao.getFileStates();

        }

        @Override
        public void onResume() {
            super.onResume();
            data = dao.getFileStates();
            initView();
        }


        /**
         * 初始化控件
         */
        private void initView() {
            list_download = (ListView) mView.findViewById(R.id.list_download);
            adapter = new DownloadManageAdapter(getActivity(), data, dao);
            list_download.setAdapter(adapter);
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            dao.updateFileState(data);
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    /**
     *
     * 项目名称：MultithreadedDownload 类名称：UpdateReceiver 类描述：
     * 接收器类，用来接收后台service发送过来的下载进度 创建人：wpy 创建时间：2014-10-13 上午10:11:20
     *
     */
    private class UpdateReceiver extends BroadcastReceiver {
        /**
         * 注册广播接收器
         *
         * @param action
         */
        public void registerAction(String action) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(action);
            registerReceiver(this, intentFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.DOWNLOADMANAGEACTION)) {
                String url = intent.getStringExtra("url");
                int completeSize = intent.getIntExtra("completeSize", 0);
                for (int i = 0; i < DownloadingFragment.data.size(); i++) {
                    FileState fileState = DownloadingFragment.data.get(i);
                    if (fileState.getUrl().equals(url)) {
                        fileState.setCompleteSize(completeSize);
                        DownloadingFragment.data.set(i, fileState);
                    }
                }
                DownloadingFragment.adapter.setList(DownloadingFragment.data);
                DownloadingFragment.adapter.notifyDataSetChanged();
            }
        }

    }
}

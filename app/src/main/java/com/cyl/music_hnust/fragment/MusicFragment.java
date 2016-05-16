package com.cyl.music_hnust.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cyl.music_hnust.DownloadActivity;
import com.cyl.music_hnust.LocalMusic;
import com.cyl.music_hnust.MyActivity;
import com.cyl.music_hnust.PlayerActivity;
import com.cyl.music_hnust.PlaylistActivity;
import com.cyl.music_hnust.PlaylistSongActivity;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.DividerItemDecoration;
import com.cyl.music_hnust.adapter.MyStaggeredViewAdapter;
import com.cyl.music_hnust.adapter.PopupPlayListAdapter;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.db.DBDao;
import com.cyl.music_hnust.list.MusicList;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.utils.CommonUtils;
import com.cyl.music_hnust.utils.MusicInfo;
import com.cyl.music_hnust.utils.ScanUtil;
import com.cyl.music_hnust.utils.ToastUtil;
import com.cyl.music_hnust.view.ExStaggeredGridLayoutManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Monkey on 2015/6/29.
 */
public class MusicFragment extends Fragment implements View.OnClickListener, MyStaggeredViewAdapter.OnItemClickListener, PopupPlayListAdapter.OnItemClickListener {

    private Callbacks mCallbacks;

    public interface Callbacks {
        public void OnFragmentClick(View v);
    }

    private View mView;
    private CardView localmusic; //本地音乐
    private CardView downloaded; //下载歌曲
    private CardView love; //最喜欢
    private ImageView playlist_add; //增加歌单
    private TextView playlist_manage; //歌单管理
    private TextView localmusic_num; //本地歌曲数量
    private static RecyclerView mRecyclerView; //歌单列表

    public static TextView singer_name; //播放控制栏
    public static TextView song_name;
    public static ImageButton next_buttom, play_buttom, list_buttom;
    private static ImageView singer_pic;
    private LinearLayout cent_menu;

    /**
     * 播放列表弹出窗口
     */
    private PopupWindow mPopupWindow;
    /**
     * 弹出窗口播放列表
     */
    private RecyclerView popPlayListView;

    private TextView popPlaysumTextTextView;
    private TextView tv_no;

    private ExStaggeredGridLayoutManager mLayoutManager;

    private RecyclerView.LayoutManager mpLayoutManager;
    private static MyStaggeredViewAdapter playlistadapter;

    private static int type = 1;
    private List<String> al_playlist;// 播放列表集合


    private String TAG = "My_Fragment_Music";


    public static MusicPlayService mService;
    private DBDao dbDao;
    private MyHandler handler;

    private RequestQueue mRequestQueue;
    private MyApplication myApplication;
    private static ImageLoader imageLoader;


    private static class MyHandler extends Handler {
        private final WeakReference<Fragment> myfragment;

        private MyHandler(Fragment fragment) {
            myfragment = new WeakReference<Fragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            Fragment musicFragment = myfragment.get();
            if (musicFragment != null) {
                switch (msg.what) {
                    case 0:
                        playlistadapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callbacks) {
            mCallbacks = (Callbacks) context;
        } else {
            throw new ClassCastException(context.toString() + "must implement mCallbacks!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_music, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myApplication = new MyApplication();
        mRequestQueue = myApplication.getHttpQueues();
        imageLoader = myApplication.getImageLoader();

        dbDao = new DBDao(getContext());
        handler = new MyHandler(MusicFragment.this);

        initView();

        //获取所有歌单列表
        ScanUtil scanUtil = new ScanUtil(getContext());
        scanUtil.scanPlaylistFromDB();
        al_playlist = MusicList.playlist;

        mLayoutManager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        playlistadapter = new MyStaggeredViewAdapter(getContext(), al_playlist, type);
        playlistadapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(playlistadapter);

        mRecyclerView.setLayoutManager(mLayoutManager);


    }


    private void initView() {
        localmusic = (CardView) mView.findViewById(R.id.localmusic); //本地音乐
        localmusic_num = (TextView) mView.findViewById(R.id.localmusic_num); //本地音乐
        downloaded = (CardView) mView.findViewById(R.id.downloaded); //下载歌曲
        love = (CardView) mView.findViewById(R.id.love); //最喜欢
        playlist_add = (ImageView) mView.findViewById(R.id.playlist_add); //增加歌单
        playlist_manage = (TextView) mView.findViewById(R.id.playlist_manage); //歌单管理
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.id_recyclerview); //歌单列表

        //  singer_pic.setDefaultImageResId(R.drawable.playing_bar_default_avatar);

        localmusic.setOnClickListener(this);
        downloaded.setOnClickListener(this);
        love.setOnClickListener(this);
        playlist_add.setOnClickListener(this);
        playlist_manage.setOnClickListener(this);

        /**
         * 底部音乐播放控制功能控件
         */
        cent_menu = (LinearLayout) mView.findViewById(R.id.cent_menu);
        song_name = (TextView) mView.findViewById(R.id.song_name);
        singer_name = (TextView) mView.findViewById(R.id.singer_name);
        play_buttom = (ImageButton) mView.findViewById(R.id.play_buttom);
        next_buttom = (ImageButton) mView.findViewById(R.id.next_buttom);
        singer_pic = (ImageView) mView.findViewById(R.id.singer_pic);
        list_buttom = (ImageButton) mView.findViewById(R.id.list_buttom);

        play_buttom.setOnClickListener(this);
        next_buttom.setOnClickListener(this);
        singer_pic.setOnClickListener(this);
        song_name.setOnClickListener(this);
        singer_name.setOnClickListener(this);

        list_buttom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.localmusic:
                Intent intent = new Intent(getContext(), LocalMusic.class);
                intent.putExtra("action", "local");
                startActivity(intent);
                Log.e(TAG, "localmusic");
                break;
            case R.id.downloaded:
                Intent intent2 = new Intent(getActivity(), DownloadActivity.class);
                startActivity(intent2);
                Log.e(TAG, "downloaded");
                break;
            case R.id.love:
                Intent intent3 = new Intent(getContext(), LocalMusic.class);
                intent3.putExtra("action", "favor");
                startActivity(intent3);
                Log.e(TAG, "love");
                break;
            case R.id.playlist_add:
                final LinearLayout playlistForm = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.playlist_new, null);
                new AlertDialog.Builder(getContext())
                        .setView(playlistForm)
                        .setTitle("新建歌单")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText edt_playlist = (EditText) playlistForm.findViewById(R.id.edt_playlist);
                                String name = edt_playlist.getText().toString().trim();
                                if (!TextUtils.isEmpty(name)) {
                                    int[] musicid = {-1};
                                    dbDao.addPlaylist(name, musicid);
                                    al_playlist.clear();
                                    ScanUtil scanUtil = new ScanUtil(getContext());
                                    scanUtil.scanPlaylistFromDB();
                                    al_playlist = MusicList.playlist;
                                    playlistadapter.mDatas = al_playlist;
                                    playlistadapter.setmHeights(al_playlist);
                                    handler.sendEmptyMessage(0);
                                } else {
                                    ToastUtil.show(getContext(), "歌单名不能为空！");
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.playlist_manage:
                Intent it1 = new Intent(getActivity(), PlaylistActivity.class);
                it1.putExtra("action", "manage");
                startActivityForResult(it1, 2);
                Log.e(TAG, "playlist_manage");
                break;

            // FloatingActionButton的点击事件
            case R.id.next_buttom:
                mService = MyActivity.mService;
                if (mService.getSongs() != null) {
                    mCallbacks.OnFragmentClick(v);
                } else {
                    Toast.makeText(getContext(), "播放列表为空", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.play_buttom:
                mService = MyActivity.mService;

                if (mService.getSongs() != null) {
                    mCallbacks.OnFragmentClick(v);
                } else {
                    Toast.makeText(getContext(), "播放列表为空", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.singer_pic:
            case R.id.singer_name:
            case R.id.song_name:
                mService = MyActivity.mService;

                // if (mService.getSongs() != null) {
                Intent it5 = new Intent(getActivity(), PlayerActivity.class);
                startActivity(it5);
//                } else {
//                    Toast.makeText(getContext(), "播放列表为空", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.list_buttom:
                getPopupWindowInstance(v);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            //获取所有歌单列表
            al_playlist.clear();
            ScanUtil scanUtil = new ScanUtil(getContext());
            scanUtil.scanPlaylistFromDB();
            al_playlist = MusicList.playlist;
            playlistadapter.mDatas = al_playlist;

            playlistadapter.setmHeights(al_playlist);
            handler.sendEmptyMessage(0);
        }
        Log.e(TAG, "resultCode:" + resultCode + "__" + requestCode);
    }


    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.playlist_album:
                Intent it = new Intent(getActivity(), PlaylistSongActivity.class);
                it.putExtra("playlist", al_playlist.get(position));
                Log.e(TAG, "playlist:" + al_playlist.get(position));
                startActivity(it);
                break;
            case R.id.listitemBG:
                MyActivity.mService.setCurrentListItme(position);
                MyActivity.mService.playMusic(adapter.playlist.get(position).getPath());
                adapter.playIndexPosition = position;
                adapter.notifyDataSetChanged();
                break;
        }

    }

    PopupPlayListAdapter adapter;

    /**
     * 获取PopupWindow实例
     */
    private void getPopupWindowInstance(View v) {
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
            return;
        } else {

            mService = MyActivity.mService;
            List<MusicInfo> playlist = mService.getSongs();

            initPopuptWindow(v);
            if (playlist != null) {

                popPlaysumTextTextView.setText("播放列表(" + playlist.size() + ")");

                int playIndex = mService.getCurrentListItme();
                adapter = new PopupPlayListAdapter(getContext(), playlist, playIndex);
                mpLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                popPlayListView.setAdapter(adapter);

                popPlayListView.setLayoutManager(mpLayoutManager);
                //添加分割线
                popPlayListView.addItemDecoration(new DividerItemDecoration(
                        getActivity(), DividerItemDecoration.VERTICAL_LIST));
                adapter.setOnItemClickListener(this);

            } else {
                popPlaysumTextTextView.setText("播放列表(0)");
                tv_no.setVisibility(View.VISIBLE);
                popPlayListView.setVisibility(View.GONE);

            }


        }
    }

    /**
     * 创建PopupWindow
     */
    private void initPopuptWindow(View parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View popupWindow = layoutInflater.inflate(
                R.layout.popup_main_playlist, null);

        mPopupWindow = new PopupWindow(popupWindow, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        mPopupWindow = new PopupWindow(popupWindow, ViewGroup.LayoutParams.MATCH_PARENT,
//                getActivity().getWindowManager().getDefaultDisplay().getHeight()
//                        - cent_menu.getHeight()-70, true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x99ffffff);
        mPopupWindow.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        // mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        mPopupWindow.setFocusable(true);
        // mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        popupWindow.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // int bottomHeight = mMenu.getTop();
                int topHeight = popupWindow.findViewById(R.id.pop_layout)
                        .getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // y > bottomHeight ||
                    if (topHeight > y) {
                        mPopupWindow.dismiss();
                    }
                }
                return true;
            }
        });

        // popWindow消失监听方法
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                mPopupWindow = null;
            }
        });

        popPlayListView = (RecyclerView) popupWindow
                .findViewById(R.id.playlistView);

        popPlaysumTextTextView = (TextView) popupWindow
                .findViewById(R.id.playsumText);
        tv_no = (TextView) popupWindow
                .findViewById(R.id.tv_no);

        Button pop_Close = (Button) popupWindow.findViewById(R.id.btn_close_pop);
        pop_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }


    public static void initBackGround(Context context, String albumpic) {
        if (albumpic != null) {
            if (albumpic.startsWith("http://")) {
                Log.e("网络图片", ">>>>>>>>>>>>>>>>>>" + albumpic);
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(singer_pic,
                        R.drawable.playing_bar_default_avatar, R.drawable.playing_bar_default_avatar);
                imageLoader.get(albumpic, listener);


        } else {
                Log.e("本地图片", ">>>>>>>>>>>>>>>>>>" + albumpic);
                Bitmap bitmap = CommonUtils.scaleBitmap(context, albumpic);
                singer_pic.setImageBitmap(bitmap);
            }
        }else {
            singer_pic.setImageResource(R.drawable.playing_bar_default_avatar);
        }

    }


}

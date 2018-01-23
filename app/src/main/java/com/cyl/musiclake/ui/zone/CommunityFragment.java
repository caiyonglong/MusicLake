//package com.cyl.musiclake.ui.zone;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//
//import com.cyl.musiclake.R;
//import com.cyl.musiclake.callback.SecretCallback;
//import com.cyl.musiclake.ui.base.BaseFragment;
//import com.cyl.musiclake.ui.login.user.UserStatus;
//import com.cyl.musiclake.ui.common.Constants;
//import com.cyl.musiclake.ui.common.Extras;
//import com.cyl.musiclake.utils.ToastUtils;
//import com.zhy.http.okhttp.OkHttpUtils;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.Call;
//
//
///**
// * Created by Monkey on 2015/6/29.
// */
//public class CommunityFragment extends BaseFragment {
//
//
//    RecyclerView mRecyclerView;
//    SwipeRefreshLayout mSwipeRefreshLayout;
//
//
//    FloatingActionButton fab;
//    TextView tv_empty;
//
//    CommunityAdapter MyAdapter;
//    private int pagenum = 1;
//    private int flag = 1;
//    private String user_id = "";
//
//    /**
//     * 当前的动态
//     */
//    private static List<Secret> mdatas = new ArrayList<>();
//    /**
//     * 加载更多的动态
//     */
//    private static List<Secret> newdatas = new ArrayList<>();
//
//    public static CommunityFragment newInstance(int flag) {
//
//        Bundle args = new Bundle();
//        args.putInt(Extras.COMMUNITY_FLAG, flag);
//        CommunityFragment fragment = new CommunityFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    protected void listener() {
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setFab();
//            }
//        });
//    }
//
//    private boolean loading = false;
//    private int VISIBLE_THRESHOLD = 2;
//
//
//    @Override
//    protected void initDatas() {
//
//        MyAdapter = new CommunityAdapter(getContext(), mdatas);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setAdapter(MyAdapter);
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                boolean is = Math.abs(dy) > 4;
//
//                if (is) {
//                    if (dy > 0) {
//                        fab.hide();
//                    } else {
//                        fab.show();
//                    }
//                }
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//                int totalItemCount = layoutManager.getItemCount();
//
//                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//
//                Log.e("-------",totalItemCount+":"+lastVisibleItem);
//
//                if (!loading && totalItemCount < (lastVisibleItem + VISIBLE_THRESHOLD)) {
//                    new SecretTask(getContext()).execute(pagenum);
//                    loading = true;
//                }
//            }
//        });
//
//        if (UserStatus.getstatus(getContext())) {
//            user_id = UserStatus.getUserInfo(getContext()).getUser_id();
//        } else {
//            user_id = "";
//        }
//        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(true);
//                mdatas.clear();
//                pagenum = 1;
//                new MoreSecretTask().execute(pagenum);
//            }
//        });
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mSwipeRefreshLayout.setRefreshing(true);
//                mdatas.clear();
//                pagenum=1;
//                new MoreSecretTask().execute(pagenum);
//            }
//        });
//
//
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.acitvity_community;
//    }
//
//    @Override
//    public void initViews() {
//
//        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefreshlayout);
//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.community_RecyclerView);
//        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
//        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
//        flag = (int) getArguments().get(Extras.COMMUNITY_FLAG);
//        if (flag != 1) {
//            fab.setVisibility(View.GONE);
//        }
//        pagenum = 1;
//    }
//
//    public void setFab() {
//        Intent intent = new Intent(getActivity(), EditActivity.class);
//        startActivity(intent);
//    }
//
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    // Integer 是输入参数
//    class MoreSecretTask extends AsyncTask<Integer, Void, List<Secret>> {
//        @Override
//        protected List<Secret> doInBackground(Integer... params) {
//
//            Map<String, String> param = new HashMap<>();
//            param.put(Constants.FUNC, Constants.GET_SECRET_LIST);
//            param.put(Constants.PAGENUM, String.valueOf(params[0]));
//            param.put(Constants.USER_ID, user_id);
//
//            OkHttpUtils.post().url(Constants.DEFAULT_URL)
//                    .params(param)
//                    .build()
//                    .execute(new SecretCallback() {
//                        @Override
//                        public void onError(Call call, Exception e) {
//                            if (mSwipeRefreshLayout != null) {
//                                mSwipeRefreshLayout.setRefreshing(false);
//                            }
//                            tv_empty.setText(R.string.error_connection);
//                            tv_empty.setVisibility(View.VISIBLE);
//                            ToastUtils.show(getActivity(), R.string.error_connection);
//                        }
//
//                        @Override
//                        public void onResponse(SecretInfo response) {
//
//                            if (mSwipeRefreshLayout != null) {
//                                mSwipeRefreshLayout.setRefreshing(false);
//                            }
//                            if (response.getStatus() == 1) {
//
//
//                                newdatas =response.getData();
//                                //更新视图
//                                if (mSwipeRefreshLayout != null) {
//                                    mSwipeRefreshLayout.setRefreshing(false);
//                                }
//                                //没有新的数据，提示消息
//                                if (newdatas == null || newdatas.size() == 0) {
//                                    Snackbar.make(getView(),"暂无数据",Snackbar.LENGTH_SHORT).show();
//                                } else {
//                                    pagenum=pagenum+1;
//                                    mdatas.addAll(newdatas);
//                                    MyAdapter.notifyDataSetChanged();
//                                }
//
//                            }
//                        }
//                    });
//            return newdatas;
//        }
//
//        @Override
//        protected void onPostExecute(List<Secret> simpleArticleItems) {
//            super.onPostExecute(simpleArticleItems);
//
//        }
//
//    }
//
//
//    class SecretTask extends AsyncTask<Integer, Void, List<Secret>> {
//
//        private Context mContext;
//
//        public SecretTask(Context context) {
//            mContext = context;
//        }
//
//        /**
//         * Runs on the UI thread before {@link #doInBackground}.
//         */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            if (mdatas != null && mdatas.size() > 0) {
//                mdatas.add(null);
//                // notifyItemInserted(int position)，这个方法是在第position位置
//                // 被插入了一条数据的时候可以使用这个方法刷新，
//                // 注意这个方法调用后会有插入的动画，这个动画可以使用默认的，也可以自己定义。
//                MyAdapter.notifyItemInserted(mdatas.size() - 1);
//            }
//        }
//
//        /**
//         * @param params 偏移量 aid
//         * @return
//         */
//        @Override
//        protected List<Secret> doInBackground(Integer... params) {
//
//            final Map<String, String> param = new HashMap<>();
//            param.put(Constants.FUNC, Constants.GET_SECRET_LIST);
//            param.put(Constants.PAGENUM, String.valueOf(params[0]));
//            param.put(Constants.USER_ID, user_id);
//
//            OkHttpUtils.post().url(Constants.DEFAULT_URL)
//                    .params(param)
//                    .build()
//                    .execute(new SecretCallback() {
//                        @Override
//                        public void onError(Call call, Exception e) {
//                            if (mSwipeRefreshLayout != null) {
//                                mSwipeRefreshLayout.setRefreshing(false);
//                            }
//                            tv_empty.setText(R.string.error_connection);
//                            tv_empty.setVisibility(View.VISIBLE);
//                            ToastUtils.show(getActivity(), R.string.error_connection);
//                        }
//
//                        @Override
//                        public void onResponse(SecretInfo response) {
//                            if (mSwipeRefreshLayout != null) {
//                                mSwipeRefreshLayout.setRefreshing(false);
//                            }
//
//                            if (response.getStatus() == 1) {
//                                newdatas.clear();
//                                newdatas=response.getData();
//                                //没有新的数据，提示消息
//                                if (newdatas == null || newdatas.size() == 0) {
//                                    if (mdatas.size() == 0) {
//                                        MyAdapter.notifyDataSetChanged();
//                                    } else {
//                                        //删除 footer
//                                        mdatas.remove(mdatas.size() - 1);
//                                        MyAdapter.notifyDataSetChanged();
//                                        loading = false;
//                                    }
//                                    return;
//                                }
//                                pagenum=pagenum+1;
//
//
//                                if (mdatas.size() == 0) {
//                                    mdatas.addAll(newdatas);
//                                    MyAdapter.notifyDataSetChanged();
//                                } else {
//                                    //删除 footer
//                                    mdatas.remove(mdatas.size() - 1);
//                                    mdatas.addAll(newdatas);
//                                    MyAdapter.notifyDataSetChanged();
//                                    loading = false;
//                                }
//                            }
//                        }
//                    });
//
//            return newdatas;
//        }
//
//        @Override
//        protected void onPostExecute(final List<Secret> moreArticles) {
//            super.onPostExecute(moreArticles);
//
//        }
//    }
//
//}
//

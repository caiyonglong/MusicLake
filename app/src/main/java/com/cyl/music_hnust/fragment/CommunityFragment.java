package com.cyl.music_hnust.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.CommunityAdapter;
import com.cyl.music_hnust.callback.SecretCallback;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.Secret;
import com.cyl.music_hnust.model.SecretInfo;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by Monkey on 2015/6/29.
 */
public class CommunityFragment extends BaseFragment  {

    RecyclerView mRecyclerView;
    TextView tv_empty;

    CommunityAdapter MyAdapter;
    String user_id;

    private static List<Secret> mdatas = new ArrayList<>();


    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {
        MyAdapter = new CommunityAdapter(getActivity(), mdatas);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(MyAdapter);

        if (UserStatus.getstatus(getActivity())){
            user_id = UserStatus.getUserInfo(getActivity()).getUser_id();
        }
        getSecret();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_community;
    }

    @Override
    public void initViews() {
       mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
       tv_empty= (TextView) rootView.findViewById(R.id.tv_empty);

    }

    private void getSecret() {
        OkHttpUtils.post().url(Constants.DEFAULT_URL)
                .addParams(Constants.FUNC, Constants.GET_MYSECRET_LIST)
                .addParams(Constants.USER_ID, user_id)
                .build()
                .execute(new SecretCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("dddd", "err");
                    }

                    @Override
                    public void onResponse(SecretInfo response) {
                        mdatas.addAll(response.getData());
                        if (response.getData().size()==0){
                            tv_empty.setText("暂无动态！");
                            tv_empty.setVisibility(View.VISIBLE);
                        }
                        MyAdapter.notifyDataSetChanged();
                    }
                });
    }
}


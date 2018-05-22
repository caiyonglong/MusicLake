package com.cyl.musiclake.ui.main;

import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.musicapi.SearchResult;
import com.cyl.musiclake.musicapi.callback.BaseApiListener;
import com.cyl.musiclake.musicapi.impl.BaseApiImpl;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends BaseActivity implements BaseApiListener {
    BaseApiImpl searchApi;
    @BindView(R.id.tv_show)
    TextView resultTv;
    @BindView(R.id.tv_status)
    TextView statusTv;

    @OnClick(R.id.btn_test1)
    void test() {
        searchApi.getTopList("1");
    }

    @OnClick(R.id.btn_test2)
    void test2() {
        searchApi.searchSong("薛之谦");
    }

    @OnClick(R.id.btn_test3)
    void test3() {
        searchApi.getSongDetail("netease", "557581284");
    }

    @OnClick(R.id.btn_test4)
    void test4() {
        searchApi.getLyricInfo("netease", "557581284");
    }

    @OnClick(R.id.btn_test5)
    void test5() {
        searchApi.getComment("netease", "557581284");
    }

    @OnClick(R.id.btn_test6)
    void test6() {
        searchApi.getSongUrl("netease", "557581284");
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        searchApi = new BaseApiImpl(this);
    }

    @Override
    protected void initInjector() {

    }


    @Override
    public void searchResult(SearchResult searchResult) {
        statusTv.setText("searchResult");
        resultTv.setText(searchResult.toString());
    }

    @Override
    public void songDetail(JSONObject jsonObject) {
        statusTv.setText("songDetail");
        resultTv.setText(jsonObject.toString());
    }

    @Override
    public void songUrl(JSONObject jsonObject) {
        statusTv.setText("songUrl");
        resultTv.setText(jsonObject.toString());
    }

    @Override
    public void getTopList(List<Music> musicList) {
        statusTv.setText("getTopList");
        resultTv.setText(musicList.toString());
    }

    @Override
    public void getComment(JSONObject jsonObject) {
        statusTv.setText("getComment");
        resultTv.setText(jsonObject.toString());
    }

    @Override
    public void getLyric(JSONObject jsonObject) {
        statusTv.setText("getLyric");
        resultTv.setText(jsonObject.toString());
    }

    @Override
    public void getOthor(JSONObject jsonObject) {
        statusTv.setText("getOthor");
        resultTv.setText(jsonObject.toString());
    }
}

package com.cyl.music_hnust.ui.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.callback.JsonCallback;
import com.cyl.music_hnust.bean.music.OnlineArtistInfo;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.Extras;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by yonglong on 2016/11/30.
 */

public class ArtistInfoActivity extends BaseActivity {

    private static final String TAG = "ArtistInfoActivity";
    @Bind(R.id.li_container)
    LinearLayout li_container;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.loading)
    LinearLayout loading;
    @Bind(R.id.tv_empty)
    TextView tv_empty;
    @Bind(R.id.progress)
    ProgressBar progress;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_artist;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        String tingUid = getIntent().getStringExtra(Extras.TING_UID);
//        getArtistInfo(tingUid);
        new TTask().execute(tingUid);
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void listener() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getArtistInfo(String tingUid) {
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_ARTIST_INFO)
                .addParams(Constants.PARAM_TING_UID, tingUid)
                .build()
                .execute(new JsonCallback<OnlineArtistInfo>(OnlineArtistInfo.class) {
                    @Override
                    public void onResponse(OnlineArtistInfo response) {
                        if (response == null) {
                            loading.setVisibility(View.GONE);
                            return;
                        }
                        loading.setVisibility(View.GONE);
                        onSuccess(response);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                    }
                });
    }


    private class TTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String baseUrl = Constants.BASE_URL + "?" +
                    Constants.PARAM_METHOD + "=" + Constants.METHOD_ARTIST_INFO + "&" +
                    Constants.PARAM_TING_UID + "=" + params[0];

            Log.e(TAG, "请求吗 :" + baseUrl);
            try {
                URL url = new URL(baseUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                int code = conn.getResponseCode();
                Log.e(TAG, "请求吗 :" + code);
                if (code == 200) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    InputStream is = conn.getInputStream(); // 字节流转换成字符串
                    int by = 0;
                    byte[] buffer = new byte[1024];
                    while ((by = is.read(buffer)) > 0) {
                        out.write(buffer, 0, by);
                    }
                    out.close();
                    String json = new String(out.toByteArray());
                    Log.e(TAG, "请求吗 :" + json);
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                Gson gson = new Gson();
                OnlineArtistInfo response = gson.fromJson(s, OnlineArtistInfo.class);
                loading.setVisibility(View.GONE);
                onSuccess(response);

            }
        }
    }

    private void onSuccess(OnlineArtistInfo jArtistInfo) {
        String name = jArtistInfo.getName();
        String avatarUri = jArtistInfo.getAvatar_s1000();
        String country = jArtistInfo.getCountry();
        String constellation = jArtistInfo.getConstellation();
        float stature = jArtistInfo.getStature();
        float weight = jArtistInfo.getWeight();
        String birth = jArtistInfo.getBirth();
        String intro = jArtistInfo.getIntro();
        String url = jArtistInfo.getUrl();
        if (!TextUtils.isEmpty(avatarUri)) {
            ImageView ivAvatar = new ImageView(this);
            ivAvatar.setImageResource(R.drawable.default_cover);
            ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.default_cover)
                    .showImageOnFail(R.drawable.default_cover)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(avatarUri, ivAvatar, options);
            li_container.addView(ivAvatar);
        }
        if (!TextUtils.isEmpty(name)) {
            toolbar.setTitle(name);
            TextView tvName = new TextView(this);
            tvName.setTextSize(18);
            tvName.setPadding(0, 0, 0, 10);
            tvName.setText("姓名：" + name);
            li_container.addView(tvName);
        }
        if (!TextUtils.isEmpty(country)) {
            TextView tvCountry = new TextView(this);
            tvCountry.setTextSize(18);
            tvCountry.setPadding(0, 0, 0, 10);
            tvCountry.setText("国籍：" + country);
            li_container.addView(tvCountry);
        }
        if (!TextUtils.isEmpty(constellation) && !constellation.equals("未知")) {
            TextView tvConstellation =
                    new TextView(this);
            tvConstellation.setTextSize(18);
            tvConstellation.setText("星座：" + constellation);
            li_container.addView(tvConstellation);
        }
        if (stature != 0f) {
            TextView tvStature = new TextView(this);
            tvStature.setTextSize(18);
            tvStature.setPadding(0, 0, 0, 10);
            tvStature.setText("身高：" + stature);
            li_container.addView(tvStature);
        }
        if (weight != 0f) {
            TextView tvWeight = new TextView(this);
            tvWeight.setTextSize(18);
            tvWeight.setPadding(0, 0, 0, 10);
            tvWeight.setText("体重：" + weight);
            li_container.addView(tvWeight);
        }
        if (!TextUtils.isEmpty(birth) && !birth.equals("0000-00-00")) {
            TextView tvBirth = new TextView(this);
            tvBirth.setTextSize(18);
            tvBirth.setPadding(0, 0, 0, 10);
            tvBirth.setText("出生日期：" + birth);
            li_container.addView(tvBirth);
        }
        if (!TextUtils.isEmpty(intro)) {
            TextView tvIntro = new TextView(this);
            tvIntro.setTextSize(18);
            tvIntro.setPadding(0, 0, 0, 10);
            tvIntro.setText("简介：" + intro);
            li_container.addView(tvIntro);
        }
        if (!TextUtils.isEmpty(url)) {
            TextView tvUrl = new TextView(this);
            String html = "<font color='#2196F3'><a href='%s'>查看更多信息</a></font>";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvUrl.setText(Html.fromHtml(String.format(html, url), 1));
            } else {
                tvUrl.setText(Html.fromHtml(String.format(html, url)));
            }
            tvUrl.setMovementMethod(LinkMovementMethod.getInstance());
            tvUrl.setPadding(0, 0, 0, 10);
            tvUrl.setGravity(Gravity.CENTER);
            li_container.addView(tvUrl);
        }

        if (li_container.getChildCount() == 0) {
            loading.setVisibility(View.VISIBLE);
            tv_empty.setText("暂无信息");
            progress.setVisibility(View.GONE);
        }
    }

}

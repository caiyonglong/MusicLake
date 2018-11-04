package com.cyl.musiclake.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;

/**
 * Author   : D22434
 * version  : 2018/3/20
 * function :
 */

public class WebActivity extends BaseActivity {
    private String url;
    private String title;

    @BindView(R.id.webContent)
    FrameLayout mWebContent;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
    }

    @Override
    protected String setToolbarTitle() {
        title = getIntent().getStringExtra("title");
        return title;
    }

    @Override
    protected void initData() {
        AgentWeb mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mWebContent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuShare) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_article_url, getString(R.string.app_name), title, url));
            intent.setType("text/plain");
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuBrowser) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context mContext, String title, String url) {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        mContext.startActivity(intent);
    }
}

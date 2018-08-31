package com.cyl.musiclake.ui.music.importplaylist

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.webkit.WebView
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicApiServiceImpl
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.OnlinePlaylistUtils
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_import_playlist.*


class ImportPlaylistActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {

    var mWebView: WebView? = null
    var mAdapter: SongAdapter? = null
    var name: String? = null
    var vendor: String? = null
    var musicList = mutableListOf<Music>()

    override fun getLayoutResID(): Int {
        return R.layout.activity_import_playlist
    }

    override fun initView() {

    }

    override fun initData() {
        mWebView = WebView(this)

    }

    override fun initInjector() {
    }

    override fun listener() {
        super.listener()
        syncBtn.setOnClickListener {
            showLoading(true)
            val playlistLink = playlistInputView.editText?.text.toString()
            getPlaylistId(playlistLink)
        }
        importBtn.setOnClickListener {
            if (name == null) {
                return@setOnClickListener
            }
            if (vendor == null) {
                return@setOnClickListener
            }
            if (musicList.size == 0) return@setOnClickListener
            MaterialDialog.Builder(this)
                    .title("导入到歌单")
                    .content(name.toString())
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive { dialog, which ->
                        OnlinePlaylistUtils.createPlaylist(name.toString(), success = {
                            it.pid?.let { it1 -> PlaylistApiServiceImpl.collectBatchMusic(it1, vendor.toString(), musicList) }
                        })
                    }.build()

        }
    }

    private fun getPlaylistId(link: String) {
        when {
            link.contains("http://music.163.com") -> {
                val len = link.lastIndexOf("playlist/") + "playlist/".length
                val id = link.substring(len, len + link.substring(len).indexOf("/"))
                importMusic("netease", id)
            }
            link.contains("http://y.qq.com") -> {
                val len = link.lastIndexOf("id=") + "id=".length
                val id = link.substring(len, len + link.substring(len).indexOf("&"))
                importMusic("qq", id)
            }
            link.contains("https://www.xiami.com") -> {
                val len = link.lastIndexOf("collect/") + "collect/".length
                val id = link.substring(len, link.indexOf("?"))
                importMusic("xiami", id)
            }
            else -> {
                ToastUtils.show("分享链接异常，解析失败！")
            }
        }
    }

    private fun importMusic(vendor: String, url: String) {
        this.vendor = vendor
        this.name = vendor + url
        val observable = MusicApiServiceImpl.getPlaylistSongs(vendor, url, 1, 20)
        ApiManager.request(observable, object : RequestCallBack<Playlist> {
            override fun success(result: Playlist) {
                showLoading(false)
                showResultAdapter(result)
            }

            override fun error(msg: String) {
                showLoading(false)
                ToastUtils.show("分享链接异常，解析失败！")
            }
        })
    }

    fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    fun showResultAdapter(result: Playlist) {
        mAdapter = SongAdapter(result.musicList)
        this.musicList = result.musicList
        resultRsv.adapter = mAdapter
        resultRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter?.bindToRecyclerView(resultRsv)

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            if (view.id != R.id.iv_more) {
                PlayManager.play(position, result.musicList, Constants.PLAYLIST_DOWNLOAD_ID + result.pid)
                mAdapter?.notifyDataSetChanged()
                NavigationHelper.navigateToPlaying(this@ImportPlaylistActivity, view.findViewById(R.id.iv_cover))
            }
        }
        mAdapter?.setOnItemChildClickListener { _, _, position ->
            BottomDialogFragment.newInstance(result.musicList[position]).show(this@ImportPlaylistActivity)
        }

    }

}

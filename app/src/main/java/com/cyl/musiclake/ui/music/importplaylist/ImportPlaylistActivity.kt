package com.cyl.musiclake.ui.music.importplaylist

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.MusicApiServiceImpl
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.music.edit.PlaylistManagerUtils
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_import_playlist.*


@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class ImportPlaylistActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {

    var mAdapter: SongAdapter? = null
    var name: String? = null
    var vendor: String? = null
    var musicList = mutableListOf<Music>()

    override fun getLayoutResID(): Int {
        return R.layout.activity_import_playlist
    }

    override fun setToolbarTitle(): String {
        return getString(R.string.import_playlist)
    }

    override fun initView() {
    }

    override fun initData() {
        if (Intent.ACTION_SEND == intent.action && intent.type != null) {
            if (Constants.TEXT_PLAIN == intent.type) {
                val title = intent?.getStringExtra(Intent.EXTRA_TEXT)
                playlistInputView.editText?.setText(title)
            }
        }
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
                ToastUtils.show("请先同步获取歌曲！")
                return@setOnClickListener
            }
            if (vendor == null) {
                ToastUtils.show("请先同步获取歌曲！")
                return@setOnClickListener
            }
            if (musicList.size == 0) return@setOnClickListener
            MaterialDialog.Builder(this)
                    .title("是否将${musicList.size}首歌导入到歌单")
                    .positiveText(R.string.sure)
                    .negativeText(R.string.cancel)
                    .inputRangeRes(2, 20, R.color.red)
                    .input(getString(R.string.input_playlist), name.toString(), false) { _, _ -> }
                    .onPositive { dialog1, _ ->
                        val title = dialog1.inputEditText?.text.toString()
                        PlaylistManagerUtils.createPlaylist(title, success = {
                            it.pid?.let { _ ->
                                PlaylistManagerUtils.collectBatchMusic(it, vendor.toString(), musicList, success = {
                                    this@ImportPlaylistActivity.finish()
                                })
                            }
                        }, type = "")
                    }.build()
                    .show()

        }
    }

    private fun getPlaylistId(link: String) {
        try {
            when {
                link.contains("music.163.com") -> {
                    val len = link.lastIndexOf("playlist/") + "playlist/".length
                    val id = link.substring(len, len + link.substring(len).indexOf("/"))
                    importMusic(Constants.NETEASE, id)
                }
                link.contains("y.qq.com") -> {
                    val len = link.lastIndexOf("id=") + "id=".length

                    val idString = link.substring(len)
                    val id = if (idString.indexOf("&") > 0) {
                        link.substring(len, len + link.substring(len).indexOf("&")).trim()
                    } else {
                        idString
                    }
                    importMusic(Constants.QQ, id)
                }
                link.contains("www.xiami.com") -> {
                    val len = link.lastIndexOf("collect/") + "collect/".length
                    val end = if (link.indexOf("?") == -1) link.indexOf("(") else link.indexOf("?")
                    val id = link.substring(len, end).trim()
                    importMusic(Constants.XIAMI, id)
                }
                link.contains("h.xiami.com") -> {
                    val start = link.lastIndexOf("id=") + "id=".length
                    val end = link.substring(start).indexOf(" ")
                    val id = link.substring(start, start + end).trim()
                    importMusic(Constants.XIAMI, id)
                }
                else -> {
                    showLoading(false)
                    ToastUtils.show("请输入有效的链接！")
                }
            }
        } catch (e: Throwable) {
            showLoading(false)
            ToastUtils.show("请输入有效的链接！")
        }
    }

    private fun importMusic(vendor: String, pid: String) {
        this.vendor = vendor
        val observable = MusicApiServiceImpl.getPlaylistSongs(vendor, pid)
        ApiManager.request(observable, object : RequestCallBack<Playlist> {
            override fun success(result: Playlist) {
                showLoading(false)
                musicList.clear()
                musicList = result.musicList
//                result.musicList.forEach {
//                    if (!it.isCp) {
//                        musicList.add(it)
//                    }
//                }
//                result.musicList = musicList
                showResultAdapter(result)
            }

            override fun error(msg: String) {
                showLoading(false)
                ToastUtils.show(msg)
            }
        })
    }

    fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    fun showResultAdapter(result: Playlist) {
        mAdapter = SongAdapter(result.musicList)
        this.name = result.name
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
            BottomDialogFragment.newInstance(result.musicList[position], Constants.PLAYLIST_IMPORT_ID).show(this@ImportPlaylistActivity)
        }

    }

}

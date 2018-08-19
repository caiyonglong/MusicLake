package com.cyl.musiclake.ui.music.online.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.ui.music.online.contract.BaiduListContract
import com.cyl.musiclake.ui.music.online.presenter.BaiduListPresenter
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.FormatUtil
import com.cyl.musiclake.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_online.*

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class BaiduMusicListActivity : BaseActivity<BaiduListPresenter>(), BaiduListContract.View {
    private val musicList = mutableListOf<Music>()
    private val mAdapter by lazy { SongAdapter(musicList) }

    private var mViewHeader: View? = null
    private var mIvBackground: ImageView? = null
    private var mIvCover: ImageView? = null
    private var mTvTitle: TextView? = null
    private var mTvDate: TextView? = null
    private var mTvDesc: TextView? = null

    private var mOffset = 0
    private var title: String? = null
    private var type: String? = null
    private var desc: String? = null
    private var pic: String? = null
    private var mCurrentCounter = 0
    private var TOTAL_COUNTER = 0
    private val limit = 10

    override fun getLayoutResID(): Int {
        return R.layout.activity_online
    }

    override fun initView() {
        title = intent.getStringExtra(Extras.BILLBOARD_TITLE)
        type = intent.getStringExtra(Extras.BILLBOARD_TYPE)
        desc = intent.getStringExtra(Extras.BILLBOARD_DESC)
        pic = intent.getStringExtra(Extras.BILLBOARD_ALBUM)
        initHeaderView()
    }

    override fun setToolbarTitle(): String? {
        title = intent.getStringExtra(Extras.BILLBOARD_TITLE)
        return title
    }

    override fun initData() {
        mAdapter.setEnableLoadMore(true)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter
        mAdapter.bindToRecyclerView(recyclerView)
        showHeaderInfo()

        type?.let { mPresenter?.loadOnlineMusicList(it, limit, mOffset) }
    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    override fun listener() {
        mAdapter.setOnItemClickListener { _, view, position ->
            if (view.id != R.id.iv_more) {
                PlayManager.play(position, musicList, Constants.BAIDU + type)
                mAdapter.notifyDataSetChanged()
                NavigationHelper.navigateToPlaying(this)
            }
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val music = adapter.getItem(position) as Music?
            BottomDialogFragment.newInstance(music, Constants.OP_ONLINE).show(this)
        }
        mAdapter.setOnLoadMoreListener({
            recyclerView.postDelayed({
                if (mCurrentCounter < TOTAL_COUNTER) {
                    //数据全部加载完毕
                    mAdapter.loadMoreEnd()
                } else {
                    //成功获取更多数据
                    type?.let { mPresenter?.loadOnlineMusicList(it, limit, mOffset) }
                }
            }, 1000)
        }, recyclerView)
    }

    private fun initHeaderView() {
        mViewHeader = LayoutInflater.from(this).inflate(R.layout.activity_online_header, null)
        val params = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mViewHeader?.layoutParams = params

        mIvCover = mViewHeader?.findViewById<View>(R.id.iv_cover) as ImageView
        mTvTitle = mViewHeader?.findViewById<View>(R.id.tv_title) as TextView
        mTvDate = mViewHeader?.findViewById<View>(R.id.tv_update_date) as TextView
        mTvDesc = mViewHeader?.findViewById<View>(R.id.tv_comment) as TextView

        mTvDate?.text = getString(R.string.recently_update, FormatUtil.distime(System.currentTimeMillis()))
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showErrorInfo(msg: String) {
        ToastUtils.show(this, msg)
        mAdapter.loadMoreFail()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showHeaderInfo() {
        mTvTitle?.text = title
        //        mTvDate.setText(getString(R.string.recent_update, playlistInfo.getUpdate_date()));
        mTvDesc?.text = desc
        CoverLoader.loadImageView(this, pic, mIvCover)
        mAdapter.setHeaderView(mViewHeader, 0)
    }

    override fun showOnlineMusicList(musicList: List<Music>) {
        this.musicList.addAll(musicList)
        mAdapter.setNewData(this.musicList)
        mOffset += limit
        mCurrentCounter = mAdapter.data.size
        TOTAL_COUNTER = mOffset
        mAdapter.loadMoreComplete()
    }

    companion object {
        private val TAG = "BaiduMusicListActivity"
    }
}

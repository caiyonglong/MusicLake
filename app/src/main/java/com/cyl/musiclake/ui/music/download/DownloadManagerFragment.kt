package com.cyl.musiclake.ui.music.download

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseLazyFragment
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.data.download.TasksManager
import com.cyl.musiclake.data.download.TasksManagerModel
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by yonglong on 2016/11/26.
 */

class DownloadManagerFragment : BaseLazyFragment<DownloadPresenter>(), DownloadContract.View {
    private var mAdapter: TaskItemAdapter? = null
    private var models: List<TasksManagerModel> = ArrayList()

    override fun loadData() {
        mPresenter?.loadDownloading()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_notoolbar
    }

    override fun initViews() {
        mSwipeRefreshLayout?.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        TasksManager.onCreate(WeakReference(this))
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    fun postNotifyDataChanged() {
        mFragmentComponent.activity.runOnUiThread {
            mAdapter?.notifyDataSetChanged()
        }
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun listener() {
        super.listener()
        mSwipeRefreshLayout?.setOnRefreshListener { mPresenter?.loadDownloading() }
    }

    override fun hideLoading() {
        super.hideLoading()
        mSwipeRefreshLayout?.isRefreshing = false
    }

    override fun onLazyLoad() {
        models = TasksManager.getModelList()
    }

    override fun showErrorInfo(msg: String) {

    }

    override fun showSongs(musicList: List<Music>) {

    }

    override fun showDownloadList(modelList: List<TasksManagerModel>) {
        updateDownLoadList(modelList)
    }

    private fun updateDownLoadList(list: List<TasksManagerModel>) {
        hideLoading()
        if (mAdapter == null) {
            mAdapter = TaskItemAdapter(context, list)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = mAdapter
        } else {
            mAdapter?.notifyDataSetChanged()
        }
        if (models.isEmpty()) {
            showEmptyState()
        }
    }

    companion object {

        fun newInstance(): DownloadManagerFragment {
            val args = Bundle()
            val fragment = DownloadManagerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

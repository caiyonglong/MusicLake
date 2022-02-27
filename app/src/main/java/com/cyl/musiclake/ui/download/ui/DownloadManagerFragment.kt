package com.cyl.musiclake.ui.download.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.ui.download.TasksManager
import com.cyl.musiclake.ui.download.TasksManagerModel
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import java.lang.ref.WeakReference

/**
 * Created by yonglong on 2016/11/26.
 */

class DownloadManagerFragment : BaseFragment<DownloadPresenter>(), DownloadContract.View {
    private var mAdapter: TaskItemAdapter? = null

    override fun loadData() {
        mPresenter?.loadDownloading()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_notoolbar
    }

    override fun initViews() {
        TasksManager.onCreate(WeakReference(this))
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    fun postNotifyDataChanged() {
        try {
            mFragmentComponent?.activity?.runOnUiThread {
                mAdapter?.notifyDataSetChanged()
            }
        } catch (e: Throwable) {

        }
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun listener() {
        super.listener()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showErrorInfo(msg: String) {

    }

    override fun showSongs(musicList: MutableList<Music>) {

    }

    override fun showDownloadList(modelList: List<TasksManagerModel>) {
        updateDownLoadList(modelList)
    }

    private fun updateDownLoadList(list: List<TasksManagerModel>) {
        hideLoading()
        if (mAdapter == null) {
            mAdapter = context?.let { TaskItemAdapter(it, list) }
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = mAdapter
        } else {
            mAdapter?.models = list
            mAdapter?.notifyDataSetChanged()
        }
        if (list.isEmpty()) {
            emptyStateView?.visibility = View.VISIBLE
            showEmptyState()
        } else {
            emptyStateView?.visibility = View.GONE
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

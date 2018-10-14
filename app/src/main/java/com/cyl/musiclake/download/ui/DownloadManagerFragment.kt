package com.cyl.musiclake.download.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseLazyFragment
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.download.TasksManager
import com.cyl.musiclake.download.TasksManagerModel
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import java.lang.ref.WeakReference

/**
 * Created by yonglong on 2016/11/26.
 */

class DownloadManagerFragment : BaseLazyFragment<DownloadPresenter>(), DownloadContract.View {
    private var mAdapter: TaskItemAdapter? = null
    private var isCache: Boolean = false

    override fun loadData() {
        isCache = arguments?.getBoolean(Constants.KEY_IS_CACHE) ?: false
        if (isCache) {
            mPresenter?.loadDownloading(isCache)
        }
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

    override fun onLazyLoad() {
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
            mAdapter = TaskItemAdapter(context, list,isCache)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = mAdapter
        } else {
            mAdapter?.models = list
            mAdapter?.notifyDataSetChanged()
        }
        if (list.isEmpty()) {
            showEmptyState()
        }
    }

    companion object {

        fun newInstance(isCache: Boolean): DownloadManagerFragment {
            val args = Bundle()
            val fragment = DownloadManagerFragment()
            args.putBoolean(Constants.KEY_IS_CACHE, isCache)
            fragment.arguments = args
            return fragment
        }
    }
}

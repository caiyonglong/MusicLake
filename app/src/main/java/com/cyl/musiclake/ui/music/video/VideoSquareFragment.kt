package com.cyl.musiclake.ui.music.video

import android.os.Bundle
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.bean.CategoryInfo
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.ui.main.MainActivity
import com.cyl.musiclake.ui.main.PageAdapter
import com.cyl.musiclake.ui.music.mv.MvListFragment
import kotlinx.android.synthetic.main.frag_mv.*

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class VideoSquareFragment : BaseFragment<BasePresenter<*>?>() {
    override fun getLayoutId(): Int {
        return R.layout.frag_mv
    }

    override fun loadData() {
        showLoading()
        val observable = NeteaseApiServiceImpl.getVideoCatList()
        ApiManager.request(observable, object : RequestCallBack<MutableList<CategoryInfo>> {
            override fun success(result: MutableList<CategoryInfo>) {
                initVideoGroupList(result)
                hideLoading()
            }

            override fun error(msg: String) {
                hideLoading()
                showError(msg, true)
            }
        })
    }

    override fun retryLoading() {
        super.retryLoading()
        if (errorTextView?.text?.equals("需要登录") == true) {
            (activity as MainActivity).checkBindNeteaseStatus(false) { success ->
                if (success) {
                    loadData()
                }
            }
        } else {
            loadData()
        }
    }

    /**
     * 初始化分类列表
     */
    fun initVideoGroupList(list: List<CategoryInfo>) {
        val adapter = PageAdapter(childFragmentManager)
        list.forEach {
            if ("MV" == it.title || "mv" == it.title) {
                adapter.addFragment(MvListFragment.newInstance("personalized"), it.title)
            } else {
                adapter.addFragment(VideoListFragment.newInstance(it.id.toString()), it.title)
            }
        }
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        viewPager.offscreenPageLimit = 2
        viewPager.currentItem = 0
    }

    companion object {
        private const val TAG = "ChartsFragment"
        fun newInstance(): VideoSquareFragment {
            val args = Bundle()
            val fragment = VideoSquareFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
package com.cyl.musiclake.ui.music.video

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cyl.musicapi.netease.MvInfoDetail
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.bean.VideoInfoBean
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.base.BaseLazyFragment
import com.cyl.musiclake.ui.music.mv.*
import com.cyl.musiclake.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class VideoListFragment : BaseLazyFragment<MvListPresenter?>(), MvListContract.View {
    private var mOffset = 0
    private var groupId: String? = null

    //适配器
    private var mAdapter: VideoListAdapter? = null
    var videoList = mutableListOf<VideoInfoBean>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_notoolbar
    }

    override fun initViews() {
        groupId = arguments?.getString("groupId")
        //适配器
        mAdapter = VideoListAdapter(videoList)
        if (groupId != null) {
            //初始化列表
            val layoutManager = LinearLayoutManager(activity)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView.layoutManager = layoutManager
            mAdapter?.loadMoreModule?.setOnLoadMoreListener {
//               成功获取更多数据
                loadVideoList(videoList.size)
            }
            recyclerView.adapter = mAdapter
        }
    }

    override fun retryLoading() {
        super.retryLoading()
        videoList.clear()
    }

    override fun listener() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            startActivity<VideoDetailActivity>(Extras.VIDEO_VID to videoList[position].vid, Extras.VIDEO_TYPE to 1)
        }
    }

    override fun onLazyLoad() {
        showLoading()
        loadVideoList(mOffset)
    }

    private fun loadVideoList(offset: Int) {
        val observable = groupId?.let { NeteaseApiServiceImpl.getVideoList(it, offset) }
        ApiManager.request(observable, object : RequestCallBack<MutableList<VideoInfoBean>> {
            override fun success(result: MutableList<VideoInfoBean>) {
                showVideoList(result)
                hideLoading()
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
                hideLoading()
            }
        })
    }

    fun showVideoList(tt: MutableList<VideoInfoBean>) {
        this.videoList.addAll(tt)
        mAdapter?.setNewData(this.videoList)
    }

    companion object {
        private const val TAG = "ChartsFragment"

        fun newInstance(groupId: String?): VideoListFragment {
            val args = Bundle()
            args.putString("groupId", groupId)
            val fragment = VideoListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun showMvList(mvList: MutableList<MvInfoDetail>?) {

    }
}
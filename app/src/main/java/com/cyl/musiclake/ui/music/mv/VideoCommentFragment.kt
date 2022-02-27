package com.cyl.musiclake.ui.music.mv

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyl.musicapi.netease.CommentsItemInfo
import com.cyl.musicapi.netease.MvInfoDetail
import com.cyl.musicapi.netease.MvInfoDetailInfo
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.MvInfoBean
import com.cyl.musiclake.bean.VideoInfoBean
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.base.BaseLazyFragment
import kotlinx.android.synthetic.main.frag_mv_list.recyclerView

/**
 * 作者：yonglong
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 视频播放详情fragment
 */
class VideoCommentFragment : BaseLazyFragment<VideoDetailPresenter>(), VideoDetailContract.View {

    private var mCommentAdapter: MvCommentAdapter? = null
    private val mHotCommentAdapter: MvCommentAdapter? = null

    private var offset = 0
    private var vid = ""
    private var mType = 1

    private val videoCommentList = mutableListOf<CommentsItemInfo>()

    override fun loadData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_video_comment
    }

    override fun initInjector() {
        super.initInjector()
        mFragmentComponent.inject(this)
    }

    override fun initViews() {
        mCommentAdapter = MvCommentAdapter(videoCommentList)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //初始化评论adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mCommentAdapter
        recyclerView.isNestedScrollingEnabled = false
        mCommentAdapter?.loadMoreModule?.setOnLoadMoreListener {
//               成功获取更多数据
            mPresenter?.loadMvComment(vid, mType, videoCommentList.size - offset)
        }
    }

    override fun listener() {
    }

    companion object {
        fun newInstance(vid: String, type: Int = 1): VideoCommentFragment {
            val args = Bundle()
            val fragment = VideoCommentFragment()
            args.putString(Extras.VIDEO_VID, vid)
            args.putInt(Extras.VIDEO_TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun showMvComment(list: List<CommentsItemInfo>) {
        hideLoading()
        videoCommentList.addAll(list)
        mCommentAdapter?.loadMoreModule?.loadMoreComplete()
        mCommentAdapter?.setNewInstance(videoCommentList)
    }

    override fun showMvUrlInfo(mvUrl: String?) {
    }

    override fun showMvDetailInfo(mvInfoDetailInfo: VideoInfoBean?) {
    }

    override fun showVideoInfoList(mvList: MutableList<VideoInfoBean>) {
    }

    override fun showBaiduMvDetailInfo(mvInfoBean: MvInfoBean?) {
    }

    override fun showMvHotComment(mvHotCommentInfo: List<CommentsItemInfo>) {
        //热评
        mvHotCommentInfo.let {
            videoCommentList.addAll(0, it)
            offset = it.size
        }
    }

    override fun onLazyLoad() {
        vid = arguments?.getString(Extras.VIDEO_VID).toString()
        mType = arguments?.getInt(Extras.VIDEO_TYPE) ?: 1
        showLoading()
        mPresenter?.loadMvComment(vid, mType, 0)
    }
}

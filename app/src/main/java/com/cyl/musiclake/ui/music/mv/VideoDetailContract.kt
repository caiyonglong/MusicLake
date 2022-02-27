package com.cyl.musiclake.ui.music.mv

import com.cyl.musicapi.netease.CommentsItemInfo
import com.cyl.musicapi.netease.MvInfoDetail
import com.cyl.musicapi.netease.MvInfoDetailInfo
import com.cyl.musiclake.bean.MvInfoBean
import com.cyl.musiclake.bean.VideoInfoBean
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BaseContract.BaseView

interface VideoDetailContract {
    interface View : BaseView {
        fun showVideoInfoList(mvList: MutableList<VideoInfoBean>)
        fun showBaiduMvDetailInfo(mvInfoBean: MvInfoBean?)
        fun showMvDetailInfo(mvInfoDetailInfo: VideoInfoBean?)
        fun showMvUrlInfo(mvUrl: String?)
        fun showMvHotComment(mvHotCommentInfo: List<CommentsItemInfo>)
        fun showMvComment(mvCommentInfo: List<CommentsItemInfo>)
    }

    interface Presenter : BaseContract.BasePresenter<View?> {
        fun loadMv(offset: Int)
        fun loadMvDetail(mvid: String?, type: Int)
        fun loadBaiduMvInfo(songId: String?)
        fun loadSimilarMv(mvid: String?, type: Int)
        fun loadMvComment(mvid: String?, type: Int, offset: Int)
    }
}
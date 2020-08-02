package com.cyl.musiclake.ui.music.mv

import com.cyl.musicapi.netease.CommentsItemInfo
import com.cyl.musicapi.netease.MvInfoDetail
import com.cyl.musicapi.netease.MvInfoDetailInfo
import com.cyl.musiclake.bean.MvInfoBean
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BaseContract.BaseView

interface MvDetailContract {
    interface View : BaseView {
        fun showMvList(mvList: List<MvInfoDetail>)
        fun showBaiduMvDetailInfo(mvInfoBean: MvInfoBean?)
        fun showMvDetailInfo(mvInfoDetailInfo: MvInfoDetailInfo?)
        fun showMvUrlInfo(mvUrl: String?)
        fun showMvHotComment(mvHotCommentInfo: List<CommentsItemInfo>)
        fun showMvComment(mvCommentInfo: List<CommentsItemInfo>)
    }

    interface Presenter : BaseContract.BasePresenter<View?> {
        fun loadMv(offset: Int)
        fun loadMvDetail(mvid: String?)
        fun loadBaiduMvInfo(songId: String?)
        fun loadSimilarMv(mvid: String?)
        fun loadMvComment(mvid: String?, offset: Int)
    }
}
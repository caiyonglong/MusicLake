package com.cyl.musiclake.ui.music.mv

import android.view.MenuItem
import com.cyl.musicapi.netease.CommentsItemInfo
import com.cyl.musicapi.netease.MvInfoDetail
import com.cyl.musicapi.netease.MvInfoDetailInfo
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.MvInfoBean
import com.cyl.musiclake.bean.VideoInfoBean
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.player.exoplayer.ExoPlayerManager
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_video.*

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 本地视频播放界面
 */
class VideoPlayerActivity : BaseVideoPlayerActivity() {

    override fun getLayoutResID(): Int {
        return R.layout.activity_video
    }

    override fun initView() {
        video_view.controllerHideOnTouch = true//?.setVisibilityListener(ControlsVisibilityListener())
    }

    override fun setToolbarTitle(): String? {
        return intent.getStringExtra(Extras.MV_TITLE)
    }

    override fun initData() {
        val mVid = intent.getStringExtra(Extras.VIDEO_VID)
        //加载百度mv
        mVid?.let {
            showLoading()
            mPresenter?.loadBaiduMvInfo(mVid)
        }
        ExoPlayerManager.bindView(video_view)
        //加载本地视频
        intent.getStringExtra(Extras.VIDEO_PATH)?.let {
            ExoPlayerManager.setDataSource(it)
        }
    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    override fun showBaiduMvDetailInfo(mvInfoBean: MvInfoBean?) {
        if (mvInfoBean?.uri != null) {
            LogUtil.d(TAG, "url = ${mvInfoBean.uri}")
            mvInfoBean.uri?.let {
                ExoPlayerManager.setDataSource(it)
            }
        }
    }

    override fun showMvHotComment(mvHotCommentInfo: List<CommentsItemInfo>) {
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showVideoInfoList(mvList: MutableList<VideoInfoBean>) {
    }

    override fun showMvComment(mvCommentInfo: List<CommentsItemInfo>) {
    }

    override fun showMvUrlInfo(mvUrl: String?) {

    }

    override fun showMvDetailInfo(mvInfoDetailInfo: VideoInfoBean?) {
        hideLoading()
        if (mvInfoDetailInfo != null) {
            val url = when {
//                mvInfoDetailInfo.brs.p1080 != null -> mvInfoDetailInfo.brs.p1080
//                mvInfoDetailInfo.brs.p720 != null -> mvInfoDetailInfo.brs.p720
//                mvInfoDetailInfo.brs.p480 != null -> mvInfoDetailInfo.brs.p480
//                mvInfoDetailInfo.brs.p240 != null -> mvInfoDetailInfo.brs.p240
                else -> {
                    ""
                }
            }
            if (url == "") {
                ToastUtils.show(getString(R.string.mv_path_error))
//                return
            }
            LogUtil.d(TAG, "url = $url")
            //For now we just picked an arbitrary item to play
//            video_view.setPreviewImage(Uri.parse(mvInfoDetailInfo.cover))
//            video_view.setVideoURI(Uri.parse(url))
        }
    }

}

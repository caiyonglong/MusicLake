package com.cyl.musiclake.ui.widget

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.UserInfoBean
import com.cyl.musiclake.utils.CoverLoader

/**
 * Created by cyl on 2018/9/30.
 */
class NoticeView : FrameLayout {
    private val mRootView by lazy { View.inflate(context, R.layout.item_user_notice, this) }
    private val userNameTv by lazy { mRootView.findViewById<TextView>(R.id.userNameTv) }
    private val userStatusTv by lazy { mRootView.findViewById<TextView>(R.id.userStatusTv) }
    private val userCoverIv by lazy { mRootView.findViewById<ImageView>(R.id.userCoverIv) }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    fun init() {
    }

    fun setNewData(userInfo: UserInfoBean, isLeave: Boolean) {
        userNameTv?.text = userInfo.nickname
        userStatusTv?.text = if (isLeave) context.getString(R.string.user_join) else context.getString(R.string.user_leave)
        CoverLoader.loadImageView(context, userInfo.avatar, userCoverIv)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.visibility = View.VISIBLE
        this.animate()?.alpha(0f)?.translationY(0f)?.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                this@NoticeView.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })?.setDuration(3000)?.start()
    }
}
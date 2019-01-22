package com.cyl.musiclake.ui.music.playpage

import android.view.View
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.base.BasePresenter

class CoverFragment : BaseFragment<BasePresenter<BaseContract.BaseView>>() {
    private var coverView: View? = null


    override fun getLayoutId(): Int {
        return R.layout.frag_player_coverview
    }

    override fun initInjector() {
    }
}
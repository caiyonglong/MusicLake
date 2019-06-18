package com.cyl.musiclake.ui.music.artist.fragment

import android.os.Bundle
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.music.artist.contract.ArtistInfoContract
import com.cyl.musiclake.ui.music.artist.presenter.ArtistInfoPresenter
import kotlinx.android.synthetic.main.frag_artist_info.*

/**
 * Created by yonglong on 2016/11/30.
 */

class ArtistInfoFragment : BaseFragment<ArtistInfoPresenter>(), ArtistInfoContract.View {

    override fun getLayoutId(): Int {
        return R.layout.frag_artist_info
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    /**
     * 更新歌手信息
     */
    fun updateArtistDesc(info: String?) {
        tv_desc?.text = info ?: ""
    }

    companion object {
        private val TAG = "ArtistInfoFragment"

        fun newInstance(tag: String): ArtistInfoFragment {
            val args = Bundle()
            val fragment = ArtistInfoFragment()
            args.putString("Tag", tag)
            fragment.arguments = args
            return fragment
        }
    }
}

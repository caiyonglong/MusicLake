package com.cyl.musiclake.ui.music.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.TextView
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.data.db.Music
import java.util.*

class PopupDialogFragment : DialogFragment(), View.OnClickListener {

    private val musicList = ArrayList<Music>()
    private lateinit var rootView: View
    lateinit var appCompatActivity: AppCompatActivity

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog.setCanceledOnTouchOutside(true)
        val window = dialog.window
        val params = window!!.attributes
        params.gravity = Gravity.BOTTOM
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = MusicApp.getInstance().screenSize.y / 2
        window.attributes = params
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.dialog_layout, container, false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        initView()
        initListener()
    }

    private fun initView() {
        rootView.findViewById<TextView>(R.id.tv_title).text = music?.title
        rootView.findViewById<TextView>(R.id.tv_album).text = resources.getString(R.string.popup_album, music?.album)
        rootView.findViewById<TextView>(R.id.tv_artist).text = resources.getString(R.string.popup_artist, music?.artist)
    }

    private fun initListener() {
        rootView.findViewById<View>(R.id.next_play).setOnClickListener(this)
        rootView.findViewById<View>(R.id.albumView).setOnClickListener(this)
        rootView.findViewById<View>(R.id.artistView).setOnClickListener(this)
        rootView.findViewById<View>(R.id.detailView).setOnClickListener(this)
        rootView.findViewById<View>(R.id.downloadView).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.albumView -> {
                toLocalAlbum()
            }
            R.id.artistView -> {
                toLocalArit()
            }
            R.id.detailView -> {
            }
            R.id.downloadView -> {
                music?.let { it1 -> DownloadDialog.newInstance(it1).show(appCompatActivity) }
            }
        }
        dismiss()
    }


    companion object {
        var music: Music? = null

        fun newInstance(music: Music): PopupDialogFragment {
            val args = Bundle()
            this.music = music
            val fragment = PopupDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun show(context: AppCompatActivity) {
        appCompatActivity = context
        val ft = context.supportFragmentManager
        show(ft, javaClass.simpleName)
    }

    private fun toLocalAlbum() {
        activity?.let { it1 ->
            if (music != null && music?.albumId != null && music?.album != null) {
                NavigationHelper.navigateToAlbum(it1,
                        music?.albumId!!,
                        music?.album!!, null)
            }
        }
    }

    private fun toLocalArit() {
        activity?.let { it1 ->
            if (music != null && music?.artistId != null && music?.artist != null) {
                NavigationHelper.navigateToArtist(it1,
                        music?.artistId!!,
                        music?.artist!!, null)
            }
        }
    }
}

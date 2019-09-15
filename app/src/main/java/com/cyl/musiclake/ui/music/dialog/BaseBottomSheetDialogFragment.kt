package com.cyl.musiclake.ui.music.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.cyl.musiclake.MusicApp.mContext
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 *
 */
abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {
    var mRootView: View? = null
    lateinit var mContext: AppCompatActivity

    abstract fun getLayoutResId(): Int

    override fun onStart() {
        super.onStart()
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutResId(), container, false)
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return mRootView
    }

    fun showIt(context: AppCompatActivity) {
        val fm = context.supportFragmentManager
        show(fm, "dialog")
    }


    fun show(context: AppCompatActivity) {
        mContext = context
        val ft = context.supportFragmentManager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }
}
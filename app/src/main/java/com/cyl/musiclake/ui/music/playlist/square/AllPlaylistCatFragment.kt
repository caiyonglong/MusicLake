package com.cyl.musiclake.ui.music.playlist.square

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import android.view.*
import android.widget.ImageView
import com.cyl.musicapi.netease.CatListBean
import com.cyl.musicapi.netease.SubItem
import com.cyl.musiclake.api.music.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.ui.music.playlist.PlaylistActivity
import com.cyl.musiclake.ui.widget.channel.Channel
import com.cyl.musiclake.ui.widget.channel.ChannelView
import com.cyl.musiclake.utils.LogUtil
import kotlinx.android.synthetic.main.frag_playlist_category.*
import org.jetbrains.anko.support.v4.startActivity


/**
 * Des    : 精选歌单集分类选择列表
 * Author : master.
 * Date   : 2018/8/23 .
 */
class AllPlaylistCatFragment : androidx.fragment.app.DialogFragment(), ChannelView.OnChannelListener {
    private var rootView: View? = null
    private val backIv by lazy { rootView?.findViewById<ImageView>(com.cyl.musiclake.R.id.backIv) }
    private val TAG = javaClass.simpleName

    var isHighQuality: Boolean = false
    var curCateList = mutableListOf<String>()
    val map = mutableMapOf<String, MutableList<SubItem>>()

    companion object {
        var categoryTags = mutableListOf<Any>()
    }

    var successListener: ((String) -> Unit?)? = null

    override fun onStart() {
        dialog?.let {
            val lp = it.window?.attributes
            lp?.width = WindowManager.LayoutParams.MATCH_PARENT
            lp?.height = WindowManager.LayoutParams.MATCH_PARENT
            lp?.windowAnimations = com.cyl.musiclake.R.style.dialogAnim
            lp?.gravity = Gravity.BOTTOM
            it.window?.attributes = lp
            it.setCanceledOnTouchOutside(true)
        }
        super.onStart()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(com.cyl.musiclake.R.layout.frag_playlist_category, container, false)
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backIv?.setOnClickListener {
            dismissAllowingStateLoss()
        }

        ApiManager.request(NeteaseApiServiceImpl.getCatList(), object : RequestCallBack<CatListBean> {
            override fun success(result: CatListBean?) {
                /**
                 * 排序
                 */
                result?.categories?.let {
                    map[result.categories.c0] = mutableListOf()
                    map[result.categories.c1] = mutableListOf()
                    map[result.categories.c2] = mutableListOf()
                    map[result.categories.c3] = mutableListOf()
                    map[result.categories.c4] = mutableListOf()
                }
                result?.sub?.forEach {
                    when (it.category) {
                        0 -> map[result.categories.c0]?.add(it)
                        1 -> map[result.categories.c1]?.add(it)
                        2 -> map[result.categories.c2]?.add(it)
                        3 -> map[result.categories.c3]?.add(it)
                        4 -> map[result.categories.c4]?.add(it)
                    }
                }
                categoryTags.clear()

                if (!isHighQuality) {
                    categoryTags.add("我的歌单广场")
                    curCateList.forEach {
                        categoryTags.add(SubItem(name = it))
                    }
                }
                map.forEach {
                    categoryTags.add(it.key)
                    categoryTags.addAll(it.value)
                }
                init()
            }

            override fun error(msg: String?) {
            }
        })
    }

    private fun init() {
        val myChannel = curCateList
        val myChannelList = mutableListOf<Channel>()
        val recommendChannelList = mutableMapOf<String, MutableList<Channel>>()

        //我的歌单广场
        for (i in myChannel.indices) {
            val aMyChannel = myChannel[i]
            val channel: Channel
            channel = when (i) {
                in 3..5 -> //可设置频道归属板块（channelBelong），当前设置此频道归属于第二板块，当删除该频道时该频道将回到第二板块
                    Channel(aMyChannel, 2, i)
                in 8..9 -> //可设置频道归属板块（channelBelong），当前设置此频道归属于第三板块，当删除该频道时该频道将回到第三板块中
                    Channel(aMyChannel, 3, i)
                else -> Channel(aMyChannel, i as Any)
            }
            myChannelList.add(channel)
        }

        channelView.setChannelFixedCount(3)
        channelView.addPlate("我的歌单广场", myChannelList)
        //分类列表
        map.forEach {
            recommendChannelList[it.key] = mutableListOf()
            for (aMyChannel in it.value) {
                val channel = Channel(aMyChannel.name)
                recommendChannelList[it.key]?.add(channel)
            }
            channelView.addPlate(it.key, recommendChannelList[it.key])
        }
        channelView.inflateData()
        channelView.setOnChannelItemClickListener(this)
    }


    /**
     * Item点击监听
     */
    override fun channelItemClick(position: Int, channel: Channel) {
        LogUtil.i(TAG, "$position..$channel")
        startActivity<PlaylistActivity>("curTag" to channel.channelName)
    }

    /**
     * Item编辑完成
     */
    override fun channelEditFinish(channelList: List<Channel>) {
        LogUtil.i(TAG, channelList.toString())
        LogUtil.i(TAG, channelView.myChannel.toString())
        LogUtil.i(TAG, "编辑完成")
        curCateList.clear()
        channelList.forEach {
            curCateList.add(it.channelName)
        }
        successListener?.invoke("")
    }

    /**
     * Item开始编辑
     */
    override fun channelEditStart() {
    }

    /**
     *显示出对话框
     */
    fun showIt(context: FragmentActivity?) {
        dialog?.dismiss()
        val transaction = context?.supportFragmentManager?.beginTransaction()
        transaction?.add(this, tag)?.commitAllowingStateLoss()
    }

}




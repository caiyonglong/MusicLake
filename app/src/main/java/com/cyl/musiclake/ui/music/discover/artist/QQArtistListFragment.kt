package com.cyl.musiclake.ui.music.discover.artist

import android.support.v7.widget.LinearLayoutManager
import android.util.Pair
import com.cyl.musicapi.bean.SingerTag
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.music.discover.ArtistCateAdapter
import com.cyl.musiclake.ui.music.discover.ArtistListAdapter
import com.cyl.musiclake.utils.LogUtil
import kotlinx.android.synthetic.main.frag_artist_list.*

/**
 * 功能：在线排行榜
 * 邮箱：643872807@qq.com
 * 版本：4.1.3
 */
class QQArtistListFragment : BaseFragment<ArtistListPresenter>(), ArtistListContract.View {

//    private val areaList = mutableListOf("全部", "内地", "港台", "日本", "韩国", "其他")
//    private val sexList = mutableListOf("全部", "男", "女", "混合")
//    private val genreList = mutableListOf("全部", "流行", "嘻哈", "摇滚", "电子", "民谣", "R&B", "民歌", "轻音乐", "爵士", "古典", "乡村", "蓝调")
//    private val indexList = mutableListOf("全部", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#")

    var singerTag: SingerTag? = null
    //适配器
    var areaListAdapter: ArtistCateAdapter? = null
    var sexListAdapter: ArtistCateAdapter? = null
    var genreListAdapter: ArtistCateAdapter? = null
    var indexListAdapter: ArtistCateAdapter? = null
    private val filterTips = StringBuilder()


    private var mArtistAdapter: ArtistListAdapter? = null


    override fun getToolBarTitle(): String {
        return getString(R.string.all_artist)
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_artist_list
    }

    public override fun initViews() {
        //初始化列表
        areaRsv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        indexRsv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        sexRsv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        genreRsv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        resultRsv.layoutManager = LinearLayoutManager(activity)
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun loadData() {
        //适配器
        updateArtistList()
    }

    override fun retryLoading() {
        super.retryLoading()
        loadData()
    }
    override fun listener() {

    }

    /**
     * 更新歌手分类
     */
    private fun updateArtistList() {
        val area = areaListAdapter?.flagId ?: -100
        val sex = sexListAdapter?.flagId ?: -100
        val genre = genreListAdapter?.flagId ?: -100
        val index = indexListAdapter?.flagId ?: -100
        filterTips.setLength(0)
        filterTips.append(singerTag?.index?.get(indexListAdapter?.position ?: 0)?.name ?: "热门")
        filterTips.append(" ")
        if (areaListAdapter != null && areaListAdapter?.position != 0) {
            filterTips.append(singerTag?.area?.get(areaListAdapter?.position ?: 0)?.name ?: "")
            filterTips.append("-")
        }
        if (sexListAdapter != null && sexListAdapter?.position != 0) {
            filterTips.append(singerTag?.sex?.get(sexListAdapter?.position ?: 0)?.name ?: "")
            filterTips.append("-")
        }
        if (areaListAdapter != null && genreListAdapter?.position != 0) {
            filterTips.append(singerTag?.genre?.get(genreListAdapter?.position ?: 0)?.name ?: "")
        }
        val params = mapOf("area" to area, "sex" to sex, "genre" to genre, "index" to index)
        LogUtil.e("artistList", params.toString())
        mPresenter?.loadArtists(0, params)
    }

    /**
     * 显示歌手歌曲
     */
    override fun showArtistList(artistList: MutableList<Artist>) {
        //适配器
        mArtistAdapter = ArtistListAdapter(artistList)
        resultRsv.adapter = mArtistAdapter
        mArtistAdapter?.bindToRecyclerView(resultRsv)
        mArtistAdapter?.setOnItemClickListener { adapter, view, position ->
            val artist = adapter.data[position] as Artist
            NavigationHelper.navigateToArtist(mFragmentComponent.activity, artist, Pair(view.findViewById(R.id.iv_cover), getString(R.string.transition_album)))
        }
    }

    override fun showArtistTags(tags: SingerTag) {
        titleTv.text = filterTips.toString()
        if (areaListAdapter == null) {
            areaListAdapter = ArtistCateAdapter(tags.area)
            indexListAdapter = ArtistCateAdapter(tags.index)
            sexListAdapter = ArtistCateAdapter(tags.sex)
            genreListAdapter = ArtistCateAdapter(tags.genre)
            singerTag = tags

            areaRsv.adapter = areaListAdapter
            areaListAdapter?.bindToRecyclerView(areaRsv)

            indexRsv.adapter = indexListAdapter
            indexListAdapter?.bindToRecyclerView(indexRsv)

            sexRsv.adapter = sexListAdapter
            sexListAdapter?.bindToRecyclerView(sexRsv)

            genreRsv.adapter = genreListAdapter
            genreListAdapter?.bindToRecyclerView(genreRsv)

            areaListAdapter?.setOnItemClickListener { _, _, position ->
                areaListAdapter?.position = position
                areaListAdapter?.flagId = singerTag?.area?.get(position)?.id ?: -100
                areaListAdapter?.notifyDataSetChanged()
                updateArtistList()
            }
            genreListAdapter?.setOnItemClickListener { _, _, position ->
                genreListAdapter?.position = position
                genreListAdapter?.flagId = singerTag?.genre?.get(position)?.id ?: -100
                genreListAdapter?.notifyDataSetChanged()
                updateArtistList()
            }
            indexListAdapter?.setOnItemClickListener { _, _, position ->
                indexListAdapter?.position = position
                indexListAdapter?.flagId = singerTag?.index?.get(position)?.id ?: -100
                indexListAdapter?.notifyDataSetChanged()
                updateArtistList()
            }
            sexListAdapter?.setOnItemClickListener { _, _, position ->
                sexListAdapter?.position = position
                sexListAdapter?.flagId = singerTag?.sex?.get(position)?.id ?: -100
                sexListAdapter?.notifyDataSetChanged()
                updateArtistList()
            }
        }
    }
}
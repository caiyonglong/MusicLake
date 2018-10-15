package com.cyl.musiclake.ui.music.discover

import android.support.v7.widget.LinearLayoutManager
import android.util.Pair
import com.cyl.musicapi.bean.SingerTag
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseFragment
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.common.NavigationHelper
import kotlinx.android.synthetic.main.frag_artist_list.*

/**
 * 功能：在线排行榜
 * 邮箱：643872807@qq.com
 * 版本：4.1.3
 */
class ArtistListFragment : BaseFragment<ArtistListPresenter>(), ArtistListContract.View {

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
//        val areaListAdapter = ArtistCateAdapter(areaList)
//        val sexListAdapter = ArtistCateAdapter(sexList)
//        val genreListAdapter = ArtistCateAdapter(genreList)
//        val indexListAdapter = ArtistCateAdapter(indexList)

        getArtistList()
    }

    override fun listener() {

    }

    fun getArtistList() {
        val area = areaListAdapter?.flagId ?: -100
        val sex = sexListAdapter?.flagId ?: -100
        val genre = genreListAdapter?.flagId ?: -100
        val index = indexListAdapter?.flagId ?: -100
        val tips = singerTag?.area?.get(areaListAdapter?.position ?: 0)?.name ?: " "+
        singerTag?.sex?.get(sexListAdapter?.position ?: 0)?.name ?: " "+
        singerTag?.genre?.get(genreListAdapter?.position ?: 0)?.name ?: " "+
        singerTag?.index?.get(indexListAdapter?.position ?: 0)?.name ?: ""
        titleTv.text = if (tips.isEmpty()) "热门" else tips
        val params = mapOf("area" to area, "sex" to sex, "genre" to genre, "index" to index)
        mPresenter?.loadArtists(0, params)
    }

    override fun showArtistList(artistList: MutableList<Artist>) {
        //适配器
        mArtistAdapter = ArtistListAdapter(artistList)
        resultRsv.adapter = mArtistAdapter
        mArtistAdapter?.bindToRecyclerView(resultRsv)
        mArtistAdapter?.setOnItemClickListener { adapter, view, position ->
            val artist = adapter.data[position] as Artist
            NavigationHelper.navigateToPlaylist(mFragmentComponent.activity, artist, Pair(view.findViewById(R.id.iv_cover), getString(R.string.transition_album)))
        }
    }

    override fun showArtistTags(charts: SingerTag) {
        if (areaListAdapter == null) {
            areaListAdapter = ArtistCateAdapter(charts.area)
            indexListAdapter = ArtistCateAdapter(charts.index)
            sexListAdapter = ArtistCateAdapter(charts.sex)
            genreListAdapter = ArtistCateAdapter(charts.genre)
            singerTag = charts

            areaRsv.adapter = areaListAdapter
            areaListAdapter?.bindToRecyclerView(areaRsv)

            indexRsv.adapter = indexListAdapter
            indexListAdapter?.bindToRecyclerView(indexRsv)

            sexRsv.adapter = sexListAdapter
            sexListAdapter?.bindToRecyclerView(sexRsv)

            genreRsv.adapter = genreListAdapter
            genreListAdapter?.bindToRecyclerView(genreRsv)

            areaListAdapter?.setOnItemClickListener { adapter, view, position ->
                areaListAdapter?.position = position
                areaListAdapter?.flagId = singerTag?.area?.get(position)?.id ?: -100
                areaListAdapter?.notifyDataSetChanged()
                getArtistList()
            }
            genreListAdapter?.setOnItemClickListener { _, view, position ->
                genreListAdapter?.position = position
                genreListAdapter?.flagId = singerTag?.genre?.get(position)?.id ?: -100
                genreListAdapter?.notifyDataSetChanged()
                getArtistList()
            }
            indexListAdapter?.setOnItemClickListener { _, view, position ->
                indexListAdapter?.position = position
                indexListAdapter?.flagId = singerTag?.index?.get(position)?.id ?: -100
                indexListAdapter?.notifyDataSetChanged()
                getArtistList()
            }
            sexListAdapter?.setOnItemClickListener { _, view, position ->
                sexListAdapter?.position = position
                sexListAdapter?.flagId = singerTag?.sex?.get(position)?.id ?: -100
                indexListAdapter?.notifyDataSetChanged()
                getArtistList()
            }
        }
    }
}
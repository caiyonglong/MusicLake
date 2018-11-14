package com.cyl.musiclake.bean

import android.os.Parcel
import android.os.Parcelable
import com.cyl.musiclake.common.Constants
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * Created by yonglong on 2016/11/23.
 */

class MvInfoBean() : LitePalSupport() {
    var title: String? = null
    var mid: String? = null
    var mvId: String? = null

    var type: String? = Constants.LOCAL
    var picUrl: String? = null
    var desc: String? = null
    var uri: String? = null

    var artist = mutableListOf<Artist>()
}

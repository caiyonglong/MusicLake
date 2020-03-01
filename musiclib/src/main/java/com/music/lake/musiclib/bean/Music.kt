//package com.music.lake.musiclib.bean
//
//import android.os.Parcel
//import android.os.Parcelable
//
///**
// * 作者：yonglong on 2016/8/9 10:50
// * 邮箱：643872807@qq.com
// * 版本：2.5
// */
//class MusicInfo() : Parcelable {
//    //数据库存储id
//    var id: Long = 0
//
//    constructor(parcel: Parcel) : this() {
//        id = parcel.readLong()
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeLong(id)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<MusicInfo> {
//        override fun createFromParcel(parcel: Parcel): MusicInfo {
//            return MusicInfo(parcel)
//        }
//
//        override fun newArray(size: Int): Array<MusicInfo?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
//

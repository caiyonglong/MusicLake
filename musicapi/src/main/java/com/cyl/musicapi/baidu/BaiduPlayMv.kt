package com.cyl.musicapi.baidu

import com.google.gson.annotations.SerializedName

data class BaiduPlayMv(@SerializedName("result")
                       val result: MvResult,
                       @SerializedName("error_code")
                       val errorCode: Int = 0)

data class MvResult(
        @SerializedName("files")
        val files: Files,
        @SerializedName("max_definition")
        val maxDefinition: String,
        @SerializedName("min_definition")
        val minDefinition: String,
        @SerializedName("mv_info")
        val mvInfo: MvInfo,
        @SerializedName("share_pic")
        val sharePic: String,
        @SerializedName("share_url")
        val shareUrl: String,
        @SerializedName("video_info")
        val videoInfo: VideoInfo,
        @SerializedName("video_type")
        val videoType: String
)

data class VideoInfo(
        @SerializedName("del_status")
        val delStatus: String,
        @SerializedName("distribution")
        val distribution: String,
        @SerializedName("mv_id")
        val mvId: String,
        @SerializedName("provider")
        val provider: String,
        @SerializedName("sourcepath")
        val sourcepath: String,
        @SerializedName("thumbnail")
        val thumbnail: String,
        @SerializedName("thumbnail2")
        val thumbnail2: String,
        @SerializedName("tvid")
        val tvid: String,
        @SerializedName("video_id")
        val videoId: String
)

data class Files(
        @SerializedName("51")
        val x51: X31?,
        @SerializedName("41")
        val x41: X31?,
        @SerializedName("31")
        val x31: X31?,
        @SerializedName("21")
        val x21: X31?

)

data class X31(
        @SerializedName("aspect_ratio")
        val aspectRatio: String,
        @SerializedName("definition")
        val definition: String,
        @SerializedName("file_duration")
        val fileDuration: String,
        @SerializedName("file_extension")
        val fileExtension: String,
        @SerializedName("file_format")
        val fileFormat: String,
        @SerializedName("file_link")
        val fileLink: String,
        @SerializedName("file_size")
        val fileSize: String,
        @SerializedName("player_param")
        val playerParam: String,
        @SerializedName("source_path")
        val sourcePath: String,
        @SerializedName("video_file_id")
        val videoFileId: String,
        @SerializedName("video_id")
        val videoId: String
)

data class MvInfo(
        @SerializedName("aliastitle")
        val aliastitle: String,
        @SerializedName("all_artist_id")
        val allArtistId: String,
        @SerializedName("artist")
        val artist: String,
        @SerializedName("artist_id")
        val artistId: String,
        @SerializedName("artist_list")
        val artistList: List<Artist>,
        @SerializedName("del_status")
        val delStatus: String,
        @SerializedName("mv_id")
        val mvId: String,
        @SerializedName("play_nums")
        val playNums: Int,
        @SerializedName("provider")
        val provider: String,
        @SerializedName("publishtime")
        val publishtime: String,
        @SerializedName("subtitle")
        val subtitle: String,
        @SerializedName("thumbnail")
        val thumbnail: String,
        @SerializedName("thumbnail2")
        val thumbnail2: String,
        @SerializedName("title")
        val title: String
)

data class Artist(
        @SerializedName("artist_480_800")
        val artist480800: String,
        @SerializedName("artist_640_1136")
        val artist6401136: String,
        @SerializedName("artist_id")
        val artistId: String,
        @SerializedName("artist_name")
        val artistName: String,
        @SerializedName("avatar_mini")
        val avatarMini: String,
        @SerializedName("avatar_s180")
        val avatarS180: String,
        @SerializedName("avatar_s300")
        val avatarS300: String,
        @SerializedName("avatar_s500")
        val avatarS500: String,
        @SerializedName("avatar_small")
        val avatarSmall: String,
        @SerializedName("del_status")
        val delStatus: String,
        @SerializedName("ting_uid")
        val tingUid: String
)

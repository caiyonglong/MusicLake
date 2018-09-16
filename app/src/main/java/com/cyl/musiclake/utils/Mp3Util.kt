package com.cyl.musiclake.utils

import com.cyl.musiclake.bean.Music
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.audio.mp3.MP3File
import org.jaudiotagger.tag.TagException
import org.jaudiotagger.tag.id3.AbstractID3v2Frame
import org.jaudiotagger.tag.id3.framebody.FrameBodyTALB
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIT2
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1
import java.io.IOException

/**
 * Des    :
 * Author : master.
 * Date   : 2018/8/26 .
 */
object Mp3Util {

    /**
     * 修改音乐名、歌手名和专辑名
     */
    fun updateTagInfo(path: String, music: Music): Boolean {
        try {
            val file = saveInfo(MP3File(path), music.title, music.artist, music.album)
            file.commit()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: TagException) {
            e.printStackTrace()
        } catch (e: ReadOnlyFileException) {
            e.printStackTrace()
        } catch (e: InvalidAudioFrameException) {
            e.printStackTrace()
        }
        return false
    }

    private fun saveInfo(file: MP3File, title: String? = null, artist: String? = null, album: String? = null): MP3File {
        try {
            title?.let {
                if (file.iD3v2Tag.getFrame("TIT2") != null) {
                    val frame1 = file.iD3v2Tag.getFrame("TIT2") as AbstractID3v2Frame
                    val body1 = frame1.body as FrameBodyTIT2
                    body1.text = it
                    file.iD3v2Tag.setFrame("TIT2", mutableListOf(frame1))
                } else {
                    val bodyTIT2 = FrameBodyTIT2()
                    val frame1 = file.iD3v2Tag.createFrame(bodyTIT2.identifier)
                    bodyTIT2.text = it
                    frame1.body = bodyTIT2
                    file.iD3v2Tag.setFrame(bodyTIT2.identifier, mutableListOf(frame1))
                }
            }

            artist?.let {
                if (file.iD3v2Tag.getFrame("TPE1") != null) {
                    val frame2 = file.iD3v2Tag.getFrame("TPE1") as AbstractID3v2Frame
                    val body2 = frame2.body as FrameBodyTPE1
                    body2.text = it
                    file.iD3v2Tag.setFrame("TPE1", mutableListOf(frame2))
                } else {
                    val bodyTPE1 = FrameBodyTPE1()
                    val frame1 = file.iD3v2Tag.createFrame(bodyTPE1.identifier)
                    bodyTPE1.text = it
                    frame1.body = bodyTPE1
                    file.iD3v2Tag.setFrame(bodyTPE1.identifier, mutableListOf(frame1))
                }
            }

            album?.let {
                if (file.iD3v2Tag.getFrame("TALB") != null) {
                    val frame3 = file.iD3v2Tag.getFrame("TALB") as AbstractID3v2Frame
                    val body3 = frame3.body as FrameBodyTALB
                    body3.text = it
                    file.iD3v2Tag.setFrame("TALB", mutableListOf(frame3))
                } else {
                    val bodyTALB = FrameBodyTALB()
                    val frame1 = file.iD3v2Tag.createFrame(bodyTALB.identifier)
                    bodyTALB.text = it
                    frame1.body = bodyTALB
                    file.iD3v2Tag.setFrame(bodyTALB.identifier, mutableListOf(frame1))
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return file
    }


    /**
     * 通过歌曲地址，jaudiotagger获取音乐名、歌手名和专辑名
     */
    fun getTagInfo(path: String) {
        try {
            val file = MP3File(path)
            val songName = file.iD3v2Tag.frameMap["TIT2"].toString()
            val singer = file.iD3v2Tag.frameMap["TPE1"].toString()
            val album = file.iD3v2Tag.frameMap["TALB"].toString()

            LogUtil.e(javaClass.simpleName, songName)
            LogUtil.e(javaClass.simpleName, singer)
            LogUtil.e(javaClass.simpleName, album)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: TagException) {
            e.printStackTrace()
        } catch (e: ReadOnlyFileException) {
            e.printStackTrace()
        } catch (e: InvalidAudioFrameException) {
            e.printStackTrace()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
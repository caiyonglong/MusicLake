package com.cyl.musiclake

import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.utils.FormatUtil
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.audio.mp3.MP3File
import org.jaudiotagger.tag.TagException
import org.jaudiotagger.tag.id3.AbstractID3v2Frame
import org.jaudiotagger.tag.id3.framebody.*
import java.io.IOException


/**
 * Author   : D22434
 * version  : 2018/3/20
 * function :
 */

class Test {
    @org.junit.Test
    fun tt() {
        //        for (; ; ) {
        println("请输入下一个数据（直接回车结束输入）：")
        var totalTime: Long = 60
        var time = totalTime * 1000 * 60
        println(FormatUtil.formatTime(time))

        totalTime = 60
        time = totalTime * 1000 * 60
        println(FormatUtil.formatTime(time))


        totalTime = 1400
        time = totalTime * 1000 * 60
        println(FormatUtil.formatTime(time))

        totalTime = 1440
        time = totalTime * 1000 * 60
        println(FormatUtil.formatTime(time))
        val s = "Hello world"
        val str = s.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val len = str[str.size - 1].length
    }

    fun lengthOfLastWord(s: String): Int {
        val str = s.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (str.isEmpty()) 0 else str[str.size - 1].length
    }


    @org.junit.Test
    fun tt1() {
        val date = FormatUtil.formatDate(1530800858658L)
        println(date)
    }


    @org.junit.Test
    fun iii() {
        getTagInfo("D:\\qqFileRecv\\MobileFile\\我只在乎你.mp3")
        saveMusicInfo("D:\\qqFileRecv\\MobileFile\\我只在乎你.mp3")
        getTagInfo("D:\\qqFileRecv\\MobileFile\\我只在乎你.mp3")
    }

    private fun getTagInfo(path: String) {
        try {
            val file = MP3File(path)
            val songName = file.iD3v2Tag.frameMap["TIT2"].toString()
            val singer = file.iD3v2Tag.frameMap["TPE1"].toString()
            val album = file.iD3v2Tag.frameMap["TALB"].toString()
            println("TIT2"+songName)
            println(singer)
            println(album)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: TagException) {
            e.printStackTrace()
        } catch (e: ReadOnlyFileException) {
            e.printStackTrace()
        } catch (e: InvalidAudioFrameException) {
            e.printStackTrace()
        }
    }

    /**
     *
    Text="关不上的窗";
    Text="周传雄";
    Text="恋人创世纪";

     */
    private fun saveMusicInfo(path: String) {
        try {
            var file = MP3File(path)
            file = saveInfo(file, "关不上的窗", "周传雄", "恋人创世纪")
            file.commit()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: TagException) {
            e.printStackTrace()
        } catch (e: ReadOnlyFileException) {
            e.printStackTrace()
        } catch (e: InvalidAudioFrameException) {
            e.printStackTrace()
        }
    }

    private fun saveInfo(file: MP3File, title: String? = null, artist: String? = null, album: String? = null): MP3File {
        title?.let {
            val frame1 = file.iD3v2Tag.getFrame("TIT2") as AbstractID3v2Frame
            val body1 = frame1.body as FrameBodyTIT2
            body1.text = it
            file.iD3v2Tag.setFrame("TIT2", mutableListOf(frame1))
        }

        artist?.let {
            val frame2 = file.iD3v2Tag.getFrame("TPE1") as AbstractID3v2Frame
            val body2 = frame2.body as FrameBodyTPE1
            body2.text = it
            file.iD3v2Tag.setFrame("TPE1", mutableListOf(frame2))
        }

        album?.let {
            val frame3 = file.iD3v2Tag.getFrame("TALB") as AbstractID3v2Frame
            val body3 = frame3.body as FrameBodyTALB
            body3.text = it
            file.iD3v2Tag.setFrame("TALB", mutableListOf(frame3))
        }
        return file
    }

}

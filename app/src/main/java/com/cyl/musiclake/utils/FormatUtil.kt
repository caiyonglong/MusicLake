package com.cyl.musiclake.utils

import android.annotation.SuppressLint
import java.io.UnsupportedEncodingException
import java.lang.ref.SoftReference
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
/**
 * <br></br>
 * **进行一些转换工作**
 */
object FormatUtil {


    /**
     * 格式化时间
     *
     * @param time 时间值 (00:00 -23:59:59)
     * @return 时间
     */
    fun formatTime(time: Long): String {
        var temp = time
        // TODO Auto-generated method stub
        if (temp == 0L) {
            return "00:00"
        }
        temp /= 1000
        val s = (temp % 60) // s秒
        val m = (temp / 60 % 60) //m分
        val h = (temp / 60 / 60 % 24) //h小时s
        return if (h > 0) {
            "${if (h > 9) "$h" else "0$h"}:${if (m > 9) "$m" else "0$m"}:${if (s > 9) "$s" else "0$s"}"
        } else {
            "${if (m > 9) "$m" else "0$m"}:${if (s > 9) "$s" else "0$s"}"
        }
    }

    /**
     * 格式化播放次数
     *
     */
    fun formatPlayCount(count: Long): String {
        return when {
            count < 10000 -> "$count"
            else -> {
                "${count / 10000}.${count / 1000 % 10}万"
            }
        }
    }

    /**
     * 格式化时间
     *
     * @param time 时间值 (00:00 -23:59:59)
     * @return 时间
     */
    fun formatDate(time: Long): String {
        val duration = System.currentTimeMillis() - time
        return when {
            duration < 60 * 1000 -> "${duration / 1000}秒前"
            duration < 60 * 1000 * 60 -> "${duration / 1000 / 60}分钟前"
            duration < 60 * 1000 * 60 * 24 -> "${duration / 1000 / 60 / 60}小时前"
            else -> {
                val dfs = SimpleDateFormat("yyyy年MM月dd日")
                val date = dfs.format(Date(time))
                date
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
            /**
             * 格式化时间
             *
             * @param time 时间值 (00:00 -23:59:59)
             * @return 时间
             */
    fun formatDate(time: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val parse = dateFormat.parse(time)
        val duration = System.currentTimeMillis() - parse.time
        return when {
            duration < 60 * 1000 -> "${duration / 1000}秒前"
            duration < 60 * 1000 * 60 -> "${duration / 1000 / 60}分钟前"
            duration < 60 * 1000 * 60 * 24 -> "${duration / 1000 / 60 / 60}小时前"
            else -> {
                time
            }
        }
    }


    /**
     * 格式化文件大小
     *
     * @param size 文件大小值
     * @return 文件大小
     */
    fun formatSize(size: Long): String {
        // TODO Auto-generated method stub
        val df = DecimalFormat("#.00")
        var fileSize = "0B"
        if (size < 1024) {
            fileSize = df.format(size.toDouble()) + "B"
        } else if (size < 1048576) {
            fileSize = df.format(size.toDouble() / 1024) + "KB"
        } else if (size < 1073741824) {
            fileSize = df.format(size.toDouble() / 1048576) + "MB"
        } else {
            fileSize = df.format(size.toDouble() / 1073741824) + "GB"
        }
        return fileSize
    }

    /**
     * 对乱码的处理
     *
     * @param s 原字符串
     * @return GBK处理后的数据
     */
    fun formatGBKStr(s: String): String {
        var str: String? = null
        try {
            str = String(s.toByteArray(charset("ISO-8859-1")))
        } catch (e: UnsupportedEncodingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return s
    }
//
//    fun Distance(long1: Double, lat1: Double, long2: Double,
//                 lat2: Double): Float {
//        var lat1 = lat1
//        var lat2 = lat2
//        val a: Double
//        val b: Double
//        val R: Double
//        R = 6378137.0 // 地球半径
//        lat1 = lat1 * Math.PI / 180.0
//        lat2 = lat2 * Math.PI / 180.0
//        a = lat1 - lat2
//        b = (long1 - long2) * Math.PI / 180.0
//        val d: Double
//        val sa2: Double
//        val sb2: Double
//        sa2 = Math.sin(a / 2.0)
//        sb2 = Math.sin(b / 2.0)
//        d = (2.0
//                * R
//                * Math.asin(Math.sqrt(sa2 * sa2 + (Math.cos(lat1)
//                * Math.cos(lat2) * sb2 * sb2))))
//
//        return Math.ceil(d).toFloat()
//    }

    fun getTimeDifference(starTime: String): String {
        var timeString = ""
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val dateFormat1 = SimpleDateFormat("yyyy-MM-dd")
        val endTime = dateFormat.format(Date())
        try {
            val parse = dateFormat.parse(starTime)
            val parse1 = dateFormat.parse(endTime)

            val diff = parse1.time - parse.time

            val day = diff / (24 * 60 * 60 * 1000)
            val hour = diff / (60 * 60 * 1000) - day * 24
            val min = diff / (60 * 1000) - day * 24 * 60 - hour * 60
            val s = diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60

            //距当前时间大于15天时输出年月日
            if (day > 15) {
                timeString = dateFormat1.format(parse)
            } else if (day > 0) {
                timeString = day.toString() + "天前"
            } else if (hour > 0) {
                timeString = hour.toString() + "小时前"
            } else if (min > 0) {
                timeString = min.toString() + "分钟前"
            } else {
                timeString = s.toString() + "秒前"
            }

        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return timeString

    }

    /**
     * 毫秒转化成时间
     *
     * @return 时间
     */
    fun distime(time: Long): String {
        val dfs = SimpleDateFormat("yyyy-MM-dd")
        val date = Date(time)
        return dfs.format(date)
    }

    private val dfs by lazy { SimpleDateFormat("yyyy-MM-dd HH:mm:ss") }

    /**
     * 获取当前时间
     * @return 时间 yyyy-MM-dd HH:mm:ss
     */
    fun getChatDateTime(time: Long): String {
        return dfs.format(Date(time))
    }


    /**
     * 根据字符串获取当前时间
     * @return 时间 yyyy-MM-dd HH:mm:ss
     */
    fun getChatParseDateTime(time: String): Long {
        return dfs.parse(time).time
    }

    fun formatDate(date: Date?, pattern: String?): String {
        if (date == null) throw IllegalArgumentException("date is null")
        if (pattern == null) throw IllegalArgumentException("pattern is null")

        val formatter = formatFor(pattern)
        return formatter.format(date)
    }

    private val THREADLOCAL_FORMATS = object : ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>() {

        override fun initialValue(): SoftReference<Map<String, SimpleDateFormat>> {
            return SoftReference(
                    HashMap())
        }

    }

    private fun formatFor(pattern: String): SimpleDateFormat {
        val ref = THREADLOCAL_FORMATS.get()
        var formats: MutableMap<String, SimpleDateFormat>? = ref?.get() as MutableMap<String, SimpleDateFormat>
        if (formats == null) {
            formats = HashMap()
            THREADLOCAL_FORMATS.set(
                    SoftReference<Map<String, SimpleDateFormat>>(formats))
        }

        var format = formats[pattern]
        if (format == null) {
            format = SimpleDateFormat(pattern, Locale.US)
            format.timeZone = TimeZone.getTimeZone("GMT")
            formats[pattern] = format
        }

        return format
    }
}

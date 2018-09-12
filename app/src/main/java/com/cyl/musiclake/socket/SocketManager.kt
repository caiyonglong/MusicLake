package com.cyl.musiclake.socket

import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.bean.MessageEvent
import com.cyl.musiclake.bean.SocketOnlineEvent
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import com.github.nkzawa.engineio.client.Transport
import com.github.nkzawa.engineio.client.transports.PollingXHR
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Manager
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import java.io.UnsupportedEncodingException

/**
 * Des    : 实时在线socket统计
 * Author : master.
 * Date   : 2018/9/10 .
 */
class SocketManager {

    val MESSAGE_BROADCAST = "broadcast"
    var realTimeUserNum = 0
    lateinit var socket: Socket

    fun initSocket() {
        val opts = IO.Options()
        opts.transports = arrayOf(PollingXHR.NAME)
        socket = IO.socket(Constants.BASE_PLAYER_URL, opts)
        socket.io().on(Manager.EVENT_TRANSPORT) { args ->
            val transport = args[0] as Transport
            transport.on(Transport.EVENT_REQUEST_HEADERS) { args ->
                val headers = args[0] as MutableMap<String, String>
                // modify request headers
                headers["accesstoken"] = PlaylistApiServiceImpl.token ?: ""
            }
            transport.on(Transport.EVENT_RESPONSE_HEADERS) { args ->
                val headers = args[0] as MutableMap<String, String>
                // access response headers
                val cookie = headers["Set-Cookie"]?.get(0)
            }
        }
        socket.on(Socket.EVENT_CONNECT) {
            LogUtil.e("连接成功！")
        }.on("online total") { num ->
            LogUtil.e("当前在线人数：${num[0].toString()}")
            realTimeUserNum = num[0] as Int
            EventBus.getDefault().post(SocketOnlineEvent(num = realTimeUserNum))
        }.on(Socket.EVENT_DISCONNECT) {
            LogUtil.e("已断开连接")
        }.on(Socket.EVENT_ERROR) { error ->
            LogUtil.e("连接错误：$error")
        }.on(MESSAGE_BROADCAST) { broadcast ->
            try {
                val message = Gson().fromJson(broadcast[0].toString(), MessageEvent::class.java)
                EventBus.getDefault().post(message)
                LogUtil.e("收到消息：${message.toString()}")
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        LogUtil.e("初始化、建立连接！")
        socket.connect()
    }

    /**
     * 发送消息
     */
    fun sendSocketMessage(msg: String) {
        if (msg.isEmpty()) return
        if (!UserStatus.getLoginStatus()) {
            ToastUtils.show("请登录")
            return
        }
        socket.emit(MESSAGE_BROADCAST, msg)
    }

    fun toUtf8(str: String): String? {
        var result: String? = null
        try {
            result = String(str.toByteArray(charset("UTF-8")), charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return result
    }

}
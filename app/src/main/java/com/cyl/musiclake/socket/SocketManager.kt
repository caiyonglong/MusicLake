package com.cyl.musiclake.socket

import com.cyl.musicapi.playlist.UserInfo
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.bean.MessageEvent
import com.cyl.musiclake.bean.SocketOnlineEvent
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.event.ChatUserEvent
import com.cyl.musiclake.ui.chat.ChatActivity
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Manager.EVENT_ERROR
import io.socket.client.Manager.EVENT_TRANSPORT
import io.socket.client.Socket
import io.socket.client.Socket.EVENT_DISCONNECT
import io.socket.engineio.client.Transport
import io.socket.engineio.client.transports.PollingXHR
import io.socket.engineio.client.transports.WebSocket
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import java.util.concurrent.TimeUnit

/**
 * Des    : 实时在线socket统计
 * Author : master.
 * Date   : 2018/9/10 .
 */
class SocketManager {
    val MESSAGE_BROADCAST = "broadcast"
    val MESSAGE_ONLINE_TOTAL = "online total"
    val MESSAGE_ONLINE_USERS = "online users"
    var realUsersNum = 0
    lateinit var socket: Socket

    fun initSocket() {
        try {
            val opts = IO.Options()
            val okHttpClient = OkHttpClient().newBuilder().pingInterval(15, TimeUnit.SECONDS).retryOnConnectionFailure(true).build()
            opts.forceNew = true
            opts.reconnectionDelay = 30000
            opts.reconnectionDelayMax = 20
            opts.reconnectionAttempts = 5
            opts.randomizationFactor = 1.0
            opts.timeout = 60 * 1000
            opts.webSocketFactory = okHttpClient
            opts.callFactory = okHttpClient
            opts.timestampRequests = true
            opts.transports = arrayOf(WebSocket.NAME)
            socket = IO.socket("http://39.108.214.63:15003", opts)
            LogUtil.e("初始化、建立连接！")
        } catch (e: Throwable) {

        }
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

    /**
     * 接收消息
     */
    private fun receiveSocketMessage(msg: MessageEvent) {
        ChatActivity.messages.add(msg)
    }

    /**
     * 建立连接
     */
    private fun connect() {
        socket.io().on(Manager.EVENT_TRANSPORT) { args ->
            val transport = args[0] as Transport
            transport.on(Transport.EVENT_REQUEST_HEADERS) { args ->
                val headers = args[0] as MutableMap<String, List<String>>
                // modify request headers
                headers["accesstoken"] = mutableListOf(PlaylistApiServiceImpl.token ?: "")
            }
            transport.on(Transport.EVENT_RESPONSE_HEADERS) { args ->
            }
        }
        socket.on(Socket.EVENT_CONNECT) {
            LogUtil.e("连接成功！")
        }.on(MESSAGE_ONLINE_TOTAL) { num ->
            LogUtil.e("当前在线人数：${num[0] ?: 0}")
            realUsersNum = (num[0] ?: 0).toString().toInt()
            EventBus.getDefault().post(SocketOnlineEvent(num = realUsersNum))
        }.on(MESSAGE_ONLINE_USERS) { result ->
            val data = result[0] as JSONArray
            val users = mutableListOf<UserInfo>()
            for (i in 0 until data.length()) {
                val user = UserInfo(
                        id = data.getJSONObject(i).getInt("id"),
                        nickname = data.getJSONObject(i).getString("nickname"),
                        avatar = data.getJSONObject(i).getString("avatar")
                )
                users.add(user)
            }
            saveUserInfo(users)
        }.on(Socket.EVENT_DISCONNECT) {
            LogUtil.e("已断开连接")
        }.on(Socket.EVENT_ERROR) { error ->
            LogUtil.e("连接错误：$error")
        }.on(MESSAGE_BROADCAST) { broadcast ->
            try {
                val message = Gson().fromJson(broadcast[0].toString(), MessageEvent::class.java)
                EventBus.getDefault().post(message)
                receiveSocketMessage(message)
                LogUtil.e("收到消息：${broadcast[0].toString()}")
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        socket.connect()
    }

    /**
     * 断开链接
     */
    private fun disconnect() {
        socket.disconnect()
        socket.off(EVENT_TRANSPORT)
        socket.off(MESSAGE_BROADCAST)
        socket.off(MESSAGE_ONLINE_TOTAL)
        socket.off(MESSAGE_ONLINE_USERS)
        socket.off(EVENT_ERROR)
        socket.off(EVENT_DISCONNECT)
        saveUserInfo(null)
        realUsersNum = 0
        EventBus.getDefault().post(SocketOnlineEvent(0))
    }

    /**
     * 开关
     */
    fun toggleSocket(open: Boolean) {
        if (open) {
            connect()
        } else {
            disconnect()
        }
    }

    /**
     * 保存用户信息
     */
    private fun saveUserInfo(userInfo: MutableList<UserInfo>?) {
        userInfo?.let {
            ChatActivity.users = it
            EventBus.getDefault().post(ChatUserEvent(it))
        }
        if (userInfo == null) {
            EventBus.getDefault().post(ChatUserEvent(mutableListOf()))
        }
    }
}
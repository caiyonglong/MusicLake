package com.cyl.musiclake.socket

import com.cyl.musicapi.playlist.UserInfo
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
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
import io.socket.client.Socket
import io.socket.client.Socket.EVENT_DISCONNECT
import io.socket.client.Socket.EVENT_ERROR
import io.socket.engineio.client.Socket.EVENT_TRANSPORT
import io.socket.engineio.client.Transport
import io.socket.engineio.client.transports.WebSocket
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray

/**
 * Des    : 实时在线socket统计
 * Author : master.
 * Date   : 2018/9/10 .
 */
class SocketManager {
    companion object {
        val MESSAGE_BROADCAST = "broadcast"
        val MESSAGE_SHARE = "share"
        val MESSAGE_ONLINE_TOTAL = "online total"
        val MESSAGE_ONLINE_USERS = "online users"
        val MESSAGE_SOME_JOIN = "someone join"
        val MESSAGE_SOME_LEAVE = "someone leave"
    }

    var realUsersNum = 0
    lateinit var socket: Socket
    var socketListeners = mutableListOf<SocketListener>()

    fun initSocket() {
        try {
            val opts = IO.Options()
            opts.forceNew = true
            opts.timeout = 50 * 1000
            opts.transports = arrayOf(WebSocket.NAME)
            socket = IO.socket(Constants.SOCKET_URL, opts)
            buildSocket()
            LogUtil.e("初始化、建立连接！")
        } catch (e: Throwable) {

        }
    }

    /**
     * 发送消息
     */
    fun sendSocketMessage(msg: String, event: String) {
        if (msg.isEmpty()) return
        if (!UserStatus.getLoginStatus()) {
            ToastUtils.show("请登录")
            return
        }
        if (!MusicApp.isOpenSocket) {
            ToastUtils.show(MusicApp.getAppContext().getString(R.string.open_socket_tips))
            return
        }
        socket.emit(event, msg)
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
    private fun buildSocket() {
        socket.io().on(Manager.EVENT_TRANSPORT) { args ->
            val transport = args[0] as Transport
            transport.on(Transport.EVENT_REQUEST_HEADERS) { args ->
                val headers = args[0] as MutableMap<String, List<String>>
                // modify request headers
                LogUtil.e("请求头：" + PlaylistApiServiceImpl.token.toString())
                headers["accesstoken"] = mutableListOf(PlaylistApiServiceImpl.token ?: "")
            }
            transport.on(Transport.EVENT_RESPONSE_HEADERS) {
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
        }.on(Socket.EVENT_RECONNECT) {
            LogUtil.e("重连")
        }.on(Socket.EVENT_DISCONNECT) {
            LogUtil.e("已断开连接")
        }.on(Socket.EVENT_ERROR) { error ->
            LogUtil.e("连接错误：${error[0].toString()}")
            socketListeners.forEach {
                it.onError(error[0].toString())
            }
        }.on(MESSAGE_BROADCAST) { broadcast ->
            try {
                val message = Gson().fromJson(broadcast[0].toString(), MessageEvent::class.java)
                EventBus.getDefault().post(message)
                receiveSocketMessage(message)
                LogUtil.e("收到消息：${broadcast[0]}")
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }.on(MESSAGE_SOME_JOIN) { user ->
            try {
                val userInfo = Gson().fromJson(user[0].toString(), UserInfo::class.java)
                LogUtil.e("上线：${user[0]}")
                socketListeners.forEach {
                    it.onJoinEvent(userInfo)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }.on(MESSAGE_SOME_LEAVE) { user ->
            try {
                val userInfo = Gson().fromJson(user[0].toString(), UserInfo::class.java)
                LogUtil.e("下线：${user[0]}")
                socketListeners.forEach {
                    it.onLeaveEvent(userInfo)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
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
        socket.off(MESSAGE_SOME_JOIN)
        socket.off(MESSAGE_SOME_LEAVE)
        saveUserInfo(null)
        realUsersNum = 0
        EventBus.getDefault().post(SocketOnlineEvent(0))
    }

    /**
     * 开关
     */
    fun toggleSocket(open: Boolean) {
        if (MusicApp.isOpenSocket) {
            if (open) {
                socket.open()
            } else {
                socket.close()
            }
        } else {
            //关闭socket，默认一直打开，直到在设置中关闭socket
            disconnect()
        }
    }

    /**
     * 增加监听
     */
    fun addSocketListener(listener: SocketListener) {
        socketListeners.add(listener)
    }

    /**
     * 移除监听
     */
    fun removeSocketListener(listener: SocketListener) {
        socketListeners.remove(listener)
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
            ChatActivity.users.clear()
            EventBus.getDefault().post(ChatUserEvent(mutableListOf()))
        }
    }
}
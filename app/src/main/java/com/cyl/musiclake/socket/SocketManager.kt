package com.cyl.musiclake.socket

import com.cyl.musicapi.playlist.UserInfo
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.bean.MessageEvent
import com.cyl.musiclake.bean.SocketOnlineEvent
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.ui.chat.ChatActivity
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.engineio.client.Transport
import org.greenrobot.eventbus.EventBus

/**
 * Des    : 实时在线socket统计
 * Author : master.
 * Date   : 2018/9/10 .
 */
class SocketManager {

    val MESSAGE_BROADCAST = "broadcast"
    val MESSAGE_ONLINE_TOTAL = "online total"
    val MESSAGE_ONLINE_USERS = "online users"
    var realTimeUserNum = 0
    lateinit var socket: Socket

    fun initSocket() {
        try {

            val opts = IO.Options()
            opts.forceNew = true
            opts.multiplex = false
//        opts.transports = arrayOf(PollingXHR.NAME)
            socket = IO.socket(Constants.BASE_PLAYER_URL, opts)
            socket.io().on(Manager.EVENT_TRANSPORT) { args ->
                val transport = args[0] as Transport
                transport.on(Transport.EVENT_REQUEST_HEADERS) { args ->
                    val headers = args[0] as MutableMap<String, List<String>>
                    // modify request headers
                    headers["accesstoken"] = mutableListOf(PlaylistApiServiceImpl.token ?: "")
                }
                transport.on(Transport.EVENT_RESPONSE_HEADERS) { args ->
                    //                val headers = args[0] as MutableMap<String, String>
//                 access response headers
//                val cookie = headers["Set-Cookie"]?.get(0)
                }
            }
            socket.on(Socket.EVENT_CONNECT) {
                LogUtil.e("连接成功！")
            }.on(MESSAGE_ONLINE_TOTAL) { num ->
                LogUtil.e("当前在线人数：${num[0].toString()}")
                realTimeUserNum = num[0] as Int
                EventBus.getDefault().post(SocketOnlineEvent(num = realTimeUserNum))
            }.on(MESSAGE_ONLINE_USERS) { num ->
//                val data = Gson().fromJson<MutableList<UserInfo>>(num.toString(), UserInfo::class.java)
//                saveUserInfo(data)
//                LogUtil.e("当前在线信息：${data.size}")
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
            LogUtil.e("初始化、建立连接！")
            socket.connect()
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
     * 保存用户信息
     */
    private fun saveUserInfo(userInfo: MutableList<UserInfo>?) {
        userInfo?.let {
            ChatActivity.users = it
        }
    }

}
package com.cyl.musiclake.socket

import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.bean.LoginSocket
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.utils.LogUtil
import com.github.nkzawa.engineio.client.Transport
import com.github.nkzawa.engineio.client.transports.PollingXHR
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.github.nkzawa.thread.EventThread
import com.google.gson.Gson


/**
 * Des    : 实时在线socket统计
 * Author : master.
 * Date   : 2018/9/10 .
 */
class SocketManager {

    lateinit var socket: Socket

    fun initSocket() {
        val opts = IO.Options()
        opts.transports = arrayOf(PollingXHR.NAME)
        socket = IO.socket(Constants.BASE_PLAYER_URL,opts)
//        socket.on(PollingXHR.Request.EVENT_REQUEST_HEADERS) { args ->
//            socket.emit(Transport.EVENT_REQUEST_HEADERS, args[0])
//        }.on(PollingXHR.Request.EVENT_RESPONSE_HEADERS) { args ->
//            EventThread.exec {
//                socket.emit(Transport.EVENT_RESPONSE_HEADERS, args[0])
//            }
//        }
        socket.on(Socket.EVENT_CONNECT) {
            socket.emit(Transport.EVENT_REQUEST_HEADERS, Gson().toJson(LoginSocket(PlaylistApiServiceImpl.token)))
            socket.emit("broadcast", "test")
            LogUtil.e("连接成功！")
            LogUtil.e("broadcast：test")
        }.on("online total") { num ->
            LogUtil.e("当前在线人数：$num")
        }.on(Socket.EVENT_DISCONNECT) {
            LogUtil.e("已断开连接")
        }.on(Socket.EVENT_ERROR) { error ->
            LogUtil.e("连接错误：$error")
        }
        LogUtil.e("初始化、建立连接！")
        socket.connect()
    }

    fun connectSocket() {
        socket.connect()

    }

}
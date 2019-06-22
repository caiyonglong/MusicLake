package com.cyl.musicapi

import android.util.Base64
import android.util.Log
import com.cyl.musicapi.dsbridge.CompletionHandler
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

//import wendu.dsbridge.CompletionHandler


/**
 * Created by master on 2018/5/12.
 * Android 调用JS接口，返回的请求数据
 */

object AjaxHandler {
    val TAG = "AjaxHandler"
    fun onAjaxRequest(requestData: JSONObject, handler: CompletionHandler<String>) {
        Log.d(TAG, "onAjaxRequest-----$requestData")
        // 定义响应数据结构
        val responseData = HashMap<String, Any?>()
        responseData["statusCode"] = 0

        try {
            //获取超时时间
            val timeout = requestData.getInt("timeout")
            //新建okHttp请求
            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(timeout.toLong(), TimeUnit.MILLISECONDS)
                    .build()

            // 判断是否需要加密返回结果，并且当responseType是数据流的时候加密
            var contentType = ""
            var encode = false
            val responseType = requestData.optString("responseType", null)
            if (responseType != null && responseType == "stream") {
                encode = true
            }

            val rb = Request.Builder()
            rb.url(requestData.getString("url"))
            val headers = requestData.getJSONObject("headers")

            // 设置请求头
            val iterator = headers.keys()
            while (iterator.hasNext()) {
                val key = iterator.next() as String
                val value = headers.getString(key)
                val lKey = key.toLowerCase()
                if (lKey == "cookie") {
                    // Here you can use CookieJar to manage cookie in a unified way with you native code.
                    //                    CookieManager cookieManager = CookieManager.getDefault().get(requestData.getString("url"), value)
                }
                if (lKey.toLowerCase() == "content-type") {
                    contentType = value
                }
                rb.header(key, value)
            }

            // 新建POST的请求体
            if (requestData.getString("method") == "POST") {
                var data = ""
                if (requestData.getString("body") != null) {
                    data = requestData.getString("body")
                }
                val requestBody = RequestBody
                        .create(MediaType.parse(contentType), data)
                rb.post(requestBody)
            }
            // 创建并发送HTTP请求
            val call = okHttpClient.newCall(rb.build())
            val finalEncode = encode
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    //失败返回
                    responseData["responseText"] = e.message
                    Log.e(TAG, "onFailure:----$responseData")
                    handler.complete(JSONObject(responseData).toString())
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    //成功返回


                    val data: String = if (finalEncode) {
                        Base64.encodeToString(response.body()!!.bytes(), Base64.DEFAULT)
                    } else {
                        response.body()!!.string()
                    }
                    Log.d(TAG, "onResponse:----$responseData")

                    //如果需要编码，结果将由Base64编码并返回 If encoding is needed, the result is encoded by Base64 and returned
                    Log.e("TAG", "onResponse:-----$data")
                    responseData["responseText"] = data
                    responseData["statusCode"] = response.code()
                    responseData["statusMessage"] = response.message()
                    val responseHeaders = response.headers().toMultimap()
                    responseData["headers"] = responseHeaders

                    Log.d(TAG, "onResponse:-----$responseData")
                    handler.complete(JSONObject(responseData).toString())
                }
            })

        } catch (e: Exception) {
            Log.e("TAG", "抛出异常-----" + e.message)
            responseData["responseText"] = "音乐接口异常,${e.message}"
            handler.complete(JSONObject(responseData).toString())
        }

    }
}

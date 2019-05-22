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
 */

object AjaxHandler {
    fun onAjaxRequest(requestData: JSONObject, handler: CompletionHandler<String>) {
        Log.e("TAG", "-----$requestData")
        // Define response structure
        val responseData = HashMap<String, Any?>()
        responseData["statusCode"] = 0

        try {
            val timeout = requestData.getInt("timeout")
            // Create a okhttp instance and set timeout
            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(timeout.toLong(), TimeUnit.MILLISECONDS)
                    .build()

            // Determine whether you need to encode the response result.
            // And encode when responseType is stream.
            var contentType = ""
            var encode = false
            val responseType = requestData.optString("responseType", null)
            if (responseType != null && responseType == "stream") {
                encode = true
            }

            val rb = Request.Builder()
            rb.url(requestData.getString("url"))
            val headers = requestData.getJSONObject("headers")

            // Set request headers
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

            // Create request body
            if (requestData.getString("method") == "POST") {
                var data = ""
                if (requestData.getString("body") != null) {
                    data = requestData.getString("body")
                }
                val requestBody = RequestBody
                        .create(MediaType.parse(contentType), data)
                rb.post(requestBody)
            }
            // Create and send HTTP requests
            val call = okHttpClient.newCall(rb.build())
            val finalEncode = encode
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    responseData["responseText"] = e.message
                    handler.complete(JSONObject(responseData).toString())
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val data: String = if (finalEncode) {
                        Base64.encodeToString(response.body()!!.bytes(), Base64.DEFAULT)
                    } else {
                        response.body()!!.string()
                    }
                    // If encoding is needed, the result is encoded by Base64 and returned
                    Log.e("TAG", "-----$data")
                    responseData["responseText"] = data
                    responseData["statusCode"] = response.code()
                    responseData["statusMessage"] = response.message()
                    val responseHeaders = response.headers().toMultimap()
                    responseData["headers"] = responseHeaders

                    Log.e("TAG", "-----$responseData")
                    handler.complete(JSONObject(responseData).toString())
                }
            })

        } catch (e: Exception) {
            Log.e("TAG", "-----" + e.message)
            responseData["responseText"] = e.message
            handler.complete(JSONObject(responseData).toString())
        }

    }
}

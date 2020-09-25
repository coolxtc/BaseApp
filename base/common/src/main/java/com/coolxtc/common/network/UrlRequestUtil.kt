package com.coolxtc.common.network

import com.coolxtc.common.util.LogUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * Desc:
 * 最基本简单的请求封装
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
object UrlRequestUtil {
    fun requestUrl() {
        val url = "http://wwww.baidu.com"
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                LogUtils.i("hhh---,onFailure")
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                LogUtils.i("hhh---,onResponse: " + response.body()?.string())
            }
        })
    }
}
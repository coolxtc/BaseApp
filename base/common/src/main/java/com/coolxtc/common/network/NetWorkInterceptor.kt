package com.coolxtc.common.network

import android.text.TextUtils
import com.coolxtc.common.network.cache.CacheBean
import com.coolxtc.common.App
import com.coolxtc.common.Constant
import com.coolxtc.common.UserInfoModel
import com.coolxtc.common.network.cache.CacheManager
import com.coolxtc.common.util.LogUtils
import com.coolxtc.common.util.Md5Util
import okhttp3.*
import okio.Buffer
import java.net.URLEncoder
import java.util.*

/**
 * Desc:
 * 网络请求拦截器，为网络请求添加底层的公用参数及加密、
 * 里面实现了 cache 相关业务逻辑
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
class NetWorkInterceptor @JvmOverloads constructor(private val showResponse: Boolean = true) : Interceptor {
    private val tag = "NetWorkInterceptor:"
    //缓存相关
    private var mCacheManager = CacheManager(App.instance)
    private var cacheKey = ""

    override fun intercept(chain: Interceptor.Chain): Response? {
        LogUtils.i("$tag===intercept")
        val request = chain.request()
        cacheKey = request.url().toString()
        logForRequest(request)
        val newRequest = addParams(request)
        if (NetworkUtil.isNetworkAvailable(App.instance)) {
            if (newRequest.method() == "GET") {
                val response = chain.proceed(newRequest)
                val contentType = response.body()?.contentType()
                val body = response.body()?.string() ?: ""
                val cacheBean = CacheBean(System.currentTimeMillis(), body)
                LogUtils.i("$tag===cacheBean = $cacheBean")
                mCacheManager.writeCache(cacheKey, cacheBean)
                val newResponse = response.newBuilder()
                        .body(ResponseBody.create(contentType, body))
                        .build()
                return logForResponse(newResponse)
            }
            return logForResponse(chain.proceed(newRequest))
        } else {
            var cacheContent = mCacheManager.getCache(cacheKey).content
            cacheContent = if (!TextUtils.isEmpty(cacheContent) && cacheContent.endsWith("}")) {
                cacheContent.substring(0, cacheContent.length - 1) + ",\"isCache\":true}"
            } else {
                ""
            }
            LogUtils.i("$tag cacheContent = $cacheContent")
            val response = Response.Builder()
                    .body(ResponseBody.create(MediaType.parse("application/json;charset=UTF-8"), cacheContent))
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(404)
                    .message("cache")
                    .build()
            return logForResponse(response)
        }
    }

    private fun addParams(oldRequest: Request): Request {
        var url = oldRequest.url().newBuilder()
                .addQueryParameter("appId", Constant.ApiConfig.APP_ID)
                .addQueryParameter("apptype", Constant.ApiConfig.APP_TYPE)
                .addQueryParameter("deviceId", UserInfoModel.getDeviceId())
                .addQueryParameter("channel", Constant.CHANNEL)
                .addQueryParameter("time", System.currentTimeMillis().toString())
                .build()
        val newRequest = oldRequest.newBuilder()
                .header("Authorization", "Bearer ")
                .url(url)
                .build()
        // Log.i("Header","Bearer "+ UserInfoModel.getToken())
        val hash = getHash(newRequest)
        url = newRequest.url().newBuilder().addQueryParameter("hash", hash).build()
        LogUtils.i("$tag===request:$url")
        return newRequest.newBuilder().url(url).build()
    }

    private fun logForResponse(response: Response): Response {
        try {
            //===>response log
            LogUtils.e("$tag===response'log===start")
            val builder = response.newBuilder()
            val clone = builder.build()
            LogUtils.e("$tag===url : " + clone.request().url())
            LogUtils.e("$tag===code : " + clone.code())
            LogUtils.e("$tag===protocol : " + clone.protocol())
            if (!TextUtils.isEmpty(clone.message())) {
                LogUtils.e("$tag===message : " + clone.message())
            }

            if (showResponse) {
                var body = clone.body()
                if (body != null) {
                    val mediaType = body.contentType()
                    if (mediaType != null) {
                        LogUtils.e("$tag===responseBody's contentType : $mediaType")
                        if (isText(mediaType)) {
                            val resp = body.string()
                            LogUtils.e("$tag===responseBody's content : $resp")
                            body = ResponseBody.create(mediaType, resp)
                            return response.newBuilder().body(body).build()
                        } else {
                            LogUtils.e("$tag===responseBody's content :  maybe [file part] , too large too print , ignored!")
                        }
                    }
                }
            }
            LogUtils.e("$tag===response'log===end")
        } catch (ignored: Exception) {

        }

        return response
    }

    private fun logForRequest(request: Request) {
        try {
            val url = request.url().toString()
            val headers = request.headers()
            LogUtils.e("$tag===request'log===start")
            LogUtils.e("$tag===method : " + request.method())
            LogUtils.e("$tag===url : $url")
            if (headers.size() > 0) {
                LogUtils.e("$tag===headers : $headers")
            }
            val requestBody = request.body()
            if (requestBody != null) {
                val mediaType = requestBody.contentType()
                if (mediaType != null) {
                    LogUtils.e("$tag===requestBody's contentType : $mediaType")
                    if (isText(mediaType)) {
                        LogUtils.e("$tag===requestBody's content : " + bodyToString(request))
                    } else {
                        LogUtils.e("$tag===requestBody's content :  maybe [file part] , too large too print , ignored!")
                    }
                }
            }
            LogUtils.e("$tag===request'log===end")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun isText(mediaType: MediaType): Boolean {
        if (mediaType.type() == "text") {
            return true
        }
        if (mediaType.subtype() == "json" ||
                mediaType.subtype() == "xml" ||
                mediaType.subtype() == "html" ||
                mediaType.subtype() == "webviewhtml") {
            return true
        }
        return false
    }

    private fun bodyToString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            "something error when show requestBody."
        }
    }

    private fun getHash(newRequest: Request): String {
        var hash = Constant.ApiConfig.APP_KEY +
                Constant.ApiConfig.API_VERSION +
                newRequest.url().encodedPath().substringAfter(Constant.ApiConfig.API_VERSION + "/")
        for (queryName in newRequest.url().queryParameterNames()) {
            hash += URLEncoder.encode(newRequest.url().queryParameterValues(queryName)[0], "UTF-8")
        }
        LogUtils.i(tag + "hash = $hash")
        return Md5Util.getMD5(hash).toLowerCase(Locale.ROOT)
    }
}

package com.coolxtc.common

/**
 * Created by xtc on 2020/3/23.
 */
object Constant {
    const val UMENG_ID = "5e96c5630cafb22d0c000023"
    const val APK_CACHE_PATH = "/apk/"
    const val APK_CRASH_PATH = "/crash/"
    const val PUSH_URL_STR = "Push_Url"
    const val URL = "url"

    //其他apk 比如第三方apk
    const val OTHER_APK_CACHE_PATH = "/other_apk/"
    var CHANNEL = "default"

    object ApiConfig {
        const val PAGE_LIMIT = 20
        const val APP_VERSION = "v1.3.0"
        const val API_VERSION = "v1.3.0"
        const val APP_ID = "001"
        const val APP_TYPE = "base"
        const val APP_KEY = "9278aeb6e5f5405c83ccf6144469cf19"
        const val BASE_URL = "https://api.xiaotemai.com/api/mina"
    }
}
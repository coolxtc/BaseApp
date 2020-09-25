package com.coolxtc.common.util

/**
 * Desc:
 *
 * @author xtc
 * @date 2020/9/11
 * @email qsawer888@126.com
 */
object UrlUtil {
    class UrlEntity {
        /**
         * 基础url
         */
        var baseUrl = ""

        /**
         * url参数
         */
        var params: MutableMap<String, String> = mutableMapOf()

        override fun toString(): String {
            return "UrlEntity(baseUrl='$baseUrl', params=$params)"
        }
    }

    /**
     * 解析url
     *
     * @param url
     * @return
     */
    fun parse(url: String): UrlEntity {
        val entity = UrlEntity()
        val mUrl = url.trim { it <= ' ' }
        if (mUrl == "") {
            return entity
        }
        val urlParts = mUrl.split("\\?".toRegex()).toTypedArray()
        entity.baseUrl = urlParts[0]
        //没有参数
        if (urlParts.size == 1) {
            return entity
        }
        //有参数
        val params = urlParts[1].split("&".toRegex()).toTypedArray()
        entity.params = HashMap()
        for (param in params) {
            val keyValue = param.split("=".toRegex()).toTypedArray()
            entity.params[keyValue[0]] = keyValue[1]
        }
        return entity
    }
}
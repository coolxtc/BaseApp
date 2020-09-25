package com.coolxtc.common.network.cache

/**
 * 描 述:
 *
 * @author: lihui
 * @date: 2017-12-08 16:47
 */

class CacheBean(var time: Long = -1,
                var content: String = ""){
    override fun toString(): String {
        return "CacheBean(time=$time, content='$content')"
    }
}

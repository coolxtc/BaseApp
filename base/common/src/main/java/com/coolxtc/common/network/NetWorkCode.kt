package com.coolxtc.common.network

/**
 * Desc:
 * 网络请求码
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
object NetWorkCode {


    /**
     * 成功
     */
    const val SUCCESS = 0

    /**
     *  找不到用户信息
     */
    const val TOKEN_ERROR = 551

    /**
     *  缓存101
     */
    const val NETWORK_CACHE = 101

    /**
     *  网络错误404
     */
    const val NETWORK_ERR = 404

    /**
     *  网络错误toast
     */
    const val NETWORK_ERR_TOAST = "没有网络，请检查你的网络设置"

    /**
     *  低网速toast
     */
    const val NETWORK_LOW_SPEED = "网络速度似乎不稳定哦\n请检查下手机的网络连接情况~"
}
package com.coolxtc.common.network

/**
 * Desc:
 * 网络 Loading 转圈接口
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
interface NetWorkProgressView {

    fun showDialog(msg: String = "")

    fun dismissDialog()
}
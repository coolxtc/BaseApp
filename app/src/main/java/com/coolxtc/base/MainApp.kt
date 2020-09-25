package com.coolxtc.base

import com.coolxtc.common.App
import com.coolxtc.common.util.ToastUtil
import com.meituan.android.walle.WalleChannelReader


/**
 * Desc:
 *
 * @author xtc
 * @date 2020/9/16
 * @email qsawer888@126.com
 */
class MainApp : App() {
    override fun initChannel(): String {
        val channel = WalleChannelReader.getChannel(this.applicationContext)
        ToastUtil.toast(channel ?: "default")
        return channel ?: "default"
    }
}
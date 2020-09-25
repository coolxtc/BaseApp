package com.coolxtc.dev

import com.coolxtc.common.App

/**
 * Desc:
 * 模块化启动时的App类
 *
 * @author xtc
 * @date 2020/9/16
 * @email qsawer888@126.com
 */
class DevApp : App() {
    override fun initChannel(): String {
        return "dev"
    }
}
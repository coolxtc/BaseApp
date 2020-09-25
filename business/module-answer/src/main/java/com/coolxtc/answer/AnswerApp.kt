package com.coolxtc.answer

import com.coolxtc.common.App

/**
 * Desc:
 * 模块化启动时的App类
 *
 * @author xtc
 * @date 2020/9/16
 * @email qsawer888@126.com
 */
class AnswerApp : App() {
    override fun initChannel(): String {
        return "answer"
    }
}
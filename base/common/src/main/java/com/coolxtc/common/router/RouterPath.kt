package com.coolxtc.common.router

/**
 * Desc:
 * 所有页面列表。注意，每个模块下的页面分组必须保持一致，不然会匹配不到页面
 *
 * @author xtc
 * @date 2020/9/11
 * @email qsawer888@126.com
 */
object RouterPath {
    const val NONE = ""
    const val WEB_ACTIVITY = "/base/web"
    const val WELCOME_ACTIVITY = "/main/welcome"
    const val MAIN_ACTIVITY = "/main/main"
    const val ANSWER_FRAGMENT = "/answer/answer"
    const val ANSWER_ACTIVITY = "/answer/main"
    const val INDEX_FRAGMENT = "/index/index"
    const val INDEX_ACTIVITY = "/index/main"
    const val DEV_FRAGMENT = "/dev/dev"
    const val DEV_ACTIVITY = "/dev/main"
}
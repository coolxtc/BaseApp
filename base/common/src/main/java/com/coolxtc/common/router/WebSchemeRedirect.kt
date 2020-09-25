package com.coolxtc.common.router

import android.os.Bundle
import android.text.TextUtils
import com.coolxtc.common.Constant
import com.coolxtc.common.NoProguard
import com.coolxtc.common.UserInfoModel
import com.coolxtc.common.util.LogUtils
import com.coolxtc.common.util.ToastUtil
import com.coolxtc.common.util.UrlUtil


/**
 * 自定义webview scheme,
 *
 * @author ligao
 */
object WebSchemeRedirect : NoProguard {

    /**
     * 拦截链接。
     *
     * @param context
     * @param schemeStr 协议地址
     * @param handleAll 是否拦截所有的链接
     * @return 如果成功实行了拦截，则返回true。
     */
    fun handleWebClick(schemeStr: String?, handleAll: Boolean = true, params: Bundle? = null): Boolean {
        if (TextUtils.isEmpty(UserInfoModel.getDeviceId())) {
            //没有deviceId或者跳转链接为空，则需要从闪屏页面开始。
            Router.Main.jumpWelcome("")
            return true
        }
        if (schemeStr?.isEmpty() != false) {
            return false
        }
        var handled = true
        LogUtils.i("hhh---,handleWebClick schemeStr = $schemeStr")
        LogUtils.i("hhh---,handleWebClick uri = ${UrlUtil.parse(schemeStr)}")
        val baseUrl = UrlUtil.parse(schemeStr).baseUrl
        val define = baseUrl.replace(Constant.ApiConfig.APP_TYPE + ":/", "")
        when (define) {
            RouterPath.MAIN_ACTIVITY -> {
                Router.Main.jumpMain()
            }
            RouterPath.INDEX_FRAGMENT -> {
                Router.Main.jumpMain(page = RouterPath.INDEX_FRAGMENT)
            }
            RouterPath.ANSWER_FRAGMENT -> {
                Router.Main.jumpMain(page = RouterPath.ANSWER_FRAGMENT)
            }
            RouterPath.DEV_FRAGMENT -> {
                Router.Main.jumpMain(page = RouterPath.DEV_FRAGMENT)
            }
            else -> if (handleAll) {
                if (baseUrl.startsWith(Constant.ApiConfig.APP_TYPE)) {
                    ToastUtil.toast("请安装新版后重试")
                    return true
                }
                Router.Main.jumpWeb(schemeStr)
            } else {
                handled = false
            }
        }
        return handled
    }
}

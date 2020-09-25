package com.coolxtc.common

import android.app.Application
import android.view.Gravity
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.module.LoadMoreModuleConfig
import com.coolxtc.common.coustomview.MyToastStyle
import com.coolxtc.common.coustomview.refreshLayout.CustomLoadMoreView
import com.coolxtc.common.network.NetWorkHttp
import com.coolxtc.common.util.ApkUtil
import com.coolxtc.common.util.CrashHandler
import com.coolxtc.common.util.LogUtils
import com.hjq.toast.ToastUtils

/**
 * Desc:
 * App
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
abstract class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        NetWorkHttp.init()
        initRouter()
        initLog()
        initCrash()
        initToast()
        LoadMoreModuleConfig.defLoadMoreView = CustomLoadMoreView()
        Constant.CHANNEL = initChannel()
    }

    private fun initToast() {
        ToastUtils.init(this)
        ToastUtils.initStyle(MyToastStyle())
        if (ToastUtils.getToast().view is TextView) {
            (ToastUtils.getToast().view as TextView).gravity = Gravity.CENTER_HORIZONTAL
        }
    }

    private fun initCrash() {
        val crashHandler: CrashHandler = CrashHandler.getInstance()
        crashHandler.init(this)
    }

    private fun initLog() {
        LogUtils.setLogEnable(ApkUtil.isApkDebug(this))
    }

    abstract fun initChannel(): String

    private fun initRouter() {
        if (ApkUtil.isApkDebug(this)) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }
}
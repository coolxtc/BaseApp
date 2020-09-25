package com.coolxtc.common.util

import android.app.Activity
import com.coolxtc.common.activity.BaseActivity
import com.coolxtc.common.network.NetWorkHttp
import java.util.*

object ActivityUtil {
    private val activityStack: Stack<Activity> = Stack()

    /**
     * add Activity 添加Activity到栈
     */
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    fun getLastActivity(): BaseActivity? {
        val size = activityStack.size
        return if (activityStack.size > 0) {
            activityStack[(size) - 1] as BaseActivity
        } else {
            null
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            removeActivityFromStack(activity)
            activity.finish()
        }
    }

    fun removeActivityFromStack(activity: Activity?) {
        if (activity != null) {
            activityStack.remove(activity)
        }
    }

    fun getActivityCount(): Int {
        return activityStack.size
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        NetWorkHttp.cancelAllCal()
        var i = 0
        val size = activityStack.size
        while (i < size) {
            if (null != activityStack[i]) {
                activityStack[i].finish()
            }
            i++
        }
        activityStack.clear()
    }

    /**
     * 退出应用程序
     */
    fun appExit() {
        finishAllActivity()
    }

    fun killApp() {
        try {
            //杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
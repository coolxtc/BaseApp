package com.coolxtc.common.util

import android.content.Context
import android.content.pm.ApplicationInfo
import java.io.IOException
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

object ApkUtil {
    /**
     * 从apk中获取版本信息
     *
     * @param context
     * @param channelKey
     * @return
     */
    fun getChannelFromApk(context: Context, channelKey: String): String { //从apk包中获取
        val appInfo = context.applicationInfo
        val sourceDir = appInfo.sourceDir
        //默认放在meta-inf/里， 所以需要再拼接一下
        val key = "META-INF/$channelKey"
        var ret = ""
        var zipfile: ZipFile? = null
        try {
            zipfile = ZipFile(sourceDir)
            val entries: Enumeration<*> = zipfile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement() as ZipEntry
                val entryName = entry.name
                if (entryName.startsWith(key)) {
                    ret = entryName
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        //
        val split = ret.split("_").toTypedArray()
        //参数结构： key_{channel}
        return if (split.size >= 2) {
            split[1]
        } else {
            "default"
        }
    }


    fun isApkDebug(context: Context): Boolean {
        val info = context.applicationInfo
        return info.flags and ApplicationInfo.FLAG_DEBUGGABLE !== 0
    }
}
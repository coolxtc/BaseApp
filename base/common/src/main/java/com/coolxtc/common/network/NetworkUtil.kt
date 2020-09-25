package com.coolxtc.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.widget.Toast
import com.coolxtc.common.App
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

/**
 * Desc:
 * 网络工具类
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
object NetworkUtil {
    /**
     * Returns whether the network is available
     */
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val manager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (manager == null) {
        } else {
            val info = manager.allNetworkInfo
            if (info != null) {
                var i = 0
                val length = info.size
                while (i < length) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                    i++
                }
            }
        }
        return false
    }

    /**
     * 判断当前网络类型
     *
     * @param context
     * @return 1 代表gprs,2代表wifi
     */
    fun getNetworkType(context: Context): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        if (info == null || !info.isConnected) {
            return "UNCNCT" // not connected
        }
        if (info.type == ConnectivityManager.TYPE_WIFI) {
            return "WIFI"
        }
        if (info.type == ConnectivityManager.TYPE_MOBILE) {
            val networkType = info.subtype
            return when (networkType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_IDEN -> "2G"
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
                TelephonyManager.NETWORK_TYPE_LTE -> "4G"
                else -> "UNKNOWN"
            }
        }
        return "UNKNOWN"
    }

    /**
     * 判断移动网络是否连接
     *
     * @param context
     * @return
     * @throws Exception
     */
    fun isMobileDataEnable(context: Context): Boolean {
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isMobileDataEnable = false
        isMobileDataEnable = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE)!!.isConnectedOrConnecting
        return isMobileDataEnable
    }

    /**
     * 判断wifi是否连接
     *
     * @param context
     * @return
     * @throws Exception
     */
    fun isWifiDataEnable(context: Context): Boolean {
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isWifiDataEnable = false
        isWifiDataEnable = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI)!!.isConnectedOrConnecting
        return isWifiDataEnable
    }

    fun getWIFILocalIpAdress(mContext: Context): String {

        //获取wifi服务
        val wifiManager = mContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        //判断wifi是否开启
        var ip = "0.0.0.0"
        ip = if (!wifiManager.isWifiEnabled) {
            gPRSLocalIpAddress
        } else {
            val wifiInfo = wifiManager.connectionInfo
            val ipAddress = wifiInfo.ipAddress
            formatIpAddress(ipAddress)
        }
        return ip
    }

    val gPRSLocalIpAddress: String
        get() {
            try {
                val en = NetworkInterface
                        .getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val intf = en.nextElement()
                    val enumIpAddr = intf.inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                            return inetAddress.getHostAddress().toString()
                        }
                    }
                }
            } catch (ex: SocketException) {
            }
            return ""
        }

    private fun formatIpAddress(ipAdress: Int): String {
        return (ipAdress and 0xFF).toString() + "." +
                (ipAdress shr 8 and 0xFF) + "." +
                (ipAdress shr 16 and 0xFF) + "." +
                (ipAdress shr 24 and 0xFF)
    }

    fun getPrintSize(size: Long): String {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        var size = size
        size = if (size < 1024) {
            return size.toString() + "B"
        } else {
            size / 1024
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        size = if (size < 1024) {
            return size.toString() + "KB"
        } else {
            size / 1024
        }
        return if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100
            ((size / 100).toString() + "."
                    + (size % 100).toString() + "MB")
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024
            ((size / 100).toString() + "."
                    + (size % 100).toString() + "GB")
        }
    }

    private var count = 0
    private var lastSysTime = 0L
    //检测连接时间
    fun checkConnectTime(connectTime: Long) {
        if (connectTime >= 1.2 * 1000) {
            if (System.currentTimeMillis() - lastSysTime <= 60 * 1000) {
                count++
                if (count >= 3) {
                    //如果上次触发事件和当前时间小于一分钟，并连续触发3次，则弹toast
                    Toast.makeText(App.instance, NetWorkCode.NETWORK_LOW_SPEED, Toast.LENGTH_SHORT).show()
                    count = 0
                }
            } else {
                count = 0
            }
            lastSysTime = System.currentTimeMillis()
        } else {
            count = 0
        }
    }
}
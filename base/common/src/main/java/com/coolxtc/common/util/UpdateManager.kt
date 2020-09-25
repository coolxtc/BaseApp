package com.coolxtc.common.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.*
import androidx.core.content.FileProvider
import com.coolxtc.common.App
import com.coolxtc.common.Constant
import com.coolxtc.common.R
import com.coolxtc.common.UserInfoModel
import com.coolxtc.common.activity.BaseActivity
import com.coolxtc.common.activity.BlankActivity
import com.coolxtc.common.bean.ret.UpdateRet
import com.coolxtc.common.network.BaseNetWorkService
import com.coolxtc.common.network.NetWorkHttp
import com.coolxtc.common.network.NetWorkObserver
import com.coolxtc.common.network.NetWorkResponse
import com.coolxtc.common.network.fileload.FileDownloadCallback
import com.coolxtc.common.network.fileload.HttpRequest
import com.coolxtc.common.router.RouterPath
import java.io.File
import java.security.NoSuchAlgorithmException
import java.util.*


/**
 * 描 述:
 * 客户端升级组件
 *
 * @author: lihui
 * @date: 2016-03-16 09:57
 */
class UpdateManager(private val activity: BaseActivity) {
    //通知栏进度条
    private var mNotificationManager: NotificationManager? = null
    private var mNotification: Notification? = null
    private var loadNum: Int = 0
    private var timer: Timer? = null
    private var updateDialog: UpdateDialog? = null

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            // 定义一个Handler，用于处理下载线程与UI间通讯
            when (msg.what) {
                1 -> {
                    LogUtils.e("" + loadNum)
                    if (loadNum > 0) {
                        mNotification!!.tickerText = "正在下载"
                    }
                    mNotification!!.contentView.setTextViewText(R.id.content_view_text1, "下载进度 $loadNum%")
                    mNotification!!.contentView.setProgressBar(R.id.content_view_progress, 100, loadNum, false)
                    mNotificationManager!!.notify(10, mNotification)
                }
                2 -> {
                    LogUtils.e("" + loadNum)
                    if (updateDialog != null) {
                        updateDialog!!.setTextViewText("下载进度 $loadNum%")
                        updateDialog!!.setProgressBar(loadNum)
                        updateDialog!!.show()
                    }
                }
                else -> {
                }
            }
        }
    }

    private val apkPath: String
        get() {
            return try {
                App.instance.getExternalFilesDir(null)!!.absolutePath + Constant.APK_CACHE_PATH
            } catch (e: Exception) {
                e.printStackTrace()
                App.instance.filesDir.absolutePath + Constant.APK_CACHE_PATH
            }
        }

    private fun notificationInit() {
        //通知栏内显示下载进度条
        val intent = Intent(activity, BlankActivity::class.java)//点击进度条，进入程序
        val bundle = Bundle()
        bundle.putString(Constant.PUSH_URL_STR, RouterPath.MAIN_ACTIVITY)
        val pIntent = PendingIntent.getActivity(activity, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mNotificationManager = activity.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager
        mNotification = Notification()
        mNotification!!.icon = R.mipmap.ic_launcher
        mNotification!!.tickerText = "开始下载"
        mNotification!!.contentView = RemoteViews(activity.packageName, R.layout.upload)//通知栏中进度布局
        mNotification!!.contentIntent = pIntent
        mNotificationManager!!.notify(10, mNotification)
    }

    /**
     * 强制升级
     */
    fun updateForce(forceVersion_: String, forceContent_: String, forceUrl_: String) {
        //升级apk下载地址
        val url = StrUtil.null2Str(forceUrl_)
        //强制更新内容
        val forceContent = StrUtil.null2Str(forceContent_)
        //强制更新最低要求版本
        val fv = StrUtil.null2Str(forceVersion_)
        //优先判断是否需要强制更新
        if (hasUpdate(fv)) {
            var filename = ""
            try {
                filename = Md5Util.getMD5(url) + ".apk"
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            val filePath = apkPath + filename
            LogUtils.e(filePath)
            //本地目标文件
            val file = File(filePath)
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            if ((activity as Activity).isFinishing) {
                return
            }
            val dialog = DialogUtil.createMineUpdateAppDialog(activity,
                    "发现新版本", forceVersion_, forceContent, "立即更新")
            dialog.setCancelable(false)
            dialog.okButton!!.setOnClickListener { v ->
                if (file.exists() && FileUtil.isUninatllApkInfo(v.context, file.path)) {
                    //直接安装
                    openFile(activity, file)
                } else {
                    //
                    updateDialog = UpdateDialog(activity, R.style.CustomConfirmDialog)
                    updateDialog!!.onCreate(null)
                    updateDialog?.setCancelable(false)
                    val time = System.currentTimeMillis()
                    //下载apk
                    HttpRequest.download(url, file, object : FileDownloadCallback() {
                        override fun onStart() {
                            super.onStart()
                            LogUtils.i("start")
                            timer = Timer()
                            timer!!.schedule(object : TimerTask() {
                                override fun run() {
                                    sendMsg(2)
                                }
                            }, 0, 500)
                        }

                        override fun onProgress(progress: Int, networkSpeed: Long) {
                            super.onProgress(progress, networkSpeed)
                            loadNum = progress
                        }

                        override fun onFailure() {
                            super.onFailure()
                            LogUtils.e("onFailure")
                            timer!!.cancel()
                        }

                        override fun onDone() {
                            super.onDone()
                            LogUtils.i("onDone")
                            timer!!.cancel()
                            if (updateDialog != null && updateDialog!!.isShowing) {
                                updateDialog!!.dismiss()
                            }
                            openFile(activity, file)
                        }
                    })
                }
                dialog.dismiss()
            }
            dialog.otherButton!!.setOnClickListener { ActivityUtil.appExit() }
            dialog.show()
        }
    }

    interface dialogListener {
        fun dismiss()
    }

    /**
     * 非强制更新
     *
     * @param forceCheck 是否强制重新调用检测接口
     */
    @Throws(Exception::class)
    fun update(forceCheck: Boolean, showDialog: Boolean, listener: dialogListener?) {
        if (!forceCheck) {
            val versionCache = UserInfoModel.getNewVersion()
            if (!hasUpdate(versionCache)) {
                //如果不需要更新应用，就清除以前的老版本apk文件
                val filePath = apkPath
                FileUtil.deleteAllFiles(filePath)
                if (showDialog) {
                    Toast.makeText(App.instance, "已是最新版本", Toast.LENGTH_SHORT).show()
                }
                listener?.dismiss()
                return
            }
        }
        NetWorkHttp.toSubscribe(NetWorkHttp.getNetWorkService<BaseNetWorkService>().checkVersion(),
                object : NetWorkObserver<UpdateRet>(activity) {
                    override fun success(ret: NetWorkResponse<UpdateRet>) {
                        //升级apk下载地址
                        val url = StrUtil.null2Str(ret.data.downUrl)
                        //升级内容
                        val content = StrUtil.null2Str(ret.data.verMsg)
                        //最新版本
                        val v = StrUtil.null2Str(ret.data.verCode)
                        if (hasUpdate(v)) {//判断是否需要升级
                            var filename = ""
                            try {
                                filename = Md5Util.getMD5(url) + ".apk"
                            } catch (e: NoSuchAlgorithmException) {
                                e.printStackTrace()
                            }
                            val filePath = apkPath + filename
                            val tempPath = apkPath + System.currentTimeMillis() + ".download"
                            LogUtils.e(filePath)
                            //本地目标文件
                            val file = File(filePath)
                            if (!file.parentFile.exists()) {
                                file.parentFile.mkdirs()
                            }
                            if ((activity as Activity).isFinishing) {
                                return
                            }
                            val dialog = DialogUtil.createMineUpdateAppDialog(activity,
                                    "发现新版本", v, content, "立即更新")
                            dialog.okButton!!.setOnClickListener { v ->
                                if (file.exists() && FileUtil.isUninatllApkInfo(v.context, file.path)) {
                                    //直接安装
                                    openFile(activity, file)
                                } else {
                                    //
                                    notificationInit()
                                    val time = System.currentTimeMillis()
                                    //下载apk
                                    val tempFile = File(tempPath)
                                    HttpRequest.download(url, tempFile, object : FileDownloadCallback() {
                                        override fun onStart() {
                                            super.onStart()
                                            LogUtils.i("update start")
                                        }

                                        override fun onProgress(progress: Int, networkSpeed: Long) {
                                            super.onProgress(progress, networkSpeed)
                                            if (progress - loadNum > 20) {
                                                sendMsg(1)
                                                LogUtils.i("update onProgress $progress")
                                                loadNum = progress
                                            }
                                        }

                                        override fun onFailure() {
                                            super.onFailure()
                                            LogUtils.e("update onFailure")
                                            if (tempFile.exists()) {
                                                //失败后删除
                                                tempFile.delete()
                                            }
                                            Toast.makeText(activity, "下载失败,请重试", Toast.LENGTH_SHORT).show()
                                        }

                                        override fun onDone() {
                                            super.onDone()
                                            LogUtils.i("update onDone")
                                            //                                        timer.cancel();
                                            mNotificationManager!!.cancel(10)
                                            val result = tempFile.renameTo(file)
                                            if (result) {
                                                tempFile.delete()
                                                openFile(activity, file)
                                            } else {
                                                tempFile.delete()
                                                Toast.makeText(activity, "下载失败,请重试", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    })
                                }
                                dialog.dismiss()
                            }
                            dialog.setCanceledOnTouchOutside(true)
                            dialog.show()
                            dialog.setOnDismissListener { listener?.dismiss() }
                        } else {
                            //如果不需要更新应用，就清除以前的老版本apk文件
                            val filePath = apkPath
                            FileUtil.deleteAllFiles(filePath)
                            if (showDialog) {
                                Toast.makeText(App.instance, "已是最新版本", Toast.LENGTH_SHORT).show()
                            }
                            listener?.dismiss()
                        }
                    }

                    override fun error(original: NetWorkResponse<UpdateRet>) {
                        listener?.dismiss()
                    }
                }, 0)
    }


    /**
     * apk安装
     *
     * @param context
     * @param file
     */
    private fun openFile(context: Context, file: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            openFileHigh(context, file)
        } else {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive")
            context.startActivity(intent)
        }
    }

    /**
     * APK安装  兼容高版本系统
     *
     * @param context
     * @param file
     */
    private fun openFileHigh(context: Context, file: File) {
        val CAPTURE_IMAGE_FILE_PROVIDER = App.instance.packageName + ".fileprovider"
        val imageUri = FileProvider.getUriForFile(context, CAPTURE_IMAGE_FILE_PROVIDER, file)
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(imageUri,
                "application/vnd.android.package-archive")
        val resInfoList = context.packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        context.startActivity(intent)
    }

    /**
     * 通知栏更新
     *
     * @param flag
     */
    private fun sendMsg(flag: Int) {
        val msg = Message()
        msg.what = flag
        handler.sendMessage(msg)
    }

    internal inner class UpdateDialog : Dialog {

        private var ivIcon: ImageView? = null
        private var text: TextView? = null
        private var progressBar: ProgressBar? = null

        constructor(context: Context) : super(context) {}

        constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

        public override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            this.setContentView(R.layout.update_dialog)
            initUI()
        }

        private fun initUI() {
            ivIcon = findViewById<View>(R.id.content_view_image) as ImageView
            ivIcon!!.setImageBitmap(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            text = findViewById<View>(R.id.content_view_text1) as TextView
            progressBar = findViewById<View>(R.id.content_view_progress) as ProgressBar
            progressBar!!.max = 100
        }

        fun setTextViewText(txt: String) {
            text!!.text = txt
        }

        fun setProgressBar(loadNum: Int) {
            progressBar!!.progress = loadNum
        }
    }

    companion object {


        /**
         * 判断是否需要更新
         *
         * @return
         */
        fun hasUpdate(newVersion: String): Boolean {
            val version: String?
            var tag = false
            version = Constant.ApiConfig.APP_VERSION
            if (version == null) {
                LogUtils.e("version is $version")
                return false
            }
            val verArr = version.toLowerCase().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val newVerArr = newVersion.toLowerCase().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (StrUtil.str2Int(verArr[0].replace("v", "")) < StrUtil.str2Int(newVerArr[0].replace("v", ""))) {
                tag = true
            } else if (StrUtil.str2Int(verArr[0].replace("v", "")) == StrUtil.str2Int(newVerArr[0].replace("v", "")) && StrUtil.str2Int(verArr[1]) < StrUtil.str2Int(newVerArr[1])) {
                tag = true
            } else if (StrUtil.str2Int(verArr[0].replace("v", "")) == StrUtil.str2Int(newVerArr[0].replace("v", ""))
                    && StrUtil.str2Int(verArr[1]) == StrUtil.str2Int(newVerArr[1])
                    && StrUtil.str2Int(verArr[2]) < StrUtil.str2Int(newVerArr[2])) {
                tag = true
            }
            return tag
        }
    }
}

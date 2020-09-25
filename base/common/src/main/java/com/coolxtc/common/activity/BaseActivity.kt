package com.coolxtc.common.activity

import android.content.ComponentCallbacks2
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.coolxtc.common.R
import com.coolxtc.common.network.NetWorkProgressView
import com.coolxtc.common.util.ActivityUtil
import com.coolxtc.common.util.DialogUtil.BaseDialog
import com.coolxtc.common.util.DialogUtil.createProgressDialog
import com.coolxtc.common.util.LogUtils
import com.coolxtc.common.util.ScreenTools
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.loading_layout.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import pub.devrel.easypermissions.PermissionRequest

/**
 * Desc:
 * Activity基类，抽象了基本的 getContentViewResId，initView，initData 函数；
 * 原则上来说基本的页面渲染只需要调用这三个函数即可实现。
 * 调用顺序 getContentViewResId-->initView-->initData
 *
 * @param layoutId 布局 LayoutId
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
abstract class BaseActivity(private val layoutId: Int = -1) : AppCompatActivity(R.layout.activity_base), PermissionCallbacks, NetWorkProgressView {
    private var dialog: BaseDialog? = null
    private var themeColor = R.color.colorPrimary
    private val simpleName = this.javaClass.simpleName.toString()

    /**
     * 初始化View方法
     */
    abstract fun initView()

    /**
     * 初始化Data方法
     */
    abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.i("hhh---,Activity onCreate:$simpleName")
        ARouter.getInstance().inject(this)
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out)
        if (layoutId > 0) {
            layoutInflater.inflate(layoutId, layContainer, true)
        }
        toolbar_back.setOnClickListener {
            onBackPressed()
        }
        setActivityTheme(themeColor, true)
        ActivityUtil.addActivity(this)
        contentInStatusBar(false, isDark = false)
        initView()
        initData()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ARouter.getInstance().inject(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityUtil.removeActivityFromStack(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityUtil.finishActivity(this)
        LogUtils.i("hhh---,Activity onDestroy:$simpleName")
    }

    protected fun finalize() {
        // finalization logic
        LogUtils.i("hhh---,Activity finalize:$simpleName")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        /**
         * OnTrimMemory的参数是一个int数值，代表不同的内存状态：
         * TRIM_MEMORY_COMPLETE：内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
         * TRIM_MEMORY_MODERATE：内存不足，并且该进程在后台进程列表的中部。
         * TRIM_MEMORY_BACKGROUND：内存不足，并且该进程是后台进程。
         * TRIM_MEMORY_UI_HIDDEN：内存不足，并且该进程的UI已经不可见了。
         */
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            System.gc()
        }
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out)
        ActivityUtil.removeActivityFromStack(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> onBackPressed()
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}
    private var permissionListener: PermissionListener? = null
    private var permissions: Array<String> = arrayOf()
    private var permissionInfo: String = ""
    fun checkPermission(info: String, perms: Array<String>, listener: PermissionListener?) {
        permissionInfo = info
        permissionListener = listener
        permissions = perms
        EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, 1188, *perms)
                        .setRationale(info)
                        .setPositiveButtonText("给予")
                        .setNegativeButtonText("取消")
                        .build())
    }

    @AfterPermissionGranted(1188)
    private fun afterPermissionGet() {
        LogUtils.i("hhh---,afterPermissionGet")
        permissionListener?.success()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            LogUtils.i("hhh---,somePermissionPermanentlyDenied:$perms")
            val content = StringBuilder("您已拒绝为保证应用正常运行所需的必要权限:\r\n")
            for (i in perms.indices) {
                //循环输出 值
                for (e in Permission.values()) {
                    if (TextUtils.equals(e.permission, perms[i])) {
                        content.append("\r\n").append(e.desc)
                    }
                }
            }
            content.append("\r\n\r\n").append("请前往设置页手动开启")
            AppSettingsDialog.Builder(this)
                    .setTitle("提示")
                    .setRationale(content.toString())
                    .setPositiveButton("立即前往")
                    .setNegativeButton("取消")
                    .build()
                    .show()
        } else {
            checkPermission(permissionInfo, permissions, permissionListener)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.i("hhh---,onActivityResult:$requestCode")
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            checkPermission(permissionInfo, permissions, permissionListener)
        }
    }

    /**
     * 设置内容布局是否嵌入状态栏，默认不嵌入，如果嵌入，则会自动隐藏toolbar
     * @param isInsert true->内容嵌入 false->内容不嵌入
     * @param isDark true->状态栏字体黑色 false->状态栏字体白色
     */
    fun contentInStatusBar(isInsert: Boolean, isDark: Boolean) {
        hideTitleBar()
        if (isInsert) {
            fake_status_bar.visibility = View.GONE
            StatusBarUtil.setTransparentForImageView(this, null)
        } else {
            fake_status_bar.layoutParams.height = ScreenTools.getStatusHeight(this)
            StatusBarUtil.setTransparentForImageView(this, fake_status_bar)
            StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, themeColor))
        }
        if (isDark) {
            StatusBarUtil.setLightMode(this)
        } else {
            StatusBarUtil.setDarkMode(this)
        }
    }

    /**
     * 如果头部底色不是主色调，就需要在这里设置色值
     * @param colorResource 主题色值(主要是Toolbar颜色)
     * @param isDark 是否是暗色调，如果是暗色调，会使用白色back&title；如果不是，则会使用深色back&title
     */
    fun setActivityTheme(colorResource: Int, isDark: Boolean) {
        themeColor = colorResource
        if (colorResource <= 0) {
            themeColor = R.color.colorPrimary
        }
        if (isDark) {
            toolbar_back.setImageResource(R.mipmap.ic_back_white)
            toolbar_back.setBackgroundResource(R.drawable.drawable_tran_selector)
            toolbar_title.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            toolbar_back.setImageResource(R.mipmap.ic_back_black)
            toolbar_back.setBackgroundResource(R.drawable.drawable_tran_selector)
            toolbar_title.setTextColor(ContextCompat.getColor(this, R.color.c333333))
        }
        toolbar_title.setBackgroundColor(ContextCompat.getColor(this, themeColor))
        fake_status_bar.setBackgroundColor(ContextCompat.getColor(this, themeColor))
    }

    fun hideBack() {
        toolbar_back.visibility = View.GONE
    }

    fun hideTitleBar() {
        toolbar_title.visibility = View.GONE
        toolbar_back.visibility = View.GONE
        line.visibility = View.GONE
    }

    fun setTitle(title: String) {
        toolbar_title.text = title
        toolbar_title.isSelected = true
        toolbar_title.visibility = View.VISIBLE
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, themeColor))
    }

    override fun showDialog(msg: String) {
        if (isFinishing) {
            return
        }
        dialog = createProgressDialog(this, "加载中")
        dialog?.show()
    }

    override fun dismissDialog() {
        if (isFinishing || dialog == null) {
            return
        }
        dialog?.dismiss()
    }

    protected fun startLoading(retryBtListener: View.OnClickListener) {
        loading_frame.startLoading()
        loading_frame.reloadButton.setOnClickListener {
            loading_frame.startLoading()
            retryBtListener.onClick(loading_frame.reloadButton)
        }
    }

    protected fun loadError(code: Int, msg: String) {
        loading_frame.failedLoading(msg, code)
    }

    protected fun loadSuccess() {
        loading_frame.visibility = View.GONE
    }

    interface PermissionListener {
        /**
         * 所有权限请求成功
         */
        fun success()
    }

    interface PermissionEnum {
        val permission: String
        val desc: String
    }

    enum class Permission(override val permission: String, override val desc: String) : PermissionEnum {
        /*权限枚举**/
        LOCATION("android.permission.ACCESS_COARSE_LOCATION", "获取定位信息"),
        FINE_LOCATION("android.permission.ACCESS_FINE_LOCATION", "获取定位信息"),
        EXTRA_STORAGE("android.permission.WRITE_EXTERNAL_STORAGE", "读写手机存储"),
        READ_PHONE_STATE("android.permission.READ_PHONE_STATE", "获取手机信息");
    }
}
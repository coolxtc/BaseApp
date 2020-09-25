package com.coolxtc.base.activity

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.coolxtc.base.R
import com.coolxtc.base.viewmodel.WelcomeModel
import com.coolxtc.common.activity.BaseActivity
import com.coolxtc.common.router.Router
import com.coolxtc.common.router.RouterPath
import com.coolxtc.common.util.ImageUtil
import com.coolxtc.common.util.LogUtils
import com.coolxtc.common.util.UpdateManager
import kotlinx.android.synthetic.main.activity_welcome.*


/**
 * Desc:
 * 开屏页
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
@Route(path = RouterPath.WELCOME_ACTIVITY)
class WelcomeActivity : BaseActivity(R.layout.activity_welcome) {
    private val welcomeModel by lazy { WelcomeModel.getInstant(this) }
    private var timer: CountDownTimer? = null
    private var handler = Handler(Looper.getMainLooper())
    private var adUrl = ""//广告跳转地址

    @Autowired
    @JvmField
    var schemeUrl = ""//协议跳转地址

    override fun initView() {
        contentInStatusBar(isInsert = true, isDark = false)
        tv_ad_count_down.setOnClickListener {
            goHome()
        }
    }

    override fun initData() {
        initModel()
        checkPermission()
    }

    private fun initModel() {
        welcomeModel.countDownTime.observe(this, {
            LogUtils.i("hhh---, viewModel CountDown: $it")
            tv_ad_count_down.text = String.format(getString(R.string.welcome_count_down), it)
        })
        welcomeModel.startRet.observe(this, {
            LogUtils.i("hhh---, viewModel StartRet: $it")
            if (it.success) {
                if (UpdateManager.hasUpdate(it.data.update.lowVerCode)) {
                    UpdateManager(this).updateForce(
                            it.data.update.verCode,
                            it.data.update.verMsg,
                            it.data.update.downUrl
                    )
                } else {
                    showAd()
                }
            } else {
                handler.postDelayed({ welcomeModel.start() }, 3000)
            }
        })
    }

    /**
     * 权限检查
     */
    private fun checkPermission() {
        val perms = arrayOf(
                Permission.READ_PHONE_STATE.permission,
                Permission.EXTRA_STORAGE.permission,
                Permission.LOCATION.permission,
                Permission.FINE_LOCATION.permission)
        checkPermission("请给予App运行必要权限哦~", perms, object : PermissionListener {
            override fun success() {
                welcomeModel.start()
            }
        })
    }

    private fun goHome() {
        timer?.cancel()
        timer = null
        LogUtils.i("hhh---, welcome url = $adUrl")
        if (TextUtils.isEmpty(schemeUrl)) {
            schemeUrl = ""
        }
        Router.Main.jumpMain(adUrl, schemeUrl)
        finish()
    }

    private fun showAd() {
        ImageUtil.loadImgRes(this, R.mipmap.launcher_ad, iv_ad)
        iv_ad.setOnClickListener {
            adUrl = "https://www.baidu.com"
            goHome()
        }
        startCountDown()
    }

    private fun startCountDown() {
        tv_ad_count_down.visibility = View.VISIBLE
        timer = object : CountDownTimer((4 * 1000).toLong(), 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                welcomeModel.countDownTime.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                welcomeModel.countDownTime.value = 0
                goHome()
            }
        }.start()
    }

    override fun onBackPressed() {}

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
        handler.removeCallbacksAndMessages(null)
    }
}
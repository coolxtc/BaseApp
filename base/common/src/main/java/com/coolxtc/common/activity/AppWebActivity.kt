package com.coolxtc.common.activity


import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.net.http.SslError
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.coolxtc.common.App
import com.coolxtc.common.Constant
import com.coolxtc.common.R
import com.coolxtc.common.router.RouterPath
import com.coolxtc.common.router.WebSchemeRedirect
import com.coolxtc.common.util.LogUtils
import kotlinx.android.synthetic.main.app_web_activity.*


/**
 * Desc:
 * 通用WebViewActivity
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
@Route(path = RouterPath.WEB_ACTIVITY)
class AppWebActivity : BaseActivity(R.layout.app_web_activity) {
    @Autowired
    @JvmField
    var url = ""

    companion object {
        val SHARE_JS_INTERFACE = App.instance.resources.getString(R.string.app_name)
    }

    override fun initView() {
        hideTitleBar()
        contentInStatusBar(false, isDark = true)
        web_toolbar_title.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        web_toolbar_close.setOnClickListener {
            finish()
        }
        web_toolbar_refresh.setOnClickListener {
            wvWeb.reload()
            web_loading.visibility = View.VISIBLE
            web_loading.progress = 1
        }
        val setting = wvWeb.settings
        setting.javaScriptEnabled = true
        setting.domStorageEnabled = true
        setting.javaScriptCanOpenWindowsAutomatically = true
        wvWeb.addJavascriptInterface(this, SHARE_JS_INTERFACE)
        wvWeb.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                LogUtils.i("hhh---,shouldOverrideUrlLoading = $url")
                try {
                    if (url?.startsWith("tbopen://") == true) {
                        val uri = Uri.parse(url)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                        return true
                    }
                    //要跳转到微信,支付宝。
                    if (url?.startsWith("weixin://") == true || url?.startsWith("alipays://") == true) {
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        intent.addCategory("android.intent.category.BROWSABLE")
                        intent.component = null
                        startActivity(intent)
                        return true
                    }
                    if (url?.startsWith(Constant.ApiConfig.APP_TYPE + "://") == true) {
                        WebSchemeRedirect.handleWebClick(url, false)
                        return true
                    }
                } catch (e: Exception) {
                    return true
                }
//                val extraHeaders = HashMap<String, String>()
//                extraHeaders["Referer"] = Constant.JZZB_PAY_URL
//                view?.loadUrl(url, extraHeaders)
                return true
            }

            //禁止跳转到外部浏览器
            override fun onPageFinished(view: WebView, url: String) {
                //加载完成
                LogUtils.i(this.toString() + "加载完成 ：$url")
                web_toolbar_title.text = view.title
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }
        }
        val webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress in 1..99) {
                    web_loading.visibility = View.VISIBLE
                    web_loading.progress = newProgress
                } else {
                    web_loading.visibility = View.GONE
                }
            }
        }
        wvWeb.webChromeClient = webChromeClient
    }

    override fun initData() {
        //hideBaseSuspensionPV();
        window.setFormat(PixelFormat.TRANSLUCENT)
        //避免输入法界面弹出后遮挡输入光标的问题
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        if (url.isEmpty()) {
            LogUtils.d("空url")
            finish()
            return
        }
        wvWeb.loadUrl(url)
    }

    private fun destroyWebView() {
        if (wvWeb != null) {
            val parent = wvWeb.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(wvWeb)
            }
            wvWeb.stopLoading()
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            wvWeb.clearView()
            wvWeb.settings.javaScriptEnabled = false
            wvWeb.webChromeClient = null
//            wvWeb.webViewClient = null
            wvWeb.removeAllViews()
            wvWeb.destroy()
        }
    }

    override fun onDestroy() {
        destroyWebView()
        super.onDestroy()
//        EventBus.getDefault().unregister(this)
        System.gc()
        LogUtils.i("hhh---,onDestroy")
    }

    override// 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvWeb.canGoBack()) {
            wvWeb.goBack() // goBack()表示返回WebView的上一页面
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        if (wvWeb.canGoBack()) {
            wvWeb.goBack()
        } else {
            super.onBackPressed()
        }
    }

//    override fun onPause() {
//        super.onPause()
//        if (wvWeb != null) {
//            //跳出页面时，暂停H5的播放
//            wvWeb.loadUrl("javascript:appCtrl_playPause()")
//        }
//    }
}

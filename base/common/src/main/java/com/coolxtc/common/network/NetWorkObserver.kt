package com.coolxtc.common.network

import androidx.annotation.CallSuper
import com.coolxtc.common.util.LogUtils
import com.coolxtc.common.util.ToastUtil
import rx.Observer

/**
 * Desc:
 * 观察者，传入泛型即可；
 * 原则上只需实现两个抽象方法：
 * 1.onSuccess--->接口status返回200回调，返回的是传入的泛型
 * 2.onError--->接口返回非200或者本地报错回调，返回原始数据
 * onNext，onError，onCompleted是Observer原生函数，以上两个是其业务封装
 *
 * @param netWorkProgressView 传入该对象即代表需要弹出loading弹窗
 * @param showToast 是否在非200的时候弹出错误Toast，默认弹出
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
abstract class NetWorkObserver<T>(
        private val netWorkProgressView: NetWorkProgressView? = null,
        private val showToast: Boolean = true
) : Observer<NetWorkResponse<T>> {
    private val tag = "NetWorkObserver:"

    init {
        LogUtils.e("$tag===init")
        netWorkProgressView?.showDialog()
    }

    /***
     * 接口status返回200回调，返回的是传入的泛型
     */
    abstract fun success(ret: NetWorkResponse<T>)

    /***
     * 接口返回非200或者本地报错回调，返回原始数据
     */
    abstract fun error(original: NetWorkResponse<T>)

    @CallSuper
    override fun onError(e: Throwable?) {
        LogUtils.e("$tag===onError")
        e?.printStackTrace()
        ToastUtil.toast(NetWorkCode.NETWORK_ERR_TOAST)
        val resp = NetWorkResponse<T>()
        resp.success = false
        resp.errcode = NetWorkCode.NETWORK_ERR
        resp.errmsg = NetWorkCode.NETWORK_ERR_TOAST
        error(resp)
    }

    @CallSuper
    override fun onNext(t: NetWorkResponse<T>) {
        LogUtils.e("$tag===onNext")
        when (t.errcode) {
            NetWorkCode.SUCCESS -> {
                t.success = true
                success(t)
            }
            NetWorkCode.TOKEN_ERROR -> {
            }
            else -> {
                if (showToast) {
                    ToastUtil.toast(t.errmsg)
                }
                t.success = false
                error(t)
            }
        }
    }

    override fun onCompleted() {
        LogUtils.e("$tag===onCompleted")
        netWorkProgressView?.dismissDialog()
    }
}
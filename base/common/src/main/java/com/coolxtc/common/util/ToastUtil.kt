package com.coolxtc.common.util

import com.hjq.toast.ToastUtils

/**
 * com.tt.smeite.util
 *
 * @author coolxtc
 * @date 2017/11/27
 * Description:
 */
object ToastUtil {

    fun toast(txt: String) {
        ToastUtils.show(txt)
        LogUtils.i("hhh---,Toast txt:$txt")
    }

    fun toast(resId: Int) {
        ToastUtils.show(resId)
        LogUtils.i("hhh---,Toast resId:$resId")
    }
}
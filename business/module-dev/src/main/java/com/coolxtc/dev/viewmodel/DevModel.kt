package com.coolxtc.dev.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStoreOwner
import com.coolxtc.common.App
import com.coolxtc.common.UserInfoModel
import com.coolxtc.common.bean.ret.StartRet
import com.coolxtc.common.network.BaseNetWorkService
import com.coolxtc.common.network.NetWorkHttp
import com.coolxtc.common.network.NetWorkObserver
import com.coolxtc.common.network.NetWorkResponse
import com.coolxtc.common.util.ScreenTools
import com.coolxtc.common.util.ToastUtil
import com.coolxtc.common.viewmodel.BaseViewModel

/**
 * Desc:
 * DevFragment 的 ViewModel
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
class DevModel : BaseViewModel() {
    companion object {
        fun getInstant(owner: ViewModelStoreOwner): DevModel {
            return BaseViewModel.getInstant(owner)
        }
    }

    var startStatus = MutableLiveData<Boolean>()

    fun start() {
        NetWorkHttp.toSubscribe(NetWorkHttp.getNetWorkService<BaseNetWorkService>().start(
                ScreenTools.instance(App.instance).screenWidth.toString(),
                ScreenTools.instance(App.instance).screenHeight.toString()
        ), object : NetWorkObserver<StartRet>() {
            override fun success(ret: NetWorkResponse<StartRet>) {
                if (ret.data.stopServer.code != 0) {
                    ToastUtil.toast(ret.data.stopServer.msg)
                    return
                }
                UserInfoModel.setDeviceId(ret.data.deviceId)
                startStatus.value = true
            }

            override fun error(original: NetWorkResponse<StartRet>) {
                startStatus.value = false
            }
        })
    }
}
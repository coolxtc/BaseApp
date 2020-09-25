package com.coolxtc.index.viewmodel

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
import com.coolxtc.index.bean.ret.CategoryRet
import com.coolxtc.index.network.IndexNetWorkService

/**
 * Desc:
 * IndexFragment 的 ViewModel
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
class IndexModel : BaseViewModel() {
    companion object {
        fun getInstant(owner: ViewModelStoreOwner): IndexModel {
            return BaseViewModel.getInstant(owner)
        }
    }

    var startSuccess = MutableLiveData<Boolean>()

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
                startSuccess.value = true
            }

            override fun error(original: NetWorkResponse<StartRet>) {
                startSuccess.value = false
            }
        })
    }

    var categoryRet = MutableLiveData<NetWorkResponse<CategoryRet>>()

    fun getCategory() {
        //类目
        NetWorkHttp.toSubscribe(NetWorkHttp.getNetWorkService<IndexNetWorkService>().categorys(), object : NetWorkObserver<CategoryRet>() {
            override fun success(ret: NetWorkResponse<CategoryRet>) {
                categoryRet.value = ret
            }

            override fun error(original: NetWorkResponse<CategoryRet>) {
                categoryRet.value = original
            }
        })
    }
}
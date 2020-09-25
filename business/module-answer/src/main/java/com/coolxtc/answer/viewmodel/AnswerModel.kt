package com.coolxtc.answer.viewmodel

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
 * AnswerFragment 的 ViewModel
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
class AnswerModel : BaseViewModel() {
    companion object {
        fun getInstant(owner: ViewModelStoreOwner): AnswerModel {
            return BaseViewModel.getInstant(owner)
        }
    }

    val result: String
        get() {
            return """
                ${info1}${info2}是怎么回事呢？${info1}相信大家都很熟悉，但是${info1}${info2}是怎么回事呢，下面就让小编带大家一起了解吧。
                
                ${info1}${info2}，其实就是${info3}，大家可能会很惊讶${info1}怎么会${info2}呢？但事实就是这样，小编也感到非常惊讶。
                
                这就是关于${info1}${info2}的事情了，大家有什么想法呢，欢迎在评论区告诉小编一起讨论哦！
            """.trimIndent()
        }
    var info1 = ""
    var info2 = ""
    var info3 = ""

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
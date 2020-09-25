package com.coolxtc.index.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStoreOwner
import com.coolxtc.common.coustomview.TypeSelectorLayout
import com.coolxtc.common.network.NetWorkHttp
import com.coolxtc.common.network.NetWorkObserver
import com.coolxtc.common.network.NetWorkResponse
import com.coolxtc.common.viewmodel.BaseViewModel
import com.coolxtc.index.bean.ret.GoodsRet
import com.coolxtc.index.network.IndexNetWorkService

/**
 * Desc:
 * IndexFragment 的 ViewModel
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
class ListModel : BaseViewModel() {
    companion object {
        fun getInstant(owner: ViewModelStoreOwner): ListModel {
            return BaseViewModel.getInstant(owner)
        }
    }

    var listRet = MutableLiveData<NetWorkResponse<GoodsRet>>()
    val typeList = ArrayList<TypeSelectorLayout.TypeData>()

    init {
        initSelectData()
    }

    private fun initSelectData() {
        typeList.clear()
        typeList.add(TypeSelectorLayout.TypeData("综合", GoodsRet.QUERY_TYPE_SYNTHESIZE, true))
        typeList.add(TypeSelectorLayout.TypeData("销量", GoodsRet.QUERY_TYPE_SALE))
        typeList.add(TypeSelectorLayout.TypeData("价格", GoodsRet.QUERY_TYPE_PRICE, isNeedOrder = true))
        typeList.add(TypeSelectorLayout.TypeData("券额", GoodsRet.QUERY_TYPE_TICKET, isNeedOrder = true, isOrderSelected = true))
    }

    fun getGoodsList(page: Int, type: String, order: String, category: String) {
        NetWorkHttp.toSubscribe(NetWorkHttp.getNetWorkService<IndexNetWorkService>().goodses(
                page,
                type,
                order,
                "",
                category
        ), object : NetWorkObserver<GoodsRet>() {
            override fun success(ret: NetWorkResponse<GoodsRet>) {
                listRet.value = ret
            }

            override fun error(original: NetWorkResponse<GoodsRet>) {
                listRet.value = original
            }
        })
    }
}
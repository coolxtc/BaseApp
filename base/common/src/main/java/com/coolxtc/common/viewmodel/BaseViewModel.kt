package com.coolxtc.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.coolxtc.common.App
import com.coolxtc.common.viewmodel.BaseViewModel.Companion.getInstant

/**
 * Desc:
 * ViewModel 基类，实现了基类 [getInstant] 函数，子类继承后调用该函数可快速获取实例
 * 受限于 Kotlin 无法继承静态函数影响，故子类需自定义 getInstant 函数
 * ex:
 *     companion object {
 *               return BaseViewModel.getInstant(owner)
 *           fun getInstant(owner: ViewModelStoreOwner): * {
 *           }
 *       }
 *
 *       private val mainModel by lazy { *.getInstant(owner) }
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
open class BaseViewModel : ViewModel() {
    companion object {
        inline fun <reified T : BaseViewModel> getInstant(owner: ViewModelStoreOwner): T {
            return ViewModelProvider(owner, ViewModelProvider.AndroidViewModelFactory.getInstance(App.instance)).get(T::class.java)
        }
    }
}
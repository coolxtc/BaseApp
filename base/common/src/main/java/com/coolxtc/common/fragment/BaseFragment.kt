package com.coolxtc.common.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.coolxtc.common.R
import com.coolxtc.common.activity.BaseActivity
import com.coolxtc.common.network.NetWorkProgressView
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.loading_layout.*

/***
 * Desc:
 * Fragment基类，抽象了基本的 initView，initData 函数；
 * 原则上来说基本的页面渲染只需要调用这三个函数即可实现。
 * 调用顺序 initView-->initData
 * 该基类实现了懒加载逻辑，initData 函数仅会在第一次展示的时候调用
 *
 * @param layoutId 布局 LayoutId
 * @param needLazy 是否需要启用懒加载模式
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
abstract class BaseFragment(private val layoutId: Int = -1, private val needLazy: Boolean = false) : Fragment(R.layout.fragment_base), NetWorkProgressView {
    protected val simpleName = this.javaClass.simpleName.toString()
    private var isDataInit = false
    protected lateinit var rootView: View

    /**
     * 初始化View方法
     */
    abstract fun initView()

    /**
     * 初始化Data方法
     */
    abstract fun initData()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ARouter.getInstance().inject(this)
        if (layoutId > 0) {
            rootView = layoutInflater.inflate(layoutId, layFragmentContainer, true)
        }
        if (!needLazy) {
            initView()
            initData()
        }
    }

    override fun onResume() {
        super.onResume()
        if (needLazy && !isDataInit) {
            initView()
            initData()
            isDataInit = true
        }
    }

    override fun showDialog(msg: String) {
        (requireActivity() as BaseActivity).showDialog(msg)
    }

    override fun dismissDialog() {
        (requireActivity() as BaseActivity).dismissDialog()
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
        loading_frame.stopLoading()
    }
}
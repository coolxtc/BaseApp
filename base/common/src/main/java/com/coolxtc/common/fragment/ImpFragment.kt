package com.coolxtc.common.fragment

import com.coolxtc.common.R
import kotlinx.android.synthetic.main.fragment_imp.*

/**
 * Desc:
 *
 * @author xtc
 * @date 2020/9/14
 * @email qsawer888@126.com
 */
class ImpFragment(private val title: String) : BaseFragment(R.layout.fragment_imp) {
    override fun initView() {
        tv_imp.text = title
    }

    override fun initData() {

    }
}
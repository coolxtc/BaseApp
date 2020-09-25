package com.coolxtc.index.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.coolxtc.common.activity.BaseActivity
import com.coolxtc.common.router.RouterPath
import com.coolxtc.index.R
import com.coolxtc.index.fragment.IndexFragment

/**
 * Desc:
 * IndexFragment的承载Activity，模块化启动时候的入口
 *
 * @author xtc
 * @date 2020/9/14
 * @email qsawer888@126.com
 */
@Route(path = RouterPath.INDEX_ACTIVITY)
class IndexActivity : BaseActivity(R.layout.activity_index) {
    override fun initView() {
        replaceFragment(IndexFragment())
    }

    override fun initData() {

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction() // 开启一个事务
        transaction.replace(R.id.fl_index_container, fragment)
        transaction.commit()
    }
}
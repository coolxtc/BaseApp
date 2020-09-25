package com.coolxtc.answer.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.coolxtc.answer.R
import com.coolxtc.answer.fragment.AnswerFragment
import com.coolxtc.common.activity.BaseActivity
import com.coolxtc.common.router.RouterPath

/**
 * Desc:
 * AnswerFragment的承载Activity，模块化启动时候的入口
 *
 * @author xtc
 * @date 2020/9/14
 * @email qsawer888@126.com
 */
@Route(path = RouterPath.ANSWER_ACTIVITY)
class AnswerActivity : BaseActivity(R.layout.activity_index) {
    override fun initView() {
        replaceFragment(AnswerFragment())
    }

    override fun initData() {

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction() // 开启一个事务
        transaction.replace(R.id.fl_answer_container, fragment)
        transaction.commit()
    }
}
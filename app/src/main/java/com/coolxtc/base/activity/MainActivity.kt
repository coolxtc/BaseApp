package com.coolxtc.base.activity

import android.content.Intent
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.coolxtc.base.R
import com.coolxtc.common.activity.BaseActivity
import com.coolxtc.common.router.Router
import com.coolxtc.common.router.RouterPath
import com.coolxtc.common.router.WebSchemeRedirect
import com.coolxtc.common.util.ActivityUtil
import com.coolxtc.common.util.ToastUtil
import com.coolxtc.common.util.UpdateManager
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Desc:
 * 主页
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
@Route(path = RouterPath.MAIN_ACTIVITY)
class MainActivity : BaseActivity(R.layout.activity_main) {
    private var fList = ArrayList<Fragment>()

    @Autowired
    @JvmField
    var adUrl = ""

    @Autowired
    @JvmField
    var schemeUrl = ""

    @Autowired
    @JvmField
    var page = ""

    override fun initData() {
        WebSchemeRedirect.handleWebClick(schemeUrl)
        WebSchemeRedirect.handleWebClick(adUrl)
    }

    override fun onNewIntent(intent: Intent?) {
        // ARouter要在onNewIntent获取到参数的话，需要在获取参数前调用下setIntent()
        setIntent(intent)
        super.onNewIntent(intent)
        selectFragment(page)
    }

    override fun initView() {
        hideBack()
        initFragmentContainer()
        UpdateManager(this).update(forceCheck = false, showDialog = false, listener = null)
    }

    private fun selectFragment(page: String) {
        if (TextUtils.isEmpty(page)) {
            viewpager.setCurrentItem(0, false)
            setTitle("Index")
        }
        when (page) {
            RouterPath.INDEX_FRAGMENT -> {
                viewpager.setCurrentItem(0, false)
                setTitle("Index")
            }
            RouterPath.ANSWER_FRAGMENT -> {
                viewpager.setCurrentItem(1, false)
                setTitle("Answer")
            }
            RouterPath.DEV_FRAGMENT -> {
                viewpager.setCurrentItem(2, false)
                setTitle("Dev")
            }
        }
    }

    private fun initFragmentContainer() {
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_0 -> {
                    selectFragment(RouterPath.INDEX_FRAGMENT)
                }
                R.id.navigation_1 -> {
                    selectFragment(RouterPath.ANSWER_FRAGMENT)
                }
                R.id.navigation_2 -> {
                    selectFragment(RouterPath.DEV_FRAGMENT)
                }
            }
            true
        }
        fList = ArrayList<Fragment>().apply {
            add(Router.Index.getIndexFragment())
            add(Router.Answer.getAnswerFragment())
            add(Router.Dev.getDevFragment())
        }
        viewpager.setSlidingEnable(false)
        viewpager.adapter = MainPageAdapter(supportFragmentManager, fList)
        viewpager.offscreenPageLimit = fList.size
        viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                navigation.menu.getItem(position).isChecked = true
            }
        })
        selectFragment(page)
    }

    internal class MainPageAdapter(fm: FragmentManager, private val list: List<Fragment>) :
            FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return list[position]
        }

        override fun getCount(): Int {
            return list.size
        }
    }

    private var firstTime = 0L
    override fun onBackPressed() {
        val secondTime = System.currentTimeMillis()
        if (secondTime - firstTime > 2000) {
            ToastUtil.toast("再按一次退出程序")
            firstTime = secondTime
            return
        } else {
            super.onBackPressed()
            ActivityUtil.appExit()
        }
    }
}
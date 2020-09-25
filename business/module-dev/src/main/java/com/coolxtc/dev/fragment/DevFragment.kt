package com.coolxtc.dev.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.coolxtc.dev.viewmodel.DevModel
import com.coolxtc.common.fragment.BaseFragment
import com.coolxtc.common.router.Router
import com.coolxtc.common.router.RouterPath
import com.coolxtc.common.util.postEvent
import com.coolxtc.dev.R
import com.coolxtc.dev.export.event.DevTestEvent
import kotlinx.android.synthetic.main.fragmeng_dev.*


/**
 * Desc:
 * DevFragment 测试一些功能用
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
@Route(path = RouterPath.DEV_FRAGMENT)
class DevFragment : BaseFragment(R.layout.fragmeng_dev, true) {
    private val devModel by lazy { DevModel.getInstant(this) }

    override fun initData() {
        devModel.start()
        devModel.startStatus.observe(this, {

        })
    }

    override fun initView() {
        bt_test_event.setOnClickListener {
            val event = DevTestEvent()
            event.msg = "I'm test msg from $simpleName"
            postEvent(event)
        }
        bt_test_router_1.setOnClickListener {
            Router.Main.jumpWeb("https://www.baidu.com")
        }
        bt_test_router_2.setOnClickListener {
            Router.Dev.jumpDevActivity()
        }
    }
}
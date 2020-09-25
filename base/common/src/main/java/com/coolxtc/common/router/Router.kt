package com.coolxtc.common.router

import com.alibaba.android.arouter.launcher.ARouter
import com.coolxtc.common.fragment.BaseFragment
import com.coolxtc.common.fragment.ImpFragment

/**
 * Desc:
 * 页面路由
 *
 * @author xtc
 * @date 2020/9/11
 * @email qsawer888@126.com
 */
object Router {
    /**
     * Main相关跳转存放处
     */
    object Main {
        /**
         * 跳转 WelcomeActivity
         * @param schemeUrl welcome结束之后协议跳转路径
         */
        fun jumpWelcome(schemeUrl: String = "") {
            ARouter.getInstance().build(RouterPath.WELCOME_ACTIVITY)
                    .withString("schemeUrl", schemeUrl)
                    .navigation()
        }

        /**
         * 跳转 MainActivity
         * @param adUrl 打开Main之后AD跳转路径
         * @param schemeUrl 打开Main之后协议跳转路径
         * @param page 需要切换哪个页面，默认index
         */
        fun jumpMain(adUrl: String = "", schemeUrl: String = "", page: String = RouterPath.INDEX_FRAGMENT) {
            ARouter.getInstance().build(RouterPath.MAIN_ACTIVITY)
                    .withString("adUrl", adUrl)
                    .withString("schemeUrl", schemeUrl)
                    .withString("page", page)
                    .navigation()
        }

        /**
         * 跳转 AppWebActivity
         * @param url web加载链接
         */
        fun jumpWeb(url: String) {
            ARouter.getInstance().build(RouterPath.WEB_ACTIVITY)
                    .withString("url", url)
                    .navigation()
        }
    }

    /**
     * Answer相关跳转存放处
     */
    object Answer {
        /**
         * 获取 AnswerFragment
         */
        fun getAnswerFragment(): BaseFragment {
            val navigation = ARouter.getInstance().build(RouterPath.ANSWER_FRAGMENT).navigation()
            return if (navigation is BaseFragment) {
                navigation
            } else {
                ImpFragment(RouterPath.ANSWER_FRAGMENT)
            }
        }

        /**
         * 跳转 AnswerActivity
         */
        fun jumpAnswerActivity() {
            ARouter.getInstance().build(RouterPath.ANSWER_ACTIVITY).navigation()
        }
    }

    /**
     * Index相关跳转存放处
     */
    object Index {
        /**
         * 获取 IndexFragment
         */
        fun getIndexFragment(): BaseFragment {
            val navigation = ARouter.getInstance().build(RouterPath.INDEX_FRAGMENT).navigation()
            return if (navigation is BaseFragment) {
                navigation
            } else {
                ImpFragment(RouterPath.INDEX_FRAGMENT)
            }
        }

        /**
         * 跳转 IndexActivity
         */
        fun jumpIndexActivity() {
            ARouter.getInstance().build(RouterPath.INDEX_ACTIVITY).navigation()
        }
    }

    /**
     * Dev相关跳转存放处
     */
    object Dev {
        /**
         * 获取 DevFragment
         */
        fun getDevFragment(): BaseFragment {
            val navigation = ARouter.getInstance().build(RouterPath.DEV_FRAGMENT).navigation()
            return if (navigation is BaseFragment) {
                navigation
            } else {
                ImpFragment(RouterPath.DEV_FRAGMENT)
            }
        }

        /**
         * 跳转 DevActivity
         */
        fun jumpDevActivity() {
            ARouter.getInstance().build(RouterPath.DEV_ACTIVITY).navigation()
        }
    }
}
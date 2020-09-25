package com.coolxtc.index.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.coolxtc.common.fragment.BaseFragment
import com.coolxtc.common.router.RouterPath
import com.coolxtc.common.util.LogUtils
import com.coolxtc.index.R
import com.coolxtc.index.bean.ret.CategoryRet
import com.coolxtc.index.customview.MyWrapNavigatorAdapter
import com.coolxtc.index.viewmodel.IndexModel
import kotlinx.android.synthetic.main.fragmeng_index.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * Desc:
 * IndexFragment
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
@Route(path = RouterPath.INDEX_FRAGMENT)
class IndexFragment : BaseFragment(R.layout.fragmeng_index, true) {
    private val indexModel by lazy { IndexModel.getInstant(this) }

    override fun initData() {
        indexModel.start()
        initModel()
    }

    override fun initView() {

    }

    private fun initModel() {
        indexModel.startSuccess.observe(this, {
            if (it) {
                getCategory()
            }
        })
        indexModel.categoryRet.observe(this, {
            if (it.success) {
                loadSuccess()
                initPage(it.data)
            } else {
                loadError(it.errcode, it.errmsg)
            }
        })
    }

    private fun getCategory() {
        startLoading(retryListener)
        LogUtils.i("hhh---,initData")
        indexModel.getCategory()
    }

    private val retryListener = View.OnClickListener {
        getCategory()
    }

    private fun initPage(data: CategoryRet) {
        pager.adapter = CategoryDetailAdapter(childFragmentManager, data.records)
        val list = arrayListOf<String>()
        data.records.forEach { list.add(it.name) }
        val navigator = CommonNavigator(requireActivity())
        navigator.scrollPivotX = 0.35f
        navigator.adapter = MyWrapNavigatorAdapter(list, pager)
        magic_indicator.navigator = navigator
        ViewPagerHelper.bind(magic_indicator, pager)
        pager.currentItem = 0
    }

    internal class CategoryDetailAdapter(fm: FragmentManager, private val categoryList: List<CategoryRet.RecordsBean>)
        : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            val categoryId = categoryList[position].categoryId
            return ListFragment(categoryId)
        }

        override fun getCount(): Int {
            return categoryList.size
        }
    }
}
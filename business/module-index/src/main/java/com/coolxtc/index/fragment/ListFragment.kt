package com.coolxtc.index.fragment

import com.coolxtc.common.coustomview.TypeSelectorLayout
import com.coolxtc.common.coustomview.refreshLayout.PullToRefreshLayout
import com.coolxtc.common.fragment.BaseFragment
import com.coolxtc.index.R
import com.coolxtc.index.adapter.GoodsAdapter
import com.coolxtc.index.bean.ret.GoodsRet
import com.coolxtc.index.viewmodel.ListModel
import kotlinx.android.synthetic.main.fragmeng_list.*

/**
 * Desc:
 * 商品列表 Fragment
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
class ListFragment(private val categoryId: String) : BaseFragment(R.layout.fragmeng_list, true) {
    private val listModel by lazy { ListModel.getInstant(this) }
    private val ptrLayout by lazy { layout_pull_to_refresh as PullToRefreshLayout<GoodsRet.RecordsBean> }

    override fun initData() {
        initModel()
        getListData()
    }

    override fun initView() {
        layout_type_selector.init(listModel.typeList, object : TypeSelectorLayout.TypeClickListener {
            override fun onTypeClickListener() {
                showDialog()
                ptrLayout.pageIndex = 1
                getListData()
                ptrLayout.recyclerView.scrollToPosition(0)
            }
        })
        initPtrLayout()
    }

    private fun getListData() {
        listModel.getGoodsList(ptrLayout.pageIndex, layout_type_selector.getType(), layout_type_selector.getOrder(), categoryId)
    }

    private fun initPtrLayout() {
        ptrLayout.setAdapter(GoodsAdapter())
        ptrLayout.setPtrListener(object : PullToRefreshLayout.OnPtrListener {
            override fun onRefresh() {
                getListData()
            }

            override fun onLoadMore() {
                getListData()
            }
        })
    }

    private fun initModel() {
        listModel.listRet.observe(this, {
            dismissDialog()
            if (it.success) {
                ptrLayout.setResultData(it.data.records, it.data.total)
            } else {
                ptrLayout.setError(it.errmsg, it.errcode)
            }
        })
    }
}
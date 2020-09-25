package com.coolxtc.common.coustomview.refreshLayout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.coolxtc.common.Constant
import com.coolxtc.common.R
import com.coolxtc.common.util.LogUtils
import com.coolxtc.common.util.ToastUtil
import kotlinx.android.synthetic.main.layout_pull_to_refresh.view.*
import kotlinx.android.synthetic.main.loading_layout.view.*
import kotlin.math.abs

/**
 * Desc:
 * 通用的列表加载组件
 * 请配合[R.layout.layout_pull_to_refresh]使用（在layout xml中include或者代码中Inflate）
 * 使用步骤
 * 1.因 Kotlin 寻找控件插件不能生成泛型，所以需要在代码里转换一下。ex:
 *      private val ptrLayout by lazy { layout_pull_to_refresh as PullToRefreshLayout<*> }
 * 2.创建 Adapter 继承 [BaseQuickAdapter] 或 [BaseMultiItemQuickAdapter] 等
 * 3.调用 [setAdapter] 、[setPtrListener]
 * 4.获取到数据后调用 [setResultData]、[setError] 展示结果
 *
 * adapter from
 *      https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
class PullToRefreshLayout<T>(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {
    /**
     * 当前加载页数
     */
    var pageIndex = 1

    /**
     * 设置列表数据全部加载完成后的loadMoreView是否显示
     */
    var showLoadMoreEndView = false

    /**
     * 是否还有剩余可加载数据
     */
    var haveMore = true
        private set

    /**
     * 设置单次加载条目，必须和调用接口处传参相同，默认为 20
     */
    var limit = Constant.ApiConfig.PAGE_LIMIT

    val recyclerView: RecyclerView get() = rv_list
    val retryBtn: TextView get() = loading_frame.reloadButton
    val customBtn: TextView get() = loading_frame.customButton

    private lateinit var mAdapter: BaseQuickAdapter<T, BaseViewHolder>
    private var totalPager = 0

    /**
     * 下拉刷新开关
     */
    private var isEnable = true

    /**
     * 空数据图片
     */
    private var noDataPic = R.mipmap.ic_no_data_bg

    /**
     * 空数据文案
     */
    private var noDataMsg = "暂无数据"

    override fun onFinishInflate() {
        //判断用户在进行滑动操作的最小距离
        super.onFinishInflate()
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        loading_frame.startLoading()
        setColorSchemeResources(R.color.colorPrimary)
    }

    private var mTouchSlop = 0
    private var mPrevX = 0f
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mPrevX = event.x
            MotionEvent.ACTION_MOVE -> {
                val eventX = event.x
                //获取水平移动距离
                val xDiff = abs(eventX - mPrevX)
                //当水平移动距离大于滑动操作的最小距离的时候就认为进行了横向滑动
                //不进行事件拦截,并将这个事件交给子View处理
                if (xDiff > mTouchSlop) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    /**
     * 设置 Adapter
     */
    fun setAdapter(adapter: BaseQuickAdapter<T, BaseViewHolder>) {
        mAdapter = adapter
        mAdapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        rv_list.adapter = this.mAdapter
        rv_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    /**
     * 是否开启下拉刷新功能，默认开启
     */
    override fun setEnabled(enabled: Boolean) {
        isEnable = enabled
        super.setEnabled(enabled)
    }

    /**
     * 设置需要展示的数据列表，本方法会根据传入的数据是否是第一页，而执行相应的叠加处理
     * 网络错误，显示相应的布局（第一页显示全局错误页面，非第一页在列表项显示错误项）
     * 可加载时，滚动到最后一条，自动激活下一页数据获取，并给出加载中效果
     * 无数据，显示全局noData页面，如果是有数据但是最后一条，显示当前已无数据在列表项中
     *
     * @param data 加入适配器的数据集合
     */
    fun setResultData(data: List<T>, totalCount: Int) {
        var mTotalCount = totalCount
        if (mTotalCount <= 0) {
            mTotalCount = 0
        }
        totalPager = if (mTotalCount % limit > 0) {
            mTotalCount / limit + 1
        } else {
            mTotalCount / limit
        }
        //正常加载分页数据
        if (pageIndex == 1) {
            mAdapter.setList(data)
        } else {
            mAdapter.addData(data)
        }
        if (mAdapter.data.size == 0) {
            //显示空页面
            loading_frame.failedLoading(noDataPic, noDataMsg)
            loading_frame.reloadButton.visibility = GONE
            haveMore = false
        } else {
            //设置加载完成
            haveMore = if (pageIndex >= totalPager || data.size < limit) {
                mAdapter.loadMoreModule.loadMoreEnd(showLoadMoreEndView)
                false
            } else {
                mAdapter.loadMoreModule.loadMoreComplete()
                true
            }
            loading_frame.stopLoading()
        }
        isRefreshing = false
        isEnabled = isEnable
    }

    /**
     * 显示加载失败时候的 UI
     */
    fun setError(errMsg: String, errCode: Int) {
        isRefreshing = false
        if (loading_frame.isLoading) {
            loading_frame.failedLoading(errMsg, errCode)
            isEnabled = isEnable
            pageIndex = 1
        } else {
            isEnabled = true
            mAdapter.loadMoreModule.loadMoreFail()
            if (pageIndex > 1) {
                pageIndex += -1
            } else {
                ToastUtil.toast(errMsg)
            }
        }
    }

    /**
     * 设置加载回调，包含加载更多和下拉刷新
     */
    fun setPtrListener(ptrListener: OnPtrListener) {
        setOnRefreshListener {
            pageIndex = 1
            ptrListener.onRefresh()
            LogUtils.i("hhh---,refresh")
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            if (!isRefreshing) {
                pageIndex += 1
                ptrListener.onLoadMore()
                LogUtils.i("hhh---,loadMore")
            }
        }
        loading_frame.reloadButton.setOnClickListener { v: View? ->
            pageIndex = 1
            loading_frame.startLoading()
            ptrListener.onRefresh()
        }
    }

    /**
     * 自定义空数据时候的图片和文案
     */
    fun setEmptyRes(errPic: Int, errMsg: String) {
        this.noDataMsg = errMsg
        this.noDataPic = errPic
    }

    /**
     * 加载回调接口
     */
    interface OnPtrListener {
        fun onRefresh()
        fun onLoadMore()
    }
}
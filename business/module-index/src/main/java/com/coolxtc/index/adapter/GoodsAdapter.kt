package com.coolxtc.index.adapter

import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.coolxtc.common.coustomview.CenterImageSpan
import com.coolxtc.common.util.ImageUtil
import com.coolxtc.common.util.NumUtil
import com.coolxtc.common.util.ToastUtil
import com.coolxtc.index.R
import com.coolxtc.index.bean.ret.GoodsRet

/**
 * Desc:
 * 商品列表 Adapter
 *
 * @author xtc
 * @date 2020/9/7
 * @email qsawer888@126.com
 */
class GoodsAdapter : BaseQuickAdapter<GoodsRet.RecordsBean, BaseViewHolder>(R.layout.layout_goods_item), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: GoodsRet.RecordsBean) {
        ImageUtil.loadImgUrl(context, item.image, holder.getView(R.id.sdv_image), 5)
        val icon = if (item.platformType == GoodsRet.TAO_BAO_TYPE) {
            if (item.goodsStore == 0) {
                R.mipmap.ic_taobao3x
            } else {
                R.mipmap.ic_tmall3x
            }
        } else {
            R.mipmap.ic_pdd3x
        }
        val imgSpan = CenterImageSpan(context, icon, 1)
        val spannableString = SpannableString("* " + item.title)
        spannableString.setSpan(imgSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.getView<TextView>(R.id.tv_title).text = spannableString
        if (item.couponPrice.toDouble() > 0) {
            holder.getView<TextView>(R.id.tv_coupon_price).text = "${item.couponPrice.split(".")[0]}元"
            holder.getView<TextView>(R.id.tv_default_price).text = "￥${item.defaultPrice}"
            holder.getView<TextView>(R.id.tv_default_price).paint.isAntiAlias = true//抗锯齿
            holder.getView<TextView>(R.id.tv_default_price).paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
            holder.getView<TextView>(R.id.tv_coupon_price).visibility = View.VISIBLE
            holder.getView<TextView>(R.id.tv_default_price).visibility = View.VISIBLE
        } else {
            holder.getView<TextView>(R.id.tv_coupon_price).visibility = View.INVISIBLE
            holder.getView<TextView>(R.id.tv_default_price).visibility = View.INVISIBLE
        }
        val split = item.costPrice.split(".")
        holder.getView<TextView>(R.id.tv_cost_price1).text = split[0]
        if (split.size == 1) {
            holder.getView<TextView>(R.id.tv_cost_price2).text = ".00"
        } else {
            when (split[1].length) {
                1 -> {
                    holder.getView<TextView>(R.id.tv_cost_price2).text = ".${split[1]}0"
                }
                2 -> {
                    holder.getView<TextView>(R.id.tv_cost_price2).text = ".${split[1]}"
                }
                else -> {
                    holder.getView<TextView>(R.id.tv_cost_price2).text = ".${split[1].substring(0, 2)}"
                }
            }
        }
        when {
            item.sale >= 100000 -> {
                holder.getView<TextView>(R.id.tv_sale).text = "已售${item.sale / 10000}万件"
            }
            item.sale >= 10000 -> {
                holder.getView<TextView>(R.id.tv_sale).text = "已售${NumUtil.remain1Decimal((item.sale / 10000f).toString())}万件"
            }
            else -> {
                holder.getView<TextView>(R.id.tv_sale).text = "已售${item.sale}件"
            }
        }
        holder.getView<TextView>(R.id.tv_save_money).text = "存入金+${item.saveMoney}"
        holder.getView<LinearLayout>(R.id.layout_good).setOnClickListener {
            ToastUtil.toast(item.title)
        }
    }
}
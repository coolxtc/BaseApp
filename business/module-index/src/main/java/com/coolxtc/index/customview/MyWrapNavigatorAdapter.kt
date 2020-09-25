package com.coolxtc.index.customview

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.coolxtc.common.App
import com.coolxtc.index.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * Created by xtc on 2018/6/21.
 */
class MyWrapNavigatorAdapter(private val list: ArrayList<String>, private val vp: ViewPager) : CommonNavigatorAdapter() {
    override fun getTitleView(context: Context, position: Int): IPagerTitleView {
        val titleView = ColorTransitionPagerTitleView(context)
        titleView.normalColor = ContextCompat.getColor(vp.context, R.color.c333333)
        titleView.selectedColor = ContextCompat.getColor(vp.context, R.color.white)
        titleView.text = list[position]
        titleView.setOnClickListener { vp.currentItem = position }
        val paddingLR = App.instance.resources.getDimension(R.dimen.dp_17).toInt()
        titleView.setPadding(paddingLR, 0, paddingLR, 0)
        return titleView
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getIndicator(context: Context): IPagerIndicator {
        val indicator = WrapPagerIndicator(context)
        indicator.fillColor = ContextCompat.getColor(context, R.color.cff7000)
        indicator.roundRadius = App.instance.resources.getDimension(R.dimen.dp_100)
        indicator.verticalPadding = App.instance.resources.getDimension(R.dimen.dp_3).toInt()
        indicator.horizontalPadding = App.instance.resources.getDimension(R.dimen.dp_8).toInt()
        return indicator
    }
}

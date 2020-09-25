package com.coolxtc.common.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.coolxtc.common.R

/**
 * Desc:
 * 图片加载工具
 *
 * @author xtc
 * @date 2020/9/7
 * @email qsawer888@126.com
 */
object ImageUtil {
    /**
     * 网络加载
     * @param roundedCorner 圆角，单位dp，默认0
     */
    fun loadImgUrl(context: Context, url: String, iv: ImageView, roundedCorner: Int = 0) {
        val round = if (roundedCorner > 0) roundedCorner else 1
        Glide.with(context)
                .load(url)
                .error(R.mipmap.ic_moren)
                .placeholder(R.mipmap.ic_moren)
                .fallback(R.mipmap.ic_moren)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(round.dp)))
                .transition(DrawableTransitionOptions().crossFade(300))
                .into(iv)
    }

    /**
     * res加载
     * @param roundedCorner 圆角，单位dp，默认0
     */
    fun loadImgRes(context: Context, res: Int, iv: ImageView, roundedCorner: Int = 0) {
        val round = if (roundedCorner > 0) roundedCorner else 1
        Glide.with(context)
                .load(res)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(round.dp)))
                .transition(DrawableTransitionOptions().crossFade(300))
                .into(iv)
    }
}

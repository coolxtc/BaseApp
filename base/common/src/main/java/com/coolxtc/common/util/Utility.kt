package com.coolxtc.common.util

import android.content.res.Resources

/**
 * Desc:
 *
 * @author xtc
 * @date 2020/9/7
 * @email qsawer888@126.com
 */
val Int.dp: Int
    get() = android.util.TypedValue.applyDimension(
            android.util.TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()

val Int.sp: Int
    get() = android.util.TypedValue.applyDimension(
            android.util.TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()

val Int.hexString: String
    get() = Integer.toHexString(this)
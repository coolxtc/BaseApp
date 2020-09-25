package com.coolxtc.common.util

import java.math.BigDecimal

/**
 * com.qinxin.xiaotemai.util
 *
 * @author Beta-Tan
 * @date 2018/5/23
 * Description:
 */
object NumUtil {
    //保留一位小数
    fun remain1Decimal(string: String): String {
        val result = BigDecimal(string).setScale(1, BigDecimal.ROUND_UP).toDouble().toString()
        return result
    }

    //保留两位小数
    fun remain2Decimal(string: String): String {
        val result = BigDecimal(string).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble().toString()
        val split = result.split(".")
        return when (split.size) {
            1 -> {
                "$result.00"
            }
            2 -> {
                if (split[1].length == 1) {
                    result + "0"
                } else {
                    result
                }
            }
            else -> {
                result
            }
        }
    }
}
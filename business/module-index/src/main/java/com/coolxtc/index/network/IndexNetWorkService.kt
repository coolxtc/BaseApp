package com.coolxtc.index.network

import com.coolxtc.common.Constant
import com.coolxtc.common.network.BaseNetWorkService
import com.coolxtc.common.network.NetWorkResponse
import com.coolxtc.index.bean.ret.CategoryRet
import com.coolxtc.index.bean.ret.GoodsRet
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Desc:
 *
 * @author xtc
 * @date 2020/9/11
 * @email qsawer888@126.com
 */
interface IndexNetWorkService : BaseNetWorkService {
    @GET("categorys")
    fun categorys(@Query("platformType") platformType: String = "1"): Observable<NetWorkResponse<CategoryRet>>

    @GET("goodses")
    fun goodses(
            @Query("page") page: Int,
            @Query("type") type: String,
            @Query("order") order: String = "-1",
            @Query("keyword") keyword: String = "",
            @Query("category") category: String = "",
            @Query("platformType") platformType: String = "1",
            @Query("limit") limit: Int = Constant.ApiConfig.PAGE_LIMIT,
    ): Observable<NetWorkResponse<GoodsRet>>
}
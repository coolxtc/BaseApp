package com.coolxtc.common.network

import com.coolxtc.common.bean.ret.StartRet
import com.coolxtc.common.bean.ret.UpdateRet
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Desc:
 * 网络请求接口存放处
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
interface BaseNetWorkService {
    @GET("start")
    fun start(
            @Query("width") width: String,
            @Query("height") height: String,
    ): Observable<NetWorkResponse<StartRet>>

    @GET("checkVersion")
    fun checkVersion(): Observable<NetWorkResponse<UpdateRet>>
}
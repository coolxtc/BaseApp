package com.coolxtc.common.network

import com.coolxtc.common.Constant
import com.coolxtc.common.util.LogUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSession

/**
 * Desc:
 * 网络请求封装，App内调用 init() 函数初始化，业务调用 toSubscribe() 函数发起请求
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
object NetWorkHttp {
    private var mBaseUrl = "{head_url}/{version}/"
    private lateinit var mOkHttpClient: OkHttpClient
    lateinit var retrofit: Retrofit

    fun init() {
        mBaseUrl = mBaseUrl.replace("{head_url}", Constant.ApiConfig.BASE_URL)
        mBaseUrl = mBaseUrl.replace("{version}", Constant.ApiConfig.API_VERSION)
        LogUtils.i("baseUrl = $mBaseUrl")
        initRetrofit()
    }

    private fun initRetrofit() {
        mOkHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(NetWorkInterceptor())
                .eventListenerFactory(NetWorkEventListener.FACTORY)
                .hostnameVerifier { _: String?, _: SSLSession? -> true }
                .build()
        retrofit = Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    inline fun <reified T : BaseNetWorkService> getNetWorkService(): T {
        return retrofit.create(T::class.java)
    }

    fun <T> toSubscribe(observable: Observable<NetWorkResponse<T>>, b: NetWorkObserver<T>, delayMILLISECONDS: Long = 0L) {
        observable.subscribeOn(Schedulers.io())
                .delay(delayMILLISECONDS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b as Observer<in Any>)
    }

    fun cancelAllCal() {
        mOkHttpClient.dispatcher().cancelAll()
    }
}
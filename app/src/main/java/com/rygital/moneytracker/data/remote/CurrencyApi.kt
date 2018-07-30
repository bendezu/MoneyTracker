package com.rygital.moneytracker.data.remote

import com.rygital.moneytracker.data.model.RateResponse
import io.reactivex.Observable
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File

interface CurrencyApi {
    companion object {
        private const val ENDPOINT: String = "https://openexchangerates.org/api/"
    }

    @GET("latest.json")
    fun getCurrencyRates(@Query("app_id") appId: String): Observable<RateResponse>

    class Creator {
        companion object {
            fun createSlaApi(cacheFile: File): CurrencyApi {
                var cache: Cache? = null

                try {
                    cache = Cache(cacheFile, (10 * 1024 * 1024).toLong())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val okHttpClient = OkHttpClient.Builder()
                        .cache(cache)
                        .build()

                val retrofit = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .baseUrl(ENDPOINT)
                        .build()
                return retrofit.create<CurrencyApi>(CurrencyApi::class.java)
            }
        }
    }
}
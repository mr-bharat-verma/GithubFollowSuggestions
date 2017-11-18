package com.githubfollow.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Logger
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtils {
    const val TIMEOUT = 15 //sec

    fun getOkHttpClient(
            apiName: String, headers: Map<String, String>?): OkHttpClient {
        val logging = HttpLoggingInterceptor(Logger { message -> Log.d(apiName, message) })
        logging.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    var request = chain.request()
                    if (headers != null && !headers.isEmpty()) {
                        for ((key, value) in headers) {
                            request = request.newBuilder().addHeader(key,
                                    value).build()
                        }
                    }
                    chain.proceed(request)
                }
                .addInterceptor(logging)
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
        return builder.build()
    }

    fun <S> createRetrofitApi(serviceClass: Class<S>, baseUrl: String,
                              headers: Map<String, String>? = null): S {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient(serviceClass.name, headers))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(serviceClass)
    }
}
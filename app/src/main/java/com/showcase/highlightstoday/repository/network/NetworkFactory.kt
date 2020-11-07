package com.showcase.highlightstoday.repository.network

import com.showcase.highlightstoday.repository.gateways.NetworkGateway
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * A self-contained factory for Network module.
 * Use this factory to get a single point of entrance to the network layer.
 */
object NetworkFactory {

    private const val baseUrl = "https://newsapi.org/"
    private const val connectionTimeout = 35L
    private const val readTimeout = 35L
    private const val writeTimeout = 35L
    private const val callTimeout = 10L

    /**
     * Creates a new instance of a network gateway
     */
    fun createGateway(): NetworkGateway {
        return RemoteGateway(getRemoteApi())
    }

    private fun getRemoteApi(): NewsApi {
        val retrofit = retrofit(
            okHttpClient(httpLoggingInterceptor()),
            gsonFactoryConverter()
        )

        return retrofit.create(NewsApi::class.java)
    }

    private fun retrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    private fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .callTimeout(callTimeout, TimeUnit.SECONDS)
            .build()
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private fun gsonFactoryConverter(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}
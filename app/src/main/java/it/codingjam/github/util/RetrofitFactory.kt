package it.codingjam.github.util

import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    inline fun <reified T> createService(debug: Boolean, baseUrl: HttpUrl): T {
        val httpClient = OkHttpClient.Builder()

        if (debug) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }

        val gson = GsonBuilder().create()

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(DenvelopingConverter(gson))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build()
                .create(T::class.java)
    }
}
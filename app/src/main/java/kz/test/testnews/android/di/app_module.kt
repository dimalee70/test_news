package kz.test.testnews.android.di

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import kz.test.testnews.android.api.ApiManager
import kz.test.testnews.android.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

val remoteDatasourceModule = module {
    single { createOkHttpClient() }
//    single { WeatherServiceAPI() }
//    single { createWebService<ApiManager>(get(),  getProperty(Constants.BASE_URL))}
        single { createWebService<ApiManager>(get(),getKoin().getProperty("SERVER_URL")!!) }
}

fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
        .pingInterval(Constants.PING_INTERFAL, TimeUnit.SECONDS)
        .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

}

//fun createWebService(okHttpClient: OkHttpClient, url: String): ApiManager {
//    val retrofit = Retrofit.Builder()
//        .baseUrl(url)
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
//    return retrofit.create(ApiManager::class.java)
//}


inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}

object DatasourceProperties {
    const val SERVER_URL = "SERVER_URL"
}

//fun provideWeatherApi(retrofit: Retrofit): ApiManager = retrofit.create(ApiManager::class.java)
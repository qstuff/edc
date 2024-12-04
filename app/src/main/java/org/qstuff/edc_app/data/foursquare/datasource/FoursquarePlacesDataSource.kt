package org.qstuff.edc_app.data.foursquare.datasource

import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.qstuff.edc_app.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Retrofit connector to the foursquare API.
 * Header fields (Authorization, Accept) are injected with an Interceptor.
 */
class FoursquarePlacesDataSource(
    private val authToken: String,
    cacheDir: File
) {

    companion object {
        private const val CACHE_SIZE = 10 * 1024 * 1024L
        private const val CONNECTION_TIMEOUT = 60L
        private const val READ_TIMEOUT = 60L
        private const val WRITE_TIMEOUT = 60L
        private const val CALL_TIMEOUT = 100L
        private const val BASE_URL = "https://api.foursquare.com/v3/"
    }

    var apiService: FoursquarePlacesApiService

    private val httpClient =
        OkHttpClient.Builder()
            .addInterceptor(createHeaderInterceptor())
            .addInterceptor(createLoggingInterceptor())
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
            .cache(Cache(cacheDir, CACHE_SIZE))
            .build()

    init {
        apiService = configure().create(FoursquarePlacesApiService::class.java)
    }

    private fun configure(): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

    private fun createHeaderInterceptor() =
        Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .addHeader("Authorization", authToken)
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }

    private fun createLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        )

}
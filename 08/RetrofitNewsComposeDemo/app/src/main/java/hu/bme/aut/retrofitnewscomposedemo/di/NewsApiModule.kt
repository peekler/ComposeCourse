package hu.bme.aut.retrofitnewscomposedemo.di

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.retrofitnewscomposedemo.BuildConfig
import hu.bme.aut.retrofitnewscomposedemo.network.NewsAPI
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsApiModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor()).build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.NEWS_BASE_URL)
            .addConverterFactory(Json{ ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()) )
            //.addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsAPI(retrofit: Retrofit): NewsAPI {
        return retrofit.create(NewsAPI::class.java)
    }
}

class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request: Request = chain.request()
        val t1 = System.nanoTime()
        Log.d("TAG_HTTP", java.lang.String.format("Sending request %s on %s%n%s",
            request.url, chain.connection(), request.headers))
        val response: okhttp3.Response = chain.proceed(request)
        val t2 = System.nanoTime()
        Log.d("TAG_HTTP", java.lang.String.format("Received response for %s in %.1fms%n%s",
            response.request.url, (t2 - t1) / 1e6, response.headers))
        return response
    }
}

package com.ylallencheng.searchgithubusers.di

import android.app.Application
import com.ylallencheng.searchgithubusers.BuildConfig
import com.ylallencheng.searchgithubusers.R
import com.ylallencheng.searchgithubusers.SGUApplication
import com.ylallencheng.searchgithubusers.io.GithubService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideGithubService(application: SGUApplication): GithubService {

        // set HTTP timeout
        val okHttpClientBuilder =
                OkHttpClient.Builder().apply {
                    connectTimeout(60L, TimeUnit.SECONDS)
                    readTimeout(60L, TimeUnit.SECONDS)

                    // write log in debug mode
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                    }
                }

        return Retrofit.Builder()
                .baseUrl(application.getString(R.string.base_url))
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .build()
                .create(GithubService::class.java)
    }
}
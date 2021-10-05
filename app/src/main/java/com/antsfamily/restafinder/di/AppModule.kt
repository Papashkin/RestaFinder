package com.antsfamily.restafinder.di

import android.content.Context
import com.antsfamily.restafinder.data.local.SharedPrefs
import com.antsfamily.restafinder.data.local.CoordinatesProvider
import com.antsfamily.restafinder.data.local.DataFetchingTimer
import com.antsfamily.restafinder.data.remote.ApiConfig
import com.antsfamily.restafinder.data.remote.RestaurantService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRestaurantService(okHttpClient: OkHttpClient): RestaurantService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.API_ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RestaurantService::class.java)
    }

    @Provides
    @Singleton
    fun provideDataFetchingTimer(): DataFetchingTimer = DataFetchingTimer()

    @Provides
    @Singleton
    fun provideCoordinatesProvider(): CoordinatesProvider = CoordinatesProvider()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPrefs =
        SharedPrefs(context)
}

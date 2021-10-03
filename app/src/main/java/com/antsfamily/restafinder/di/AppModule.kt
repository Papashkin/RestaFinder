package com.antsfamily.restafinder.di

import com.antsfamily.restafinder.remote.ApiConfig
import com.antsfamily.restafinder.remote.RestaurantService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}

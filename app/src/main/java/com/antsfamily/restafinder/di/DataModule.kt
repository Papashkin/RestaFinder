package com.antsfamily.restafinder.di

import com.antsfamily.restafinder.data.remote.repository.ContentRepositoryImpl
import com.antsfamily.restafinder.domain.repository.ContentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsContentRepository(contentRepositoryImpl: ContentRepositoryImpl): ContentRepository
}

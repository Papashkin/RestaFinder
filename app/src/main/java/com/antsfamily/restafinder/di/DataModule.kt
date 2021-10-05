package com.antsfamily.restafinder.di

import com.antsfamily.restafinder.data.DataRepository
import com.antsfamily.restafinder.data.DataRepositoryImpl
import com.antsfamily.restafinder.data.local.LocalSource
import com.antsfamily.restafinder.data.local.LocalSourceImpl
import com.antsfamily.restafinder.data.remote.RemoteSource
import com.antsfamily.restafinder.data.remote.RemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsDataRepository(dataRepositoryImpl: DataRepositoryImpl): DataRepository

    @Binds
    abstract fun bindsLocalSource(localSourceImpl: LocalSourceImpl): LocalSource

    @Binds
    abstract fun bindsRemoteSource(remoteSourceImpl: RemoteSourceImpl): RemoteSource
}

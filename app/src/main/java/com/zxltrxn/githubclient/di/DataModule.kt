package com.zxltrxn.githubclient.di

import com.zxltrxn.githubclient.data.repository.AppRepository
import com.zxltrxn.githubclient.data.repository.IAuthRepository
import com.zxltrxn.githubclient.data.repository.IDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideIDataRepository(appRepository: AppRepository): IDataRepository{
        return appRepository
    }

    @Singleton
    @Provides
    fun provideIAuthRepository(appRepository: AppRepository): IAuthRepository {
        return appRepository
    }
}
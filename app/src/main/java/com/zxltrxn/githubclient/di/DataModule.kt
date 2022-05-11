package com.zxltrxn.githubclient.di

import com.zxltrxn.githubclient.data.repository.AppRepository
import com.zxltrxn.githubclient.data.repository.IAuthRepository
import com.zxltrxn.githubclient.data.repository.IDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindIDataRepository(appRepository: AppRepository): IDataRepository

    @Singleton
    @Binds
    abstract fun bindIAuthRepository(appRepository: AppRepository): IAuthRepository
}
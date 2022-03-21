package com.zxltrxn.githubclient.di

import android.content.Context
import com.zxltrxn.githubclient.data.storage.SharedPrefsUserStorage
import com.zxltrxn.githubclient.data.storage.UserStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Singleton
    @Provides
    fun provideSharedPrefsUserStorage(@ApplicationContext context: Context): UserStorage{
        return SharedPrefsUserStorage(context = context)
    }
}
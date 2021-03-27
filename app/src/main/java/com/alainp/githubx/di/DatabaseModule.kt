package com.alainp.githubx.di

import android.content.Context
import com.alainp.githubx.data.AppDatabase
import com.alainp.githubx.data.UserDao
import com.alainp.githubx.data.UserRemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideUserRemoteKeyDao(appDatabase: AppDatabase): UserRemoteKeyDao {
        return appDatabase.userRemoteKeyDao()
    }
}

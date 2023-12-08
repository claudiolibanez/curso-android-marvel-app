package com.example.marvelapp.framework.di

import android.content.Context
import androidx.room.Room
import com.example.core.data.DbConstants
import com.example.marvelapp.framework.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DbConstants.APP_DATABASE_NAME
    ).build()

    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase) = appDatabase.favoriteDao()

    @Provides
    fun provideCharacterDao(appDatabase: AppDatabase) = appDatabase.characterDao()

    @Provides
    fun provideRemoteKeyDao(appDatabase: AppDatabase) = appDatabase.remoteKeyDao()
}
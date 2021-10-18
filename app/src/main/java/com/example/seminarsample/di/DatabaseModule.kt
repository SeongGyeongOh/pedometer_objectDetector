package com.example.seminarsample.di

import android.content.Context
import com.example.seminarsample.data.db.CommonDatabase
import com.example.seminarsample.data.db.StepCountDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideCommonDatabase(context: Context): CommonDatabase {
        return CommonDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideStepCounterDao(commonDatabase: CommonDatabase): StepCountDao {
        return commonDatabase.stepCountDao()
    }
}
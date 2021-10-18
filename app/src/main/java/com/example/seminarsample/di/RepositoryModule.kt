package com.example.seminarsample.di

import com.example.seminarsample.data.repository.local.StepCountRepositoryImpl
import com.example.seminarsample.domain.repository.local.StepCountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStepCountRepository(
        stepCountRepositoryImpl: StepCountRepositoryImpl
    ): StepCountRepository
}
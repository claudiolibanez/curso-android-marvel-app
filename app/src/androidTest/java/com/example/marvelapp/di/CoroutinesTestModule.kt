package com.example.marvelapp.di

import com.example.core.usecase.base.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class AppCoroutinesTestDispatchers(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())
) : CoroutineDispatchers {
    override fun default(): CoroutineDispatcher = testDispatcher
    override fun io(): CoroutineDispatcher = testDispatcher
    override fun main(): CoroutineDispatcher = testDispatcher
    override fun unconfined(): CoroutineDispatcher = testDispatcher
}

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesTestModule {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    fun provideTestDispatchers(): CoroutineDispatchers = AppCoroutinesTestDispatchers()
}
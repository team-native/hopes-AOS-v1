package com.example.hopes.di

import com.example.hopes.data.remote.*
import com.example.hopes.data.repository.*
import com.example.hopes.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module @InstallIn(SingletonComponent::class)
abstract class DataBindingModule {
    @Binds abstract fun bindAuthRemoteDataSource(implementation: AuthRemoteDataSourceImpl): AuthRemoteDataSource
    @Binds abstract fun bindChatRemoteDataSource(implementation: ChatRemoteDataSourceImpl): ChatRemoteDataSource
    @Binds abstract fun bindSettingsRemoteDataSource(implementation: SettingsRemoteDataSourceImpl): SettingsRemoteDataSource
    @Binds abstract fun bindAuthRepository(implementation: AuthRepositoryImpl): AuthRepository
    @Binds abstract fun bindChatRepository(implementation: ChatRepositoryImpl): ChatRepository
    @Binds abstract fun bindSettingsRepository(implementation: SettingsRepositoryImpl): SettingsRepository
}

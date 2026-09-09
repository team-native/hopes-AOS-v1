package com.example.hopes.di

import com.example.hopes.data.remote.AuthRemoteDataSource
import com.example.hopes.data.remote.AuthRemoteDataSourceImpl
import com.example.hopes.data.remote.ChatRemoteDataSource
import com.example.hopes.data.remote.ChatRemoteDataSourceImpl
import com.example.hopes.data.remote.SettingsRemoteDataSource
import com.example.hopes.data.remote.SettingsRemoteDataSourceImpl
import com.example.hopes.data.repository.AuthRepositoryImpl
import com.example.hopes.data.repository.ChatRepositoryImpl
import com.example.hopes.data.repository.SettingsRepositoryImpl
import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** 데이터 계층 구현체를 domain 계약에 연결한다. */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindingModule {
    @Binds
    abstract fun bindAuthRemoteDataSource(
        implementation: AuthRemoteDataSourceImpl,
    ): AuthRemoteDataSource

    @Binds
    abstract fun bindChatRemoteDataSource(
        implementation: ChatRemoteDataSourceImpl,
    ): ChatRemoteDataSource

    @Binds
    abstract fun bindSettingsRemoteDataSource(
        implementation: SettingsRemoteDataSourceImpl,
    ): SettingsRemoteDataSource

    @Binds
    abstract fun bindAuthRepository(
        implementation: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    abstract fun bindChatRepository(
        implementation: ChatRepositoryImpl,
    ): ChatRepository

    @Binds
    abstract fun bindSettingsRepository(
        implementation: SettingsRepositoryImpl,
    ): SettingsRepository
}

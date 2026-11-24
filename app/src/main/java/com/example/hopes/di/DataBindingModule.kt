package com.example.hopes.di

import com.example.hopes.core.connectivity.ConnectivityNetworkMonitor
import com.example.hopes.core.connectivity.NetworkMonitor
import com.example.hopes.data.remote.AuthRemoteDataSource
import com.example.hopes.data.remote.AttachmentRemoteDataSource
import com.example.hopes.data.remote.AttachmentRemoteDataSourceImpl
import com.example.hopes.data.remote.AuthRemoteDataSourceImpl
import com.example.hopes.data.remote.ChatRemoteDataSource
import com.example.hopes.data.remote.ChatRemoteDataSourceImpl
import com.example.hopes.data.remote.SettingsRemoteDataSource
import com.example.hopes.data.remote.SettingsRemoteDataSourceImpl
import com.example.hopes.feature.community.data.CommunityRepository
import com.example.hopes.feature.community.data.CommunityStore
import com.example.hopes.data.repository.AuthRepositoryImpl
import com.example.hopes.data.repository.AttachmentRepositoryImpl
import com.example.hopes.data.repository.ChatRepositoryImpl
import com.example.hopes.data.repository.SettingsRepositoryImpl
import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.repository.AttachmentRepository
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.repository.ChatAssistRepository
import com.example.hopes.domain.repository.CampusLocationRepository
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.feature.chat.data.ChatAssistRemoteDataSource
import com.example.hopes.feature.chat.data.ChatAssistRemoteDataSourceImpl
import com.example.hopes.feature.chat.data.ChatAssistRepositoryImpl
import com.example.hopes.feature.chat.data.CampusLocationRemoteDataSource
import com.example.hopes.feature.chat.data.CampusLocationRemoteDataSourceImpl
import com.example.hopes.feature.chat.data.CampusLocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** 데이터 계층 구현체를 domain 계약에 연결한다. */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindingModule {
    @Binds
    abstract fun bindAuthRemoteDataSource(implementation: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    abstract fun bindAttachmentRemoteDataSource(implementation: AttachmentRemoteDataSourceImpl): AttachmentRemoteDataSource

    @Binds
    abstract fun bindChatRemoteDataSource(implementation: ChatRemoteDataSourceImpl): ChatRemoteDataSource

    @Binds
    abstract fun bindSettingsRemoteDataSource(implementation: SettingsRemoteDataSourceImpl): SettingsRemoteDataSource

    @Binds
    abstract fun bindAuthRepository(implementation: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindAttachmentRepository(implementation: AttachmentRepositoryImpl): AttachmentRepository

    @Binds
    abstract fun bindChatRepository(implementation: ChatRepositoryImpl): ChatRepository

    @Binds
    abstract fun bindSettingsRepository(implementation: SettingsRepositoryImpl): SettingsRepository

    @Binds
    abstract fun bindCommunityRepository(implementation: CommunityStore): CommunityRepository

    @Binds
    abstract fun bindChatAssistRemoteDataSource(implementation: ChatAssistRemoteDataSourceImpl): ChatAssistRemoteDataSource

    @Binds
    abstract fun bindChatAssistRepository(implementation: ChatAssistRepositoryImpl): ChatAssistRepository

    @Binds
    abstract fun bindCampusLocationRemoteDataSource(implementation: CampusLocationRemoteDataSourceImpl): CampusLocationRemoteDataSource

    @Binds
    abstract fun bindCampusLocationRepository(implementation: CampusLocationRepositoryImpl): CampusLocationRepository

    @Binds
    abstract fun bindNetworkMonitor(implementation: ConnectivityNetworkMonitor): NetworkMonitor
}

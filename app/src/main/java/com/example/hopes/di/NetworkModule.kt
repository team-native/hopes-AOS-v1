package com.example.hopes.di

import com.example.hopes.BuildConfig
import com.example.hopes.core.network.AuthorizationInterceptor
import com.example.hopes.data.api.AuthApiService
import com.example.hopes.data.api.ChatApiService
import com.example.hopes.data.api.SettingsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton fun provideJson(): Json = Json { ignoreUnknownKeys = true; explicitNulls = false }
    @Provides @Singleton fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply { level = if (BuildConfig.ENABLE_NETWORK_LOG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }
    @Provides @Singleton @Named("public") fun providePublicClient(logging: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()
    @Provides @Singleton @Named("authenticated") fun provideAuthenticatedClient(logging: HttpLoggingInterceptor, authorizationInterceptor: AuthorizationInterceptor): OkHttpClient = OkHttpClient.Builder().addInterceptor(authorizationInterceptor).addInterceptor(logging).build()
    @Provides @Singleton @Named("public") fun providePublicRetrofit(json: Json, @Named("public") client: OkHttpClient): Retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client).addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()
    @Provides @Singleton @Named("authenticated") fun provideAuthenticatedRetrofit(json: Json, @Named("authenticated") client: OkHttpClient): Retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client).addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()
    @Provides fun provideAuthApi(@Named("public") retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)
    @Provides fun provideChatApi(@Named("authenticated") retrofit: Retrofit): ChatApiService = retrofit.create(ChatApiService::class.java)
    @Provides fun provideSettingsApi(@Named("authenticated") retrofit: Retrofit): SettingsApiService = retrofit.create(SettingsApiService::class.java)
}

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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /** 알 수 없는 서버 필드를 허용하는 공통 JSON 직렬화기를 제공한다. */
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    /** 빌드 환경에 따라 HTTP 본문 로그를 제한한다. */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.ENABLE_LOG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /** 인증이 필요 없는 API 전용 HTTP 클라이언트를 제공한다. */
    @Provides
    @Singleton
    @Named("public")
    fun providePublicClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = createBaseClient(loggingInterceptor).build()

    /** access token을 추가하는 인증 API 전용 HTTP 클라이언트를 제공한다. */
    @Provides
    @Singleton
    @Named("authenticated")
    fun provideAuthenticatedClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authorizationInterceptor: AuthorizationInterceptor,
    ): OkHttpClient = createBaseClient(loggingInterceptor)
        .addInterceptor(authorizationInterceptor)
        .build()

    /** 인증이 없는 Retrofit 인스턴스를 제공한다. */
    @Provides
    @Singleton
    @Named("public")
    fun providePublicRetrofit(
        json: Json,
        @Named("public") publicClient: OkHttpClient,
    ): Retrofit = createRetrofit(json, publicClient)

    /** 인증 Retrofit 인스턴스를 제공한다. */
    @Provides
    @Singleton
    @Named("authenticated")
    fun provideAuthenticatedRetrofit(
        json: Json,
        @Named("authenticated") authenticatedClient: OkHttpClient,
    ): Retrofit = createRetrofit(json, authenticatedClient)

    @Provides
    fun provideAuthApi(
        @Named("public") retrofit: Retrofit,
    ): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides
    fun provideChatApi(
        @Named("authenticated") retrofit: Retrofit,
    ): ChatApiService = retrofit.create(ChatApiService::class.java)

    @Provides
    fun provideSettingsApi(
        @Named("authenticated") retrofit: Retrofit,
    ): SettingsApiService = retrofit.create(SettingsApiService::class.java)

    private fun createBaseClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient.Builder = OkHttpClient.Builder()
        .connectTimeout(BuildConfig.CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .readTimeout(BuildConfig.CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .writeTimeout(BuildConfig.CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)

    private fun createRetrofit(
        json: Json,
        client: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}

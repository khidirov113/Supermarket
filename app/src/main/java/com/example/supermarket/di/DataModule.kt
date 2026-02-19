package com.example.supermarket.di

import android.content.Context
import androidx.work.WorkManager
import com.example.supermarket.data.local.TokenManager
import com.example.supermarket.data.remote.network.AuthApi
import com.example.supermarket.data.remote.network.BannerApi
import com.example.supermarket.data.remote.network.CatalogApiService
import com.example.supermarket.data.remote.network.NotificationApi
import com.example.supermarket.data.remote.network.ProductApi
import com.example.supermarket.data.remote.network.ProfileApi
import com.example.supermarket.data.remote.network.QrApi
import com.example.supermarket.data.remote.network.StoreApi
import com.example.supermarket.data.repository.AuthRepositoryImpl
import com.example.supermarket.data.repository.BannerRepositoryImpl
import com.example.supermarket.data.repository.CatalogRepositoryImpl
import com.example.supermarket.data.repository.NotificationRepositoryImpl
import com.example.supermarket.data.repository.ProductRepositoryImpl
import com.example.supermarket.data.repository.ProfileRepositoryImpl
import com.example.supermarket.data.repository.QrRepositoryImpl
import com.example.supermarket.data.repository.SettingRepositoryImpl
import com.example.supermarket.data.repository.StoreRepositoryImpl
import com.example.supermarket.domain.repository.AuthRepository
import com.example.supermarket.domain.repository.BannerRepository
import com.example.supermarket.domain.repository.CatalogRepository
import com.example.supermarket.domain.repository.NotificationRepository
import com.example.supermarket.domain.repository.ProductRepository
import com.example.supermarket.domain.repository.ProfileRepository
import com.example.supermarket.domain.repository.QrRepository
import com.example.supermarket.domain.repository.SettingRepository
import com.example.supermarket.domain.repository.StoreRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    fun bindSettingRepository(
        impl: SettingRepositoryImpl
    ): SettingRepository


    @Binds
    @Singleton
    abstract fun bindStoreRepository(
        impl: StoreRepositoryImpl
    ): StoreRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository


    @Binds
    @Singleton
    abstract fun bindBannerRepository(
        impl: BannerRepositoryImpl
    ): BannerRepository

    @Binds
    @Singleton
    abstract fun bindCatalogRepository(
        impl: CatalogRepositoryImpl
    ): CatalogRepository

    @Binds
    @Singleton
    abstract fun bindQrRepository(
        impl: QrRepositoryImpl
    ): QrRepository


    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository

    companion object {


        @Provides
        @Singleton
        fun provideProfileApi(retrofit: Retrofit): ProfileApi = retrofit.create()

        @Provides
        @Singleton
        fun provideBaseUrl(): String = "https://market.tajsoft.tj"

        @Provides
        @Singleton
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            }
        }

        @Provides
        @Singleton
        fun provideWorkManager(
            @ApplicationContext context: Context
        ): WorkManager = WorkManager.getInstance(context)

        @Provides
        @Singleton
        fun provideOkHttpClient(
            tokenManager: TokenManager
        ): OkHttpClient {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    val token = runBlocking { tokenManager.token.first() }
                    val request = chain.request().newBuilder()

                    if (!token.isNullOrBlank()) {
                        request.addHeader("Authorization", "Bearer $token")
                    }
                    chain.proceed(request.build())
                }
                .build()
        }

        @Provides
        @Singleton
        fun provideConverterFactory(json: Json): Converter.Factory {
            return json.asConverterFactory("application/json".toMediaType())
        }

        @Provides
        @Singleton
        fun provideRetrofit(
            converterFactory: Converter.Factory,
            okHttpClient: OkHttpClient
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://market.tajsoft.tj/")
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .build()
        }

        @Provides
        @Singleton
        fun provideApiServiceBanner(retrofit: Retrofit): BannerApi = retrofit.create()

        @Provides
        @Singleton
        fun provideApiServiceProduct(retrofit: Retrofit): ProductApi = retrofit.create()

        @Provides
        @Singleton
        fun provideApiServiceCatalog(retrofit: Retrofit): CatalogApiService = retrofit.create()

        @Provides
        @Singleton
        fun provideAuthApiService(retrofit: Retrofit): AuthApi = retrofit.create()

        @Provides
        @Singleton
        fun provideNotificationApiService(retrofit: Retrofit): NotificationApi = retrofit.create()


        @Provides
        @Singleton
        fun provideStoreApiService(retrofit: Retrofit): StoreApi = retrofit.create()

        @Provides
        @Singleton
        fun provideQrApi(retrofit: Retrofit): QrApi = retrofit.create()

    }
}

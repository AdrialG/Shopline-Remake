package com.adrialg.shopline_remaster.injection

import android.content.Context
import com.adrialg.shopline_remaster.api.ApiService
import com.adrialg.shopline_remaster.data.Const
import com.adrialg.shopline_remaster.data.Session
import com.crocodic.core.data.CoreSession
import com.crocodic.core.helper.NetworkHelper
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create()

    @Singleton
    @Provides
    fun provideSession(@ApplicationContext context: Context, gson: Gson) = Session(context, gson)

    @Singleton
    @Provides
    fun provideOkHttpClient(session: CoreSession): OkHttpClient {
        return NetworkHelper.provideOkHttpClient().newBuilder().apply {
            addInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .method(original.method, original.body)

                val token = session.getString(Const.TOKEN.API_TOKEN)
                if (token.isNotEmpty()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }

                val request = requestBuilder.build()
                it.proceed(request)
            }
        }.build()
    }

    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return NetworkHelper.provideApiService(
            baseUrl = "https://magang.crocodic.net/ki/kelompok_3/ki-e-commerse-app/public/",
            okHttpClient = okHttpClient,
            converterFactory = listOf(ScalarsConverterFactory.create())
        )
    }

}
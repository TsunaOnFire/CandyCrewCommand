package com.aph.willywonka.core.di

import com.aph.willywonka.data.network.HeaderInterceptor
import com.aph.willywonka.data.network.WorkersApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private fun getClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor(HeaderInterceptor()).build()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://2q2woep105.execute-api.eu-west-1.amazonaws.com/napptilus/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
    }

    @Singleton
    @Provides
    fun provideCharacterApiClient(retrofit: Retrofit):WorkersApiClient{
        return retrofit.create(WorkersApiClient::class.java)
    }
}
package com.aph.willywonka.core.di

import android.content.Context
import com.aph.willywonka.data.network.HeaderInterceptor
import com.aph.willywonka.data.network.WorkersApiClient
import com.aph.willywonka.utils.WorkerListUtils
import com.aph.willywonka.utils.WorkerListUtilsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun workerListUtilsProvider(@ApplicationContext context: Context): WorkerListUtils =
        WorkerListUtilsImpl(context.getSharedPreferences(WorkerListUtilsImpl.WORKER_LIST_UTILS_NAME, 0))
}
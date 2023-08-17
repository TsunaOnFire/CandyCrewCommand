package com.aph.willywonka.core.di

import android.content.Context
import androidx.room.Room
import com.aph.willywonka.data.database.WorkerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val WORKER_DATABASE_NAME="worker_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(context,WorkerDatabase::class.java,
        WORKER_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideWorkerDao(db: WorkerDatabase) = db.getWorkerDao()
}
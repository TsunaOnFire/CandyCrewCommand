package com.aph.willywonka.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aph.willywonka.BuildConfig
import com.aph.willywonka.data.database.dao.WorkerDao
import com.aph.willywonka.data.database.entities.WorkerEntity

@Database(entities = [
    WorkerEntity::class,
], version = BuildConfig.VERSION_CODE)
abstract class WorkerDatabase: RoomDatabase() {

    abstract fun getWorkerDao(): WorkerDao
}
package com.aph.willywonka.data.database.dao

import androidx.room.*
import com.aph.willywonka.data.database.entities.WorkerEntity
import com.aph.willywonka.data.database.entities.workerTableName

@Dao
interface WorkerDao {
    @Query("SELECT * FROM $workerTableName ORDER BY id ASC")
    suspend fun getAllWorkers():List<WorkerEntity>

    @Query("SELECT * FROM $workerTableName WHERE id = :id")
    suspend fun getWorkerById(id: Int):WorkerEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters:List<WorkerEntity>)

    @Query("DELETE FROM $workerTableName")
    suspend fun deleteAllWorkers()

    @Update(entity = WorkerEntity::class)
    suspend fun update(worker: WorkerEntity)
}
package com.aph.willywonka.data

import com.aph.willywonka.data.database.dao.WorkerDao
import com.aph.willywonka.data.database.entities.WorkerEntity
import com.aph.willywonka.data.database.entities.toBDO
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.data.model.toBO
import com.aph.willywonka.data.network.WorkerService
import javax.inject.Inject

class WorkerRepository @Inject constructor(
    private val api: WorkerService,
    private val workerDao: WorkerDao
) {

    suspend fun getWorkersFromApiByPage(page: Int?):List<WorkerBO>{
        val response = api.getWorkersByPage(page)
        return response.map{it.toBO()}
    }

    suspend fun getWorkerFromApiById(id: Int):WorkerBO? {
        val worker = api.getWorkerById(id)
        if(worker != null && worker.id == null){
            worker.id = id
        }
        return worker?.toBO()
    }

    suspend fun getAllWorkersFromDatabase():List<WorkerBO>{
        val response: List<WorkerEntity> = workerDao.getAllWorkers()
        return response.map{it.toBO()}
    }

    suspend fun getWorkerFromDatabaseById(id: Int):WorkerBO{
        val response: WorkerEntity = workerDao.getWorkerById(id)
        return response.toBO()
    }

    suspend fun insertWorkers(characters:List<WorkerEntity>){
        workerDao.insertAll(characters)
    }

    suspend fun clearWorkers(){
        workerDao.deleteAllWorkers()
    }

    suspend fun updateWorker(worker: WorkerBO){
        workerDao.update(worker.toBDO())
    }

}
package com.aph.willywonka.data.network

import com.aph.willywonka.data.network.model.WorkerDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkerService @Inject constructor(private val api: WorkersApiClient) {

    suspend fun getWorkersByPage(page: Int?): List<WorkerDTO> {
        return withContext(Dispatchers.IO) {
            val response = api.getWorkersByPage(page ?: 1)
            //Log.d("Response","Error:"+ response.errorBody()?.charStream()?.readText())
            response.body()?.results ?: emptyList()
        }

    }

    suspend fun getWorkerById(id: Int): WorkerDTO? {
        return withContext(Dispatchers.IO) {
            val response = api.getWorkerById(id)
            //Log.d("Response","Error:"+ response.errorBody()?.charStream()?.readText())
            response.body()
        }

    }
}
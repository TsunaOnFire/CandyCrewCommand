package com.aph.willywonka.data.network

import com.aph.willywonka.data.network.model.WorkerDTO
import com.aph.willywonka.data.network.responses.WorkersByPageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkersApiClient {
    @GET("oompa-loompas")
    suspend fun getWorkersByPage(@Query("page") page: Int): Response<WorkersByPageResponse>

    @GET("oompa-loompas/{workerId}")
    suspend fun getWorkerById(@Path("workerId") workerId: Int): Response<WorkerDTO>
}

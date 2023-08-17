package com.aph.willywonka.data.network.responses

import com.aph.willywonka.data.network.model.WorkerDTO
import com.google.gson.annotations.SerializedName

data class WorkersByPageResponse(
    @SerializedName("current") var current: Int,
    @SerializedName("total") var total: Int,
    @SerializedName("results") var results: List<WorkerDTO>?
)
